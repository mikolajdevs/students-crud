package org.example;

import org.example.gui.StudentsGUI;

public class Main {

    public static void main(String[] args) {
        StudentManager studentManager = new StudentManagerImpl();
        StudentsGUI.getInstance(studentManager).setVisible(true);
    }
}
