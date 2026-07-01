import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * MainMenu.java
 * -------------
 * This is the main dashboard screen shown after a successful admin login.
 * It provides navigation buttons to:
 *   - Manage Students (Add/Update/Delete/View/Search)
 *   - Manage Attendance (Mark attendance + view reports)
 *   - Logout (returns to Login screen)
 *
 * This class acts as the central hub of the application, connecting
 * the Login screen to the Student and Attendance modules.
 */
public class MainMenu extends JFrame implements ActionListener {

    // ---------------------------------------------------------
    // GUI Components
    // ---------------------------------------------------------
    private JButton manageStudentsButton;
    private JButton manageAttendanceButton;
    private JButton logoutButton;
    private JButton exitButton;

    /**
     * Constructor: builds and displays the main menu window.
     */
    public MainMenu() {
        setTitle("Student Attendance Management System - Main Menu");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        // Heading
        JLabel heading = new JLabel("MAIN MENU");
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setBounds(170, 30, 200, 30);
        add(heading);

        JLabel subHeading = new JLabel("Student Attendance Management System");
        subHeading.setFont(new Font("Arial", Font.PLAIN, 12));
        subHeading.setBounds(120, 65, 300, 20);
        add(subHeading);

        // Manage Students button
        manageStudentsButton = new JButton("Manage Students");
        manageStudentsButton.setBounds(130, 120, 240, 40);
        manageStudentsButton.addActionListener(this);
        add(manageStudentsButton);

        // Manage Attendance button
        manageAttendanceButton = new JButton("Manage Attendance");
        manageAttendanceButton.setBounds(130, 175, 240, 40);
        manageAttendanceButton.addActionListener(this);
        add(manageAttendanceButton);

        // Logout button
        logoutButton = new JButton("Logout");
        logoutButton.setBounds(130, 230, 240, 40);
        logoutButton.addActionListener(this);
        add(logoutButton);

        // Exit button
        exitButton = new JButton("Exit Application");
        exitButton.setBounds(130, 285, 240, 40);
        exitButton.addActionListener(this);
        add(exitButton);

        setVisible(true);
    }

    /**
     * Handles button click events from the main menu.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == manageStudentsButton) {
            // Open the Student Management window
            Student studentWindow = new Student();
            studentWindow.openManagementWindow();

        } else if (source == manageAttendanceButton) {
            // Open the Attendance Management window
            Attendance attendanceWindow = new Attendance();
            attendanceWindow.openAttendanceWindow();

        } else if (source == logoutButton) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?", "Logout",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();      // close main menu
                new Login();         // go back to login screen
            }

        } else if (source == exitButton) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to exit?", "Exit Application",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                DatabaseConnection.closeConnection();
                System.exit(0);
            }
        }
    }
}
