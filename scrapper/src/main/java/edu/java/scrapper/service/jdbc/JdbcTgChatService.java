package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.exception.ChatNotExistException;
import edu.java.scrapper.model.ChatDTO;
import edu.java.scrapper.repository.jdbc.JdbcTgChatRepository;
import edu.java.scrapper.service.TgChatService;
import org.springframework.transaction.annotation.Transactional;

public class JdbcTgChatService implements TgChatService {
    private final JdbcTgChatRepository jdbcTgChatRepository;

    public JdbcTgChatService(JdbcTgChatRepository jdbcTgChatRepository) {
        this.jdbcTgChatRepository = jdbcTgChatRepository;
    }

    @Transactional
    public ChatDTO register(long tgChatId) {
        var tgChat = jdbcTgChatRepository.findChatById(tgChatId);

        if (tgChat == null) {
            jdbcTgChatRepository.addChat(tgChatId);
            tgChat = jdbcTgChatRepository.findChatById(tgChatId);
        }

        return tgChat;
    }

    @Transactional
    public ChatDTO unregister(long tgChatId) {
        var tgChat = jdbcTgChatRepository.findChatById(tgChatId);

        if (tgChat == null) {
            throw new ChatNotExistException("No such chat in DB");
        }

        jdbcTgChatRepository.removeChat(tgChatId);

        return tgChat;
    }
}
