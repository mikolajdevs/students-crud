-- Drop the table if it exists
DROP TABLE IF EXISTS students;

-- Create the students table
CREATE TABLE IF NOT EXISTS students
(
    studentID TEXT PRIMARY KEY,
    name      TEXT    NOT NULL,
    age       INTEGER NOT NULL,
    grade     REAL    NOT NULL
);

-- Insert sample data into students table
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