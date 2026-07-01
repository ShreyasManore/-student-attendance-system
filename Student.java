import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Student.java
 * ------------
 * This class has two responsibilities (kept together for simplicity,
 * as is common in small college mini-projects):
 *
 *   1. Acts as a simple JavaBean / POJO model representing one student
 *      (fields: studentId, name, rollNo, department, year, email).
 *
 *   2. Provides the Swing GUI panel/frame for Student Management
 *      (Add, Update, Delete, View, Search students) and the JDBC
 *      logic to perform these operations on the 'students' table.
 *
 * In a larger professional project these would normally be split into
 * separate Model and View/Controller classes, but for a third-year
 * mini-project this combined structure is realistic and easy to explain.
 */
public class Student extends JFrame implements ActionListener {

    // ---------------------------------------------------------
    // Student object fields (the "model" part of this class)
    // ---------------------------------------------------------
    private int studentId;
    private String name;
    private String rollNo;
    private String department;
    private int year;
    private String email;

    // ---------------------------------------------------------
    // Getters and Setters (standard OOP encapsulation)
    // ---------------------------------------------------------
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // ---------------------------------------------------------
    // GUI Components for Student Management screen
    // ---------------------------------------------------------
    private JTextField nameField, rollNoField, departmentField, yearField, emailField;
    private JTextField searchField;
    private JTextField idField; // shows selected student_id (read-only)
    private JTable studentTable;
    private DefaultTableModel tableModel;

    private JButton addButton, updateButton, deleteButton, clearButton;
    private JButton searchByNameButton, searchByRollButton, showAllButton;
    private JButton backButton;

    /**
     * Default (no-arg) constructor required for the JavaBean style model usage.
     * NOTE: This does NOT open the GUI. Use openManagementWindow() for that.
     */
    public Student() {
    }

    /**
     * Parameterized constructor - convenient way to build a Student object
     * quickly (used internally when reading rows from the database).
     */
    public Student(int studentId, String name, String rollNo, String department, int year, String email) {
        this.studentId = studentId;
        this.name = name;
        this.rollNo = rollNo;
        this.department = department;
        this.year = year;
        this.email = email;
    }

    /**
     * Opens the Student Management GUI window.
     * Called from MainMenu when the admin clicks "Manage Students".
     */
    public void openManagementWindow() {
        setTitle("Student Management");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        // ----- Form Panel (top section) -----
        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setBounds(20, 20, 90, 25);
        add(idLabel);

        idField = new JTextField();
        idField.setBounds(120, 20, 80, 25);
        idField.setEditable(false); // auto-filled when a row is selected
        add(idField);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(220, 20, 60, 25);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(280, 20, 150, 25);
        add(nameField);

        JLabel rollLabel = new JLabel("Roll No:");
        rollLabel.setBounds(450, 20, 60, 25);
        add(rollLabel);

        rollNoField = new JTextField();
        rollNoField.setBounds(520, 20, 120, 25);
        add(rollNoField);

        JLabel deptLabel = new JLabel("Department:");
        deptLabel.setBounds(20, 60, 90, 25);
        add(deptLabel);

        departmentField = new JTextField();
        departmentField.setBounds(120, 60, 150, 25);
        add(departmentField);

        JLabel yearLabel = new JLabel("Year:");
        yearLabel.setBounds(290, 60, 50, 25);
        add(yearLabel);

        yearField = new JTextField();
        yearField.setBounds(330, 60, 50, 25);
        add(yearField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(400, 60, 50, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(450, 60, 190, 25);
        add(emailField);

        // ----- Action Buttons -----
        addButton = new JButton("Add Student");
        addButton.setBounds(20, 100, 120, 28);
        addButton.addActionListener(this);
        add(addButton);

        updateButton = new JButton("Update Student");
        updateButton.setBounds(150, 100, 130, 28);
        updateButton.addActionListener(this);
        add(updateButton);

        deleteButton = new JButton("Delete Student");
        deleteButton.setBounds(290, 100, 130, 28);
        deleteButton.addActionListener(this);
        add(deleteButton);

        clearButton = new JButton("Clear Fields");
        clearButton.setBounds(430, 100, 110, 28);
        clearButton.addActionListener(this);
        add(clearButton);

        backButton = new JButton("Back to Menu");
        backButton.setBounds(700, 100, 120, 28);
        backButton.addActionListener(this);
        add(backButton);

        // ----- Search Panel -----
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setBounds(20, 140, 60, 25);
        add(searchLabel);

        searchField = new JTextField();
        searchField.setBounds(90, 140, 180, 25);
        add(searchField);

        searchByNameButton = new JButton("Search by Name");
        searchByNameButton.setBounds(280, 140, 140, 25);
        searchByNameButton.addActionListener(this);
        add(searchByNameButton);

        searchByRollButton = new JButton("Search by Roll No");
        searchByRollButton.setBounds(430, 140, 150, 25);
        searchByRollButton.addActionListener(this);
        add(searchByRollButton);

        showAllButton = new JButton("Show All");
        showAllButton.setBounds(590, 140, 110, 25);
        showAllButton.addActionListener(this);
        add(showAllButton);

        // ----- Table to display student records -----
        String[] columns = {"ID", "Name", "Roll No", "Department", "Year", "Email"};
        tableModel = new DefaultTableModel(columns, 0) {
            // Make table cells non-editable directly
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable = new JTable(tableModel);
        studentTable.getSelectionModel().addListSelectionListener(evt -> fillFieldsFromSelectedRow());

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBounds(20, 180, 800, 270);
        add(scrollPane);

        // Load all existing students into the table when window opens
        loadAllStudents();

        setVisible(true);
    }

    /**
     * Handles all button click events for the Student Management screen.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == addButton) {
            addStudent();
        } else if (source == updateButton) {
            updateStudent();
        } else if (source == deleteButton) {
            deleteStudent();
        } else if (source == clearButton) {
            clearFields();
        } else if (source == searchByNameButton) {
            searchByName();
        } else if (source == searchByRollButton) {
            searchByRollNo();
        } else if (source == showAllButton) {
            loadAllStudents();
        } else if (source == backButton) {
            this.dispose(); // close this window and return to main menu
        }
    }

    // ===========================================================
    // CRUD Operations (Create, Read, Update, Delete) using JDBC
    // ===========================================================

    /**
     * Adds a new student record to the database using values from the form fields.
     */
    private void addStudent() {
        if (!validateFields()) return;

        String query = "INSERT INTO students (name, roll_no, department, year, email) VALUES (?, ?, ?, ?, ?)";
        Connection con = DatabaseConnection.getConnection();
        if (con == null) return;

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, nameField.getText().trim());
            pst.setString(2, rollNoField.getText().trim());
            pst.setString(3, departmentField.getText().trim());
            pst.setInt(4, Integer.parseInt(yearField.getText().trim()));
            pst.setString(5, emailField.getText().trim());

            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Student added successfully!");
                clearFields();
                loadAllStudents();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding student: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Year must be a number.");
        }
    }

    /**
     * Updates an existing student's details.
     * The student to update is identified by the Student ID field
     * (which gets filled automatically when a table row is selected).
     */
    private void updateStudent() {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a student from the table to update.");
            return;
        }
        if (!validateFields()) return;

        String query = "UPDATE students SET name=?, roll_no=?, department=?, year=?, email=? WHERE student_id=?";
        Connection con = DatabaseConnection.getConnection();
        if (con == null) return;

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, nameField.getText().trim());
            pst.setString(2, rollNoField.getText().trim());
            pst.setString(3, departmentField.getText().trim());
            pst.setInt(4, Integer.parseInt(yearField.getText().trim()));
            pst.setString(5, emailField.getText().trim());
            pst.setInt(6, Integer.parseInt(idField.getText().trim()));

            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Student updated successfully!");
                clearFields();
                loadAllStudents();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating student: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Year must be a number.");
        }
    }

    /**
     * Deletes the currently selected student record from the database.
     * Also deletes their attendance records (handled by ON DELETE CASCADE in SQL).
     */
    private void deleteStudent() {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a student from the table to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this student?\n(All their attendance records will also be deleted)",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        String query = "DELETE FROM students WHERE student_id=?";
        Connection con = DatabaseConnection.getConnection();
        if (con == null) return;

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, Integer.parseInt(idField.getText().trim()));
            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Student deleted successfully!");
                clearFields();
                loadAllStudents();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting student: " + ex.getMessage());
        }
    }

    /**
     * Loads all student records from the database and displays them in the table.
     * This is the "View Student Records" feature.
     */
    private void loadAllStudents() {
        String query = "SELECT * FROM students ORDER BY student_id";
        runQueryAndFillTable(query, null);
    }

    /**
     * Searches for students whose name contains the text typed in the search field.
     */
    private void searchByName() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name to search.");
            return;
        }
        String query = "SELECT * FROM students WHERE name LIKE ?";
        runQueryAndFillTable(query, "%" + keyword + "%");
    }

    /**
     * Searches for a student by exact (or partial) roll number.
     */
    private void searchByRollNo() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a roll number to search.");
            return;
        }
        String query = "SELECT * FROM students WHERE roll_no LIKE ?";
        runQueryAndFillTable(query, "%" + keyword + "%");
    }

    /**
     * Helper method: runs a SELECT query (with an optional single parameter)
     * and fills the JTable with the results. Used by loadAllStudents(),
     * searchByName(), and searchByRollNo() to avoid duplicate code.
     */
    private void runQueryAndFillTable(String query, String parameter) {
        Connection con = DatabaseConnection.getConnection();
        if (con == null) return;

        tableModel.setRowCount(0); // clear existing rows

        try (PreparedStatement pst = con.prepareStatement(query)) {
            if (parameter != null) {
                pst.setString(1, parameter);
            }
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("roll_no"),
                        rs.getString("department"),
                        rs.getInt("year"),
                        rs.getString("email")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching students: " + ex.getMessage());
        }
    }

    /**
     * When the user clicks on a row in the table, this method copies
     * the selected student's data into the form fields above
     * (so they can easily edit or delete that record).
     */
    private void fillFieldsFromSelectedRow() {
        int row = studentTable.getSelectedRow();
        if (row == -1) return;

        idField.setText(tableModel.getValueAt(row, 0).toString());
        nameField.setText(tableModel.getValueAt(row, 1).toString());
        rollNoField.setText(tableModel.getValueAt(row, 2).toString());
        departmentField.setText(tableModel.getValueAt(row, 3).toString());
        yearField.setText(tableModel.getValueAt(row, 4).toString());
        emailField.setText(tableModel.getValueAt(row, 5).toString());
    }

    /**
     * Clears all input fields and the table selection.
     */
    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        rollNoField.setText("");
        departmentField.setText("");
        yearField.setText("");
        emailField.setText("");
        studentTable.clearSelection();
    }

    /**
     * Basic validation to make sure required fields are not empty
     * before inserting/updating a record.
     */
    private boolean validateFields() {
        if (nameField.getText().trim().isEmpty() ||
            rollNoField.getText().trim().isEmpty() ||
            departmentField.getText().trim().isEmpty() ||
            yearField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields (Name, Roll No, Department, Year).");
            return false;
        }
        return true;
    }
}
