package edu.java.scrapper.service.jooq;

import edu.java.scrapper.exception.LinkNotExistException;
import edu.java.scrapper.model.LinkDTO;
import edu.java.scrapper.repository.jooq.JooqLinkRepository;
import edu.java.scrapper.repository.jooq.JooqTgChatRepository;
import edu.java.scrapper.service.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JooqLinkService implements LinkService {
    private final JooqLinkRepository jooqLinkRepository;
    private final JooqTgChatRepository jooqTgChatRepository;

    public JooqLinkService(JooqLinkRepository jooqLinkRepository, JooqTgChatRepository jooqTgChatRepository) {
        this.jooqLinkRepository = jooqLinkRepository;
        this.jooqTgChatRepository = jooqTgChatRepository;
    }

    @Transactional
    public LinkDTO add(long tgChatId, URI url) {
        var stringUrl = url.toString();
        var link = jooqLinkRepository.findLinkByUrl(stringUrl);
        if (link == null) {
            jooqLinkRepository.addLink(stringUrl);
            link = jooqLinkRepository.findLinkByUrl(stringUrl);
        }
        jooqLinkRepository.addChatLinkMapping(tgChatId, link.getLinkId());
        link.setTgChatList(jooqTgChatRepository.findAllByLinkId(link.getLinkId()));
        return link;
    }

    @Transactional
    public void remove(long tgChatId, URI url) {
        var stringUrl = url.toString();
        var link = jooqLinkRepository.findLinkByUrl(stringUrl);

        if (link == null) {
            throw new LinkNotExistException("No such link in DB");
        }

        jooqLinkRepository.removeChatLinkMapping(tgChatId, link.getLinkId());
        link.setTgChatList(jooqTgChatRepository.findAllByLinkId(link.getLinkId()));
    }

    @Transactional
    public List<LinkDTO> getLinks(long tgChatId) {
        return jooqLinkRepository.findAllByChatId(tgChatId);
    }
}
