package edu.java.shared.client;

import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class ClientUtils {
    private ClientUtils() {}

    public static ExchangeFilterFunction getFilterWithRetry(
        List<Integer> codes,
        ClientInfo.BackOffType backOffType,
        int maxAttempts,
        Duration duration
    ) {
        return (request, next) -> next.exchange(request)
            .flatMap(clientResponse -> Mono.just(clientResponse)
                .filter(response ->  clientResponse.statusCode().isError())
                .flatMap(response -> clientResponse.createException())
                .flatMap(Mono::error)
                .thenReturn(clientResponse))
            .retryWhen(getRetryBackoff(codes, backOffType, maxAttempts, duration));
    }

    private static Retry getRetryBackoff(
        List<Integer> codes,
        ClientInfo.BackOffType backOffType,
        int maxAttempts,
        Duration duration
    ) {
        var backOffFilter = getBackoffFilter(codes);

        return switch (backOffType) {
            case exponential -> Retry
                .backoff(maxAttempts, duration)
                .filter(backOffFilter)
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal)  -> retrySignal.failure());
            case linear -> new RetryLinear(duration, maxAttempts, null)
                .filter(backOffFilter);
            case constant -> Retry
                .fixedDelay(maxAttempts, duration)
                .filter(backOffFilter)
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal)  -> retrySignal.failure());
        };
    }

    private static Predicate<Throwable> getBackoffFilter(List<Integer> codes) {
        return throwable -> {
            if (throwable instanceof WebClientRequestException) {
                return true;
            }

            if (throwable instanceof WebClientResponseException) {
                return codes.contains(((WebClientResponseException) throwable).getStatusCode().value());
            }

            return false;
        };
    }
}
