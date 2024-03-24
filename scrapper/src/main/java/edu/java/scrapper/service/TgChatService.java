package edu.java.scrapper.service;

import edu.java.scrapper.model.TgChat;
import java.util.List;

public interface TgChatService {
    TgChat register(long tgChatId);

    void unregister(long tgChatId);

    List<TgChat> getChats(long linkId);
}
