package edu.java.scrapper.http;

import edu.java.scrapper.model.LinkUpdateRequest;
import edu.java.scrapper.model.QuestionList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;

public interface BotService {
    @GetExchange("/updates")
    ResponseEntity<?> sendUpdates(@RequestBody LinkUpdateRequest linkUpdateRequest);
}
