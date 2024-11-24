package org.example;

import org.example.gui.StudentsGUI;

public class Main {

    public static void main(String[] args) {
        StudentManager studentManager = new StudentManagerImpl();
        new StudentsGUI(studentManager).setVisible(true);
    }
}
