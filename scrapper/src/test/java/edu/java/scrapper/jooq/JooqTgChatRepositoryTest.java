package edu.java.scrapper.jooq;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.repository.jdbc.JdbcTgChatRepository;
import edu.java.scrapper.repository.jooq.JooqLinkRepository;
import edu.java.scrapper.repository.jooq.JooqTgChatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JooqTgChatRepositoryTest extends IntegrationTest {
    private final JdbcTemplate jdbcTemplate;
    private final JooqLinkRepository jooqLinkRepository;
    private final JooqTgChatRepository jooqTgChatRepository;

    @Autowired
    public JooqTgChatRepositoryTest(
        JdbcTemplate jdbcTemplate,
        JooqLinkRepository jooqLinkRepository,
        JooqTgChatRepository jooqTgChatRepository
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.jooqLinkRepository = jooqLinkRepository;
        this.jooqTgChatRepository = jooqTgChatRepository;
    }

    @BeforeEach
    public void truncate() {
        jdbcTemplate.update("TRUNCATE TABLE chat CASCADE;");
        jdbcTemplate.update("TRUNCATE TABLE link CASCADE;");
        jdbcTemplate.update("TRUNCATE TABLE chat_link_map CASCADE;");
    }

    @Test
    @Transactional
    @Rollback
    public void addTest() {
        jooqTgChatRepository.addChat(5);

        var getChat = "SELECT * FROM chat WHERE chat_id = ?;";

        var chats = jdbcTemplate.query(getChat, JdbcTgChatRepository.getTgChatRowMapper(), 5);

        assertThat(chats.size()).isEqualTo(1);
        assertThat(chats.getFirst().getChatId()).isEqualTo(5);
    }

    @Test
    @Transactional
    @Rollback
    public void removeTest() {
        jooqTgChatRepository.addChat(5);

        var getChat = "SELECT * FROM chat WHERE chat_id = ?;";

        var chats = jdbcTemplate.query(getChat, JdbcTgChatRepository.getTgChatRowMapper(), 5);

        assertThat(chats.size()).isEqualTo(1);

        jooqTgChatRepository.removeChat(5);

        chats = jdbcTemplate.query(getChat, JdbcTgChatRepository.getTgChatRowMapper(), 5);

        assertThat(chats.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    public void findChatTest() {
        jooqTgChatRepository.addChat(5);

        var chat = jooqTgChatRepository.findChatById(5).get();

        assertThat(chat.getChatId()).isEqualTo(5);
    }

    @Test
    @Transactional
    @Rollback
    public void findAllTest() {
        jooqTgChatRepository.addChat(5);
        jooqTgChatRepository.addChat(6);

        var chats = jooqTgChatRepository.findAll();

        var findAll = "SELECT * FROM chat";

        var expectedChats = jdbcTemplate.query(findAll, JdbcTgChatRepository.getTgChatRowMapper());

        assertThat(chats).containsExactlyInAnyOrderElementsOf(expectedChats);
    }

    @Test
    @Transactional
    @Rollback
    public void findAllByLinkIdTest() {
        jooqTgChatRepository.addChat(5);
        jooqTgChatRepository.addChat(6);
        jooqTgChatRepository.addChat(7);

        jooqLinkRepository.addLink("testlink");

        var link = jooqLinkRepository.findLinkByUrl("testlink").get();

        jooqLinkRepository.addChatLinkMapping(5, link.getLinkId());
        jooqLinkRepository.addChatLinkMapping(6, link.getLinkId());

        var chats = jooqTgChatRepository.findAllByLinkId(link.getLinkId());

        assertThat(chats.size()).isEqualTo(2);
    }

}
