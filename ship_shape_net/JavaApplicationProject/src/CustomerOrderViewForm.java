
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author chari
 */
public class CustomerOrderViewForm extends javax.swing.JFrame {

    private int userId;

    private DefaultTableModel tableModel;

    /**
     * Creates new form CustomerOrderViewForm
     */
    public CustomerOrderViewForm(int userId) throws ClassNotFoundException {
        initComponents();
        this.userId = userId;
        setTitle("Your Orders");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        initializeUI();

    }

    private CustomerOrderViewForm() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        ordersTable = new javax.swing.JTable();
        BackjButton1 = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();

        ordersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(ordersTable);

        BackjButton1.setText("Back");
        BackjButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackjButton1ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        deleteButton.setText("Delete");

        editButton.setText("Edit");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(226, Short.MAX_VALUE)
                .addComponent(editButton)
                .addGap(37, 37, 37)
                .addComponent(deleteButton)
                .addGap(358, 358, 358))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(380, 380, 380)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteButton)
                    .addComponent(editButton))
                .addContainerGap(63, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BackjButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackjButton1ActionPerformed
        CustomerDashboard customerDashboard = new CustomerDashboard(userId);
        customerDashboard.setVisible(true);
        this.setVisible(false);        // TODO add your handling code here:
    }//GEN-LAST:event_BackjButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CustomerOrderViewForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CustomerOrderViewForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CustomerOrderViewForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CustomerOrderViewForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CustomerOrderViewForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BackjButton1;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton editButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable ordersTable;
    // End of variables declaration//GEN-END:variables

    private void initializeUI() throws ClassNotFoundException {
        // Create the table model and table
        tableModel = new DefaultTableModel(new String[]{"Order ID", "Item Name", "Category", "Quantity", "Special Note","Total", "Status","Order Date"}, 0);
        ordersTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(ordersTable);
        add(scrollPane, BorderLayout.CENTER);  // Ensures the table is central

        // Create buttons and their panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));  // Ensure buttons are in a flow layout

        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        BackjButton1 = new JButton("Back");

        // Set action listeners for the buttons
        editButton.addActionListener(e -> {
            try {
                editOrder();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CustomerOrderViewForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        deleteButton.addActionListener(e -> {
            try {
                deleteOrder();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CustomerOrderViewForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); // Corrected method name

        BackjButton1.addActionListener(e -> {
            CustomerDashboard customerDashboard = new CustomerDashboard(userId);
            customerDashboard.setVisible(true);
            this.setVisible(false);        // TODO add your handling code here:
        }); // Corrected method name

        // Add buttons to the panel and panel to the frame
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(BackjButton1);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load orders into the table
        loadOrders();
    }

    private void loadOrders() throws ClassNotFoundException {
        String query = "SELECT order_id, item_name, item_category, item_quantity, special_note, total_price,status,orderDate FROM customerorders WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getInt("order_id"), rs.getString("item_name"), rs.getString("item_category"), rs.getInt("item_quantity"), rs.getString("special_note"),rs.getInt("total_price"), rs.getString("status"), rs.getString("orderDate")});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading orders: " + ex.getMessage());
        }
    }

    private void editOrder() throws ClassNotFoundException {
    int selectedRow = ordersTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select an order to edit.");
        return;
    }
    // Assuming the first column is the order ID
    int orderId = (Integer) ordersTable.getValueAt(selectedRow, 0);
    // Proceed to fetch the order details and show in an editable form
    editOrderDetails(orderId);
}
    
    private void editOrderDetails(int orderId) throws ClassNotFoundException {
    // Placeholder for opening an editing dialog
    OrderEditDialog editDialog = new OrderEditDialog(this, true, orderId);
    editDialog.setVisible(true);
    if (editDialog.isSaved()) {
        loadOrders();  // Reload orders if changes were saved
        
        CustomerDashboard customerDashboard = new CustomerDashboard(userId);
            customerDashboard.setVisible(true);
            this.setVisible(false); 
    }
}



    private void deleteOrder() throws ClassNotFoundException {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to delete.");
            return;
        }
        int orderId = (Integer) tableModel.getValueAt(selectedRow, 0);

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement("DELETE FROM customerorders WHERE order_id = ?")) {
            pstmt.setInt(1, orderId);
            int result = pstmt.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Order deleted successfully.");
                tableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete order.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting order: " + ex.getMessage());
        }
    }

}
