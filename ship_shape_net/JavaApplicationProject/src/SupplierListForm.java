import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SupplierListForm extends JFrame {

    private JTable supplierTable;
    private DefaultTableModel tableModel;

    public SupplierListForm() throws ClassNotFoundException {
        setTitle("Supplier List");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create table model and set column names
        tableModel = new DefaultTableModel(new String[]{"SupplierID", "SupplierName", "Category", "ContactPerson", "Email", "PhoneNumber", "Address", "Country", "MarinePartsSupplied"}, 0);
        supplierTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(supplierTable);
        add(scrollPane, BorderLayout.CENTER);

        // Add buttons
        JPanel buttonPanel = new JPanel();
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton backButton = new JButton("Back");

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Load supplier data
        loadSupplierData();

        backButton.addActionListener(e -> {
            SuppliersDashboard suppliersDashboard = new SuppliersDashboard();
            suppliersDashboard.setVisible(true);
            this.setVisible(false);        // TODO add your handling code here:
        }); // Corrected method name
        // Add action listeners for buttons
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    editSupplier();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(SupplierListForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    deleteSupplier();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(SupplierListForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void loadSupplierData() throws ClassNotFoundException {
        String query = "SELECT * FROM Suppliers";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("SupplierID"));
                row.add(rs.getString("SupplierName"));
                row.add(rs.getString("Category"));
                row.add(rs.getString("ContactPerson"));
                row.add(rs.getString("Email"));
                row.add(rs.getString("PhoneNumber"));
                row.add(rs.getString("Address"));
                row.add(rs.getString("Country"));
                row.add(rs.getString("MarinePartsSupplied"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading supplier data: " + e.getMessage());
        }
    }

    private void editSupplier() throws ClassNotFoundException {
        int selectedRow = supplierTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a supplier to edit.");
            return;
        }

        String supplierID = (String) tableModel.getValueAt(selectedRow, 0);
        // Open edit form with selected supplier ID
        EditSupplierForm editForm = new EditSupplierForm(supplierID);
        editForm.setVisible(true);

        // Listen for form closure to refresh the supplier list if changes were saved
        editForm.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                if (editForm.isSaved()) {
                    // Refresh the supplier data
                    tableModel.setRowCount(0); // Clear existing data
                    try {
                        loadSupplierData(); // Reload data from the database
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(SupplierListForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    private void deleteSupplier() throws ClassNotFoundException {
        int selectedRow = supplierTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a supplier to delete.");
            return;
        }

        String supplierID = (String) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this supplier?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String query = "DELETE FROM Suppliers WHERE SupplierID = ?";
            try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, supplierID);
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Supplier deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete supplier.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting supplier: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new SupplierListForm().setVisible(true);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(SupplierListForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
