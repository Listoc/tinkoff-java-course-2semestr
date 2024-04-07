package edu.java.bot.service;

import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.telegram.Bot;
import edu.java.shared.model.LinkUpdateRequest;
import org.springframework.stereotype.Service;

@Service
public class UpdatesHandler {
    private final Bot bot;

    public UpdatesHandler(Bot bot) {
        this.bot = bot;
    }

    public void handle(LinkUpdateRequest linkUpdateRequest) {
        var chatIds = linkUpdateRequest.tgChatIds();
        var message = linkUpdateRequest.description() + "\n" + linkUpdateRequest.url();

        for (var chatId : chatIds) {
            bot.execute(new SendMessage(chatId, message).parseMode(ParseMode.Markdown));
        }
    }
}
