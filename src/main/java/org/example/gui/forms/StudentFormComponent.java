package org.example.gui.forms;

import org.example.StudentManager;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public abstract class StudentFormComponent extends JPanel {
    protected final JTextField studentIDField;
    protected final JTextField nameField;
    protected final JTextField ageField;
    protected final JTextField gradeField;
    protected JButton submitButton = new JButton();
    protected final StudentManager studentManager;

    protected StudentFormComponent(StudentManager studentManager) {
        this.studentManager = studentManager;

        setLayout(new GridBagLayout());

        int jTextFieldColumns = 15;
        studentIDField = new JTextField(jTextFieldColumns);
        nameField = new JTextField(jTextFieldColumns);
        ageField = new JTextField(jTextFieldColumns);
        gradeField = new JTextField(jTextFieldColumns);

        List<JTextField> formFields = Arrays.asList(studentIDField, nameField, ageField, gradeField);
        initFormGrid(formFields);
    }

    private void initFormGrid(List<JTextField> formFields) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 4, 2, 4);

        List<JLabel> labels = Arrays.asList(
                new JLabel("Student ID:"),
                new JLabel("Name:"),
                new JLabel("Age:"),
                new JLabel("Grade:")
        );

        for (int y = 0; y < 4; y++) {
            gbc.gridx = 0;
            gbc.gridy = y;
            add(labels.get(y), gbc);

            gbc.gridx = 1;
            add(formFields.get(y), gbc);
        }

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(submitButton, gbc);
    }

    protected void resetForm() {
        final var defaultText = "";
        studentIDField.setText(defaultText);
        nameField.setText(defaultText);
        ageField.setText(defaultText);
        gradeField.setText(defaultText);
    }

}
