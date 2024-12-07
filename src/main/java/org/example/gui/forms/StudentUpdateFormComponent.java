package org.example.gui.forms;

import org.example.Student;
import org.example.StudentManager;
import org.example.gui.GuiService;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class StudentUpdateFormComponent extends StudentFormComponent implements SelectionListener {

    public StudentUpdateFormComponent(StudentManager studentManager) {
        super(studentManager);
        submitButton.setText("Update student");
        GuiService.addSelectionListener(this);
    }

    @Override
    protected void createFormFields() {
        formFields = Arrays.asList(
                studentIDField = new JTextField(jTextFieldColumns),
                nameField = new JTextField(jTextFieldColumns),
                ageField = new JTextField(jTextFieldColumns),
                gradeField = new JTextField(jTextFieldColumns)
        );
    }

    @Override
    protected List<JLabel> createLabels() {
        return Arrays.asList(
                new JLabel("Student ID:"),
                new JLabel("Name:"),
                new JLabel("Age:"),
                new JLabel("Grade:")
        );
    }

    @Override
    protected void onSubmit() {
        String studentID = studentIDField.getText().trim();
        String name = nameField.getText().trim();
        int age;
        double grade;

        try {
            age = Integer.parseInt(ageField.getText().trim());
            grade = Double.parseDouble(gradeField.getText().trim());
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            return;
        }

        Student newStudent = new Student(name, age, grade, studentID);
        studentManager.updateStudent(newStudent);
        GuiService.refresh();
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
}
