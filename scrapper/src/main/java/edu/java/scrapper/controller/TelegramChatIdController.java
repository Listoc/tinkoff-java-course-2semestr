package edu.java.scrapper.controller;

import edu.java.scrapper.exception.ChatAlreadyExistException;
import edu.java.scrapper.exception.ChatNotExistException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TelegramChatIdController {
    private final Map<Long, List<String>> links = new ConcurrentHashMap<>();

    @PostMapping("/tg-chat/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void registerChat(@PathVariable Long id) {
        if (links.containsKey(id)) {
            throw new ChatAlreadyExistException("Chat has already been registered");
        }

        links.put(id, new ArrayList<>());
    }

    @DeleteMapping("/tg-chat/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteChat(@PathVariable Long id) {
        var result = links.remove(id);

        if (result == null) {
            throw new ChatNotExistException("Chat is not registered");
        }
    }
}
