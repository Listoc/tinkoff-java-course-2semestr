package edu.java.bot.controller;

import edu.java.shared.model.LinkUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdatesController {
    @PostMapping("/updates")
    public ResponseEntity<?> getUpdates(@Valid @RequestBody LinkUpdateRequest linkUpdateRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
