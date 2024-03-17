package edu.java.scrapper.model;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
public class Link {
    private long linkId;
    private URI url;
    private OffsetDateTime lastCheckTime;
    private List<TgChat> tgChatList;
}
