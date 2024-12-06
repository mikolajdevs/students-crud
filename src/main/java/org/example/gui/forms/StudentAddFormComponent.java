package org.example.gui.forms;

import org.example.Student;
import org.example.StudentManager;
import org.example.gui.GuiContext;

import javax.swing.*;

public class StudentAddFormComponent extends StudentFormComponent {

    public StudentAddFormComponent(StudentManager studentManager) {
        super(studentManager);
        submitButton.setText("Add student");
    }

    @Override
    protected void onSubmit() {
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

        Student newStudent = new Student(name, age, grade);

        studentManager.addStudent(newStudent);
        GuiContext.refresh();

        JOptionPane.showMessageDialog(
                this,
                "Student added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );

        resetForm();
    }
}
