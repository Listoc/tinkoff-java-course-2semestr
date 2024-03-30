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
public class StartCommand extends AbstractCommand implements Command {
    private final ScrapperClient scrapperClient;

    @Autowired
    public StartCommand(ScrapperClient scrapperClient) {
        super("/start", "регистрация в боте");
        this.scrapperClient = scrapperClient;
    }

    @Override
    public SendMessage handle(@NotNull Update update) {
        String text;

        try {
            scrapperClient.registerChat(update.message().chat().id());
            text = "Вы были зарегистрированы! Попробуйте команду `/help`";
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                text = "Вы уже зарегистрированы. Попробуйте команду `/help`";
            } else {
                text = "Ошибка на сервере!";
            }
        } catch (WebClientRequestException e) {
            text = "Сервер недоступен!";
        }

        return new SendMessage(update.message().chat().id(), text).parseMode(ParseMode.Markdown);
    }
}
