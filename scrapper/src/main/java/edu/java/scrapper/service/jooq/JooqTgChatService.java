package edu.java.scrapper.service.jooq;

import edu.java.scrapper.exception.ChatNotExistException;
import edu.java.scrapper.model.ChatDTO;
import edu.java.scrapper.repository.jooq.JooqTgChatRepository;
import edu.java.scrapper.service.TgChatService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JooqTgChatService implements TgChatService {
    private final JooqTgChatRepository jooqTgChatRepository;

    public JooqTgChatService(JooqTgChatRepository jooqTgChatRepository) {
        this.jooqTgChatRepository = jooqTgChatRepository;
    }

    @Transactional
    public ChatDTO register(long tgChatId) {
        var tgChat = jooqTgChatRepository.findChatById(tgChatId);

        if (tgChat == null) {
            jooqTgChatRepository.addChat(tgChatId);
            tgChat = jooqTgChatRepository.findChatById(tgChatId);
        }

        return tgChat;
    }

    @Transactional
    public void unregister(long tgChatId) {
        var tgChat = jooqTgChatRepository.findChatById(tgChatId);

        if (tgChat == null) {
            throw new ChatNotExistException("No such chat in DB");
        }

        jooqTgChatRepository.removeChat(tgChatId);
    }
}
