package edu.java.scrapper.service.jooq;

import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.client.GithubClient;
import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.model.ChatDTO;
import edu.java.scrapper.repository.jooq.JooqLinkRepository;
import edu.java.scrapper.repository.jooq.JooqTgChatRepository;
import edu.java.scrapper.service.LinkUpdater;
import edu.java.shared.model.LinkUpdateRequest;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class JooqLinkUpdater implements LinkUpdater {
    private final JooqLinkRepository jooqLinkRepository;
    private final JooqTgChatRepository jooqTgChatRepository;
    private final BotClient botClient;
    private final StackOverflowClient stackOverflowClient;
    private final GithubClient githubClient;
    private final static int GITHUB_PATH_LENGTH = 3;
    private final static int SOF_PATH_LENGTH = 4;

    public JooqLinkUpdater(
        JooqLinkRepository jooqLinkRepository,
        JooqTgChatRepository jooqTgChatRepository,
        BotClient botClient,
        StackOverflowClient stackOverflowClient,
        GithubClient githubClient
    ) {
        this.jooqLinkRepository = jooqLinkRepository;
        this.jooqTgChatRepository = jooqTgChatRepository;
        this.botClient = botClient;
        this.stackOverflowClient = stackOverflowClient;
        this.githubClient = githubClient;
    }

    @Override
    public void update() {
        var links = jooqLinkRepository.findAllBeforeDate(OffsetDateTime.now().minusMinutes(2));
        URI url;
        StringBuilder description;

        for (var link : links) {
            var chats = tgChatListToLongArray(jooqTgChatRepository.findAllByLinkId(link.getLinkId()));

            if (chats.length == 0) {
                jooqLinkRepository.removeLinkByUrl(link.getUrl().toString());
                continue;
            }

            url = link.getUrl();

            if (url.getHost() == null) {
                continue;
            }

            if (link.getUrl().getHost().equals("github.com")) {
                var split = url.getPath().split("/");

                if (split.length != GITHUB_PATH_LENGTH) {
                    throw new IllegalArgumentException("Bad github link");
                }

                var response = githubClient.fetchRepository(split[1], split[2]);

                if (response.lastUpdateDate().isAfter(link.getLastCheck())) {
                    var commits = githubClient.fetchCommits(split[1], split[2])
                        .stream()
                        .filter((c) -> c.commit().author().commitDate().isAfter(link.getLastCheck()))
                        .toList();

                    if (commits.isEmpty()) {
                        description = new StringBuilder("Появились изменения в репозитории");
                    } else {
                        description = new StringBuilder("В репозитории появились новые коммиты:\n");
                        for (var commit : commits) {
                            description.append(commit.commit().message()).append("\n");
                        }
                    }

                    botClient.sendUpdates(
                        new LinkUpdateRequest(
                            link.getLinkId(),
                            link.getUrl(),
                                description.toString(),
                            chats)
                    );
                }
            } else if (link.getUrl().getHost().equals("stackoverflow.com")) {
                var split = url.getPath().split("/");

                if (split.length != SOF_PATH_LENGTH) {
                    throw new IllegalArgumentException("Bad sof link");
                }

                var response = stackOverflowClient.fetchQuestion(Integer.parseInt(split[2]));

                if (response.lastUpdateDate().isAfter(link.getLastCheck())) {
                    var answers = stackOverflowClient.fetchAnswers(Integer.parseInt(split[2]))
                        .stream()
                        .filter((a) -> a.answerDate().isAfter(link.getLastCheck()))
                        .toList();

                    if (answers.isEmpty()) {
                        description = new StringBuilder("Появились изменения в вопросе");
                    } else {
                        description = new StringBuilder("В вопросе появились новые ответы:\n");
                        for (var answer : answers) {
                            description.append(answer.title()).append("\n");
                        }
                    }

                    botClient.sendUpdates(
                        new LinkUpdateRequest(
                            link.getLinkId(),
                            link.getUrl(),
                                description.toString(),
                            chats)
                    );
                }
            } else {
                jooqLinkRepository.removeLinkByUrl(link.getUrl().toString());
                botClient.sendUpdates(
                    new LinkUpdateRequest(
                        link.getLinkId(),
                        link.getUrl(),
                        "Ссылка недействительна и была удалена",
                        chats)
                );
            }
            jooqLinkRepository.updateLinkLastCheckDate(link.getLinkId());
        }
    }

    private static long[] tgChatListToLongArray(List<ChatDTO> list) {
        var array = new long[list.size()];

        for (int i = 0; i < list.size(); ++i) {
            array[i] = list.get(i).getChatId();
        }

        return array;
    }
}
