package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.model.LinkDTO;
import edu.java.scrapper.exception.CantAddToDBException;
import edu.java.scrapper.exception.ChatNotExistException;
import edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.repository.jdbc.JdbcTgChatRepository;
import edu.java.scrapper.service.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public class JdbcLinkService implements LinkService {
    private final JdbcLinkRepository jdbcLinkRepository;
    private final JdbcTgChatRepository jdbcTgChatRepository;
    private final static String CHAT_IS_NOT_REGISTERED_MESSAGE = "Chat is not registered";

    public JdbcLinkService(JdbcLinkRepository jdbcLinkRepository, JdbcTgChatRepository jdbcTgChatRepository) {
        this.jdbcLinkRepository = jdbcLinkRepository;
        this.jdbcTgChatRepository = jdbcTgChatRepository;
    }

    @Transactional
    public LinkDTO add(long tgChatId, URI url) {
        var stringUrl = url.toString();
        var chat = jdbcTgChatRepository.findChatById(tgChatId);

        if (chat.isEmpty()) {
            throw new ChatNotExistException(CHAT_IS_NOT_REGISTERED_MESSAGE);
        }

        var link = jdbcLinkRepository.findLinkByUrl(stringUrl);

        if (link.isEmpty()) {
            jdbcLinkRepository.addLink(stringUrl);
            link = jdbcLinkRepository.findLinkByUrl(stringUrl);
        }

        var linkValue = link.orElseThrow(() -> new CantAddToDBException("Cant add link to DB"));

        jdbcLinkRepository.addChatLinkMapping(tgChatId, linkValue.getLinkId());
        linkValue.setTgChatList(jdbcTgChatRepository.findAllByLinkId(linkValue.getLinkId()));

        return linkValue;
    }

    @Transactional
    public void remove(long tgChatId, URI url) {
        var stringUrl = url.toString();
        var chat = jdbcTgChatRepository.findChatById(tgChatId);

        if (chat.isEmpty()) {
            throw new ChatNotExistException(CHAT_IS_NOT_REGISTERED_MESSAGE);
        }

        var link = jdbcLinkRepository.findLinkByUrl(stringUrl);

        link.ifPresent(value -> jdbcLinkRepository.removeChatLinkMapping(tgChatId, value.getLinkId()));
    }

    @Transactional
    public List<LinkDTO> getLinks(long tgChatId) {
        var chat = jdbcTgChatRepository.findChatById(tgChatId);

        if (chat.isEmpty()) {
            throw new ChatNotExistException(CHAT_IS_NOT_REGISTERED_MESSAGE);
        }

        return jdbcLinkRepository.findAllByChatId(tgChatId);
    }

    @Transactional
    public List<LinkDTO> getLinksBeforeDateTime(OffsetDateTime dateTime) {
        return jdbcLinkRepository.findAllBeforeDateTime(dateTime);
    }

    @Transactional
    public void updateLastCheckTime(long linkId) {
        jdbcLinkRepository.updateLinkLastCheckDate(linkId);
    }
}
