package edu.java.scrapper.configuration;

import edu.java.scrapper.repository.jooq.JooqLinkRepository;
import edu.java.scrapper.repository.jooq.JooqTgChatRepository;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.TgChatService;
import edu.java.scrapper.service.jooq.JooqLinkService;
import edu.java.scrapper.service.jooq.JooqTgChatService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {
    @Bean
    public LinkService linkService(
        JooqLinkRepository linkRepository,
        JooqTgChatRepository tgChatRepository
    ) {
        return new JooqLinkService(linkRepository, tgChatRepository);
    }

    @Bean
    public TgChatService tgChatService(
        JooqTgChatRepository tgChatRepository
    ) {
        return new JooqTgChatService(tgChatRepository);
    }
}
