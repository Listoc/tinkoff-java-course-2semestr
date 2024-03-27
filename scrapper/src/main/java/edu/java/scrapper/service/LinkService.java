package edu.java.scrapper.service;

import edu.java.scrapper.model.Link;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkService {
    Link add(long tgChatId, URI url);

    void remove(long tgChatId, URI url);

    List<Link> getLinks(long tgChatId);

    List<Link> getLinksBeforeDateTime(OffsetDateTime dateTime);

    void updateLastCheckTime(long linkId);
}
