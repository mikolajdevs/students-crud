package org.example;

import org.example.gui.GuiService;
import org.example.gui.messages.MessageType;

import java.sql.*;
import java.util.ArrayList;

public class StudentManagerImpl implements StudentManager {

    public StudentManagerImpl() {
        this.initializeDatabase();
    }

    public StudentManagerImpl(boolean withTestStudents) {
        if (withTestStudents) {
            String sqlScript = """
                    INSERT INTO students (studentID, name, age, grade)
                    VALUES
                        ('s1', 'John Doe', 20, 85.5),
                        ('s2', 'Jane Smith', 22, 90.0),
                        ('s3', 'Michael Johnson', 21, 78.5),
                        ('s4', 'Emily Davis', 19, 92.3),
                        ('s5', 'Daniel Brown', 23, 88.0),
                        ('s6', 'Sophia Wilson', 20, 95.7),
                        ('s7', 'James Taylor', 22, 70.8),
                        ('s8', 'Olivia Moore', 21, 85.0),
                        ('s9', 'Liam Harris', 24, 80.5),
                        ('s10', 'Ava Clark', 19, 91.2);
                    """;
            System.out.println(sqlScript);
            this.initializeDatabase(sqlScript);
        }
    }

    private void initializeDatabase() {
        initializeDatabase(null);
    }

    private void initializeDatabase(String toAppend) {
        String sqlScript = """
                CREATE TABLE IF NOT EXISTS students
                (
                studentID TEXT PRIMARY KEY,
                name      TEXT    NOT NULL,
                age       INTEGER NOT NULL,
                grade     REAL    NOT NULL
                );
                """;

        try (
                Connection connection = connect();
                Statement statement = connection.createStatement()
        ) {
            statement.execute(sqlScript);
            if (toAppend != null) {
                statement.execute(toAppend);
            }
            statement.execute(sqlScript);
            System.out.println("Database initialized.");
        } catch (SQLException e) {
            System.out.printf("Error initializing database: %s%n", e.getMessage());
        }
    }

    /**
     * Creates and returns database connection needed for every database request
     */
    private Connection connect() {
        String url = "jdbc:sqlite:db/students.db";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    /**
     * Adds new student with auto generated id
     */
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

    /**
     * Updates student with given id if they already exist using prepared statement
     */
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

    /**
     * Generate next id of type String with pattern s{x} where 'x' is the highest number
     * from existing ids +1.
     */
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
