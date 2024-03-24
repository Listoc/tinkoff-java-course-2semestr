package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.exception.ChatNotExistException;
import edu.java.scrapper.model.ChatDTO;
import edu.java.scrapper.repository.jdbc.JdbcTgChatRepository;
import edu.java.scrapper.service.TgChatService;
import edu.java.scrapper.exception.CantAddToDBException;
import edu.java.scrapper.repository.jdbc.JdbcTgChatRepository;
import edu.java.scrapper.service.TgChatService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public class JdbcTgChatService implements TgChatService {
    private final JdbcTgChatRepository jdbcTgChatRepository;

    public JdbcTgChatService(JdbcTgChatRepository jdbcTgChatRepository) {
        this.jdbcTgChatRepository = jdbcTgChatRepository;
    }

    @Transactional
    public ChatDTO register(long tgChatId) {
        var tgChat = jdbcTgChatRepository.findChatById(tgChatId);

        if (tgChat.isEmpty()) {
            jdbcTgChatRepository.addChat(tgChatId);
            tgChat = jdbcTgChatRepository.findChatById(tgChatId);
        }

        return tgChat.orElseThrow(() -> new CantAddToDBException("Cant add new chat to DB"));
    }

    @Transactional
    public void unregister(long tgChatId) {
        var tgChat = jdbcTgChatRepository.findChatById(tgChatId);

        tgChat.ifPresent(value -> jdbcTgChatRepository.removeChat(value.getChatId()));
    }

    @Transactional
    public List<ChatDTO> getChats(long linkId) {
        return jdbcTgChatRepository.findAllByLinkId(linkId);
    }
}
