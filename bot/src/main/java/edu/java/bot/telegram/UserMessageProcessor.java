package edu.java.bot.telegram;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public interface UserMessageProcessor {
    BotCommand[] commands();

    SendMessage process(Update update);
}
