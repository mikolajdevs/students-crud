package org.example.gui;

import org.example.Student;
import org.example.StudentManager;
import org.example.gui.messages.ConsoleComponent;
import org.example.gui.forms.StudentAddFormComponent;
import org.example.gui.forms.StudentUpdateFormComponent;
import org.example.gui.messages.MessageConsole;
import org.example.gui.table.StudentsTableComponent;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Objects;
import java.util.List;
import java.util.stream.IntStream;

import static org.example.gui.messages.ConsoleComponent.DEFAULT_CONSOLE_HEIGHT;
import static org.example.gui.messages.MessageConsole.nowSuffix;

public class StudentsGUI extends JFrame {
    private static StudentsGUI instance;
    private final StudentManager studentManager;
    private final JPanel mainPanel = new JPanel();
    private boolean showConsole = true;
    private ConsoleComponent console;

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

    /**
     * Inits UI components: table, forms, console
     */
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

        final StudentsTableComponent studentsTableComponent = new StudentsTableComponent(studentManager);

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

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(studentsTableComponent, BorderLayout.CENTER);

        final boolean messageConsoleExists = GuiService.getMessageHandlers()
                .stream().anyMatch(handler -> handler instanceof MessageConsole);
        if (messageConsoleExists) {
            initConsole();
        }

        add(formPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Creates console components for displaying system messages
     */
    private void initConsole() {
        JPanel consoleWrapper = new JPanel();
        consoleWrapper.setLayout(new BorderLayout());

        console = MessageConsole.getConsole();
        Dimension consoleDimension = console.getPreferredSize();
        console.setPreferredSize(new Dimension(consoleDimension.width, DEFAULT_CONSOLE_HEIGHT));

        JButton displayAllStudentsButton = displayAllStudentsButton();

        JButton toggleButton;
        URL terminalIconUrl = getClass().getResource("/images/terminal.png");
        if (terminalIconUrl != null) {
            ImageIcon terminalIcon = new ImageIcon(Objects.requireNonNull(GuiUtils.getIconImage(terminalIconUrl)));
            Image scaledImage = terminalIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

            toggleButton = new JButton(new ImageIcon(scaledImage));
            toggleButton.setBorderPainted(false);
            toggleButton.setContentAreaFilled(false);
        } else {
            toggleButton = new JButton("Toggle console");
        }
        toggleButton.addActionListener(_ -> {
            showConsole = !showConsole;
            int consoleHeight = showConsole ? DEFAULT_CONSOLE_HEIGHT : 0;
            console.setPreferredSize(new Dimension(consoleDimension.width, consoleHeight));
            displayAllStudentsButton.setEnabled(showConsole);
            consoleWrapper.revalidate();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.add(displayAllStudentsButton);
        buttonPanel.add(toggleButton);

        consoleWrapper.add(buttonPanel, BorderLayout.NORTH);
        consoleWrapper.add(console, BorderLayout.CENTER);
        mainPanel.add(consoleWrapper, BorderLayout.SOUTH);
    }

    private JButton displayAllStudentsButton() {
        JButton displayAllStudentsButton = new JButton("Display All Students");
        displayAllStudentsButton.addActionListener(_ -> {
                    List<Student> students = this.studentManager.displayAllStudents();
                    StringBuilder sb = new StringBuilder();
                    sb.append("Students (").append(students.size()).append("): [\n");
                    StringBuilder offset = new StringBuilder();
                    IntStream.range(0, 5).forEach(_ -> offset.append(' '));
                    students.forEach(student -> sb.append(offset).append(student.toString()).append(",\n"));
                    sb.append("]");
                    console.log(nowSuffix() + sb);
                }
        );
        return displayAllStudentsButton;
    }

    JButton calculateAverageButton() {
        final var button = new JButton("Calculate Average");
        button.addActionListener(_ -> {
            final var avg = studentManager.calculateAverageGrade();
            GuiService.handleMessage("Average grade: %s".formatted(avg));
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