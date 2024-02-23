package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.command.HelpCommand;
import edu.java.bot.command.ListCommand;
import edu.java.bot.command.StartCommand;
import edu.java.bot.command.TrackCommand;
import edu.java.bot.command.UntrackCommand;
import edu.java.bot.model.User;
import edu.java.bot.repository.InMemoryRepository;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class CommandTest {
    private static final Update mock;
    private static final User user = new User(5L);

    static {
        mock = mock(Update.class);
        when(mock.message()).thenReturn(mock(Message.class));
        when(mock.message().chat()).thenReturn(mock(Chat.class));
        when(mock.message().chat().id()).thenReturn(5L);
        when(mock.message().from()).thenReturn(mock(com.pengrad.telegrambot.model.User.class));
        when(mock.message().from().id()).thenReturn(5L);
    }

    @Test
    public void helpTest() {
        var repository = new InMemoryRepository();
        var help = new HelpCommand(List.of(new ListCommand(repository)));

        var expectedText = "Bot supports next commands:\n`/list` — get all tracked links\n";

        var resultParameters = help.handle(mock).getParameters();

        assertThat(resultParameters.get("text")).isEqualTo(expectedText);
        assertThat(resultParameters.get("chat_id")).isEqualTo(5L);
        assertThat(resultParameters.get("parse_mode")).isEqualTo("Markdown");
    }

    @Test
    public void emptyListTest() {
        var repository = new InMemoryRepository();
        repository.addUser(new User(5L));
        var command = new ListCommand(repository);

        var expectedText = "You don't have any tracked links. Try to add one by using `/track` command followed by link.";

        var resultParameters = command.handle(mock).getParameters();

        assertThat(resultParameters.get("text")).isEqualTo(expectedText);
        assertThat(resultParameters.get("chat_id")).isEqualTo(5L);
        assertThat(resultParameters.get("parse_mode")).isEqualTo("Markdown");
    }

    @Test
    public void notEmptyListTest() {
        var repository = new InMemoryRepository();
        repository.addUser(user);
        repository.addLinkToUser(user, "test");

        var command = new ListCommand(repository);

        var expectedText = "Links you are tracking:\n`test`\n";

        var resultParameters = command.handle(mock).getParameters();

        assertThat(resultParameters.get("text")).isEqualTo(expectedText);
        assertThat(resultParameters.get("chat_id")).isEqualTo(5L);
        assertThat(resultParameters.get("parse_mode")).isEqualTo("Markdown");
    }

    @Test
    public void FirstStartTest() {
        var repository = new InMemoryRepository();

        var command = new StartCommand(repository);

        var expectedText = "You have been registered. Try `/help` to get all commands";

        var resultParameters = command.handle(mock).getParameters();

        assertThat(resultParameters.get("text")).isEqualTo(expectedText);
        assertThat(resultParameters.get("chat_id")).isEqualTo(5L);
        assertThat(resultParameters.get("parse_mode")).isEqualTo("Markdown");
        assertThat(repository.addUser(user)).isFalse();
    }

    @Test
    public void SecondStartTest() {
        var repository = new InMemoryRepository();
        repository.addUser(user);

        var command = new StartCommand(repository);

        var expectedText = "You are already registered.";

        var resultParameters = command.handle(mock).getParameters();

        assertThat(resultParameters.get("text")).isEqualTo(expectedText);
        assertThat(resultParameters.get("chat_id")).isEqualTo(5L);
        assertThat(resultParameters.get("parse_mode")).isEqualTo("Markdown");
    }

    @Test
    public void TrackWithoutLinkTest() {
        when(mock.message().text()).thenReturn("/track");

        var user = new User(5L);

        var repository = new InMemoryRepository();
        repository.addUser(user);

        var command = new TrackCommand(repository);

        var expectedText = "You need to follow `/track` command by a link";

        var resultParameters = command.handle(mock).getParameters();

        assertThat(resultParameters.get("text")).isEqualTo(expectedText);
        assertThat(resultParameters.get("chat_id")).isEqualTo(5L);
        assertThat(resultParameters.get("parse_mode")).isEqualTo("Markdown");
    }

    @Test
    public void TrackWithLinkTest() {
        when(mock.message().text()).thenReturn("/track someLink");

        var repository = new InMemoryRepository();
        repository.addUser(user);

        var command = new TrackCommand(repository);

        var expectedText = "Now you track new link:\n`someLink`";

        var resultParameters = command.handle(mock).getParameters();

        assertThat(resultParameters.get("text")).isEqualTo(expectedText);
        assertThat(resultParameters.get("chat_id")).isEqualTo(5L);
        assertThat(resultParameters.get("parse_mode")).isEqualTo("Markdown");
    }

    @Test
    public void UntrackWithoutLinkTest() {
        when(mock.message().text()).thenReturn("/untrack");

        var repository = new InMemoryRepository();
        repository.addUser(user);

        var command = new UntrackCommand(repository);

        var expectedText = "You need to follow `/untrack` command by a link";

        var resultParameters = command.handle(mock).getParameters();

        assertThat(resultParameters.get("text")).isEqualTo(expectedText);
        assertThat(resultParameters.get("chat_id")).isEqualTo(5L);
        assertThat(resultParameters.get("parse_mode")).isEqualTo("Markdown");
    }

    @Test
    public void UntrackWithLinkTest() {
        when(mock.message().text()).thenReturn("/untrack someLink");

        var repository = new InMemoryRepository();
        repository.addUser(user);
        repository.addLinkToUser(user, "someLink");

        var command = new UntrackCommand(repository);

        var expectedText = "Now you don't track that link:\n`someLink`";

        var resultParameters = command.handle(mock).getParameters();

        assertThat(resultParameters.get("text")).isEqualTo(expectedText);
        assertThat(resultParameters.get("chat_id")).isEqualTo(5L);
        assertThat(resultParameters.get("parse_mode")).isEqualTo("Markdown");
    }

    @Test
    public void noUserTests() {
        when(mock.message().text()).thenReturn("/some testLink");

        var repository = new InMemoryRepository();

        var list = new ListCommand(repository);
        var track = new TrackCommand(repository);
        var untrack = new UntrackCommand(repository);

        var expectedText = "You need to register first. Try `/start` command.";

        var resultList = list.handle(mock).getParameters().get("text");
        var resultTrack = track.handle(mock).getParameters().get("text");;
        var resultUntrack = untrack.handle(mock).getParameters().get("text");;

        assertThat(resultList).isEqualTo(expectedText);
        assertThat(resultTrack).isEqualTo(expectedText);
        assertThat(resultUntrack).isEqualTo(expectedText);
    }
}
