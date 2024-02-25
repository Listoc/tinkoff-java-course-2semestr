package edu.java.scrapper;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.client.GithubClient;
import edu.java.scrapper.model.RepositoryResponse;
import org.junit.jupiter.api.Test;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.NoSuchElementException;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@WireMockTest(httpPort = 80)
public class GitHubClientTest {

    @Test
    public void fetchRepositoryTest(WireMockRuntimeInfo wireMockRuntimeInfo) {
        GithubClient githubClient = new GithubClient("localhost");
        var mock = wireMockRuntimeInfo.getWireMock();

        mock.loadMappingsFrom("src/test/resources/git/");

        var result = githubClient.fetchRepository("Listoc", "distributed-systems");
        var expected = new RepositoryResponse(
            "https://github.com/Listoc/distributed-systems",
            OffsetDateTime.of(
                2024,
                2,
                8,
                19,
                4,
                49,
                0,
                ZoneOffset.UTC
            )
        );

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void noSuchRepositoryTest(WireMockRuntimeInfo wireMockRuntimeInfo) {
        GithubClient githubClient = new GithubClient("localhost");

        assertThatThrownBy(() -> githubClient.fetchRepository("someUser", "someRep"))
            .isInstanceOf(NoSuchElementException.class);
    }
}
