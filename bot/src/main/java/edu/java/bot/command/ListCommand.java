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
public class ListCommand extends AbstractCommand implements Command {
    private final Repository repository;

    @Autowired
    public ListCommand(@NotNull Repository repository) {
        super("/list", "get all tracked links");
        this.repository = repository;
    }

    @Override
    public SendMessage handle(@NotNull Update update) {
        StringBuilder text;

        if (!repository.isUserRegistered(new User(update.message().from().id()))) {
            text = new StringBuilder("You need to register first. Try `/start` command.");
        } else {
            var result = repository.getLinksByUser(new User(update.message().from().id()));

            if (result.isEmpty()) {
                text = new StringBuilder(
                    "You don't have any tracked links. Try to add one by using `/track` command followed by link."
                );
            } else {
                text = new StringBuilder("Links you are tracking:\n");
                for (var link : result) {
                    text.append('`').append(link).append('`').append('\n');
                }
            }
        }

        return new SendMessage(update.message().chat().id(), text.toString()).parseMode(ParseMode.Markdown);
    }
}
