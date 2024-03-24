package edu.java.scrapper.repository.jdbc;

import edu.java.scrapper.model.LinkDTO;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcLinkRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<LinkDTO> rowMapper = getLinkRowMapper();

    public JdbcLinkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addLink(String url) {
        var addLink = "INSERT INTO link(url, last_check) VALUES (?, ?);";

        jdbcTemplate.update(addLink, url, OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    }

    public void addChatLinkMapping(long tgChatId, long linkId) {
        var addMap = "INSERT INTO chat_link_map(chat_id, link_id) VALUES (?, ?) ON CONFLICT DO NOTHING;";
        jdbcTemplate.update(addMap, tgChatId, linkId);
    }

    public void removeChatLinkMapping(long tgChatId, long linkId) {
        var addMap = "DELETE FROM chat_link_map WHERE chat_id = ? AND link_id = ?;";
        jdbcTemplate.update(addMap, tgChatId, linkId);
    }

    public Optional<LinkDTO> findLinkByUrl(String url) {
        var findLink = "SELECT * FROM link WHERE url = ?;";
        var linkList = jdbcTemplate.query(findLink, rowMapper, url);

        if (linkList.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(linkList.getFirst());
    }

    public void removeLinkByUrl(String url) {
        var removeLink = "DELETE FROM link WHERE url = ?;";

        jdbcTemplate.update(removeLink, url);
    }

    public List<LinkDTO> findAll() {
        var findAllLinks = "SELECT * FROM link";
        return jdbcTemplate.query(findAllLinks, rowMapper);
    }

    public List<LinkDTO> findAllByChatId(long tgChatId) {
        var findAllLinks =
            "SELECT * FROM link l JOIN chat_link_map clm ON l.link_id = clm.link_id WHERE clm.chat_id = ?;";
        return jdbcTemplate.query(findAllLinks, rowMapper, tgChatId);
    }

    public List<LinkDTO> findAllBeforeDateTime(OffsetDateTime dateTime) {
        var findAllLinks = "SELECT * FROM link WHERE last_check < ?;";
        return jdbcTemplate.query(findAllLinks, rowMapper, dateTime);
    }

    public void updateLinkLastCheckDate(long linkId) {
        var removeLink = "UPDATE link SET last_check = ? WHERE link_id = ?;";

        jdbcTemplate.update(removeLink, OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS), linkId);
    }

    public static RowMapper<LinkDTO> getLinkRowMapper() {
        return (r, i) -> {
            var link = new LinkDTO();
            var formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ssX");
            link.setLinkId(r.getLong("link_id"));
            link.setUrl(URI.create(r.getString("url")));
            link.setLastCheck(OffsetDateTime.parse(r.getString("last_check"), formatter));
            return link;
        };
    }
}
