package edu.java.scrapper.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record RepositoryResponse(
    @JsonProperty("html_url")
    String url,
    @JsonProperty("pushed_at")
    OffsetDateTime lastUpdateDate
) {}
