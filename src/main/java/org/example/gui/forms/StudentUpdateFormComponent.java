package org.example.gui.forms;

import org.example.Student;
import org.example.StudentManager;
import org.example.gui.GuiService;
import org.example.gui.messages.MessageType;

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
        String age = ageField.getText().trim();
        String grade = gradeField.getText().trim();

        boolean isAnyFieldEmpty = studentID.isEmpty() || name.isEmpty() || age.isEmpty() || grade.isEmpty();
        if (isAnyFieldEmpty) {
            GuiService.handleMessage("Each field is required", MessageType.FAILURE);
            return;
        }
        try {
            int ageParsed = Integer.parseInt(ageField.getText().trim());
            double gradeParsed = Double.parseDouble(gradeField.getText().trim());
            if (ageParsed <= 0) {
                GuiService.handleMessage("Age must be a positive integer", MessageType.FAILURE);
                return;
            } else if (gradeParsed < 0.0 || gradeParsed > 100.0) {
                GuiService.handleMessage("Grade must be between 0.0 and 100.0", MessageType.FAILURE);
                return;
            }
            Student newStudent = new Student(name, ageParsed, gradeParsed, studentID);
            studentManager.updateStudent(newStudent);
            GuiService.refresh();
        } catch (NumberFormatException e) {
            GuiService.handleMessage("Age and grade must be valid numbers", MessageType.FAILURE);
        }
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
