package org.example.gui;

import org.example.StudentManager;

import javax.swing.*;
import java.awt.*;

public class StudentsGUI extends JFrame {
    private final StudentManager studentManager;

    public StudentsGUI(StudentManager studentManager) {
        this.studentManager = studentManager;
        initUiComponents();
    }

    private void initUiComponents() {
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        StudentsTableComponent studentsTableComponent = new StudentsTableComponent(studentManager);

        JPanel formPanel = gradientPanel();
        formPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JPanel studentFormComponent = new StudentFormComponent(studentManager, studentsTableComponent);
        formPanel.add(studentFormComponent);


        add(formPanel, BorderLayout.WEST);
        add(studentsTableComponent, BorderLayout.CENTER);
    }

    JPanel gradientPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();

                Color darkBlue = Color.decode("#090979");
                Color lightBlue = Color.decode("#00d4ff");

                GradientPaint gradientPaint = new GradientPaint(0, 0, darkBlue, 0, height, lightBlue);

                g2d.setPaint(gradientPaint);
                g2d.fillRect(0, 0, width, height);
            }
        };
    }
}