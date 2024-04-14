package edu.java.bot.kafka;

import edu.java.bot.service.UpdatesHandler;
import edu.java.shared.model.LinkUpdateRequest;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.Valid;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class UpdatesListener {
    private final UpdatesHandler updatesHandler;
    private final Counter counter;

    public UpdatesListener(UpdatesHandler updatesHandler, MeterRegistry meterRegistry) {
        this.updatesHandler = updatesHandler;
        this.counter = meterRegistry.counter("messages_counter");
    }

    @KafkaListener(id = "1", topics = "#{@updatesTopic.name}", containerFactory = "kafkaListenerContainerFactory")
    public void listen(@Valid LinkUpdateRequest linkUpdateRequest) {
        updatesHandler.handle(linkUpdateRequest);
        counter.increment();
    }
}
