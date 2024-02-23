package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public abstract class AbstractCommand {
    private final String command;
    private final String description;

    protected AbstractCommand(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String command() {
        return command;
    }

    public String description() {
        return description;
    }

    public abstract SendMessage handle(Update update);
}
