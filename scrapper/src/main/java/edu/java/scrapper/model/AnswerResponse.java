package edu.java.scrapper.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record AnswerResponse(
    @JsonProperty("link")
    String link,
    @JsonProperty("creation_date")
    OffsetDateTime answerDate
) {
}
