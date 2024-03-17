package edu.java.scrapper.scheduler;

import edu.java.scrapper.service.LinkUpdater;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LinkUpdateScheduler {
    private final LinkUpdater linkUpdater;

    public LinkUpdateScheduler(LinkUpdater linkUpdater) {
        this.linkUpdater = linkUpdater;
    }

    @Scheduled(fixedDelayString = "#{@scheduler.interval}")
    public void update() {
        linkUpdater.update();
    }
}
