package edu.java.bot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.net.URI;

public record LinkUpdateRequest(
    @JsonProperty("id") @NotNull long id,
    @JsonProperty("url") @NotNull URI url,
    @JsonProperty("description") @NotNull String description,
    @JsonProperty("tgChatIds") @NotEmpty long[] tgChatIds) {
}
