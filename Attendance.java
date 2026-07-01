import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Attendance.java
 * ---------------
 * This class handles everything related to attendance:
 *   - Marking a student Present/Absent for a given date & subject
 *   - Viewing a student's attendance history
 *   - Calculating attendance percentage
 *   - Generating a simple monthly attendance summary
 *
 * Like Student.java, this class combines a small "model" part
 * (attendanceId, studentId, date, status, subject) with the Swing
 * GUI and JDBC logic, which keeps the project simple enough for
 * a third-year mini-project while still being realistic.
 */
public class Attendance extends JFrame implements ActionListener {

    // ---------------------------------------------------------
    // Attendance record fields (the "model" part)
    // ---------------------------------------------------------
    private int attendanceId;
    private int studentId;
    private String date;   // stored as String in format yyyy-MM-dd
    private String status; // "Present" or "Absent"
    private String subject;

    // Getters and setters
    public int getAttendanceId() { return attendanceId; }
    public void setAttendanceId(int attendanceId) { this.attendanceId = attendanceId; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    // ---------------------------------------------------------
    // GUI Components - Mark Attendance section
    // ---------------------------------------------------------
    private JComboBox<String> studentComboBox;   // shows "rollNo - name"
    private JTextField dateField;                // format: yyyy-MM-dd
    private JTextField subjectField;
    private JComboBox<String> statusComboBox;     // Present / Absent
    private JButton markButton, todayButton;

    // ---------------------------------------------------------
    // GUI Components - Report section
    // ---------------------------------------------------------
    private JComboBox<String> reportStudentComboBox;
    private JTextField monthField; // format: yyyy-MM (e.g. 2026-06)
    private JButton viewHistoryButton, calcPercentageButton, monthlySummaryButton;
    private JLabel percentageLabel;

    private JTable reportTable;
    private DefaultTableModel reportTableModel;

    private JButton backButton;

    /**
     * Default constructor required for model-style usage.
     */
    public Attendance() {
    }

    /**
     * Parameterized constructor - quick way to build an Attendance object.
     */
    public Attendance(int attendanceId, int studentId, String date, String status, String subject) {
        this.attendanceId = attendanceId;
        this.studentId = studentId;
        this.date = date;
        this.status = status;
        this.subject = subject;
    }

    /**
     * Opens the Attendance Management GUI window.
     * Called from MainMenu when the admin clicks "Manage Attendance".
     */
    public void openAttendanceWindow() {
        setTitle("Attendance Management");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        // =======================================================
        // SECTION 1: Mark Attendance
        // =======================================================
        JLabel markHeading = new JLabel("Mark Attendance");
        markHeading.setFont(new Font("Arial", Font.BOLD, 16));
        markHeading.setBounds(20, 10, 250, 25);
        add(markHeading);

        JLabel studentLabel = new JLabel("Student:");
        studentLabel.setBounds(20, 45, 80, 25);
        add(studentLabel);

        studentComboBox = new JComboBox<>();
        studentComboBox.setBounds(100, 45, 220, 25);
        add(studentComboBox);

        JLabel dateLabel = new JLabel("Date (yyyy-MM-dd):");
        dateLabel.setBounds(340, 45, 140, 25);
        add(dateLabel);

        dateField = new JTextField();
        dateField.setBounds(480, 45, 100, 25);
        add(dateField);

        todayButton = new JButton("Today");
        todayButton.setBounds(590, 45, 70, 25);
        todayButton.addActionListener(this);
        add(todayButton);

        JLabel subjectLabel = new JLabel("Subject:");
        subjectLabel.setBounds(20, 80, 80, 25);
        add(subjectLabel);

        subjectField = new JTextField();
        subjectField.setBounds(100, 80, 220, 25);
        add(subjectField);

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setBounds(340, 80, 80, 25);
        add(statusLabel);

        statusComboBox = new JComboBox<>(new String[]{"Present", "Absent"});
        statusComboBox.setBounds(420, 80, 120, 25);
        add(statusComboBox);

        markButton = new JButton("Mark Attendance");
        markButton.setBounds(590, 80, 150, 25);
        markButton.addActionListener(this);
        add(markButton);

        // Separator line (visual only)
        JSeparator sep1 = new JSeparator();
        sep1.setBounds(20, 120, 850, 2);
        add(sep1);

        // =======================================================
        // SECTION 2: Reports
        // =======================================================
        JLabel reportHeading = new JLabel("Attendance Reports");
        reportHeading.setFont(new Font("Arial", Font.BOLD, 16));
        reportHeading.setBounds(20, 130, 250, 25);
        add(reportHeading);

        JLabel reportStudentLabel = new JLabel("Student:");
        reportStudentLabel.setBounds(20, 165, 80, 25);
        add(reportStudentLabel);

        reportStudentComboBox = new JComboBox<>();
        reportStudentComboBox.setBounds(100, 165, 220, 25);
        add(reportStudentComboBox);

        viewHistoryButton = new JButton("View History");
        viewHistoryButton.setBounds(340, 165, 130, 25);
        viewHistoryButton.addActionListener(this);
        add(viewHistoryButton);

        calcPercentageButton = new JButton("Calculate %");
        calcPercentageButton.setBounds(480, 165, 130, 25);
        calcPercentageButton.addActionListener(this);
        add(calcPercentageButton);

        JLabel monthLabel = new JLabel("Month (yyyy-MM):");
        monthLabel.setBounds(20, 200, 130, 25);
        add(monthLabel);

        monthField = new JTextField();
        monthField.setBounds(150, 200, 100, 25);
        add(monthField);

        monthlySummaryButton = new JButton("Monthly Summary (All Students)");
        monthlySummaryButton.setBounds(260, 200, 250, 25);
        monthlySummaryButton.addActionListener(this);
        add(monthlySummaryButton);

        percentageLabel = new JLabel(" ");
        percentageLabel.setFont(new Font("Arial", Font.BOLD, 14));
        percentageLabel.setForeground(new Color(0, 102, 0));
        percentageLabel.setBounds(520, 200, 300, 25);
        add(percentageLabel);

        backButton = new JButton("Back to Menu");
        backButton.setBounds(750, 165, 120, 25);
        backButton.addActionListener(this);
        add(backButton);

        // ----- Table to display attendance / report results -----
        String[] columns = {"Att. ID", "Student", "Date", "Subject", "Status"};
        reportTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reportTable = new JTable(reportTableModel);
        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.setBounds(20, 240, 850, 300);
        add(scrollPane);

        // Load student list into both combo boxes
        loadStudentsIntoComboBox(studentComboBox);
        loadStudentsIntoComboBox(reportStudentComboBox);

        setVisible(true);
    }

    /**
     * Handles all button click events for the Attendance screen.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == markButton) {
            markAttendance();
        } else if (source == todayButton) {
            // Fill the date field with today's date automatically
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dateField.setText(sdf.format(new Date()));
        } else if (source == viewHistoryButton) {
            viewAttendanceHistory();
        } else if (source == calcPercentageButton) {
            calculateAttendancePercentage();
        } else if (source == monthlySummaryButton) {
            generateMonthlySummary();
        } else if (source == backButton) {
            this.dispose();
        }
    }

    // ===========================================================
    // Helper: Load "rollNo - name" entries into a JComboBox
    // ===========================================================
    private void loadStudentsIntoComboBox(JComboBox<String> comboBox) {
        comboBox.removeAllItems();
        String query = "SELECT student_id, name, roll_no FROM students ORDER BY name";
        Connection con = DatabaseConnection.getConnection();
        if (con == null) return;

        try (PreparedStatement pst = con.prepareStatement(query)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                // We encode the student_id at the start so we can extract it later
                String entry = rs.getInt("student_id") + " | " + rs.getString("roll_no") + " - " + rs.getString("name");
                comboBox.addItem(entry);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading students: " + ex.getMessage());
        }
    }

    /**
     * Extracts the student_id from a combo box entry like "3 | CSE003 - Amit Kumar".
     */
    private int extractStudentId(String comboBoxEntry) {
        if (comboBoxEntry == null) return -1;
        String idPart = comboBoxEntry.split("\\|")[0].trim();
        return Integer.parseInt(idPart);
    }

    // ===========================================================
    // Mark Attendance
    // ===========================================================
    private void markAttendance() {
        String selectedStudent = (String) studentComboBox.getSelectedItem();
        String dateText = dateField.getText().trim();
        String subjectText = subjectField.getText().trim();
        String statusText = (String) statusComboBox.getSelectedItem();

        if (selectedStudent == null) {
            JOptionPane.showMessageDialog(this, "Please add a student first (no students found).");
            return;
        }
        if (dateText.isEmpty() || subjectText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both date and subject.");
            return;
        }
        if (!isValidDate(dateText)) {
            JOptionPane.showMessageDialog(this, "Date must be in yyyy-MM-dd format (e.g. 2026-06-20).");
            return;
        }

        int studentId = extractStudentId(selectedStudent);

        String query = "INSERT INTO attendance (student_id, date, status, subject) VALUES (?, ?, ?, ?)";
        Connection con = DatabaseConnection.getConnection();
        if (con == null) return;

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, studentId);
            pst.setString(2, dateText);
            pst.setString(3, statusText);
            pst.setString(4, subjectText);

            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Attendance marked: " + statusText + " for " + dateText);
                subjectField.setText("");
                dateField.setText("");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error marking attendance: " + ex.getMessage());
        }
    }

    /**
     * Basic date format validation using a simple regex check (yyyy-MM-dd).
     */
    private boolean isValidDate(String date) {
        return date.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    // ===========================================================
    // View Attendance History (for one student)
    // ===========================================================
    private void viewAttendanceHistory() {
        String selectedStudent = (String) reportStudentComboBox.getSelectedItem();
        if (selectedStudent == null) {
            JOptionPane.showMessageDialog(this, "No student selected.");
            return;
        }
        int studentId = extractStudentId(selectedStudent);

        String query = "SELECT a.attendance_id, s.name, a.date, a.subject, a.status " +
                        "FROM attendance a JOIN students s ON a.student_id = s.student_id " +
                        "WHERE a.student_id = ? ORDER BY a.date DESC";

        Connection con = DatabaseConnection.getConnection();
        if (con == null) return;

        reportTableModel.setRowCount(0);
        percentageLabel.setText(" ");

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, studentId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("attendance_id"),
                        rs.getString("name"),
                        rs.getString("date"),
                        rs.getString("subject"),
                        rs.getString("status")
                };
                reportTableModel.addRow(row);
            }

            if (reportTableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No attendance records found for this student.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching attendance history: " + ex.getMessage());
        }
    }

    // ===========================================================
    // Calculate Attendance Percentage (for one student)
    // ===========================================================
    private void calculateAttendancePercentage() {
        String selectedStudent = (String) reportStudentComboBox.getSelectedItem();
        if (selectedStudent == null) {
            JOptionPane.showMessageDialog(this, "No student selected.");
            return;
        }
        int studentId = extractStudentId(selectedStudent);

        // Count total classes and classes attended (Present)
        String totalQuery = "SELECT COUNT(*) AS total FROM attendance WHERE student_id = ?";
        String presentQuery = "SELECT COUNT(*) AS present FROM attendance WHERE student_id = ? AND status = 'Present'";

        Connection con = DatabaseConnection.getConnection();
        if (con == null) return;

        try {
            int total = 0, present = 0;

            try (PreparedStatement pst = con.prepareStatement(totalQuery)) {
                pst.setInt(1, studentId);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) total = rs.getInt("total");
            }

            try (PreparedStatement pst = con.prepareStatement(presentQuery)) {
                pst.setInt(1, studentId);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) present = rs.getInt("present");
            }

            if (total == 0) {
                percentageLabel.setText("No attendance records found.");
                return;
            }

            // Calculate percentage (cast to double to avoid integer division)
            double percentage = ((double) present / total) * 100;

            // Extract name for display (entry format: "id | rollNo - name")
            String studentName = selectedStudent.substring(selectedStudent.indexOf('-') + 1).trim();

            percentageLabel.setText(String.format("%s: %d/%d classes = %.2f%%",
                    studentName, present, total, percentage));

            // Simple warning if attendance is below 75% (common college rule)
            if (percentage < 75.0) {
                JOptionPane.showMessageDialog(this,
                        String.format("Warning: Attendance is %.2f%%, which is below the 75%% requirement.", percentage),
                        "Low Attendance Warning", JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error calculating percentage: " + ex.getMessage());
        }
    }

    // ===========================================================
    // Generate Monthly Attendance Summary (for all students)
    // ===========================================================
    private void generateMonthlySummary() {
        String month = monthField.getText().trim(); // expected format: yyyy-MM

        if (!month.matches("\\d{4}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Please enter month in yyyy-MM format (e.g. 2026-06).");
            return;
        }

        // Query: for each student, count total classes and present classes
        // within the given month, then compute percentage.
        String query =
                "SELECT s.student_id, s.name, s.roll_no, " +
                "       COUNT(a.attendance_id) AS total_classes, " +
                "       SUM(CASE WHEN a.status = 'Present' THEN 1 ELSE 0 END) AS present_classes " +
                "FROM students s " +
                "LEFT JOIN attendance a ON s.student_id = a.student_id " +
                "       AND DATE_FORMAT(a.date, '%Y-%m') = ? " +
                "GROUP BY s.student_id, s.name, s.roll_no " +
                "ORDER BY s.name";

        Connection con = DatabaseConnection.getConnection();
        if (con == null) return;

        reportTableModel.setRowCount(0);
        // Temporarily change table columns for this summary view
        reportTableModel.setColumnIdentifiers(new String[]{"Student ID", "Name", "Roll No", "Total Classes", "Present", "Percentage"});
        percentageLabel.setText("Monthly Summary for: " + month);

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, month);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int total = rs.getInt("total_classes");
                int present = rs.getInt("present_classes");
                double percent = (total == 0) ? 0.0 : ((double) present / total) * 100;

                Object[] row = {
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("roll_no"),
                        total,
                        present,
                        String.format("%.2f%%", percent)
                };
                reportTableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error generating monthly summary: " + ex.getMessage());
        }
    }
}
