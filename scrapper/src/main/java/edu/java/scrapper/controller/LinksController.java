package edu.java.scrapper.controller;

import edu.java.scrapper.exception.ChatNotExistException;
import edu.java.scrapper.exception.LinkAlreadyExistException;
import edu.java.scrapper.exception.LinkNotExistException;
import edu.java.shared.model.LinkRequest;
import edu.java.shared.model.LinkResponse;
import edu.java.shared.model.ListLinksResponse;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LinksController {
    private static final String CHAT_NOT_EXIST_EXCEPTION_MESSAGE = "Chat is not registered";
    private final Map<Long, List<LinkResponse>> links = new ConcurrentHashMap<>();
    private final AtomicLong linkId = new AtomicLong(0);

    public LinksController() {
        links.put(0L, new ArrayList<>());
    }

    @GetMapping("/links")
    @ResponseStatus(HttpStatus.OK)
    public ListLinksResponse getAllLinks(@RequestHeader("Tg-Chat-Id") Long id) {
        if (!links.containsKey(id)) {
            throw new ChatNotExistException(CHAT_NOT_EXIST_EXCEPTION_MESSAGE);
        }

        return new ListLinksResponse(links.get(id), links.get(id).size());
    }

    @PostMapping("/links")
    @ResponseStatus(HttpStatus.OK)
    public void addLink(@RequestHeader("Tg-Chat-Id") Long id, @Valid @RequestBody LinkRequest linkRequest) {
        if (!links.containsKey(id)) {
            throw new ChatNotExistException(CHAT_NOT_EXIST_EXCEPTION_MESSAGE);
        }

        if (links.get(id).stream().anyMatch(linkResponse -> linkResponse.url().equals(linkRequest.url()))) {
            throw new LinkAlreadyExistException("Link has already been added to this chat");
        }

        links.get(id).add(new LinkResponse(linkId.incrementAndGet(), linkRequest.url()));
    }

    @DeleteMapping("/links")
    @ResponseStatus(HttpStatus.OK)
    public void removeLink(@RequestHeader("Tg-Chat-Id") Long id, @Valid @RequestBody LinkRequest linkRequest) {
        if (!links.containsKey(id)) {
            throw new ChatNotExistException(CHAT_NOT_EXIST_EXCEPTION_MESSAGE);
        }

        if (links.get(id).stream().noneMatch(linkResponse -> linkResponse.url().equals(linkRequest.url()))) {
            throw new LinkNotExistException("This chat doesn't track this link");
        }

        links.put(
            id,
            links.get(id).stream().filter(linkResponse -> !linkResponse.url().equals(linkRequest.url())).toList()
        );
    }
}
