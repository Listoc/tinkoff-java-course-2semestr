package edu.java.scrapper.http;

import edu.java.scrapper.model.RepositoryResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface RepositoryService {
    @GetExchange("/repos/{owner}/{repo}")
    RepositoryResponse getRepository(@PathVariable String owner, @PathVariable String repo);
}
