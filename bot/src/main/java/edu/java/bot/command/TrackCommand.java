package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class TrackCommand extends AbstractCommand implements Command {
    private final ScrapperClient scrapperClient;

    @Autowired
    public TrackCommand(ScrapperClient scrapperClient) {
        super("/track", "добавить новую ссылку в отслеживание");
        this.scrapperClient = scrapperClient;
    }

    @Override
    public SendMessage handle(@NotNull Update update) {
        var split = update.message().text().split(" ");
        String text;

        if (split.length < 2) {
            text = "После команды `/track` должа идти ссылка";
        } else {
            try {
                scrapperClient.addLink(update.message().chat().id(), URI.create(split[1]));
                text = "Теперь вы отслеживаете новую ссылку:\n" + '`' + split[1] + '`';
            } catch (WebClientResponseException e) {
                if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                    text = "Сначала вам нужно зарегистрироваться. Попробуйте команду `/start`.";
                } else {
                    text = "Ошибка на сервере!";
                }
            } catch (WebClientRequestException e) {
                text = "Сервер недоступен!";
            }
        }

        return new SendMessage(update.message().chat().id(), text).parseMode(ParseMode.Markdown);
    }
}
