package edu.java.scrapper.repository.jooq;

import edu.java.scrapper.model.LinkDTO;
import edu.java.scrapper.repository.jooq.generated.tables.ChatLinkMap;
import edu.java.scrapper.repository.jooq.generated.tables.Link;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class JooqLinkRepository {
    private final DSLContext dsl;
    private final Link link;
    private final ChatLinkMap map;

    public JooqLinkRepository(DSLContext dsl) {
        this.dsl = dsl;
        this.link = Link.LINK;
        this.map = ChatLinkMap.CHAT_LINK_MAP;
    }

    public void addLink(String url) {
        dsl.insertInto(link)
            .set(link.URL, url)
            .set(link.LAST_CHECK, OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS))
            .execute();
    }

    public void addChatLinkMapping(long tgChatId, long linkId) {
        dsl.insertInto(map)
            .set(map.CHAT_ID, tgChatId)
            .set(map.LINK_ID, linkId)
            .onConflictDoNothing()
            .execute();
    }

    public void removeChatLinkMapping(long tgChatId, long linkId) {
        dsl.delete(map)
            .where(map.CHAT_ID.eq(tgChatId))
            .and(map.LINK_ID.eq(linkId))
            .execute();
    }

    public LinkDTO findLinkByUrl(String url) {
        var links = dsl.select()
            .from(link)
            .where(link.URL.eq(url))
            .fetch();

        if (links.isEmpty()) {
            return null;
        }

        return links.getFirst().into(new LinkDTO());
    }

    public void removeLinkByUrl(String url) {
        dsl.delete(link)
            .where(link.URL.eq(url))
            .execute();
    }

    public List<LinkDTO> findAll() {
        var links = dsl.select()
            .from(link)
            .fetch();

        return links.into(LinkDTO.class);
    }

    public List<LinkDTO> findAllByChatId(long tgChatId) {
        var links = dsl.select()
            .from(link)
            .join(map).onKey()
            .where(map.CHAT_ID.eq(tgChatId))
            .fetch();

        return links.into(LinkDTO.class);
    }

    public List<LinkDTO> findAllBeforeDate(OffsetDateTime dateTime) {
        var links = dsl.select()
            .from(link)
            .where(link.LAST_CHECK.lt(dateTime))
            .fetch();

        return links.into(LinkDTO.class);
    }

    public void updateLinkLastCheckDate(long linkId) {
        dsl.update(link)
            .set(link.LAST_CHECK, OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS))
            .where(link.LINK_ID.eq(linkId))
            .execute();
    }
}
