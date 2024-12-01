package org.example;

import java.sql.*;
import java.util.ArrayList;

public class StudentManagerImpl implements StudentManager {

    private Connection connect() {
        String url = "jdbc:sqlite:students.db";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    @Override
    public void addStudent(Student student) {
        String sql = "INSERT INTO students(studentID, name, age, grade) VALUES(?,?,?,?)";
        String nextStudentID = generateNextStudentID();
        try (
                Connection connection = this.connect();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, nextStudentID);
            statement.setString(2, student.getName());
            statement.setInt(3, student.getAge());
            statement.setDouble(4, student.getGrade());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeStudent(String studentID) {
        String sql = "DELETE FROM students WHERE studentID = ?";

        try (
                Connection conn = this.connect();
                PreparedStatement statement = conn.prepareStatement(sql)
        ) {
            statement.setString(1, studentID);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student removed successfully.");
            } else {
                System.out.println("No student found with ID: " + studentID);
            }
        } catch (SQLException e) {
            System.out.println("Error removing student: " + e.getMessage());
        }
    }

    @Override
    public void updateStudent(String studentID) {

    }

    @Override
    public ArrayList<Student> displayAllStudents() {
        ArrayList<Student> students = new ArrayList<>();
        String sql = "SELECT studentID, name, age, grade FROM students";
        try (
                Connection connection = this.connect();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql)
        ) {
            while (rs.next()) {
                String studentID = rs.getString("studentID");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                double grade = rs.getDouble("grade");

                Student student = new Student(name, age, grade, studentID);
                students.add(student);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving students: " + e.getMessage());
        }
        return students;
    }

    @Override
    public double calculateAverageGrade() {
        return 0;
    }

    private String generateNextStudentID() {
        String sql = "SELECT studentID FROM students";
        int maxIDNumber = 0;

        try (
                Connection connection = this.connect();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {
                String studentID = rs.getString("studentID");
                if (studentID.startsWith("s")) {
                    String numberPart = studentID.substring(1);
                    try {
                        int idNumber = Integer.parseInt(numberPart);
                        if (idNumber > maxIDNumber) {
                            maxIDNumber = idNumber;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid studentID format: " + studentID);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error generating next studentID: " + e.getMessage());
        }

        return "s" + (maxIDNumber + 1);
    }

}
