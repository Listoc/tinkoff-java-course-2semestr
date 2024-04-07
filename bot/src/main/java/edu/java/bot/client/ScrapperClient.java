package edu.java.bot.client;

import edu.java.bot.http.ScrapperService;
import edu.java.shared.client.ClientInfo;
import edu.java.shared.client.ClientUtils;
import edu.java.shared.model.LinkRequest;
import edu.java.shared.model.ListLinksResponse;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

public class ScrapperClient {
    private final ScrapperService service;

    public ScrapperClient(@NotNull ClientInfo clientInfo) {
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

        this.service = factory.createClient(ScrapperService.class);
    }

    public ListLinksResponse getAllLinks(Long id) {
        return service.getAllLinks(id);
    }

    public void addLink(Long id, URI link) {
        service.addLink(id, new LinkRequest(link));
    }

    public void removeLink(Long id, URI link) {
        service.removeLink(id, new LinkRequest(link));
    }

    public void registerChat(Long id) {
        service.registerChat(id);
    }

    public void deleteChat(Long id) {
        service.deleteChat(id);
    }
}
