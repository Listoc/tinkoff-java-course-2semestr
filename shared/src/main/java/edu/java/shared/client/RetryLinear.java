package edu.java.shared.client;

import java.time.Duration;
import java.util.function.Predicate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;
import reactor.util.retry.Retry;

public class RetryLinear extends Retry {
    public final Duration minBackoff;
    public final long maxAttempts;
    public final Predicate<Throwable> errorFilter;

    public RetryLinear(Duration minBackoff, long maxAttempts, Predicate<Throwable> errorFilter) {
        super(Context.empty());
        this.minBackoff = minBackoff;
        this.maxAttempts = maxAttempts;
        this.errorFilter = errorFilter;
    }

    public RetryLinear filter(Predicate<Throwable> errorFilter) {
        return new RetryLinear(this.minBackoff, this.maxAttempts, errorFilter);
    }

    @Override
    public Flux<Long> generateCompanion(Flux<RetrySignal> flux) {
        return Flux.deferContextual((cv) -> {
            return flux.contextWrite(cv).concatMap((retryWhenState) -> {
                Retry.RetrySignal copy = retryWhenState.copy();
                Throwable currentFailure = copy.failure();
                long iteration = copy.totalRetries();

                if (currentFailure == null) {
                    return Mono.error(new IllegalStateException("Retry.RetrySignal#failure() not expected to be null"));
                } else if (!this.errorFilter.test(currentFailure) || iteration >= this.maxAttempts) {
                    return Mono.error(currentFailure);
                } else {
                    Duration next = minBackoff.multipliedBy(iteration + 1);

                    return Mono.delay(next);
                }
            });
        });
    }
}
