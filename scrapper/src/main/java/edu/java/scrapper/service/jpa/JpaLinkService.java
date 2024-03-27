package edu.java.scrapper.service.jpa;

import edu.java.scrapper.exception.ChatNotExistException;
import edu.java.scrapper.model.LinkDTO;
import edu.java.scrapper.repository.jpa.JpaLink;
import edu.java.scrapper.repository.jpa.JpaLinkRepository;
import edu.java.scrapper.repository.jpa.JpaTgChatRepository;
import edu.java.scrapper.service.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public class JpaLinkService implements LinkService {
    private final JpaLinkRepository jpaLinkRepository;
    private final JpaTgChatRepository jpaTgChatRepository;
    private final static String CHAT_IS_NOT_REGISTERED_MESSAGE = "Chat is not registered";

    public JpaLinkService(JpaLinkRepository jpaLinkRepository, JpaTgChatRepository jpaTgChatRepository) {
        this.jpaLinkRepository = jpaLinkRepository;
        this.jpaTgChatRepository = jpaTgChatRepository;
    }

    @Transactional
    public LinkDTO add(long tgChatId, URI url) {
        var optionalJpaLink = jpaLinkRepository.findByUrl(url.toString());
        JpaLink jpaLink;
        var chat = jpaTgChatRepository.findById(tgChatId);

        if (chat.isEmpty()) {
            throw new ChatNotExistException(CHAT_IS_NOT_REGISTERED_MESSAGE);
        }

        if (optionalJpaLink.isEmpty()) {
            jpaLink = new JpaLink();
            jpaLink.setUrl(url.toString());
            jpaLink.setLastCheck(OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            jpaLinkRepository.save(jpaLink);
        } else {
            jpaLink = optionalJpaLink.get();
        }

        if (!chat.get().getLinks().contains(jpaLink)) {
            chat.get().addLink(jpaLink);
        }

        return mapToDto(jpaLink);
    }

    @Transactional
    public void remove(long tgChatId, URI url) {
        var chat = jpaTgChatRepository.findById(tgChatId);

        if (chat.isEmpty()) {
            throw new ChatNotExistException(CHAT_IS_NOT_REGISTERED_MESSAGE);
        }

        var link = jpaLinkRepository.findByUrl(url.toString());

        link.ifPresent(value -> chat.get().removeLink(value));
    }

    @Transactional
    public List<LinkDTO> getLinks(long tgChatId) {
        var chat = jpaTgChatRepository.findById(tgChatId);

        if (chat.isEmpty()) {
            throw new ChatNotExistException(CHAT_IS_NOT_REGISTERED_MESSAGE);
        }

        return mapToDto(chat.get().getLinks());
    }

    @Transactional
    public List<LinkDTO> getLinksBeforeDateTime(OffsetDateTime dateTime) {
        return mapToDto(jpaLinkRepository.findAllByLastCheckLessThan(dateTime));
    }

    @Transactional
    public void updateLastCheckTime(long linkId) {
        var link = jpaLinkRepository.findById(linkId);

        link.ifPresent(jpaLink -> jpaLink.setLastCheck(OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
    }

    private LinkDTO mapToDto(JpaLink jpaLink) {
        var link = new LinkDTO();

        link.setLinkId(jpaLink.getLinkId());
        link.setUrl(URI.create(jpaLink.getUrl()));
        link.setLastCheck(jpaLink.getLastCheck());

        return link;
    }

    private List<LinkDTO> mapToDto(List<JpaLink> jpaLinks) {
        var links = new ArrayList<LinkDTO>();
        LinkDTO link;
        for (var jpaLink : jpaLinks) {
            link = new LinkDTO();

            link.setLinkId(jpaLink.getLinkId());
            link.setUrl(URI.create(jpaLink.getUrl()));
            link.setLastCheck(jpaLink.getLastCheck());

            links.add(link);
        }

        return links;
    }
}
