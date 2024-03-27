package edu.java.scrapper.controller;

import edu.java.scrapper.service.LinkService;
import edu.java.shared.model.LinkRequest;
import edu.java.shared.model.LinkResponse;
import edu.java.shared.model.ListLinksResponse;
import jakarta.validation.Valid;
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
    private final LinkService linkService;

    public LinksController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping("/links")
    @ResponseStatus(HttpStatus.OK)
    public ListLinksResponse getAllLinks(@RequestHeader("Tg-Chat-Id") Long id) {
        var links = linkService.getLinks(id).stream().map((l) -> new LinkResponse(l.getLinkId(), l.getUrl())).toList();
        return new ListLinksResponse(links, links.size());
    }

    @PostMapping("/links")
    @ResponseStatus(HttpStatus.OK)
    public void addLink(@RequestHeader("Tg-Chat-Id") Long id, @Valid @RequestBody LinkRequest linkRequest) {
        linkService.add(id, linkRequest.url());
    }

    @DeleteMapping("/links")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLink(@RequestHeader("Tg-Chat-Id") Long id, @Valid @RequestBody LinkRequest linkRequest) {
        linkService.remove(id, linkRequest.url());
    }
}
