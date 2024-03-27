package edu.java.scrapper.service.jpa;

import edu.java.scrapper.exception.ChatAlreadyExistException;
import edu.java.scrapper.model.ChatDTO;
import edu.java.scrapper.repository.jpa.JpaChat;
import edu.java.scrapper.repository.jpa.JpaLinkRepository;
import edu.java.scrapper.repository.jpa.JpaTgChatRepository;
import edu.java.scrapper.service.TgChatService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public class JpaTgChatService implements TgChatService {
    private final JpaLinkRepository jpaLinkRepository;
    private final JpaTgChatRepository jpaTgChatRepository;

    public JpaTgChatService(JpaLinkRepository jpaLinkRepository, JpaTgChatRepository jpaTgChatRepository) {
        this.jpaLinkRepository = jpaLinkRepository;
        this.jpaTgChatRepository = jpaTgChatRepository;
    }

    @Transactional
    public ChatDTO register(long tgChatId) {
        var optionalChat = jpaTgChatRepository.findById(tgChatId);
        JpaChat chat;

        if (optionalChat.isEmpty()) {
            chat = new JpaChat();

            chat.setChatId(tgChatId);
        } else {
            throw new ChatAlreadyExistException("Chat already exists!");
        }

        jpaTgChatRepository.save(chat);

        return mapToDto(chat);
    }

    @Transactional
    public void unregister(long tgChatId) {
        var chat = jpaTgChatRepository.findById(tgChatId);

        chat.ifPresent(jpaTgChatRepository::delete);
    }

    @Transactional
    public List<ChatDTO> getChats(long linkId) {
        var link = jpaLinkRepository.findById(linkId);

        return link.map(jpaLink -> mapToDto(jpaLink.getChats())).orElse(null);

    }

    public ChatDTO mapToDto(JpaChat jpaChat) {
        var chat = new ChatDTO();

        chat.setChatId(jpaChat.getChatId());

        return chat;
    }

    public List<ChatDTO> mapToDto(List<JpaChat> jpaChats) {
        var chats = new ArrayList<ChatDTO>();
        ChatDTO chat;

        for (var jpaChat : jpaChats) {
            chat = new ChatDTO();

            chat.setChatId(jpaChat.getChatId());

            chats.add(chat);
        }

        return chats;
    }
}
