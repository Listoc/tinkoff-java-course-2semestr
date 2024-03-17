package edu.java.scrapper.jdbc;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.repository.jdbc.JdbcTgChatRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JdbcLinkRepositoryTest extends IntegrationTest {
    private final JdbcTemplate jdbcTemplate;
    private final JdbcLinkRepository jdbcLinkRepository;

    @Autowired
    public JdbcLinkRepositoryTest(
        JdbcTemplate jdbcTemplate,
        JdbcLinkRepository jdbcLinkRepository
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcLinkRepository = jdbcLinkRepository;
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
        jdbcLinkRepository.addLink("testlink");

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
        jdbcLinkRepository.addLink("testlink");

        var link = jdbcLinkRepository.findLinkByUrl("testlink");
        var date = link.getLastCheckTime();

        Thread.sleep(1500);

        jdbcLinkRepository.updateLinkLastCheckDate(link.getLinkId());

        link = jdbcLinkRepository.findLinkByUrl("testlink");
        var date2 = link.getLastCheckTime();

        assertThat(date).isBefore(date2);
    }

    @Test
    @Transactional
    @Rollback
    public void removeTest() {
        jdbcLinkRepository.addLink("testlink");

        var getLink = "SELECT * FROM link WHERE url = ?;";

        var links = jdbcTemplate.query(getLink, JdbcLinkRepository.getLinkRowMapper(), "testlink");

        assertThat(links.size()).isEqualTo(1);

        jdbcLinkRepository.removeLinkByUrl("testlink");

        links = jdbcTemplate.query(getLink, JdbcLinkRepository.getLinkRowMapper(), "testlink");

        assertThat(links.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    public void findLinkTest() {
        jdbcLinkRepository.addLink("testlink");

        var link = jdbcLinkRepository.findLinkByUrl("testlink");

        assertThat(link.getUrl()).isEqualTo(URI.create("testlink"));
        assertThat(link.getLastCheckTime()).isBetween(OffsetDateTime.now().minusSeconds(1), OffsetDateTime.now().plusSeconds(1));
    }

    @Test
    @Transactional
    @Rollback
    public void addChatLinkMappingTest() {
        jdbcLinkRepository.addLink("testlink");

        var link = jdbcLinkRepository.findLinkByUrl("testlink");

        jdbcLinkRepository.addChatLinkMapping(5, link.getLinkId());

        var getMapping = "SELECT chat_id FROM chat_link_map WHERE link_id = ?;";

        var chats = jdbcTemplate.query(getMapping, JdbcTgChatRepository.getTgChatRowMapper(), link.getLinkId());

        assertThat(chats.getFirst().getChatId()).isEqualTo(5);
    }

    @Test
    @Transactional
    @Rollback
    public void removeChatLinkMappingTest() {
        jdbcLinkRepository.addLink("testlink");

        var link = jdbcLinkRepository.findLinkByUrl("testlink");

        jdbcLinkRepository.addChatLinkMapping(5, link.getLinkId());

        var getMappingsCount = "SELECT count(*) FROM chat_link_map;";

        var count = jdbcTemplate.query(getMappingsCount, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt(1);
            }
        });

        assertThat(count.getFirst()).isEqualTo(1);

        jdbcLinkRepository.removeChatLinkMapping(5, link.getLinkId());

        count = jdbcTemplate.query(getMappingsCount, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt(1);
            }
        });

        assertThat(count.getFirst()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    public void findAllTest() {
        jdbcLinkRepository.addLink("testlink");
        jdbcLinkRepository.addLink("anotherlink");

        var links = jdbcLinkRepository.findAll();

        var findAll = "SELECT * FROM link";

        var expectedLinks = jdbcTemplate.query(findAll, JdbcLinkRepository.getLinkRowMapper());

        assertThat(links).containsExactlyInAnyOrderElementsOf(expectedLinks);
    }

    @Test
    @Transactional
    @Rollback
    public void findAllByChatIdTest() {
        jdbcLinkRepository.addLink("testlink");
        jdbcLinkRepository.addLink("anotherlink");
        jdbcLinkRepository.addLink("finallink");

        var link1 = jdbcLinkRepository.findLinkByUrl("testlink");
        var link2 = jdbcLinkRepository.findLinkByUrl("anotherlink");

        jdbcLinkRepository.addChatLinkMapping(5, link1.getLinkId());
        jdbcLinkRepository.addChatLinkMapping(5, link2.getLinkId());

        var links = jdbcLinkRepository.findAllByChatId(5);

        assertThat(links.size()).isEqualTo(2);
        assertThat(links).containsExactlyInAnyOrderElementsOf(List.of(link1, link2));
    }

}
