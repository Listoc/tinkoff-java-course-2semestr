package edu.java.bot;

import edu.java.bot.model.User;
import edu.java.bot.repository.InMemoryRepository;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class RepositoryTest {
    @Test
    public void addUserFirstTime() {
        var repository = new InMemoryRepository();
        assertThat(repository.addUser(new User(5L))).isTrue();
    }

    @Test
    public void addUserSecondTime() {
        var repository = new InMemoryRepository();
        repository.addUser(new User(5L));
        assertThat(repository.addUser(new User(5L))).isFalse();
    }

    @Test
    public void getLinksByUserWithoutUser() {
        var repository = new InMemoryRepository();

        assertThat(repository.getLinksByUser(new User(5L))).isNull();
    }

    @Test
    public void getLinksByUserWithNoLinks() {
        var repository = new InMemoryRepository();
        repository.addUser(new User(5L));

        assertThat(repository.getLinksByUser(new User(5L))).isEmpty();
    }

    @Test
    public void getLinksByUserWithLinks() {
        var repository = new InMemoryRepository();
        var user = new User(5L);
        repository.addUser(user);
        repository.addLinkToUser(user, "test");
        repository.addLinkToUser(user, "test2");

        assertThat(repository.getLinksByUser(user)).containsExactlyInAnyOrderElementsOf(List.of("test", "test2"));
    }

    @Test
    public void addLinkToUserWithoutUser() {
        var repository = new InMemoryRepository();

        assertThat(repository.addLinkToUser(new User(5L), "test")).isFalse();
    }

    @Test
    public void addLinkToUserFirstTime() {
        var repository = new InMemoryRepository();
        var user = new User(5L);
        repository.addUser(user);

        assertThat(repository.addLinkToUser(user, "test")).isTrue();
    }

    @Test
    public void deleteLinkFromUserWithoutUser() {
        var repository = new InMemoryRepository();

        assertThat(repository.deleteLinkByUser(new User(5L), "test")).isFalse();
    }

    @Test
    public void deleteLinkFromUserWithUser() {
        var repository = new InMemoryRepository();
        var user = new User(5L);
        repository.addUser(user);
        repository.addLinkToUser(user, "test");

        assertThat(repository.deleteLinkByUser(user, "test")).isTrue();
        assertThat(repository.getLinksByUser(user)).isEmpty();
    }
}
