package edu.java.scrapper.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record CommitResponse(
    @JsonProperty("commit")
    Commit commit
) {
    public record Commit(
        @JsonProperty("message")
        String message,
        @JsonProperty("author")
        Author author
    ) {
        public record Author(
            @JsonProperty("date")
            OffsetDateTime commitDate
        ) {}
    }
}
