package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ClientGUI extends JPanel implements ActionListener {
    JPanel pn1, pn2, pn3, pn4;
    boolean btnSaveModeClient  = false;
    JLabel lb_title,lb_client_ip, lb_client_port;
    JTextField tf_client_ip, tf_client_port;

    JButton btnConnect;

    Client client = new Client();

    public ClientGUI(){
        super(new BorderLayout());


        pn1 = new JPanel();
        
        lb_title = new JLabel();
        lb_title.setForeground(Color.RED);
        lb_client_ip = new JLabel();
        lb_client_ip.setForeground(Color.RED);
        lb_client_port = new JLabel();
        lb_client_port.setForeground(Color.RED);

        tf_client_ip = new JTextField(null, 10);
        tf_client_ip.setFont(new Font("Arial", Font.BOLD, 12));
        tf_client_ip.setPreferredSize(new Dimension(100, 25));
        tf_client_port = new JTextField();
        tf_client_port.setFont(new Font("Arial", Font.BOLD, 12));
        tf_client_port.setPreferredSize(new Dimension(100, 25));

        pn1.add(lb_title);
        pn1.add(Box.createRigidArea(new Dimension(10,0)));
        pn1.add(lb_client_ip);
        pn1.add(Box.createRigidArea(new Dimension(10,0)));
        pn1.add(tf_client_ip);
        pn1.add(Box.createRigidArea(new Dimension(10,0)));
        pn1.add(lb_client_port);
        pn1.add(Box.createRigidArea(new Dimension(10,0)));
        pn1.add(tf_client_port);

        lb_title.setText("Monitoring");
        lb_client_ip.setText("IP : ");
        lb_client_port.setText("Port : ");


        btnConnect = new JButton("Connect");
        btnConnect.addActionListener(this);
        btnConnect.setActionCommand("btnConnect");
        pn1.add(Box.createRigidArea(new Dimension(20, 0)));
        pn1.add(btnConnect);




        add(pn1, BorderLayout.PAGE_START);
    }
    public void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame frame = new JFrame("Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComponent newContentPane = ClientGUI.this;
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        frame.setMinimumSize(new Dimension(500,200));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();
        if(str.equals("btnConnect"))
        {
            if(btnSaveModeClient == false) {
                try {
                    client.startClient(tf_client_ip.getText(), Integer.parseInt(tf_client_port.getText()));
                    btnConnect.setText("Disconnect");
                    btnSaveModeClient = true;
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,"Connect failed");
                }catch (NumberFormatException nex)
                {
                    JOptionPane.showMessageDialog(this,"Please input valid Port");
                }
            }
            else {
                int choose = JOptionPane.showConfirmDialog(this, "Do you want to disconnect?", "Close Client", JOptionPane.YES_NO_OPTION);
                if (choose == JOptionPane.YES_OPTION) {
                    try {
                        client.closeSocket();
                        btnSaveModeClient = false;
                        btnConnect.setText("Connect");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this,"Failed");
                    }
                }
            }
        }
    }
}
