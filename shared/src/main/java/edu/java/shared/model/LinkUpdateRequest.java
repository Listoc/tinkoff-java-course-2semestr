package edu.java.shared.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

public record LinkUpdateRequest(
    @JsonProperty("id")
    @NotNull
    Long id,

    @JsonProperty("url")
    @NotNull
    URI url,

    @JsonProperty("description")
    @NotNull
    String description,

    @JsonProperty("tgChatIds")
    @NotEmpty
    List<Long> tgChatIds) {
}
