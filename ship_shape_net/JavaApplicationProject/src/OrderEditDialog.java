import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderEditDialog extends JDialog {
    private final int orderId;
    private boolean saved = false;

    private JTextField itemNameField;
    private JTextField categoryField;
    private JTextField quantityField;
    private JButton saveButton;
    private JButton cancelButton;

    public OrderEditDialog(Frame parent, boolean modal, int orderId) throws ClassNotFoundException {
        super(parent, modal);
        this.orderId = orderId;
        initializeUI();
        loadOrderDetails();
        setLocationRelativeTo(parent);
        pack();
    }

    private void initializeUI() {
        setTitle("Edit Order");
        setLayout(new GridLayout(5, 2));

        add(new JLabel("Item Name:"));
        itemNameField = new JTextField(20);
        add(itemNameField);

        add(new JLabel("Category:"));
        categoryField = new JTextField(20);
        add(categoryField);

        add(new JLabel("Quantity:"));
        quantityField = new JTextField(20);
        add(quantityField);

        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                saveOrder();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(OrderEditDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        add(saveButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> cancelEdit());
        add(cancelButton);
    }

    private void loadOrderDetails() throws ClassNotFoundException {
        String query = "SELECT item_name, item_category, item_quantity FROM customerorders WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                itemNameField.setText(rs.getString("item_name"));
                categoryField.setText(rs.getString("item_category"));
                quantityField.setText(String.valueOf(rs.getInt("item_quantity")));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading order details: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveOrder() throws ClassNotFoundException {
        String updateQuery = "UPDATE customerorders SET item_name = ?, item_category = ?, item_quantity = ? WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, itemNameField.getText());
            pstmt.setString(2, categoryField.getText());
            pstmt.setInt(3, Integer.parseInt(quantityField.getText()));
            pstmt.setInt(4, orderId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Order updated successfully!");
                saved = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No changes were made to the order.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating order: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelEdit() {
        dispose();
    }

    public boolean isSaved() {
        return saved;
    }
}
