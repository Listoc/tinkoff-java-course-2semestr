package edu.java.scrapper.service.proccesor;

import edu.java.scrapper.client.GithubClient;
import edu.java.scrapper.model.LinkDTO;
import org.springframework.stereotype.Service;

@Service
public class GithubLinkUpdateProcessorService implements LinkUpdateProcessorService {
    private final GithubClient githubClient;
    private final static int PATH_LENGTH = 3;

    public GithubLinkUpdateProcessorService(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    @Override
    public String process(LinkDTO link) {
        var uri = link.getUrl();
        var host = uri.getHost();
        var split = uri.getPath().split("/");
        String message = "Появились изменения в репозитории.";

        if (host == null || !host.equals("github.com") || split.length != PATH_LENGTH) {
            return null;
        }

        var response = githubClient.fetchRepository(split[1], split[2]);

        if (response.lastUpdateDate().isAfter(link.getLastCheck())) {
            message += checkCommits(link, split[1], split[2]);
            return message;
        }

        return "";
    }

    private String checkCommits(LinkDTO link, String owner, String repository) {
        var commits = githubClient.fetchCommits(owner, repository)
            .stream()
            .filter((c) -> c.commit().author().commitDate().isAfter(link.getLastCheck()))
            .toList();

        StringBuilder builder;

        if (commits.isEmpty()) {
            return "";
        }

        builder = new StringBuilder("\n\nНовые коммиты:\n");

        for (var commit : commits.reversed()) {
            builder.append(commit.commit().message()).append("\n");
        }

        return builder.toString();
    }
}
