
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MonthlyReportForm extends JFrame {

    private JTable salesTable;
    private DefaultTableModel tableModel;
    private JTextField monthField;
    private JTextField yearField;

    public MonthlyReportForm() {
        setTitle("Monthly Sales Report");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create table model and set column names
        tableModel = new DefaultTableModel(new String[]{"SaleID", "SaleDate", "ItemName", "Quantity", "TotalAmount", "Category"}, 0);
        salesTable = new JTable(tableModel);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(salesTable);
        add(scrollPane, BorderLayout.CENTER);

        // Create input fields for month and year
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Month (MM):"));
        monthField = new JTextField(5);
        inputPanel.add(monthField);
        inputPanel.add(new JLabel("Year (YYYY):"));
        yearField = new JTextField(5);
        inputPanel.add(yearField);

        // Add buttons
        JPanel buttonPanel = new JPanel();
        JButton generateButton = new JButton("Generate Report");
        JButton topOrderedButton = new JButton("Top Ordered Items");
        JButton pdfButton = new JButton("Download PDF");
        JButton closeButton = new JButton("Close");

        buttonPanel.add(generateButton);
        buttonPanel.add(topOrderedButton);
        buttonPanel.add(pdfButton);
        buttonPanel.add(closeButton);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners for buttons
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    generateReport();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MonthlyReportForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        topOrderedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    generateTopOrdersReport();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MonthlyReportForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        pdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    downloadPDF();
                } catch (DocumentException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void generateTopOrdersReport() throws ClassNotFoundException {
        String query = "SELECT order_id, orderDate, item_name, item_quantity, total_price, item_category "
                + "FROM customerorders "
                + "ORDER BY "
                + "  (SELECT COUNT(*) "
                + "   FROM customerorders AS co2 "
                + "   WHERE co2.item_category = customerorders.item_category) DESC";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {

            tableModel.setRowCount(0); // Clear existing data
            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("order_id"));
                row.add(rs.getString("orderDate"));
                row.add(rs.getString("item_name"));
                row.add(rs.getString("item_quantity"));
                row.add(rs.getString("total_price"));
                row.add(rs.getString("item_category"));

                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating report: " + e.getMessage());
        }
    }

    private void generateReport() throws ClassNotFoundException {
        String month = monthField.getText();
        String year = yearField.getText();

        if (month.isEmpty() || year.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both month and year.");
            return;
        }

        String startDate = year + "-" + month + "-01";
        String endDate = year + "-" + month + "-31";

        String query = "SELECT order_id, orderDate, item_name, item_quantity, total_price,item_category "
                + "FROM customerorders "
                + "WHERE orderDate BETWEEN ? AND ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);
            ResultSet rs = pstmt.executeQuery();

            tableModel.setRowCount(0); // Clear existing data
            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("order_id"));
                row.add(rs.getString("orderDate"));
                row.add(rs.getString("item_name"));
                row.add(rs.getString("item_quantity"));
                row.add(rs.getString("total_price"));
                row.add(rs.getString("item_category"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating report: " + e.getMessage());
        }
    }

    private void downloadPDF() throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("MonthlySalesReport.pdf"));
        document.open();

        // Create a table with the same number of columns as the table model
        PdfPTable table = new PdfPTable(tableModel.getColumnCount());

        // Add table headers
        for (int column = 0; column < tableModel.getColumnCount(); column++) {
            table.addCell(tableModel.getColumnName(column));
        }

        // Add table rows
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                Object cellValue = tableModel.getValueAt(i, j);
                table.addCell(cellValue != null ? cellValue.toString() : "");
            }
        }

        document.add(table);
        document.close();

        JOptionPane.showMessageDialog(this, "PDF downloaded successfully!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MonthlyReportForm().setVisible(true);
            }
        });
    }
}
