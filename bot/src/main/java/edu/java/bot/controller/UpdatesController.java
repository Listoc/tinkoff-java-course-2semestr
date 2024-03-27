package edu.java.bot.controller;

import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.telegram.Bot;
import edu.java.shared.model.LinkUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdatesController {
    private final Bot bot;

    public UpdatesController(Bot bot) {
        this.bot = bot;
    }

    @PostMapping("/updates")
    @ResponseStatus(HttpStatus.OK)
    public void getUpdates(@Valid @RequestBody LinkUpdateRequest linkUpdateRequest) {
        var chatIds = linkUpdateRequest.tgChatIds();
        var message = linkUpdateRequest.description() + "\n" + linkUpdateRequest.url();

        for (var chatId : chatIds) {
            bot.execute(new SendMessage(chatId, message).parseMode(ParseMode.Markdown));
        }
    }
}
