package org.example;

import org.example.gui.GuiService;
import org.example.gui.StudentsGUI;
import org.example.gui.messages.MessageConsole;

public class Main {

    public static void main(String[] args) {
        StudentManager studentManager = getStudentManager(args);
        GuiService.setMessageHandlers(new MessageConsole());
        StudentsGUI.getInstance(studentManager).setVisible(true);
    }

    /**
     * Handles user's optional argument for inserting sample records to the db
     */
    private static StudentManager getStudentManager(String[] args) {
        if (args.length == 1 && args[0].equals("--insert-test-records")) {
            return new StudentManagerImpl(true);
        }
        return new StudentManagerImpl();
    }
}
