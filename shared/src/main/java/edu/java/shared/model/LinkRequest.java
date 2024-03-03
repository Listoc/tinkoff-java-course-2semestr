package edu.java.shared.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.net.URI;

public record LinkRequest(
    @JsonProperty("url")
    @NotNull
    URI url) {
}
