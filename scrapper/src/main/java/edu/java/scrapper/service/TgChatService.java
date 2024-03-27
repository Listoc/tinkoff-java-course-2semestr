package edu.java.scrapper.service;

import edu.java.scrapper.model.ChatDTO;
import java.util.List;

public interface TgChatService {
    ChatDTO register(long tgChatId);

    void unregister(long tgChatId);

    List<ChatDTO> getChats(long linkId);
}
