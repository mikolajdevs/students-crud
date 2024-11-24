package org.example.gui;

import org.example.Student;
import org.example.StudentManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Arrays;

public class StudentFormComponent extends JPanel implements SelectionListener {
    private final JTextField studentIDField;
    private final JTextField nameField;
    private final JTextField ageField;
    private final JTextField gradeField;
    private final JButton addButton;
    private final StudentManager studentManager;
    private final StudentsTableComponent studentsTableComponent;

    public StudentFormComponent(StudentManager studentManager, StudentsTableComponent studentsTableComponent) {
        this.studentManager = studentManager;
        this.studentsTableComponent = studentsTableComponent;

        setLayout(new GridBagLayout());

        int jTextFieldColumns = 15;
        studentIDField = new JTextField(jTextFieldColumns);
        nameField = new JTextField(jTextFieldColumns);
        ageField = new JTextField(jTextFieldColumns);
        gradeField = new JTextField(jTextFieldColumns);
        addButton = new JButton("Add Student");

        List<JTextField> formFields = Arrays.asList(studentIDField, nameField, ageField, gradeField);
        initFormGrid(formFields);

        addButton.addActionListener(_ -> addStudent());

        SelectionContext.addSelectionListener(this);
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
        add(addButton, gbc);
    }

    private void addStudent() {
        String studentID = studentIDField.getText().trim();
        String name = nameField.getText().trim();
        int age;
        double grade;

        try {
            age = Integer.parseInt(ageField.getText().trim());
            grade = Double.parseDouble(gradeField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid age and grade values.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student newStudent = new Student(name, age, grade, studentID);

        studentManager.addStudent(newStudent);
        studentsTableComponent.refreshTable();

        JOptionPane.showMessageDialog(
                this,
                "Student added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );

        resetForm();
    }

    @Override
    public void onSelectionChange(Student newStudent) {
        if (newStudent != null) {
            studentIDField.setText(newStudent.getStudentID());
            nameField.setText(newStudent.getName());
            ageField.setText(String.valueOf(newStudent.getAge()));
            gradeField.setText(String.valueOf(newStudent.getGrade()));
        } else {
            resetForm();
        }
    }

    private void resetForm() {
        studentIDField.setText("");
        nameField.setText("");
        ageField.setText("");
        gradeField.setText("");
    }
}
