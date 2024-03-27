package edu.java.scrapper.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record AnswerList(
    @JsonProperty("items")
    List<AnswerResponse> items
) {
}
