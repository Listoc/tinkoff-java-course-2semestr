package edu.java.scrapper;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.util.HashSet;
import static org.assertj.core.api.Assertions.assertThat;

public class Hw4Test extends IntegrationTest {
    @AfterAll
    public static void close() {
        POSTGRES.close();
    }

    @Test
    public void someTest() throws SQLException {
        var connection = POSTGRES.createConnection("");
        var result = connection.createStatement().executeQuery("SELECT tablename FROM pg_catalog.pg_tables WHERE schemaname = 'public'");
        var tables = new HashSet<String>();

        while (result.next()) {
            tables.add(result.getString(1));
        }

        assertThat(tables).contains("chat", "link", "chat_link_map");
    }
}
