
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JobListForm extends JFrame {

    private JTable jobTable;
    private DefaultTableModel tableModel;

    public JobListForm() throws ClassNotFoundException {
        setTitle("Job List");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create table model and set column names
        tableModel = new DefaultTableModel(new String[]{"JobID", "JobName", "JobDescription", "JobLocation", "StartDate", "EndDate", "AssignedEmployees"}, 0);
        jobTable = new JTable(tableModel);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(jobTable);
        add(scrollPane, BorderLayout.CENTER);

        // Add buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton assignButton = new JButton("Assign Employees");
        JButton closeButton = new JButton("Close");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(assignButton);
        buttonPanel.add(closeButton);

        add(buttonPanel, BorderLayout.SOUTH);

// Load job data
        loadJobData();

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        // Add action listeners for buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddJobForm(JobListForm.this).setVisible(true);
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    editJob();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(JobListForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    deleteJob();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(JobListForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        assignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    assignEmployees();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(JobListForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    void loadJobData() throws ClassNotFoundException {
        String query = "SELECT j.JobID, j.JobName, j.JobDescription, j.JobLocation, j.StartDate, j.EndDate, "
                + "GROUP_CONCAT(e.EmployeeName SEPARATOR ', ') AS AssignedEmployees "
                + "FROM Jobs j "
                + "LEFT JOIN EmployeeJobAssignments a ON j.JobID = a.JobID "
                + "LEFT JOIN Employees e ON a.EmployeeID = e.EmployeeID "
                + "GROUP BY j.JobID";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("JobID"));
                row.add(rs.getString("JobName"));
                row.add(rs.getString("JobDescription"));
                row.add(rs.getString("JobLocation"));
                row.add(rs.getString("StartDate"));
                row.add(rs.getString("EndDate"));
                row.add(rs.getString("AssignedEmployees"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading job data: " + e.getMessage());
        }
    }

    private void editJob() throws ClassNotFoundException {
        int selectedRow = jobTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a job to edit.");
            return;
        }

        String jobID = (String) tableModel.getValueAt(selectedRow, 0);
        EditJobForm editForm = new EditJobForm(jobID);
        editForm.setVisible(true);

        // Refresh the job list after editing
        editForm.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                tableModel.setRowCount(0); // Clear existing data
                try {
                    loadJobData(); // Reload data from the database
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(JobListForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void deleteJob() throws ClassNotFoundException {
        int selectedRow = jobTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a job to delete.");
            return;
        }

        String jobID = (String) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this job?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String query = "DELETE FROM Jobs WHERE JobID = ?";
            try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, jobID);
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Job deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete job.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting job: " + e.getMessage());
            }
        }
    }

    private void assignEmployees() throws ClassNotFoundException {
        int selectedRow = jobTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a job to assign employees.");
            return;
        }

        String jobID = (String) tableModel.getValueAt(selectedRow, 0);
        AssignEmployeesForm assignForm = new AssignEmployeesForm(jobID);
        assignForm.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new JobListForm().setVisible(true);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(JobListForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
