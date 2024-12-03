package org.example.gui.forms;

import org.example.Student;
import org.example.StudentManager;
import org.example.gui.GuiContext;

import javax.swing.*;

public class StudentUpdateFormComponent extends StudentFormComponent implements SelectionListener {

    public StudentUpdateFormComponent(StudentManager studentManager) {
        super(studentManager);
        submitButton.setText("Update student");
        GuiContext.addSelectionListener(this);
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
            JOptionPane.showMessageDialog(this, "Please enter valid age and grade values.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student newStudent = new Student(name, age, grade, studentID);
        studentManager.updateStudent(newStudent);
        GuiContext.refresh();

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
}
