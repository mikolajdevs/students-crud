package org.example;

import java.util.ArrayList;

public interface StudentManager {
    void addStudent(Student student);

    void removeStudent(String studentID);

    void updateStudent(Student student);

    ArrayList<Student> displayAllStudents();

    double calculateAverageGrade();
}
