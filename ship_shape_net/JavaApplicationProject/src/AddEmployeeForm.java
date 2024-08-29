import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddEmployeeForm extends JFrame {
    private JTextField employeeNameField;
    private JTextField jobRoleField;
    private JTextField scheduleField;
    private JTextField locationField;
    private JTextField contactNumberField;
    private JTextField emailField;
    private EmployeeListForm parent;

    public AddEmployeeForm(EmployeeListForm parent) {
        this.parent = parent;
        setTitle("Add Employee");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create form fields
        employeeNameField = new JTextField(20);
        jobRoleField = new JTextField(20);
        scheduleField = new JTextField(20);
        locationField = new JTextField(20);
        contactNumberField = new JTextField(20);
        emailField = new JTextField(20);

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(7, 2));
        formPanel.add(new JLabel("Employee Name:"));
        formPanel.add(employeeNameField);
        formPanel.add(new JLabel("Job Role:"));
        formPanel.add(jobRoleField);
        formPanel.add(new JLabel("Schedule:"));
        formPanel.add(scheduleField);
        formPanel.add(new JLabel("Location:"));
        formPanel.add(locationField);
        formPanel.add(new JLabel("Contact Number:"));
        formPanel.add(contactNumberField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        // Add Save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    saveEmployee();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(AddEmployeeForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Add components to frame
        add(formPanel, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);
    }

    private void saveEmployee() throws ClassNotFoundException {
        String employeeName = employeeNameField.getText();
        String jobRole = jobRoleField.getText();
        String schedule = scheduleField.getText();
        String location = locationField.getText();
        String contactNumber = contactNumberField.getText();
        String email = emailField.getText();

        String query = "INSERT INTO Employees (EmployeeName, JobRole, Schedule, Location, ContactNumber, Email) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, employeeName);
            pstmt.setString(2, jobRole);
            pstmt.setString(3, schedule);
            pstmt.setString(4, location);
            pstmt.setString(5, contactNumber);
            pstmt.setString(6, email);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Employee added successfully!");
                parent.loadEmployeeData(); // Refresh employee list in parent form
                dispose(); // Close the form
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add employee.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding employee: " + e.getMessage());
        }
    }
}
