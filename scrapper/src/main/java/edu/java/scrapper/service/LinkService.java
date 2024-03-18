package edu.java.scrapper.service;

import edu.java.scrapper.model.LinkDTO;
import java.net.URI;
import java.util.List;

public interface LinkService {
    LinkDTO add(long tgChatId, URI url);

    LinkDTO remove(long tgChatId, URI url);

    List<LinkDTO> getLinks(long tgChatId);
}
