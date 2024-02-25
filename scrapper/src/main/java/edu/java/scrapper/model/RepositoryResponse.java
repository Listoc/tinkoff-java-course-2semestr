package edu.java.scrapper.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record RepositoryResponse(
    @JsonProperty("html_url")
    String url,
    @JsonProperty("updated_at")
    OffsetDateTime lastUpdateDate
) {}
