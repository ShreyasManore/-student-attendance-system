import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Login.java
 * ----------
 * This class creates the Admin Login screen using Java Swing.
 * The admin enters a username and password which is verified
 * against the 'admin' table in the database.
 *
 * On successful login, the MainMenu screen is opened.
 */
public class Login extends JFrame implements ActionListener {

    // ---------------------------------------------------------
    // GUI Components
    // ---------------------------------------------------------
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton exitButton;
    private JLabel messageLabel;

    /**
     * Constructor: sets up the login window layout.
     */
    public Login() {
        // Frame settings
        setTitle("Student Attendance Management System - Admin Login");
        setSize(400, 280);
        setLocationRelativeTo(null); // center the window on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // using absolute positioning - simple for a student project
        setResizable(false);

        // Heading label
        JLabel heading = new JLabel("ADMIN LOGIN");
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        heading.setBounds(120, 20, 200, 30);
        add(heading);

        // Username label and text field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 80, 100, 25);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(160, 80, 180, 25);
        add(usernameField);

        // Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 120, 100, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(160, 120, 180, 25);
        add(passwordField);

        // Login button
        loginButton = new JButton("Login");
        loginButton.setBounds(80, 170, 100, 30);
        loginButton.addActionListener(this);
        add(loginButton);

        // Exit button
        exitButton = new JButton("Exit");
        exitButton.setBounds(210, 170, 100, 30);
        exitButton.addActionListener(this);
        add(exitButton);

        // Message label to show errors (e.g. "Invalid credentials")
        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        messageLabel.setBounds(50, 210, 300, 25);
        add(messageLabel);

        setVisible(true);
    }

    /**
     * Handles button click events (Login / Exit).
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            attemptLogin();
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }

    /**
     * Verifies the entered username/password against the database.
     * Also supports a hardcoded fallback admin login in case the
     * database is not set up yet (useful for quick testing/demo).
     */
    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter both username and password.");
            return;
        }

        // ---- Option 1: Hardcoded fallback credentials ----
        // (kept simple for demo purposes, in case DB is unavailable)
        if (username.equals("admin") && password.equals("admin123")) {
            // Try DB login too, but allow hardcoded login to succeed regardless
            loginSuccess();
            return;
        }

        // ---- Option 2: Database-based login ----
        String query = "SELECT * FROM admin WHERE username = ? AND password = ?";
        Connection con = DatabaseConnection.getConnection();

        if (con == null) {
            messageLabel.setText("Database not connected. Try admin/admin123.");
            return;
        }

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                loginSuccess();
            } else {
                messageLabel.setText("Invalid username or password.");
            }
        } catch (Exception ex) {
            messageLabel.setText("Error while checking login: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Called when login is successful.
     * Closes the login window and opens the Main Menu.
     */
    private void loginSuccess() {
        JOptionPane.showMessageDialog(this, "Login Successful! Welcome Admin.");
        this.dispose(); // close login window
        new MainMenu(); // open main menu screen
    }

    /**
     * Main method - entry point of the application.
     */
    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread (standard Swing practice)
        SwingUtilities.invokeLater(Login::new);
    }
}
