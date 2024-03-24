package edu.java.scrapper.service.proccesor;

import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.model.Link;
import org.springframework.stereotype.Service;

@Service
public class StackOverFlowLinkUpdateProcessorService implements LinkUpdateProcessorService {
    private final StackOverflowClient stackOverflowClient;
    private final static int PATH_LENGTH = 4;

    public StackOverFlowLinkUpdateProcessorService(StackOverflowClient stackOverflowClient) {
        this.stackOverflowClient = stackOverflowClient;
    }

    @Override
    public String process(Link link) {
        var uri = link.getUrl();
        var host = uri.getHost();
        var split = uri.getPath().split("/");
        int questionId;

        if (host == null || !host.equals("stackoverflow.com") || split.length != PATH_LENGTH) {
            return null;
        }

        try {
            questionId = Integer.parseInt(split[2]);
        } catch (NumberFormatException e) {
            return null;
        }

        var response = stackOverflowClient.fetchQuestion(questionId);

        if (response.lastUpdateDate().isAfter(link.getLastCheckTime())) {
            return "Появились изменения в вопросе.";
        }

        return "";
    }
}
