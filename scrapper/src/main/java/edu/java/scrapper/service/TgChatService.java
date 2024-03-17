package edu.java.scrapper.service;

import edu.java.scrapper.model.TgChat;

public interface TgChatService {
    TgChat register(long tgChatId);

    TgChat unregister(long tgChatId);
}
