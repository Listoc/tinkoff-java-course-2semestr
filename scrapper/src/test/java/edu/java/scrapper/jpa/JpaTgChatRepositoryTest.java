package edu.java.scrapper.jpa;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.repository.jdbc.JdbcTgChatRepository;
import edu.java.scrapper.repository.jpa.JpaChat;
import edu.java.scrapper.repository.jpa.JpaLinkRepository;
import edu.java.scrapper.repository.jpa.JpaTgChatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JpaTgChatRepositoryTest extends IntegrationTest {
    private final JdbcTemplate jdbcTemplate;
    private final JpaTgChatRepository jpaTgChatRepository;

    @Autowired
    public JpaTgChatRepositoryTest(
        JdbcTemplate jdbcTemplate,
        JpaTgChatRepository jpaTgChatRepository
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.jpaTgChatRepository = jpaTgChatRepository;
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
        var jpaChat = new JpaChat();
        jpaChat.setChatId(5);
        jpaTgChatRepository.saveAndFlush(jpaChat);

        var getChat = "SELECT * FROM chat WHERE chat_id = ?;";

        var chats = jdbcTemplate.query(getChat, JdbcTgChatRepository.getTgChatRowMapper(), 5);

        assertThat(chats.size()).isEqualTo(1);
        assertThat(chats.getFirst().getChatId()).isEqualTo(5);
    }

    @Test
    @Transactional
    @Rollback
    public void removeTest() {
        var jpaChat = new JpaChat();
        jpaChat.setChatId(5);
        jpaTgChatRepository.saveAndFlush(jpaChat);

        assertThat(jpaTgChatRepository.findAll().size()).isEqualTo(1);

        jpaTgChatRepository.delete(jpaChat);

        assertThat(jpaTgChatRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    public void findChatTest() {
        var jpaChat = new JpaChat();
        jpaChat.setChatId(5);
        jpaTgChatRepository.saveAndFlush(jpaChat);

        var chat = jpaTgChatRepository.findById(5L).get();

        assertThat(chat.getChatId()).isEqualTo(5);
    }
}
