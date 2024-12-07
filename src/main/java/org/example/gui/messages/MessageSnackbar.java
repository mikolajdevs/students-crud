package org.example.gui.messages;

import org.example.gui.StudentsGUI;

import javax.swing.*;

public class MessageSnackbar extends MessageHandler {
    @Override
    public void handle(String message, MessageType type) {
        String title;
        int messageType;
        if (type == MessageType.SUCCESS) {
            title = "Success";
            messageType = JOptionPane.INFORMATION_MESSAGE;
        } else {
            title = "Failure";
            messageType = JOptionPane.ERROR_MESSAGE;
        }
        JOptionPane.showMessageDialog(StudentsGUI.getInstance(), message, title, messageType);
    }
}
