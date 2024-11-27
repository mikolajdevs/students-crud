package org.example.gui;

import org.example.Student;
import org.example.gui.forms.SelectionListener;
import org.example.gui.table.Refreshable;

import java.util.ArrayList;
import java.util.List;

public class GuiContext {
    private static final List<SelectionListener> selectionListeners = new ArrayList<>();
    private static final List<Refreshable> refreshListeners = new ArrayList<>();

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

}
