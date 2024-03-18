package edu.java.scrapper.service;

import edu.java.scrapper.model.ChatDTO;

public interface TgChatService {
    ChatDTO register(long tgChatId);

    ChatDTO unregister(long tgChatId);
}
