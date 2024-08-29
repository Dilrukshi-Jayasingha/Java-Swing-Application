import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InventoryManagementForm extends JFrame {
    private JTable inventoryTable;
    private DefaultTableModel tableModel;

    public InventoryManagementForm() throws ClassNotFoundException {
        setTitle("Inventory Management");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create table model and set column names
        tableModel = new DefaultTableModel(new String[]{"ItemID", "ItemName", "Category", "Quantity", "Location"}, 0);
        inventoryTable = new JTable(tableModel);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(inventoryTable);
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

        // Load inventory data
        loadInventoryData();
        
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
                new AddInventoryItemForm(InventoryManagementForm.this).setVisible(true);
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    editInventoryItem();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(InventoryManagementForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    deleteInventoryItem();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(InventoryManagementForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    void loadInventoryData() throws ClassNotFoundException {
        String query = "SELECT i.ItemID, i.ItemName, i.Category, s.Quantity, s.Location FROM InventoryItems i JOIN InventoryStock s ON i.ItemID = s.ItemID";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("ItemID"));
                row.add(rs.getString("ItemName"));
                row.add(rs.getString("Category"));
                row.add(rs.getString("Quantity"));
                row.add(rs.getString("Location"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading inventory data: " + e.getMessage());
        }
    }

    private void editInventoryItem() throws ClassNotFoundException {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an inventory item to edit.");
            return;
        }

        String itemID = (String) tableModel.getValueAt(selectedRow, 0);
        EditInventoryItemForm editForm = new EditInventoryItemForm(itemID);
        editForm.setVisible(true);

        // Refresh the inventory list after editing
        editForm.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                tableModel.setRowCount(0); // Clear existing data
                try {
                    loadInventoryData(); // Reload data from the database
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(InventoryManagementForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void deleteInventoryItem() throws ClassNotFoundException {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an inventory item to delete.");
            return;
        }

        String itemID = (String) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this inventory item?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String query = "DELETE FROM InventoryItems WHERE ItemID = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, itemID);
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Inventory item deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete inventory item.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting inventory item: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new InventoryManagementForm().setVisible(true);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(InventoryManagementForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
