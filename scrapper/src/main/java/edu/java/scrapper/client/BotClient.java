package edu.java.scrapper.client;

import edu.java.scrapper.http.BotService;
import edu.java.shared.client.ClientInfo;
import edu.java.shared.client.ClientUtils;
import edu.java.shared.model.LinkUpdateRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

public class BotClient {
    private final BotService service;

    public BotClient(@NotNull ClientInfo clientInfo) {
        var webClient = WebClient
            .builder()
            .filter(
                ClientUtils.getFilterWithRetry(
                    clientInfo.codes(),
                    clientInfo.backOffType(),
                    clientInfo.maxAttempts(),
                    clientInfo.duration()
                )
            )
            .baseUrl(clientInfo.url())
            .build();
        var factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build();

        this.service = factory.createClient(BotService.class);
    }

    public ResponseEntity<?> sendUpdates(LinkUpdateRequest linkUpdateRequest) {
        return service.sendUpdates(linkUpdateRequest);
    }

}
