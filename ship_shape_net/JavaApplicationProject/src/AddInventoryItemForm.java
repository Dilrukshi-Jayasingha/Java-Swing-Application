import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddInventoryItemForm extends JFrame {
    private JTextField itemNameField;
    private JTextField categoryField;
    private JTextField quantityField;
    private JTextField locationField;
    private InventoryManagementForm parent;

    public AddInventoryItemForm(InventoryManagementForm parent) {
        this.parent = parent;
        setTitle("Add Inventory Item");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create form fields
        itemNameField = new JTextField(20);
        categoryField = new JTextField(20);
        quantityField = new JTextField(20);
        locationField = new JTextField(20);

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2));
        formPanel.add(new JLabel("Item Name:"));
        formPanel.add(itemNameField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);
        formPanel.add(new JLabel("Location:"));
        formPanel.add(locationField);

        // Add Save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    saveInventoryItem();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(AddInventoryItemForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Add components to frame
        add(formPanel, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);
    }

    private void saveInventoryItem() throws ClassNotFoundException {
        String itemName = itemNameField.getText();
        String category = categoryField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        String location = locationField.getText();

        String query1 = "INSERT INTO InventoryItems (ItemName, Category, ReorderLevel, SupplierID) VALUES (?, ?, ?, ?)";
        String query2 = "INSERT INTO InventoryStock (ItemID, Location, Quantity) VALUES (LAST_INSERT_ID(), ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt1 = conn.prepareStatement(query1);
             PreparedStatement pstmt2 = conn.prepareStatement(query2)) {
            pstmt1.setString(1, itemName);
            pstmt1.setString(2, category);
            pstmt1.setInt(3, 10); // Assuming reorder level as 10 for simplicity
            pstmt1.setInt(4, 1);  // Assuming SupplierID as 1 for simplicity

            int affectedRows1 = pstmt1.executeUpdate();
            if (affectedRows1 > 0) {
                pstmt2.setString(1, location);
                pstmt2.setInt(2, quantity);
                int affectedRows2 = pstmt2.executeUpdate();
                if (affectedRows2 > 0) {
                    JOptionPane.showMessageDialog(this, "Inventory item added successfully!");
                    parent.loadInventoryData(); // Refresh inventory list in parent form
                    dispose(); // Close the form
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add inventory item stock.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add inventory item.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding inventory item: " + e.getMessage());
        }
    }
}
