package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.command.StartCommand;
import edu.java.bot.model.User;
import edu.java.bot.repository.InMemoryRepository;
import edu.java.bot.telegram.UserMessageProcessorImpl;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class MessageProcessorTest {
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
    public void properCommand() {
        when(mock.message().text()).thenReturn("/start");
        var repository = new InMemoryRepository();

        var processor = new UserMessageProcessorImpl(List.of(new StartCommand(repository)));

        var expectedText = "You have been registered. Try `/help` to get all commands";

        var resultParameters = processor.process(mock).getParameters();;

        assertThat(resultParameters.get("text")).isEqualTo(expectedText);
        assertThat(resultParameters.get("chat_id")).isEqualTo(5L);
        assertThat(resultParameters.get("parse_mode")).isEqualTo("Markdown");
        assertThat(repository.addUser(user)).isFalse();
    }

    @Test
    public void wrongCommand() {
        when(mock.message().text()).thenReturn("/wrong");

        var repository = new InMemoryRepository();

        var processor = new UserMessageProcessorImpl(List.of(new StartCommand(repository)));

        var expectedText = "No such command. Try `/help` to get all commands.";

        var resultParameters = processor.process(mock).getParameters();;

        assertThat(resultParameters.get("text")).isEqualTo(expectedText);
        assertThat(resultParameters.get("chat_id")).isEqualTo(5L);
        assertThat(resultParameters.get("parse_mode")).isEqualTo("Markdown");
    }
}
