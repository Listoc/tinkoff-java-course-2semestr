package edu.java.scrapper.client;

import edu.java.scrapper.http.QuestionService;
import edu.java.scrapper.model.QuestionResponse;
import jakarta.validation.constraints.NotNull;
import java.util.NoSuchElementException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

public class StackOverflowClient {
    private final QuestionService service;

    public StackOverflowClient(@NotNull String baseUrl) {
        var webClient = WebClient.builder().baseUrl(baseUrl).build();
        var factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build();

        this.service = factory.createClient(QuestionService.class);
    }

    public StackOverflowClient() {
        this("https://api.stackexchange.com/2.3");
    }

    public QuestionResponse fetchQuestion(int id) {
        try {
            return service.getQuestion(id).items().getFirst();
        } catch (NoSuchElementException | WebClientResponseException e) {
            throw new NoSuchElementException("No such question id on Stack Overflow");
        }
    }
}
