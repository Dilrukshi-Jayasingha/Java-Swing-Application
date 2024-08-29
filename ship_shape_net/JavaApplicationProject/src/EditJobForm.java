import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EditJobForm extends JFrame {
    private JTextField jobNameField;
    private JTextField jobDescriptionField;
    private JTextField jobLocationField;
    private JTextField startDateField;
    private JTextField endDateField;
    private String jobID;
    private boolean saved = false;

    public EditJobForm(String jobID) throws ClassNotFoundException {
        this.jobID = jobID;
        setTitle("Edit Job");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create form fields
        jobNameField = new JTextField(20);
        jobDescriptionField = new JTextField(20);
        jobLocationField = new JTextField(20);
        startDateField = new JTextField(20);
        endDateField = new JTextField(20);

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2));
        formPanel.add(new JLabel("Job Name:"));
        formPanel.add(jobNameField);
        formPanel.add(new JLabel("Job Description:"));
        formPanel.add(jobDescriptionField);
        formPanel.add(new JLabel("Job Location:"));
        formPanel.add(jobLocationField);
        formPanel.add(new JLabel("Start Date (YYYY-MM-DD):"));
        formPanel.add(startDateField);
        formPanel.add(new JLabel("End Date (YYYY-MM-DD):"));
        formPanel.add(endDateField);

        // Add Save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    saveJob();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(EditJobForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Add components to frame
        add(formPanel, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);

        // Load job details
        loadJobDetails();
    }

    private void loadJobDetails() throws ClassNotFoundException {
        String query = "SELECT * FROM Jobs WHERE JobID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, jobID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                jobNameField.setText(rs.getString("JobName"));
                jobDescriptionField.setText(rs.getString("JobDescription"));
                jobLocationField.setText(rs.getString("JobLocation"));
                startDateField.setText(rs.getString("StartDate"));
                endDateField.setText(rs.getString("EndDate"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading job details: " + e.getMessage());
        }
    }

    private void saveJob() throws ClassNotFoundException {
        String jobName = jobNameField.getText();
        String jobDescription = jobDescriptionField.getText();
        String jobLocation = jobLocationField.getText();
        String startDate = startDateField.getText();
        String endDate = endDateField.getText();

        String query = "UPDATE Jobs SET JobName = ?, JobDescription = ?, JobLocation = ?, StartDate = ?, EndDate = ? WHERE JobID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, jobName);
            pstmt.setString(2, jobDescription);
            pstmt.setString(3, jobLocation);
            pstmt.setString(4, startDate);
            pstmt.setString(5, endDate);
            pstmt.setString(6, jobID);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                saved = true;
                JOptionPane.showMessageDialog(this, "Job updated successfully!");
                dispose(); // Close the form
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update job.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating job: " + e.getMessage());
        }
    }

    public boolean isSaved() {
        return saved;
    }
}
