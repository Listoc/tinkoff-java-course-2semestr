package edu.java.scrapper.jdbc;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.repository.jdbc.JdbcTgChatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JdbcTgChatRepositoryTest extends IntegrationTest {
    private final JdbcTemplate jdbcTemplate;
    private final JdbcLinkRepository jdbcLinkRepository;
    private final JdbcTgChatRepository jdbcTgChatRepository;

    @Autowired
    public JdbcTgChatRepositoryTest(
        JdbcTemplate jdbcTemplate,
        JdbcLinkRepository jdbcLinkRepository,
        JdbcTgChatRepository jdbcTgChatRepository
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcLinkRepository = jdbcLinkRepository;
        this.jdbcTgChatRepository = jdbcTgChatRepository;
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
        jdbcTgChatRepository.addChat(5);

        var getChat = "SELECT * FROM chat WHERE chat_id = ?;";

        var chats = jdbcTemplate.query(getChat, JdbcTgChatRepository.getTgChatRowMapper(), 5);

        assertThat(chats.size()).isEqualTo(1);
        assertThat(chats.getFirst().getChatId()).isEqualTo(5);
    }

    @Test
    @Transactional
    @Rollback
    public void removeTest() {
        jdbcTgChatRepository.addChat(5);

        var getChat = "SELECT * FROM chat WHERE chat_id = ?;";

        var chats = jdbcTemplate.query(getChat, JdbcTgChatRepository.getTgChatRowMapper(), 5);

        assertThat(chats.size()).isEqualTo(1);

        jdbcTgChatRepository.removeChat(5);

        chats = jdbcTemplate.query(getChat, JdbcTgChatRepository.getTgChatRowMapper(), 5);

        assertThat(chats.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    public void findChatTest() {
        jdbcTgChatRepository.addChat(5);

        var chat = jdbcTgChatRepository.findChatById(5).get();

        assertThat(chat.getChatId()).isEqualTo(5);
    }

    @Test
    @Transactional
    @Rollback
    public void findAllTest() {
        jdbcTgChatRepository.addChat(5);
        jdbcTgChatRepository.addChat(6);

        var chats = jdbcTgChatRepository.findAll();

        var findAll = "SELECT * FROM chat";

        var expectedChats = jdbcTemplate.query(findAll, JdbcTgChatRepository.getTgChatRowMapper());

        assertThat(chats).containsExactlyInAnyOrderElementsOf(expectedChats);
    }

    @Test
    @Transactional
    @Rollback
    public void findAllByLinkIdTest() {
        jdbcTgChatRepository.addChat(5);
        jdbcTgChatRepository.addChat(6);
        jdbcTgChatRepository.addChat(7);

        jdbcLinkRepository.addLink("testlink");

        var link = jdbcLinkRepository.findLinkByUrl("testlink").get();

        jdbcLinkRepository.addChatLinkMapping(5, link.getLinkId());
        jdbcLinkRepository.addChatLinkMapping(6, link.getLinkId());

        var chats = jdbcTgChatRepository.findAllByLinkId(link.getLinkId());

        assertThat(chats.size()).isEqualTo(2);
    }

}
