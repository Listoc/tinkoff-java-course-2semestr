package edu.java.scrapper.service.jooq;

import edu.java.scrapper.exception.CantAddToDBException;
import edu.java.scrapper.exception.ChatNotExistException;
import edu.java.scrapper.model.LinkDTO;
import edu.java.scrapper.repository.jooq.JooqLinkRepository;
import edu.java.scrapper.repository.jooq.JooqTgChatRepository;
import edu.java.scrapper.service.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public class JooqLinkService implements LinkService {
    private final JooqLinkRepository jooqLinkRepository;
    private final JooqTgChatRepository jooqTgChatRepository;
    private final static String CHAT_IS_NOT_REGISTERED_MESSAGE = "Chat is not registered";

    public JooqLinkService(JooqLinkRepository jooqLinkRepository, JooqTgChatRepository jooqTgChatRepository) {
        this.jooqLinkRepository = jooqLinkRepository;
        this.jooqTgChatRepository = jooqTgChatRepository;
    }

    @Transactional
    public LinkDTO add(long tgChatId, URI url) {
        var stringUrl = url.toString();
        var chat = jooqTgChatRepository.findChatById(tgChatId);

        if (chat.isEmpty()) {
            throw new ChatNotExistException(CHAT_IS_NOT_REGISTERED_MESSAGE);
        }

        var link = jooqLinkRepository.findLinkByUrl(stringUrl);

        if (link.isEmpty()) {
            jooqLinkRepository.addLink(stringUrl);
            link = jooqLinkRepository.findLinkByUrl(stringUrl);
        }

        var linkValue = link.orElseThrow(() -> new CantAddToDBException("Cant add link to DB"));

        jooqLinkRepository.addChatLinkMapping(tgChatId, linkValue.getLinkId());
        linkValue.setTgChatList(jooqTgChatRepository.findAllByLinkId(linkValue.getLinkId()));

        return linkValue;
    }

    @Transactional
    public void remove(long tgChatId, URI url) {
        var stringUrl = url.toString();
        var chat = jooqTgChatRepository.findChatById(tgChatId);

        if (chat.isEmpty()) {
            throw new ChatNotExistException(CHAT_IS_NOT_REGISTERED_MESSAGE);
        }

        var link = jooqLinkRepository.findLinkByUrl(stringUrl);

        link.ifPresent(value -> jooqLinkRepository.removeChatLinkMapping(tgChatId, value.getLinkId()));
    }

    @Transactional
    public List<LinkDTO> getLinks(long tgChatId) {
        var chat = jooqTgChatRepository.findChatById(tgChatId);

        if (chat.isEmpty()) {
            throw new ChatNotExistException(CHAT_IS_NOT_REGISTERED_MESSAGE);
        }

        return jooqLinkRepository.findAllByChatId(tgChatId);
    }

    @Override
    public List<LinkDTO> getLinksBeforeDateTime(OffsetDateTime dateTime) {
        return jooqLinkRepository.findAllBeforeDateTime(dateTime);
    }

    @Override
    public void updateLastCheckTime(long linkId) {
        jooqLinkRepository.updateLinkLastCheckDate(linkId);
    }
}
