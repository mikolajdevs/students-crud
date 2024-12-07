package org.example.gui.messages;

public abstract class MessageHandler {
    public void handle(String message) {
        this.handle(message, MessageType.SUCCESS);
    }

    public abstract void handle(String message, MessageType type);
}
