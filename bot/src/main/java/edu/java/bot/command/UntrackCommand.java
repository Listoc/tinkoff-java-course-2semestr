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
public class UntrackCommand extends AbstractCommand implements Command {
    private final Repository repository;

    @Autowired
    public UntrackCommand(@NotNull Repository repository) {
        super("/untrack", "remove link from tracking");
        this.repository = repository;
    }

    @Override
    public SendMessage handle(@NotNull Update update) {
        var split = update.message().text().split(" ");
        String text;

        if (split.length < 2) {
            text = "You need to follow `/untrack` command by a link";
        } else {
            var result = repository.deleteLinkByUser(new User(update.message().from().id()), split[1]);

            if (result) {
                text = "Now you don't track that link:\n" + '`' + split[1] + '`';
            } else {
                text = "You need to register first. Try `/start` command.";
            }
        }

        return new SendMessage(update.message().chat().id(), text).parseMode(ParseMode.Markdown);
    }
}
