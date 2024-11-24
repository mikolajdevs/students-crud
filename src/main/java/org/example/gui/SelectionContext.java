package org.example.gui;

import org.example.Student;

import java.util.ArrayList;
import java.util.List;

public class SelectionContext {
    private static Student student;
    private static final List<SelectionListener> listeners = new ArrayList<>();

    public static void setStudent(Student student) {
        SelectionContext.student = student;
        notifyListeners();
    }

    public static Student getStudent() {
        return student;
    }

    public static void addSelectionListener(SelectionListener listener) {
        listeners.add(listener);
    }

    private static void notifyListeners() {
        for (SelectionListener listener : listeners) {
            listener.onSelectionChange(student);
        }
    }
}
