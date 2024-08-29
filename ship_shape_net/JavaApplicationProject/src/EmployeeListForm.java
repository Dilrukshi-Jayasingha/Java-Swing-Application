
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmployeeListForm extends JFrame {

    private JTable employeeTable;
    private DefaultTableModel tableModel;

    public EmployeeListForm() throws ClassNotFoundException {
        setTitle("Employee List");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create table model and set column names
        tableModel = new DefaultTableModel(new String[]{"EmployeeID", "EmployeeName", "JobRole", "Schedule", "Location", "ContactNumber", "Email"}, 0);
        employeeTable = new JTable(tableModel);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        add(scrollPane, BorderLayout.CENTER);

        // Add buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton closeButton = new JButton("Close");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Load employee data
        loadEmployeeData();

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
                new AddEmployeeForm(EmployeeListForm.this).setVisible(true);
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    editEmployee();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(EmployeeListForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    deleteEmployee();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(EmployeeListForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    void loadEmployeeData() throws ClassNotFoundException {
        String query = "SELECT * FROM Employees";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

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

    private void editEmployee() throws ClassNotFoundException {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to edit.");
            return;
        }

        String employeeID = (String) tableModel.getValueAt(selectedRow, 0);
        EditEmployeeForm editForm = new EditEmployeeForm(employeeID);
        editForm.setVisible(true);

        // Refresh the employee list after editing
        editForm.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                try {
                    tableModel.setRowCount(0); // Clear existing data
                    loadEmployeeData(); // Reload data from the database
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(EmployeeListForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void deleteEmployee() throws ClassNotFoundException {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to delete.");
            return;
        }

        String employeeID = (String) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this employee?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String query = "DELETE FROM Employees WHERE EmployeeID = ?";
            try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, employeeID);
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Employee deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete employee.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting employee: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new EmployeeListForm().setVisible(true);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(EmployeeListForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
