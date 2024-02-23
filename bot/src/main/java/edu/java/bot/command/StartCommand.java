package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.User;
import edu.java.bot.repository.Repository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartCommand extends AbstractCommand implements Command {
    private final Repository repository;

    @Autowired
    public StartCommand(@NotNull Repository repository) {
        super("/start", "register in the bot");
        this.repository = repository;
    }

    @Override
    public SendMessage handle(@NotNull Update update) {
        String text;
        boolean result = repository.addUser(new User(update.message().from().id()));

        if (result) {
            text = "You have been registered. Try `/help` to get all commands";
        } else {
            text = "You are already registered.";
        }

        return new SendMessage(update.message().chat().id(), text).parseMode(ParseMode.Markdown);
    }
}
