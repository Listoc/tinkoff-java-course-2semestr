package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.exception.LinkNotExistException;
import edu.java.scrapper.model.LinkDTO;
import edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.repository.jdbc.JdbcTgChatRepository;
import edu.java.scrapper.service.LinkService;
import java.net.URI;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public class JdbcLinkService implements LinkService {
    private final JdbcLinkRepository jdbcLinkRepository;
    private final JdbcTgChatRepository jdbcTgChatRepository;

    public JdbcLinkService(JdbcLinkRepository jdbcLinkRepository, JdbcTgChatRepository jdbcTgChatRepository) {
        this.jdbcLinkRepository = jdbcLinkRepository;
        this.jdbcTgChatRepository = jdbcTgChatRepository;
    }

    @Transactional
    public LinkDTO add(long tgChatId, URI url) {
        var stringUrl = url.toString();
        var link = jdbcLinkRepository.findLinkByUrl(stringUrl);
        if (link == null) {
            jdbcLinkRepository.addLink(stringUrl);
            link = jdbcLinkRepository.findLinkByUrl(stringUrl);
        }
        jdbcLinkRepository.addChatLinkMapping(tgChatId, link.getLinkId());
        link.setTgChatList(jdbcTgChatRepository.findAllByLinkId(link.getLinkId()));
        return link;
    }

    @Transactional
    public LinkDTO remove(long tgChatId, URI url) {
        var stringUrl = url.toString();
        var link = jdbcLinkRepository.findLinkByUrl(stringUrl);

        if (link == null) {
            throw new LinkNotExistException("No such link in DB");
        }

        jdbcLinkRepository.removeChatLinkMapping(tgChatId, link.getLinkId());
        link.setTgChatList(jdbcTgChatRepository.findAllByLinkId(link.getLinkId()));
        return link;
    }

    @Transactional
    public List<LinkDTO> getLinks(long tgChatId) {
        return jdbcLinkRepository.findAllByChatId(tgChatId);
    }
}
