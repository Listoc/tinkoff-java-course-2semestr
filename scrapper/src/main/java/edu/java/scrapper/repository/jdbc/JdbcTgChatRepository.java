package edu.java.scrapper.repository.jdbc;

import edu.java.scrapper.model.ChatDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTgChatRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ChatDTO> rowMapper = getTgChatRowMapper();

    public JdbcTgChatRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addChat(long tgChatId) {
        var addChat = "INSERT INTO chat(chat_id) VALUES (?);";

        jdbcTemplate.update(addChat, tgChatId);
    }

    public void removeChat(long tgChatId) {
        var removeChat = "DELETE FROM chat WHERE chat_id = ?;";

        jdbcTemplate.update(removeChat, tgChatId);
    }

    public Optional<ChatDTO> findChatById(long tgChatId) {
        var findChat = "SELECT * FROM chat WHERE chat_id = ?;";
        var chatList = jdbcTemplate.query(findChat, rowMapper, tgChatId);

        if (chatList.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(chatList.getFirst());
    }

    public List<ChatDTO> findAllByLinkId(long linkId) {
        var findAllChats =
            "SELECT * FROM chat c JOIN chat_link_map clm ON c.chat_id = clm.chat_id WHERE clm.link_id = ?;";
        return jdbcTemplate.query(findAllChats, rowMapper, linkId);
    }

    public List<ChatDTO> findAll() {
        var findAllChats = "SELECT * FROM chat";
        return jdbcTemplate.query(findAllChats, rowMapper);
    }

    public static RowMapper<ChatDTO> getTgChatRowMapper() {
        return (r, i) -> {
            var tgChat = new ChatDTO();
            tgChat.setChatId(r.getLong("chat_id"));
            return tgChat;
        };
    }
}
