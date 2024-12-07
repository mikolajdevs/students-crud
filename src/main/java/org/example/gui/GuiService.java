package org.example.gui;

import org.example.Student;
import org.example.gui.forms.SelectionListener;
import org.example.gui.messages.MessageConsole;
import org.example.gui.messages.MessageHandler;
import org.example.gui.messages.MessageType;
import org.example.gui.table.Refreshable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiService {
    private static final List<SelectionListener> selectionListeners = new ArrayList<>();
    private static final List<Refreshable> refreshListeners = new ArrayList<>();
    private static final List<MessageHandler> messageHandlers = new ArrayList<>();

    public static void addSelectionListener(SelectionListener listener) {
        selectionListeners.add(listener);
    }

    public static void addRefreshListener(Refreshable listener) {
        refreshListeners.add(listener);
    }

    public static void onSelect(Student student) {
        notifySelectionListeners(student);
    }

    private static void notifySelectionListeners(Student student) {
        selectionListeners.forEach(listener -> listener.onSelectionChange(student));
    }

    public static void refresh() {
        notifyRefreshListeners();
    }

    private static void notifyRefreshListeners() {
        refreshListeners.forEach(Refreshable::refresh);
    }

    public static void setMessageHandlers(MessageHandler... handlers) {
        messageHandlers.clear();
        if (handlers != null && handlers.length > 0) {
            messageHandlers.addAll(Arrays.asList(handlers));
        }
    }

    public static List<MessageHandler> getMessageHandlers() {
        return messageHandlers;
    }

    public static void handleMessage(String message) {
        messageHandlers.forEach(handler -> handler.handle(message));
    }

    public static void handleMessage(String message, MessageType type) {
        messageHandlers.forEach(handler -> handler.handle(message, type));
    }
}
