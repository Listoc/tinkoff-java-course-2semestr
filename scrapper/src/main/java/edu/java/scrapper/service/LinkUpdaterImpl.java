package edu.java.scrapper.service;

import edu.java.scrapper.configuration.UpdaterProperties;
import edu.java.scrapper.model.ChatDTO;
import edu.java.scrapper.service.proccesor.LinkUpdateProcessorService;
import edu.java.scrapper.service.proccesor.UpdatesSender;
import edu.java.shared.model.LinkUpdateRequest;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class LinkUpdaterImpl implements LinkUpdater {
    private final LinkService linkService;
    private final TgChatService tgChatService;
    private final List<LinkUpdateProcessorService> linkUpdateProcessorServiceList;
    private final UpdaterProperties updaterProperties;
    private final UpdatesSender updatesSender;

    public LinkUpdaterImpl(
        LinkService linkService,
        TgChatService tgChatService,
        List<LinkUpdateProcessorService> linkUpdateProcessorServiceList,
        UpdaterProperties updaterProperties,
        UpdatesSender updatesSender
    ) {
        this.linkService = linkService;
        this.tgChatService = tgChatService;
        this.linkUpdateProcessorServiceList = linkUpdateProcessorServiceList;
        this.updaterProperties = updaterProperties;
        this.updatesSender = updatesSender;
    }

    public void update() {
        var links = linkService.getLinksBeforeDateTime(OffsetDateTime.now().minus(updaterProperties.interval()));

        String message = null;

        for (var link : links) {
            for (var processor : linkUpdateProcessorServiceList) {
                message = processor.process(link);

                if (message != null) {
                    break;
                }
            }

            if (message != null && !message.isEmpty()) {
                updatesSender.send(
                    new LinkUpdateRequest(
                        link.getLinkId(),
                        link.getUrl(),
                        message,
                        tgChatService.getChats(link.getLinkId()).stream().map(ChatDTO::getChatId).toList()
                    )
                );
            }

            linkService.updateLastCheckTime(link.getLinkId());
        }
    }
}
