package edu.java.bot.kafka;

import edu.java.bot.service.UpdatesHandler;
import edu.java.shared.model.LinkUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class UpdatesListener {
    private final UpdatesHandler updatesHandler;

    public UpdatesListener(UpdatesHandler updatesHandler) {
        this.updatesHandler = updatesHandler;
    }

    @KafkaListener(id = "1", topics = "#{@updatesTopic.name}", containerFactory = "kafkaListenerContainerFactory")
    public void listen(@Valid LinkUpdateRequest linkUpdateRequest) {
        updatesHandler.handle(linkUpdateRequest);
    }
}
