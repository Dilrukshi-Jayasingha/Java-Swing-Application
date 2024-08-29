import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrderStatusDialog extends JDialog {
    private JTextArea emailBodyTextArea;
    private JButton sendButton;

    public OrderStatusDialog(Window parent, String orderStatus,int totalPrice) {
        super(parent, "Send Order Status Notification", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());

        // Create email body text area
        emailBodyTextArea = new JTextArea(10, 30);
        emailBodyTextArea.setText("Dear Customer,\n\nYour order status has been updated to: " + orderStatus + ".\nYour Total is Rs."+ totalPrice +"\n\nBest regards,\nYour Company");
        add(new JScrollPane(emailBodyTextArea), BorderLayout.CENTER);

        // Create send button
        sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(OrderStatusDialog.this, "Email sent successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        });
        add(sendButton, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }
}
