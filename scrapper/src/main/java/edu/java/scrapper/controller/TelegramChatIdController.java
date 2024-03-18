package edu.java.scrapper.controller;

import edu.java.scrapper.service.TgChatService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TelegramChatIdController {
    private final TgChatService tgChatService;

    public TelegramChatIdController(TgChatService tgChatService) {
        this.tgChatService = tgChatService;
    }

    @PostMapping("/tg-chat/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void registerChat(@PathVariable Long id) {
        tgChatService.register(id);
    }

    @DeleteMapping("/tg-chat/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteChat(@PathVariable Long id) {
        tgChatService.unregister(id);
    }
}
