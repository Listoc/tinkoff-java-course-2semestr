package edu.java.scrapper;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class Hw4Test extends IntegrationTest {
    @AfterAll
    public static void close() {
        POSTGRES.close();
    }

    @Test
    public void someTest() {
        assertThat(POSTGRES.getDatabaseName()).isEqualTo("scrapper");
        assertThat(POSTGRES.getUsername()).isEqualTo("postgres");
        assertThat(POSTGRES.getPassword()).isEqualTo("postgres");
    }
}
