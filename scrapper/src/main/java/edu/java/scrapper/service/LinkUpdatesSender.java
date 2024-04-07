package edu.java.scrapper.service;

import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.shared.model.LinkUpdateRequest;
import org.springframework.stereotype.Service;

@Service
public class LinkUpdatesSender {
    private final ScrapperQueueProducer scrapperQueueProducer;
    private final BotClient botClient;
    private final ApplicationConfig applicationConfig;

    public LinkUpdatesSender(
        ScrapperQueueProducer scrapperQueueProducer,
        BotClient botClient,
        ApplicationConfig applicationConfig
    ) {
        this.scrapperQueueProducer = scrapperQueueProducer;
        this.botClient = botClient;
        this.applicationConfig = applicationConfig;
    }

    public void send(LinkUpdateRequest linkUpdateRequest) {
        if (applicationConfig.useQueue()) {
            scrapperQueueProducer.send(linkUpdateRequest);
        } else {
            botClient.sendUpdates(linkUpdateRequest);
        }
    }
}
