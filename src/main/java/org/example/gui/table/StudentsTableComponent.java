package org.example.gui.table;

import org.example.Student;
import org.example.StudentManager;
import org.example.gui.GuiService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

public class StudentsTableComponent extends JPanel implements Refreshable {
    private final StudentManager studentManager;
    private final JTable studentsTable;
    private final DefaultTableModel tableModel;
    public final static String[] columnNames = {"Student ID", "Name", "Age", "Grade"};

    private int hoveredRow = -1;

    public StudentsTableComponent(StudentManager studentManager) {
        this.studentManager = studentManager;
        GuiService.addRefreshListener(this);

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentsTable = new JTable(tableModel) {
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                return new HoverTableCellRenderer();
            }
        };

        studentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentsTable.setRowSelectionAllowed(true);
        studentsTable.setColumnSelectionAllowed(false);
        studentsTable.setFillsViewportHeight(true);

        refresh();
        JScrollPane scrollPane = new JScrollPane(studentsTable);

        studentsTable.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = studentsTable.rowAtPoint(e.getPoint());
                if (row != hoveredRow) {
                    hoveredRow = row;
                    studentsTable.repaint();
                }
            }
        });

        JButton clearSelectionButton = new JButton("Clear selection");
        clearSelectionButton.setEnabled(false);
        clearSelectionButton.addActionListener(_ -> studentsTable.clearSelection());

        JButton deleteButton = new JButton("Delete Selected Student");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(_ -> {
            int selectedRow = studentsTable.getSelectedRow();
            if (selectedRow != -1) {
                int columnIndex = studentsTable.getColumnModel().getColumnIndex(columnNames[0]);
                String studentID = (String) studentsTable.getValueAt(selectedRow, columnIndex);
                studentManager.removeStudent(studentID);
                refresh();
                studentsTable.clearSelection();
            }
        });

        studentsTable.getSelectionModel().addListSelectionListener(event -> {
            int selectedRow = studentsTable.getSelectedRow();
            Color darkRed = Color.decode("#a10000");
            if (selectedRow != -1) {
                deleteButton.setEnabled(true);
                clearSelectionButton.setEnabled(true);
                deleteButton.setBackground(darkRed);
                deleteButton.setForeground(Color.WHITE);
            } else {
                deleteButton.setEnabled(false);
                clearSelectionButton.setEnabled(false);
                deleteButton.setBackground(UIManager.getColor("Button.background"));
                deleteButton.setForeground(UIManager.getColor("Button.foreground"));
            }

            if (!event.getValueIsAdjusting()) {
                updateSelectionContext();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearSelectionButton);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Students Table",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16),
                Color.DARK_GRAY
        ));
    }

    private void updateSelectionContext() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow != -1) {
            TableColumnModel columnModel = studentsTable.getColumnModel();

            String studentID = (String) studentsTable.getValueAt(selectedRow, columnModel.getColumnIndex(columnNames[0]));
            String name = (String) studentsTable.getValueAt(selectedRow, columnModel.getColumnIndex(columnNames[1]));
            int age = (int) studentsTable.getValueAt(selectedRow, columnModel.getColumnIndex(columnNames[2]));
            double grade = (double) studentsTable.getValueAt(selectedRow, columnModel.getColumnIndex(columnNames[3]));
            GuiService.onSelect(new Student(name, age, grade, studentID));
        }
    }


    /**
     * Refreshes the table with the latest student data from the database.
     */
    @Override
    public void refresh() {
        tableModel.setRowCount(0);
        ArrayList<Student> students = studentManager.displayAllStudents();
        for (Student student : students) {
            Object[] rowData = {
                    student.getStudentID(),
                    student.getName(),
                    student.getAge(),
                    student.getGrade()
            };
            tableModel.addRow(rowData);
        }
    }

    private class HoverTableCellRenderer extends DefaultTableCellRenderer {
        Color colorHover = Color.decode("#edf5fc");

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (row == hoveredRow && !isSelected) {
                c.setBackground(colorHover);
            } else if (!isSelected) {
                c.setBackground(Color.WHITE);
            }
            return c;
        }
    }
}
