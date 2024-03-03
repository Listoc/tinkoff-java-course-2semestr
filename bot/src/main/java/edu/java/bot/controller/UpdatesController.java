package edu.java.bot.controller;

import edu.java.shared.model.LinkUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdatesController {
    @PostMapping("/updates")
    @ResponseStatus(HttpStatus.OK)
    public void getUpdates(@Valid @RequestBody LinkUpdateRequest linkUpdateRequest) {

    }
}
