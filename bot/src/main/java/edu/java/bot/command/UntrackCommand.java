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
public class UntrackCommand extends AbstractCommand implements Command {
    private final ScrapperClient scrapperClient;

    @Autowired
    public UntrackCommand(ScrapperClient scrapperClient) {
        super("/untrack", "удалить ссылку из отслеживания");
        this.scrapperClient = scrapperClient;
    }

    @Override
    public SendMessage handle(@NotNull Update update) {
        var split = update.message().text().split(" ");
        String text;

        if (split.length < 2) {
            text = "После команды `/untrack` должа идти ссылка";
        } else {
            try {
                scrapperClient.removeLink(update.message().chat().id(), URI.create(split[1]));
                text = "Вы больше не отслеживаете следующую ссылку:\n" + '`' + split[1] + '`';
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
