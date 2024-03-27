package edu.java.scrapper.service.proccesor;

import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.model.LinkDTO;
import org.springframework.stereotype.Service;

@Service
public class StackOverFlowLinkUpdateProcessorService implements LinkUpdateProcessorService {
    private final StackOverflowClient stackOverflowClient;
    private final static int PATH_LENGTH = 4;

    public StackOverFlowLinkUpdateProcessorService(StackOverflowClient stackOverflowClient) {
        this.stackOverflowClient = stackOverflowClient;
    }

    @Override
    public String process(LinkDTO link) {
        var uri = link.getUrl();
        var host = uri.getHost();
        var split = uri.getPath().split("/");
        int questionId;
        String message = "Появились изменения в вопросе.";

        if (host == null || !host.equals("stackoverflow.com") || split.length != PATH_LENGTH) {
            return null;
        }

        try {
            questionId = Integer.parseInt(split[2]);
        } catch (NumberFormatException e) {
            return null;
        }

        var response = stackOverflowClient.fetchQuestion(questionId);

        if (response.lastUpdateDate().isAfter(link.getLastCheck())) {
            message += checkAnswers(link, questionId);
            return message;
        }

        return "";
    }

    private String checkAnswers(LinkDTO link, int id) {
        var answers = stackOverflowClient.fetchAnswers(id)
            .stream()
            .filter((a) -> a.answerDate().isAfter(link.getLastCheck()))
            .toList();

        StringBuilder builder;

        if (answers.isEmpty()) {
            return "";
        }

        builder = new StringBuilder("\n\nНовые ответы:\n");

        for (var answer : answers.reversed()) {
            builder.append(answer.link()).append("\n");
        }

        return builder.toString();
    }
}
