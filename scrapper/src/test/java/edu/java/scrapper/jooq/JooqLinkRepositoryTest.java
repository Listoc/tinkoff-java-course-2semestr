package edu.java.scrapper.jooq;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import edu.java.scrapper.repository.jdbc.JdbcTgChatRepository;
import edu.java.scrapper.repository.jooq.JooqLinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JooqLinkRepositoryTest extends IntegrationTest {
    private final JdbcTemplate jdbcTemplate;
    private final JooqLinkRepository jooqLinkRepository;

    @Autowired
    public JooqLinkRepositoryTest(
        JdbcTemplate jdbcTemplate,
        JooqLinkRepository jooqLinkRepository
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.jooqLinkRepository = jooqLinkRepository;
    }

    @BeforeEach
    public void truncate() {
        jdbcTemplate.update("TRUNCATE TABLE chat CASCADE;");
        jdbcTemplate.update("TRUNCATE TABLE link CASCADE;");
        jdbcTemplate.update("TRUNCATE TABLE chat_link_map CASCADE;");

        var addChat = "INSERT INTO chat VALUES (?)";

        jdbcTemplate.update(addChat, 5);
    }

    @Test
    @Transactional
    @Rollback
    public void addTest() {
        jooqLinkRepository.addLink("testlink");

        var getLink = "SELECT * FROM link WHERE url = ?;";

        var links = jdbcTemplate.query(getLink, JdbcLinkRepository.getLinkRowMapper(), "testlink");

        assertThat(links.size()).isEqualTo(1);
        assertThat(links.getFirst().getUrl()).isEqualTo(URI.create("testlink"));
        assertThat(links.getFirst().getLastCheck()).isBetween(OffsetDateTime.now().minusSeconds(5), OffsetDateTime.now().plusSeconds(5));
    }

    @Test
    @Transactional
    @Rollback
    public void updateDateTest() throws InterruptedException {
        jooqLinkRepository.addLink("testlink");

        var link = jooqLinkRepository.findLinkByUrl("testlink").get();
        System.out.println(link);
        var date = link.getLastCheck();

        Thread.sleep(1500);

        jooqLinkRepository.updateLinkLastCheckDate(link.getLinkId());

        link = jooqLinkRepository.findLinkByUrl("testlink").get();
        System.out.println(link);
        var date2 = link.getLastCheck();

        assertThat(date).isBefore(date2);
    }

    @Test
    @Transactional
    @Rollback
    public void removeTest() {
        jooqLinkRepository.addLink("testlink");

        var getLink = "SELECT * FROM link WHERE url = ?;";

        var links = jdbcTemplate.query(getLink, JdbcLinkRepository.getLinkRowMapper(), "testlink");

        assertThat(links.size()).isEqualTo(1);

        jooqLinkRepository.removeLinkByUrl("testlink");

        links = jdbcTemplate.query(getLink, JdbcLinkRepository.getLinkRowMapper(), "testlink");

        assertThat(links.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    public void findLinkTest() {
        jooqLinkRepository.addLink("testlink");

        var link = jooqLinkRepository.findLinkByUrl("testlink").get();

        assertThat(link.getUrl()).isEqualTo(URI.create("testlink"));
        assertThat(link.getLastCheck()).isBetween(OffsetDateTime.now().minusSeconds(2), OffsetDateTime.now().plusSeconds(2));
    }

    @Test
    @Transactional
    @Rollback
    public void addChatLinkMappingTest() {
        jooqLinkRepository.addLink("testlink");

        var link = jooqLinkRepository.findLinkByUrl("testlink").get();

        jooqLinkRepository.addChatLinkMapping(5, link.getLinkId());

        var getMapping = "SELECT chat_id FROM chat_link_map WHERE link_id = ?;";

        var chats = jdbcTemplate.query(getMapping, JdbcTgChatRepository.getTgChatRowMapper(), link.getLinkId());

        assertThat(chats.getFirst().getChatId()).isEqualTo(5);
    }

    @Test
    @Transactional
    @Rollback
    public void removeChatLinkMappingTest() {
        jooqLinkRepository.addLink("testlink");

        var link = jooqLinkRepository.findLinkByUrl("testlink").get();

        jooqLinkRepository.addChatLinkMapping(5, link.getLinkId());

        var getMappingsCount = "SELECT count(*) FROM chat_link_map;";

        var count = jdbcTemplate.query(getMappingsCount, (rs, rowNum) -> rs.getInt(1));

        assertThat(count.getFirst()).isEqualTo(1);

        jooqLinkRepository.removeChatLinkMapping(5, link.getLinkId());

        count = jdbcTemplate.query(getMappingsCount, (rs, rowNum) -> rs.getInt(1));

        assertThat(count.getFirst()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    public void findAllTest() {
        jooqLinkRepository.addLink("testlink");
        jooqLinkRepository.addLink("anotherlink");

        var links = jooqLinkRepository.findAll();

        var findAll = "SELECT * FROM link";

        var expectedLinks = jdbcTemplate.query(findAll, JdbcLinkRepository.getLinkRowMapper());

        assertThat(links).containsExactlyInAnyOrderElementsOf(expectedLinks);
    }

    @Test
    @Transactional
    @Rollback
    public void findAllByChatIdTest() {
        jooqLinkRepository.addLink("testlink");
        jooqLinkRepository.addLink("anotherlink");
        jooqLinkRepository.addLink("finallink");

        var link1 = jooqLinkRepository.findLinkByUrl("testlink").get();
        var link2 = jooqLinkRepository.findLinkByUrl("anotherlink").get();

        jooqLinkRepository.addChatLinkMapping(5, link1.getLinkId());
        jooqLinkRepository.addChatLinkMapping(5, link2.getLinkId());

        var links = jooqLinkRepository.findAllByChatId(5);

        assertThat(links.size()).isEqualTo(2);
        assertThat(links).containsExactlyInAnyOrderElementsOf(List.of(link1, link2));
    }

}
