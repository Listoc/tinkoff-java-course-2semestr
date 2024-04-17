package edu.java.scrapper.service.proccesor;

import edu.java.shared.model.LinkUpdateRequest;

public interface UpdatesSender {
    void send(LinkUpdateRequest linkUpdateRequest);
}
