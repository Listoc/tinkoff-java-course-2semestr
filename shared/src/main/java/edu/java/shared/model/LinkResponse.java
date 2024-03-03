package edu.java.shared.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.net.URI;

public record LinkResponse(
    @JsonProperty("id")
    @NotNull
    Long id,

    @JsonProperty("url")
    @NotNull
    URI url) {
}
