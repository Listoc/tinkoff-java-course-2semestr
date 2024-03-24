package edu.java.scrapper.jpa;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.repository.jdbc.JdbcTgChatRepository;
import edu.java.scrapper.repository.jpa.JpaLink;
import edu.java.scrapper.repository.jpa.JpaLinkRepository;
import edu.java.scrapper.repository.jpa.JpaTgChatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JpaLinkRepositoryTest extends IntegrationTest {
    private final JdbcTemplate jdbcTemplate;
    private final JpaLinkRepository jpaLinkRepository;
    private final JpaTgChatRepository jpaTgChatRepository;

    @Autowired
    public JpaLinkRepositoryTest(
            JdbcTemplate jdbcTemplate,
            JpaLinkRepository jpaLinkRepository, JpaTgChatRepository jpaTgChatRepository
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.jpaLinkRepository = jpaLinkRepository;
        this.jpaTgChatRepository = jpaTgChatRepository;
    }

    @BeforeEach
    public void truncate() {
        jdbcTemplate.update("TRUNCATE TABLE chat CASCADE;");
        jdbcTemplate.update("TRUNCATE TABLE link CASCADE;");
        jdbcTemplate.update("TRUNCATE TABLE chat_link_map CASCADE;");

        var addChat = "INSERT INTO chat VALUES (?)";

        jdbcTemplate.update(addChat, 5);

        var jpaLink = new JpaLink();
        jpaLink.setUrl("testlink");
        jpaLink.setLastCheck(OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        jpaLinkRepository.save(jpaLink);
    }

    @Test
    @Transactional
    @Rollback
    public void addTest() {
        var getLink = "SELECT * FROM link WHERE url = ?;";

        var links = jdbcTemplate.query(getLink, JdbcLinkRepository.getLinkRowMapper(), "testlink");

        assertThat(links.size()).isEqualTo(1);
        assertThat(links.getFirst().getUrl()).isEqualTo(URI.create("testlink"));
        assertThat(links.getFirst().getLastCheckTime()).isBetween(OffsetDateTime.now().minusSeconds(5), OffsetDateTime.now().plusSeconds(5));
    }

    @Test
    @Transactional
    @Rollback
    public void updateDateTest() throws InterruptedException {
        var link = jpaLinkRepository.findByUrl("testlink").get();
        var date = link.getLastCheck();

        Thread.sleep(1500);

        link.setLastCheck(OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        jpaLinkRepository.save(link);

        link = jpaLinkRepository.findByUrl("testlink").get();
        var date2 = link.getLastCheck();

        assertThat(date).isBefore(date2);
    }

    @Test
    @Transactional
    @Rollback
    public void removeTest() throws InterruptedException {
        var jpaLink = jpaLinkRepository.findByUrl("testlink").get();

        assertThat(jpaLinkRepository.findAll().size()).isEqualTo(1);

        jpaLinkRepository.delete(jpaLink);

        assertThat(jpaLinkRepository.findAll().size()).isEqualTo(0);
    }


    @Test
    @Transactional
    @Rollback
    public void addChatLinkMappingTest() {
        var link = jpaLinkRepository.findByUrl("testlink").get();
        var chat = jpaTgChatRepository.findById(5L).get();

        chat.addLink(link);
        jpaTgChatRepository.saveAndFlush(chat);

        var getMapping = "SELECT chat_id FROM chat_link_map WHERE link_id = ?;";

        var chats = jdbcTemplate.query(getMapping, JdbcTgChatRepository.getTgChatRowMapper(), link.getLinkId());

        assertThat(chats.getFirst().getChatId()).isEqualTo(5);
    }

    @Test
    @Transactional
    @Rollback
    public void removeChatLinkMappingTest() {
        var link = jpaLinkRepository.findByUrl("testlink").get();
        var chat = jpaTgChatRepository.findById(5L).get();

        chat.addLink(link);
        jpaTgChatRepository.saveAndFlush(chat);

        var getMappingsCount = "SELECT count(*) FROM chat_link_map;";

        var count = jdbcTemplate.query(getMappingsCount, (rs, rowNum) -> rs.getInt(1));

        assertThat(count.getFirst()).isEqualTo(1);

        chat.removeLink(link);
        jpaTgChatRepository.saveAndFlush(chat);

        count = jdbcTemplate.query(getMappingsCount, (rs, rowNum) -> rs.getInt(1));

        assertThat(count.getFirst()).isEqualTo(0);
    }
}
