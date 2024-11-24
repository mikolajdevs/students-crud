package org.example;

public class Student {
    private final String name;
    private final int age;
    private final double grade;
    private final String studentID;

    public Student(String name, int age, double grade, String studentID) {
        this.name = name;
        this.age = age;
        this.grade = grade;
        this.studentID = studentID;
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

    public void displayInfo() {
        System.out.println("Student ID: " + studentID + ", Name: " + name + ", Age: " + age + ", Grade: " + grade);
    }
}
