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

public class EditInventoryItemForm extends JFrame {
    private JTextField itemNameField;
    private JTextField categoryField;
    private JTextField quantityField;
    private JTextField locationField;
    private String itemID;
    private boolean saved = false;

    public EditInventoryItemForm(String itemID) throws ClassNotFoundException {
        this.itemID = itemID;
        setTitle("Edit Inventory Item");
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
                    Logger.getLogger(EditInventoryItemForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Add components to frame
        add(formPanel, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);

        // Load inventory item details
        loadInventoryItemDetails();
    }

    private void loadInventoryItemDetails() throws ClassNotFoundException {
        String query = "SELECT i.ItemName, i.Category, s.Quantity, s.Location FROM InventoryItems i JOIN InventoryStock s ON i.ItemID = s.ItemID WHERE i.ItemID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, itemID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                itemNameField.setText(rs.getString("ItemName"));
                categoryField.setText(rs.getString("Category"));
                quantityField.setText(rs.getString("Quantity"));
                locationField.setText(rs.getString("Location"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading inventory item details: " + e.getMessage());
        }
    }

    private void saveInventoryItem() throws ClassNotFoundException {
        String itemName = itemNameField.getText();
        String category = categoryField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        String location = locationField.getText();

        String query1 = "UPDATE InventoryItems SET ItemName = ?, Category = ? WHERE ItemID = ?";
        String query2 = "UPDATE InventoryStock SET Quantity = ?, Location = ? WHERE ItemID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt1 = conn.prepareStatement(query1);
             PreparedStatement pstmt2 = conn.prepareStatement(query2)) {
            pstmt1.setString(1, itemName);
            pstmt1.setString(2, category);
            pstmt1.setString(3, itemID);

            int affectedRows1 = pstmt1.executeUpdate();
            if (affectedRows1 > 0) {
                pstmt2.setInt(1, quantity);
                pstmt2.setString(2, location);
                pstmt2.setString(3, itemID);
                int affectedRows2 = pstmt2.executeUpdate();
                if (affectedRows2 > 0) {
                    saved = true;
                    JOptionPane.showMessageDialog(this, "Inventory item updated successfully!");
                    dispose(); // Close the form
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update inventory item stock.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update inventory item.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating inventory item: " + e.getMessage());
        }
    }

    public boolean isSaved() {
        return saved;
    }
}
