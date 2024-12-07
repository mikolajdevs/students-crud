package org.example.gui.messages;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageConsole extends MessageHandler {
    private final static ConsoleComponent console = new ConsoleComponent();
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final static char CHECK = '✔';
    private final static char X = '✖';

    @Override
    public void handle(String message, MessageType type) {
        char prefix = type == MessageType.SUCCESS ? CHECK : X;
        console.log("%s %s%s".formatted(prefix, nowSuffix(), message));
    }

    public static ConsoleComponent getConsole() {
        return console;
    }

    public static String nowSuffix() {
        return "%s\s\s\s".formatted(LocalDateTime.now().format(formatter));
    }
}
