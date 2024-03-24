package edu.java.scrapper.service.proccesor;

import edu.java.scrapper.client.GithubClient;
import edu.java.scrapper.model.Link;
import org.springframework.stereotype.Service;

@Service
public class GithubLinkUpdateProcessorService implements LinkUpdateProcessorService {
    private final GithubClient githubClient;
    private final static int PATH_LENGTH = 3;

    public GithubLinkUpdateProcessorService(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    @Override
    public String process(Link link) {
        var uri = link.getUrl();
        var host = uri.getHost();
        var split = uri.getPath().split("/");

        if (host == null || !host.equals("github.com") || split.length != PATH_LENGTH) {
            return null;
        }

        var response = githubClient.fetchRepository(split[1], split[2]);

        if (response.lastUpdateDate().isAfter(link.getLastCheckTime())) {
            return "Появились изменения в репозитории.";
        }

        return "";
    }
}
