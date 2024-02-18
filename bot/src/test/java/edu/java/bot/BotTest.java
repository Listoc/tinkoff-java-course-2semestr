package edu.java.bot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.command.StartCommand;
import edu.java.bot.repository.InMemoryRepository;
import edu.java.bot.telegram.MyTelegramBot;
import edu.java.bot.telegram.UserMessageProcessorImpl;
import org.junit.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BotTest {
    private static final Update mock;

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

        var bot = new MyTelegramBot("test", processor);

        assertThat(bot.process(List.of(mock))).isEqualTo(UpdatesListener.CONFIRMED_UPDATES_ALL);

        bot.close();
    }
}
