package org.example;

import org.example.gui.GuiService;
import org.example.gui.StudentsGUI;
import org.example.gui.messages.MessageConsole;

public class Main {

    public static void main(String[] args) {
        GuiService.setMessageHandlers(new MessageConsole());
        StudentManager studentManager = new StudentManagerImpl();
        StudentsGUI.getInstance(studentManager).setVisible(true);
    }
}
