package org.example.gui.forms;

import org.example.Student;
import org.example.StudentManager;
import org.example.gui.GuiService;
import org.example.gui.messages.MessageType;

public class StudentAddFormComponent extends StudentFormComponent {

    public StudentAddFormComponent(StudentManager studentManager) {
        super(studentManager);
        submitButton.setText("Add student");
    }

    @Override
    protected void onSubmit() {
        String name = nameField.getText().trim();
        String age = ageField.getText().trim();
        String grade = gradeField.getText().trim();

        boolean isAnyFieldEmpty = name.isEmpty() || age.isEmpty() || grade.isEmpty();
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
            Student newStudent = new Student(name, ageParsed, gradeParsed);
            studentManager.addStudent(newStudent);
            GuiService.refresh();
            resetForm();
        } catch (NumberFormatException e) {
            GuiService.handleMessage("Age and grade must be valid numbers", MessageType.FAILURE);
        }
    }
}
