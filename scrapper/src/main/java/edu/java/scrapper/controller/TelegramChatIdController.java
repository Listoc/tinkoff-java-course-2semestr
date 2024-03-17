package edu.java.scrapper.controller;

import edu.java.scrapper.service.jdbc.JdbcTgChatService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TelegramChatIdController {
    private final JdbcTgChatService jdbcTgChatService;

    public TelegramChatIdController(JdbcTgChatService jdbcTgChatService) {
        this.jdbcTgChatService = jdbcTgChatService;
    }

    @PostMapping("/tg-chat/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void registerChat(@PathVariable Long id) {
        jdbcTgChatService.register(id);
    }

    @DeleteMapping("/tg-chat/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteChat(@PathVariable Long id) {
        jdbcTgChatService.unregister(id);
    }
}
