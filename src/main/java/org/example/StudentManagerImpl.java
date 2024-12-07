package org.example;

import org.example.gui.GuiService;
import org.example.gui.messages.MessageType;

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
            GuiService.handleMessage("Student with ID " + nextStudentID + " added successfully");
        } catch (SQLException e) {
            GuiService.handleMessage("Error adding student: " + e.getMessage(), MessageType.FAILURE);
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
                GuiService.handleMessage("Student " + studentID + " removed successfully.");
            } else {
                GuiService.handleMessage("No student found with ID: " + studentID, MessageType.FAILURE);
            }
        } catch (SQLException e) {
            GuiService.handleMessage("Error removing student: " + e.getMessage(), MessageType.FAILURE);
        }
    }

    @Override
    public void updateStudent(Student student) {
        String sql = "UPDATE students SET name = ?, age = ?, grade = ? WHERE studentID = ?";
        try (
                Connection connection = this.connect();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            final var studentID = student.getStudentID();
            statement.setString(1, student.getName());
            statement.setInt(2, student.getAge());
            statement.setDouble(3, student.getGrade());
            statement.setString(4, studentID);
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                GuiService.handleMessage("Update failed: No student found with ID " + studentID, MessageType.FAILURE);
            } else {
                GuiService.handleMessage("Student with ID " + studentID + " updated successfully.");
            }
        } catch (SQLException e) {
            GuiService.handleMessage("Error updating student: " + e.getMessage(), MessageType.FAILURE);
        }
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
            GuiService.handleMessage("Error retrieving students: " + e.getMessage(), MessageType.FAILURE);
        }
        return students;
    }

    @Override
    public double calculateAverageGrade() {
        String sql = "SELECT AVG(grade) AS averageGrade FROM students";
        double averageGrade = 0.0;
        try (
                Connection connection = this.connect();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql)
        ) {
            if (rs.next()) {
                averageGrade = rs.getDouble("averageGrade");
            }
        } catch (SQLException e) {
            GuiService.handleMessage("Error calculating average grade: " + e.getMessage(), MessageType.FAILURE);
        }
        return averageGrade;
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
