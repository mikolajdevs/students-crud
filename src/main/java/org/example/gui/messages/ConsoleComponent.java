package org.example.gui.messages;

import org.example.gui.GuiUtils;

import javax.swing.*;
import java.awt.*;

public class ConsoleComponent extends JScrollPane {
    public final static int DEFAULT_CONSOLE_HEIGHT = 150;
    private final JTextArea textArea;
    private boolean isFirstMessage = true;

    public ConsoleComponent() {
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(GuiUtils.C_NAVY);
        textArea.setForeground(Color.WHITE);
        setViewportView(textArea);
    }

    public void log(String message) {
        this.appendMessage(message);
    }

    private void appendMessage(String message) {
        if (isFirstMessage) {
            textArea.append(message);
            isFirstMessage = false;
        } else {
            textArea.append("\n" + message);
        }

        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
