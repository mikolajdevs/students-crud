package org.example.gui.messages;

/**
 * Abstract class for message handling, default is Console but can be changed to Snackbar in Main.java.
 * Messages handling implementation allows adding multiple handlers.
 */
public abstract class MessageHandler {
    public void handle(String message) {
        this.handle(message, MessageType.SUCCESS);
    }

    public abstract void handle(String message, MessageType type);
}
