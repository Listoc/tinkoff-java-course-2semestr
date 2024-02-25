package edu.java.scrapper;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.model.QuestionResponse;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.NoSuchElementException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@WireMockTest(httpPort = 80)
public class StackOverflowClientTest {
    @Test
    public void fetchQuestionTest(WireMockRuntimeInfo wireMockRuntimeInfo) {
        var stackOverflowClient = new StackOverflowClient("localhost");
        var mock = wireMockRuntimeInfo.getWireMock();

        mock.loadMappingsFrom("src/test/resources/stackoverflow/");

        var result = stackOverflowClient.fetchQuestion(65397363);
        var expected = new QuestionResponse(
            65397363,
            OffsetDateTime.of(
                2020,
                12,
                23,
                16,
                27,
                52,
                0,
                ZoneOffset.UTC
            )
        );

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void noSuchQuestionTest() {
        var stackOverflowClient = new StackOverflowClient("localhost");

        assertThatThrownBy(() -> stackOverflowClient.fetchQuestion(123412))
            .isInstanceOf(NoSuchElementException.class);
    }
}
