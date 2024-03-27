package edu.java.scrapper.http;

import edu.java.scrapper.model.AnswerList;
import edu.java.scrapper.model.QuestionList;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface QuestionService {
    @GetExchange("/questions/{id}?site=stackoverflow")
    QuestionList getQuestion(@PathVariable int id);

    @GetExchange("/questions/{id}/answers?site=stackoverflow&filter=!nNPvSNXCix")
    AnswerList getAnswers(@PathVariable int id);
}
