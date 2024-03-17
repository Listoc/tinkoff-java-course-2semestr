package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.client.GithubClient;
import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.model.TgChat;
import edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.repository.jdbc.JdbcTgChatRepository;
import edu.java.scrapper.service.LinkUpdater;
import edu.java.shared.model.LinkUpdateRequest;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class JdbcLinkUpdater implements LinkUpdater {
    private final JdbcLinkRepository jdbcLinkRepository;
    private final JdbcTgChatRepository jdbcTgChatRepository;
    private final BotClient botClient;
    private final StackOverflowClient stackOverflowClient;
    private final GithubClient githubClient;
    private final static int GITHUB_PATH_LENGTH = 3;
    private final static int SOF_PATH_LENGTH = 4;


    public JdbcLinkUpdater(
        JdbcLinkRepository jdbcLinkRepository,
        JdbcTgChatRepository jdbcTgChatRepository,
        BotClient botClient,
        StackOverflowClient stackOverflowClient,
        GithubClient githubClient
    ) {
        this.jdbcLinkRepository = jdbcLinkRepository;
        this.jdbcTgChatRepository = jdbcTgChatRepository;
        this.botClient = botClient;
        this.stackOverflowClient = stackOverflowClient;
        this.githubClient = githubClient;
    }

    @Override
    public void update() {
        var links = jdbcLinkRepository.findAllBeforeDate(OffsetDateTime.now().minusMinutes(2));
        URI url;

        for (var link : links) {
            var chats = tgChatListToLongArray(jdbcTgChatRepository.findAllByLinkId(link.getLinkId()));

            if (chats.length == 0) {
                jdbcLinkRepository.removeLinkByUrl(link.getUrl().toString());
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
                if (response.lastUpdateDate().isAfter(link.getLastCheckTime())) {
                    botClient.sendUpdates(
                        new LinkUpdateRequest(
                            link.getLinkId(),
                            link.getUrl(),
                            "Появились изменения в репозитории",
                            chats)
                    );
                }
            } else if (link.getUrl().getHost().equals("stackoverflow.com")) {
                var split = url.getPath().split("/");

                if (split.length != SOF_PATH_LENGTH) {
                    throw new IllegalArgumentException("Bad sof link");
                }

                var response = stackOverflowClient.fetchQuestion(Integer.parseInt(split[2]));

                if (response.lastUpdateDate().isAfter(link.getLastCheckTime())) {
                    botClient.sendUpdates(
                        new LinkUpdateRequest(
                            link.getLinkId(),
                            link.getUrl(),
                            "Появились изменения в вопросе",
                            chats)
                    );
                }
            } else {
                jdbcLinkRepository.removeLinkByUrl(link.getUrl().toString());
                botClient.sendUpdates(
                    new LinkUpdateRequest(
                        link.getLinkId(),
                        link.getUrl(),
                        "Ссылка недействительна и была удалена",
                        chats)
                );
            }
            jdbcLinkRepository.updateLinkLastCheckDate(link.getLinkId());
        }
    }

    private static long[] tgChatListToLongArray(List<TgChat> list) {
        var array = new long[list.size()];

        for (int i = 0; i < list.size(); ++i) {
            array[i] = list.get(i).getChatId();
        }

        return array;
    }
}
