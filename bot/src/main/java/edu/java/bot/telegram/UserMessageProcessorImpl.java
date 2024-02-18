package edu.java.bot.telegram;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMessageProcessorImpl implements UserMessageProcessor {
    private final Map<String, Command> commands;

    @Autowired
    public UserMessageProcessorImpl(List<Command> commandList) {
        this.commands = new HashMap<>();

        for (var command : commandList) {
            commands.put(command.command(), command);
        }
    }

    @Override
    public BotCommand[] commands() {
        var commandArray = new BotCommand[commands.size()];
        int i = 0;
        for (var command : commands.values()) {
            commandArray[i] = new BotCommand(command.command(), command.description());
            i++;
        }
        return commandArray;
    }

    @Override
    public SendMessage process(@NotNull Update update) {
        var commandText = update.message().text().split(" ")[0].trim();

        var result = commands.get(commandText);

        if (result != null) {
            return result.handle(update);
        }

        return new SendMessage(
            update.message().chat().id(),
            "No such command. Try `/help` to get all commands.")
            .parseMode(ParseMode.Markdown);
    }
}
