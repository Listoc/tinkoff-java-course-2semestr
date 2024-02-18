package edu.java.bot.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import jakarta.annotation.PreDestroy;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class MyTelegramBot implements Bot {
    private final UserMessageProcessor userMessageProcessor;
    private final TelegramBot bot;

    public MyTelegramBot(@NotNull String token, @NotNull UserMessageProcessor userMessageProcessor) {
        this.userMessageProcessor = userMessageProcessor;
        this.bot = new TelegramBot(token);
        this.bot.setUpdatesListener(this);
        this.bot.execute(new SetMyCommands(userMessageProcessor.commands()));
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(@NotNull BaseRequest<T, R> request) {
        return bot.execute(request);
    }

    @Override
    public int process(List<Update> updateList) {
        for (var update : updateList) {
            if (update.message() == null) {
                continue;
            }
            execute(userMessageProcessor.process(update));
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @PreDestroy
    @Override
    public void close() {
        bot.removeGetUpdatesListener();
        bot.shutdown();
    }
}
