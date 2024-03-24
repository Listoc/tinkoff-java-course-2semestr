package edu.java.scrapper.service;

<<<<<<< HEAD
import edu.java.scrapper.model.ChatDTO;
=======
import edu.java.scrapper.model.TgChat;
import java.util.List;
>>>>>>> hw5

public interface TgChatService {
    ChatDTO register(long tgChatId);

    void unregister(long tgChatId);

    List<ChatDTO> getChats(long linkId);
}
