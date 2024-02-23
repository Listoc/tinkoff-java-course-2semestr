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
public class TrackCommand extends AbstractCommand implements Command {
    private final Repository repository;

    @Autowired
    public TrackCommand(@NotNull Repository repository) {
        super("/track", "add new link to tracking");
        this.repository = repository;
    }

    @Override
    public SendMessage handle(@NotNull Update update) {
        var split = update.message().text().split(" ");
        String text;

        if (split.length < 2) {
            text = "You need to follow `/track` command by a link";
        } else {
            var result = repository.addLinkToUser(new User(update.message().from().id()), split[1]);

            if (result) {
                text = "Now you track new link:\n" + '`' + split[1] + '`';
            } else {
                text = "You need to register first. Try `/start` command.";
            }
        }

        return new SendMessage(update.message().chat().id(), text).parseMode(ParseMode.Markdown);
    }
}
