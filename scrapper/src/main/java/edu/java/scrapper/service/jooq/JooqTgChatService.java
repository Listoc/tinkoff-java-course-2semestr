package edu.java.scrapper.service.jooq;

import edu.java.scrapper.exception.CantAddToDBException;
import edu.java.scrapper.model.ChatDTO;
import edu.java.scrapper.repository.jooq.JooqTgChatRepository;
import edu.java.scrapper.service.TgChatService;
import java.util.List;
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

        if (tgChat.isEmpty()) {
            jooqTgChatRepository.addChat(tgChatId);
            tgChat = jooqTgChatRepository.findChatById(tgChatId);
        }

        return tgChat.orElseThrow(() -> new CantAddToDBException("Cant add new chat to DB"));
    }

    @Transactional
    public void unregister(long tgChatId) {
        var tgChat = jooqTgChatRepository.findChatById(tgChatId);

        tgChat.ifPresent(value -> jooqTgChatRepository.removeChat(value.getChatId()));
    }

    @Override
    public List<ChatDTO> getChats(long linkId) {
        return jooqTgChatRepository.findAllByLinkId(linkId);
    }
}
