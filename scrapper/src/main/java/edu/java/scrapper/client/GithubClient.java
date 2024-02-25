package edu.java.scrapper.client;

import edu.java.scrapper.http.RepositoryService;
import edu.java.scrapper.model.RepositoryResponse;
import jakarta.validation.constraints.NotNull;
import java.util.NoSuchElementException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

public class GithubClient {
    private final RepositoryService service;

    public GithubClient(@NotNull String baseUrl) {
        var webClient = WebClient.builder().baseUrl(baseUrl).build();
        var factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build();

        this.service = factory.createClient(RepositoryService.class);
    }

    public GithubClient() {
        this("https://api.github.com");
    }

    public RepositoryResponse fetchRepository(@NotNull String owner, @NotNull String repo) {
        try {
            return service.getRepository(owner, repo);
        } catch (WebClientResponseException e) {
            throw new NoSuchElementException("No such repository in github");
        }
    }
}
