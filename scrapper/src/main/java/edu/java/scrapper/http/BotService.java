package edu.java.scrapper.http;

import edu.java.shared.model.LinkUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface BotService {
    @PostExchange("/updates")
    ResponseEntity<?> sendUpdates(@RequestBody LinkUpdateRequest linkUpdateRequest);
}
