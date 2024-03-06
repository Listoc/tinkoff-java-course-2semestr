package edu.java.scrapper.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record QuestionResponse(
    @JsonProperty("question_id")
    long questionId,
    @JsonProperty("last_activity_date")
    OffsetDateTime lastUpdateDate
) {}
