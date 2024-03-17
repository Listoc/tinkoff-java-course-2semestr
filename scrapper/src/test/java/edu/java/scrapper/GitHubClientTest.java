package edu.java.scrapper;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.client.GithubClient;
import edu.java.scrapper.model.RepositoryResponse;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.NoSuchElementException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@WireMockTest
public class GitHubClientTest {

    @Test
    public void fetchRepositoryTest(WireMockRuntimeInfo wireMockRuntimeInfo) {
        GithubClient githubClient = new GithubClient(wireMockRuntimeInfo.getHttpBaseUrl());
        var mock = wireMockRuntimeInfo.getWireMock();

        mock.loadMappingsFrom("src/test/resources/git/");

        var result = githubClient.fetchRepository("Listoc", "distributed-systems");
        var expected = new RepositoryResponse(
            "https://github.com/Listoc/distributed-systems",
            OffsetDateTime.of(
                2024,
                2,
                22,
                17,
                28,
                33,
                0,
                ZoneOffset.UTC
            )
        );

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void noSuchRepositoryTest(WireMockRuntimeInfo wireMockRuntimeInfo) {
        GithubClient githubClient = new GithubClient(wireMockRuntimeInfo.getHttpBaseUrl());

        assertThatThrownBy(() -> githubClient.fetchRepository("someUser", "someRep"))
            .isInstanceOf(NoSuchElementException.class);
    }
}
