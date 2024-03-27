/*
 * This file is generated by jOOQ.
 */
package edu.java.scrapper.repository.jooq.generated;


import edu.java.scrapper.repository.jooq.generated.tables.Chat;
import edu.java.scrapper.repository.jooq.generated.tables.ChatLinkMap;
import edu.java.scrapper.repository.jooq.generated.tables.Link;
import edu.java.scrapper.repository.jooq.generated.tables.records.ChatLinkMapRecord;
import edu.java.scrapper.repository.jooq.generated.tables.records.ChatRecord;
import edu.java.scrapper.repository.jooq.generated.tables.records.LinkRecord;

import javax.annotation.processing.Generated;

import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in the
 * default schema.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<ChatRecord> CONSTRAINT_1 = Internal.createUniqueKey(Chat.CHAT, DSL.name("CONSTRAINT_1"), new TableField[] { Chat.CHAT.CHAT_ID }, true);
    public static final UniqueKey<ChatLinkMapRecord> CONSTRAINT_7B8 = Internal.createUniqueKey(ChatLinkMap.CHAT_LINK_MAP, DSL.name("CONSTRAINT_7B8"), new TableField[] { ChatLinkMap.CHAT_LINK_MAP.CHAT_ID, ChatLinkMap.CHAT_LINK_MAP.LINK_ID }, true);
    public static final UniqueKey<LinkRecord> CONSTRAINT_2 = Internal.createUniqueKey(Link.LINK, DSL.name("CONSTRAINT_2"), new TableField[] { Link.LINK.LINK_ID }, true);
    public static final UniqueKey<LinkRecord> CONSTRAINT_23 = Internal.createUniqueKey(Link.LINK, DSL.name("CONSTRAINT_23"), new TableField[] { Link.LINK.URL }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<ChatLinkMapRecord, ChatRecord> CONSTRAINT_7 = Internal.createForeignKey(ChatLinkMap.CHAT_LINK_MAP, DSL.name("CONSTRAINT_7"), new TableField[] { ChatLinkMap.CHAT_LINK_MAP.CHAT_ID }, Keys.CONSTRAINT_1, new TableField[] { Chat.CHAT.CHAT_ID }, true);
    public static final ForeignKey<ChatLinkMapRecord, LinkRecord> CONSTRAINT_7B = Internal.createForeignKey(ChatLinkMap.CHAT_LINK_MAP, DSL.name("CONSTRAINT_7B"), new TableField[] { ChatLinkMap.CHAT_LINK_MAP.LINK_ID }, Keys.CONSTRAINT_2, new TableField[] { Link.LINK.LINK_ID }, true);
}
