package edu.java.bot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

public class LinkUpdateRequest {
    @JsonProperty("id")
    private final long id;
    @JsonProperty("url")
    private final URI url;
    @JsonProperty("description")
    private final String description;
    @JsonProperty("tgChatIds")
    private final long[] tgChatIds;

    public LinkUpdateRequest(long id, URI url, String description, long[] tgChatIds) {
        this.id = id;
        this.url = url;
        this.description = description;
        this.tgChatIds = tgChatIds;
    }
}
