package edu.java.scrapper.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record QuestionList(
    @JsonProperty("items")
    List<QuestionResponse> items
) {}
