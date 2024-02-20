package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand extends AbstractCommand implements Command {
    private final List<Command> commands;

    @Autowired
    public HelpCommand(@NotNull List<Command> commands) {
        super("/help", "get other commands info");
        this.commands = commands;
    }

    @Override
    public SendMessage handle(@NotNull Update update) {
        StringBuilder text = new StringBuilder("Bot supports next commands:\n");

        for (var command : commands) {
            text
                .append('`')
                .append(command.command())
                .append('`')
                .append(" — ")
                .append(command.description())
                .append('\n');
        }

        return new SendMessage(update.message().chat().id(), text.toString()).parseMode(ParseMode.Markdown);
    }
}
