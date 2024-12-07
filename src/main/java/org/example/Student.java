package org.example;

public class Student {
    private final String name;
    private final int age;
    private final double grade;
    private String studentID;

    public Student(String name, int age, double grade, String studentID) {
        this.name = name;
        this.age = age;
        this.grade = grade;
        this.studentID = studentID;
    }

    public Student(String name, int age, double grade) {
        this.name = name;
        this.age = age;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getGrade() {
        return grade;
    }

    public String getStudentID() {
        return studentID;
    }

    @Override
    public String toString() {
        return "Student ID: %s, Name: %s, Age: %d, Grade: %s".formatted(studentID, name, age, grade);
    }
}
