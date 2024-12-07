package org.example.gui;

import org.example.StudentManager;
import org.example.gui.forms.StudentAddFormComponent;
import org.example.gui.forms.StudentUpdateFormComponent;
import org.example.gui.table.StudentsTableComponent;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class StudentsGUI extends JFrame {
    private final StudentManager studentManager;
    private static StudentsGUI instance;

    private StudentsGUI(StudentManager studentManager) {
        this.studentManager = studentManager;
        initUiComponents();
    }

    public static StudentsGUI getInstance(StudentManager studentManager) {
        if (instance == null) {
            instance = new StudentsGUI(studentManager);
        }
        return instance;
    }

    public static StudentsGUI getInstance() {
        return instance;
    }

    private void initUiComponents() {
        setTitle("Student Management System");
        URL iconUrl = getClass().getResource("/images/layers.png");
        BufferedImage iconImage = GuiUtils.getIconImage(iconUrl);
        if (iconImage != null) {
            setIconImage(iconImage);
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        StudentsTableComponent studentsTableComponent = new StudentsTableComponent(studentManager);

        JPanel formPanel = gradientPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel studentAddFormComponent = new StudentAddFormComponent(studentManager);
        TitledBorder border = new TitledBorder("Add new Student");
        Font defaultTitleFont = border.getTitleFont();
        Font titleFont = new Font(defaultTitleFont.getName(), defaultTitleFont.getStyle(), 16);
        border.setTitleFont(titleFont);
        studentAddFormComponent.setBorder(border);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(studentAddFormComponent, gbc);

        JPanel studentUpdateFormComponent = new StudentUpdateFormComponent(studentManager);
        TitledBorder border2 = new TitledBorder("Update existing Student");
        border2.setTitleFont(titleFont);
        studentUpdateFormComponent.setBorder(border2);
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        formPanel.add(studentUpdateFormComponent, gbc);

        gbc.gridy = 2;
        formPanel.add(calculateAverageButton(), gbc);

        gbc.gridy = 3;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(Box.createVerticalStrut(0), gbc);

        add(formPanel, BorderLayout.WEST);
        add(studentsTableComponent, BorderLayout.CENTER);
    }

    JButton calculateAverageButton() {
        final var button = new JButton("Calculate Average");
        button.addActionListener(_ -> {
            final var avg = studentManager.calculateAverageGrade();
            JOptionPane.showMessageDialog(
                    this,
                    "Average grade: " + avg,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
        return button;
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