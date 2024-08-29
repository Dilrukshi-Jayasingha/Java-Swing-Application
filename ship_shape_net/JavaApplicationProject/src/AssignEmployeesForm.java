import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AssignEmployeesForm extends JFrame {
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private String jobID;

    public AssignEmployeesForm(String jobID) throws ClassNotFoundException {
        this.jobID = jobID;
        setTitle("Assign Employees");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create table model and set column names
        tableModel = new DefaultTableModel(new String[]{"EmployeeID", "EmployeeName", "JobRole", "Schedule", "Location", "ContactNumber", "Email"}, 0);
        employeeTable = new JTable(tableModel);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        add(scrollPane, BorderLayout.CENTER);

        // Add buttons
        JPanel buttonPanel = new JPanel();
        JButton assignButton = new JButton("Assign");
        buttonPanel.add(assignButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load employee data
        loadEmployeeData();

        // Add action listener for the assign button
        assignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    assignEmployees();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(AssignEmployeesForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void loadEmployeeData() throws ClassNotFoundException {
        String query = "SELECT * FROM Employees";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("EmployeeID"));
                row.add(rs.getString("EmployeeName"));
                row.add(rs.getString("JobRole"));
                row.add(rs.getString("Schedule"));
                row.add(rs.getString("Location"));
                row.add(rs.getString("ContactNumber"));
                row.add(rs.getString("Email"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading employee data: " + e.getMessage());
        }
    }

    private void assignEmployees() throws ClassNotFoundException {
        int[] selectedRows = employeeTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one employee to assign.");
            return;
        }

        String query = "INSERT INTO EmployeeJobAssignments (JobID, EmployeeID) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            for (int selectedRow : selectedRows) {
                String employeeID = (String) tableModel.getValueAt(selectedRow, 0);
                pstmt.setString(1, jobID);
                pstmt.setString(2, employeeID);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            JOptionPane.showMessageDialog(this, "Employees assigned successfully!");
            dispose(); // Close the form
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error assigning employees: " + e.getMessage());
        }
    }
}
