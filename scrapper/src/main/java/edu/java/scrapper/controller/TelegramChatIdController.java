package edu.java.scrapper.controller;

import edu.java.scrapper.exception.ChatAlreadyExistException;
import edu.java.scrapper.exception.ChatNotExistException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TelegramChatIdController {
    private final Map<Long, List<String>> links = new HashMap<>();

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<?> registerChat(@PathVariable Long id) {
        if (links.containsKey(id)) {
            throw new ChatAlreadyExistException();
        }

        links.put(id, new ArrayList<>());

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<?> deleteChat(@PathVariable Long id) {
        var result = links.remove(id);

        if (result == null) {
            throw new ChatNotExistException();
        }

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
