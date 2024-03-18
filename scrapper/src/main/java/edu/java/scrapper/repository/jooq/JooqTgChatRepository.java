package edu.java.scrapper.repository.jooq;

import edu.java.scrapper.model.ChatDTO;
import edu.java.scrapper.repository.jooq.generated.tables.Chat;
import edu.java.scrapper.repository.jooq.generated.tables.ChatLinkMap;
import java.util.List;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class JooqTgChatRepository {
    private final DSLContext dsl;
    private final Chat chat;
    private final ChatLinkMap map;

    public JooqTgChatRepository(DSLContext dsl) {
        this.dsl = dsl;
        this.chat = Chat.CHAT;
        this.map = ChatLinkMap.CHAT_LINK_MAP;
    }

    public void addChat(long tgChatId) {
        dsl.insertInto(chat)
            .set(chat.CHAT_ID, tgChatId)
            .execute();
    }

    public void removeChat(long tgChatId) {
        dsl.delete(chat).where(chat.CHAT_ID.eq(tgChatId)).execute();
    }

    public ChatDTO findChatById(long tgChatId) {
        var chats = dsl.select()
                .from(chat)
                .where(chat.CHAT_ID.eq(tgChatId))
                .fetch();

        if (chats.isEmpty()) {
            return null;
        }

        return chats.getFirst().into(new ChatDTO());
    }

    public List<ChatDTO> findAllByLinkId(long linkId) {
        var chats = dsl.select()
                .from(chat)
                .join(map).onKey()
                .where(map.LINK_ID.eq(linkId))
                .fetch();

        return chats.into(ChatDTO.class);
    }

    public List<ChatDTO> findAll() {
        var chats = dsl.select()
                .from(chat)
                .fetch();

        return chats.into(ChatDTO.class);
    }
}
