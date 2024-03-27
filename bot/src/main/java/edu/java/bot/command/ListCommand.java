package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class ListCommand extends AbstractCommand implements Command {
    private final ScrapperClient scrapperClient;

    @Autowired
    public ListCommand(ScrapperClient scrapperClient) {
        super("/list", "получить все отслеживаемые ссылки");
        this.scrapperClient = scrapperClient;
    }

    @Override
    public SendMessage handle(@NotNull Update update) {
        StringBuilder text = new StringBuilder("Вы отслеживаете следующие ссылки:\n");

        try {
            var response = scrapperClient.getAllLinks(update.message().chat().id());
            for (var link : response.links()) {
                text.append(link.url().toString()).append('\n');
            }
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                text = new StringBuilder("Сначала вам нужно зарегистрироваться. Попробуйте команду `/start`.");
            } else {
                text = new StringBuilder("Ошибка на сервере!");
            }
        } catch (WebClientRequestException e) {
            text = new StringBuilder("Сервер недоступен!");
        }

        return new SendMessage(update.message().chat().id(), text.toString()).parseMode(ParseMode.Markdown);
    }
}
