import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatusEditDialog extends JDialog {

    private final int orderId;
    private boolean saved = false;
    private JComboBox<String> statusComboBox;

    private JTextField itemNameField;
    private JTextField total_priceField;
    private JTextField statusField;
    private JButton saveButton;
    private JButton cancelButton;

    public StatusEditDialog(Frame parent, boolean modal, int orderId) throws ClassNotFoundException {
        super(parent, modal);
        this.orderId = orderId;
        initializeUI();
        loadOrderDetails();
        setLocationRelativeTo(parent);
        pack();
    }

    private void initializeUI() {
        setTitle("Edit Status And Price");
        setLayout(new GridLayout(5, 2));

        add(new JLabel("Item Name:"));
        itemNameField = new JTextField(20);
        itemNameField.setEditable(false); // Assuming you don't want it to be editable
        add(itemNameField);

        add(new JLabel("Total:"));
        total_priceField = new JTextField(20);
        add(total_priceField);

        add(new JLabel("Status"));
        statusComboBox = new JComboBox<>(new String[]{"Reviewing", "Processing", "Completed", "Canceled"});
        add(statusComboBox);

        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                saveOrder();
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(StatusEditDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        add(saveButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> cancelEdit());
        add(cancelButton);
    }

    private void loadOrderDetails() throws ClassNotFoundException {
        String query = "SELECT item_name, total_price, status FROM customerorders WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                itemNameField.setText(rs.getString("item_name"));
                total_priceField.setText(String.valueOf(rs.getInt("total_price")));
                statusComboBox.setSelectedItem(rs.getString("status"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading order details: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveOrder() throws ClassNotFoundException, SQLException {
        String updateQuery = "UPDATE customerorders SET total_price = ?, status = ? WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            int totalPrice = Integer.parseInt(total_priceField.getText());
            String status = (String) statusComboBox.getSelectedItem();

            pstmt.setInt(1, totalPrice);
            pstmt.setString(2, status);
            pstmt.setInt(3, orderId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Order updated successfully!");

                // Show OrderStatusDialog
                OrderStatusDialog dialog = new OrderStatusDialog(SwingUtilities.getWindowAncestor(this), status, totalPrice);
                dialog.setVisible(true);

                saved = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No changes were made to the order.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating order: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            throw ex; // Rethrow to handle elsewhere if needed
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format for total price.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelEdit() {
        dispose();
    }

    public boolean isSaved() {
        return saved;
    }
}
