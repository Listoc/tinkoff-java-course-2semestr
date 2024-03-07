package edu.java.shared.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ListLinksResponse(
    @JsonProperty("links")
    @NotNull
    List<LinkResponse> links,
    @JsonProperty("size")
    @NotNull
    Integer size) {
}
