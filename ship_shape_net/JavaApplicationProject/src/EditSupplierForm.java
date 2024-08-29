
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EditSupplierForm extends JFrame {

    private boolean saved = false;
    private JTextField supplierNameField;
    private JTextField categoryField;
    private JTextField contactPersonField;
    private JTextField emailField;
    private JTextField phoneNumberField;
    private JTextField addressField;
    private JTextField countryField;
    private JTextField marinePartsSuppliedField;
    private String supplierID;

    public EditSupplierForm(String supplierID) throws ClassNotFoundException {
        this.supplierID = supplierID;
        setTitle("Edit Supplier");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create form fields
        supplierNameField = new JTextField(20);
        categoryField = new JTextField(20);
        contactPersonField = new JTextField(20);
        emailField = new JTextField(20);
        phoneNumberField = new JTextField(20);
        addressField = new JTextField(20);
        countryField = new JTextField(20);
        marinePartsSuppliedField = new JTextField(20);

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(9, 2));
        formPanel.add(new JLabel("Supplier Name:"));
        formPanel.add(supplierNameField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);
        formPanel.add(new JLabel("Contact Person:"));
        formPanel.add(contactPersonField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Phone Number:"));
        formPanel.add(phoneNumberField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);
        formPanel.add(new JLabel("Country:"));
        formPanel.add(countryField);
        formPanel.add(new JLabel("Marine Parts Supplied:"));
        formPanel.add(marinePartsSuppliedField);

        // Add Save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    saveSupplier();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(EditSupplierForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Add components to frame
        add(formPanel, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);

        // Load supplier details
        loadSupplierDetails();
    }

    private void loadSupplierDetails() throws ClassNotFoundException {
        String query = "SELECT * FROM Suppliers WHERE SupplierID = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, supplierID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                supplierNameField.setText(rs.getString("SupplierName"));
                categoryField.setText(rs.getString("Category"));
                contactPersonField.setText(rs.getString("ContactPerson"));
                emailField.setText(rs.getString("Email"));
                phoneNumberField.setText(rs.getString("PhoneNumber"));
                addressField.setText(rs.getString("Address"));
                countryField.setText(rs.getString("Country"));
                marinePartsSuppliedField.setText(rs.getString("MarinePartsSupplied"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading supplier details: " + e.getMessage());
        }
    }

    private void saveSupplier() throws ClassNotFoundException {
        String supplierName = supplierNameField.getText();
        String category = categoryField.getText();
        String contactPerson = contactPersonField.getText();
        String email = emailField.getText();
        String phoneNumber = phoneNumberField.getText();
        String address = addressField.getText();
        String country = countryField.getText();
        String marinePartsSupplied = marinePartsSuppliedField.getText();

        String query = "UPDATE Suppliers SET SupplierName = ?, Category = ?, ContactPerson = ?, Email = ?, PhoneNumber = ?, Address = ?, Country = ?, MarinePartsSupplied = ? WHERE SupplierID = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, supplierName);
            pstmt.setString(2, category);
            pstmt.setString(3, contactPerson);
            pstmt.setString(4, email);
            pstmt.setString(5, phoneNumber);
            pstmt.setString(6, address);
            pstmt.setString(7, country);
            pstmt.setString(8, marinePartsSupplied);
            pstmt.setString(9, supplierID);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Supplier updated successfully!");
               saved = true;
        dispose(); // Close the form

            } else {
                JOptionPane.showMessageDialog(this, "Failed to update supplier.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating supplier: " + e.getMessage());
        }
    }
     public boolean isSaved() {
        return saved;
    }
}
