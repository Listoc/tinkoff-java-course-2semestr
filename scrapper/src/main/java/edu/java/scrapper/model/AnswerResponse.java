package edu.java.scrapper.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record AnswerResponse(
    @JsonProperty("title")
    String title,
    @JsonProperty("creation_date")
    OffsetDateTime answerDate
) {
}
