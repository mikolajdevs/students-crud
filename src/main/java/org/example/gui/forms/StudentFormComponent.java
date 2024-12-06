package org.example.gui.forms;

import org.example.StudentManager;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public abstract class StudentFormComponent extends JPanel {
    protected JTextField studentIDField;
    protected JTextField nameField;
    protected JTextField ageField;
    protected JTextField gradeField;
    protected JButton submitButton = new JButton();
    protected final StudentManager studentManager;

    protected List<JTextField> formFields;

    protected final int jTextFieldColumns = 15;

    protected StudentFormComponent(StudentManager studentManager) {
        this.studentManager = studentManager;

        setLayout(new GridBagLayout());

        createFormFields();
        initFormGrid();

        submitButton.addActionListener(_ -> onSubmit());
    }

    protected void createFormFields() {
        formFields = Arrays.asList(
                nameField = new JTextField(jTextFieldColumns),
                ageField = new JTextField(jTextFieldColumns),
                gradeField = new JTextField(jTextFieldColumns)
        );
    }

    protected List<JLabel> createLabels() {
        return Arrays.asList(new JLabel("Name:"), new JLabel("Age:"), new JLabel("Grade:"));
    }

    private void initFormGrid() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 4, 2, 4);

        List<JLabel> labels = createLabels();

        for (int y = 0; y < labels.size(); y++) {
            gbc.gridx = 0;
            gbc.gridy = y;
            gbc.weightx = 1.0;
            add(labels.get(y), gbc);

            gbc.gridx = 1;
            gbc.weightx = 0;
            add(formFields.get(y), gbc);
        }

        gbc.gridx = 0;
        gbc.gridy = labels.size();
        gbc.gridwidth = 2;
        add(submitButton, gbc);
    }

    protected abstract void onSubmit();

    protected void resetForm() {
        formFields.forEach(jTextField -> jTextField.setText(""));
    }

}
