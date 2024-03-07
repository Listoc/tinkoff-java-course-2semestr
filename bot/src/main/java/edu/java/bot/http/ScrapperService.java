package edu.java.bot.http;

import edu.java.shared.model.LinkRequest;
import edu.java.shared.model.ListLinksResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface ScrapperService {
    @GetExchange("/links")
    ListLinksResponse getAllLinks(@RequestHeader("Tg-Chat-Id") Long id);

    @PostExchange("/links")
    void addLink(@RequestHeader("Tg-Chat-Id") Long id, @RequestBody LinkRequest linkRequest);

    @DeleteExchange("/links")
    void removeLink(@RequestHeader("Tg-Chat-Id") Long id, @RequestBody LinkRequest linkRequest);

    @PostExchange("/tg-chat/{id}")
    void registerChat(@PathVariable Long id);

    @DeleteExchange("/tg-chat/{id}")
    void deleteChat(@PathVariable Long id);
}
