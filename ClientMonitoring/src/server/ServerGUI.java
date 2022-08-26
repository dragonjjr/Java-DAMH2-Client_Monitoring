package server;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ServerGUI extends JPanel implements ActionListener{
    JPanel pn1, pn2, pn3, pn4, pn_list_client, pn_action_client;
    boolean btnSaveModeServer  = false;

    JTextField tf_search_client;
	JTable tbAction;
	String[] colMedHdr = { "ID", "Time", "Action", "Description" };
    JList list_client;
    JLabel lb_server_ip;
    JLabel lb_server_port;
    JLabel lb3;

    private DefaultListModel model;
    CardLayout clMain;

    JButton btnStart, btnSearchClient;

    BoxLayout bl1;

    public ServerGUI() throws UnknownHostException {
        super(new BorderLayout());


        pn1 = new JPanel();

        lb_server_ip = new JLabel();
        lb_server_ip.setForeground(Color.RED);
        lb_server_port = new JLabel();
        lb_server_port.setForeground(Color.RED);
        pn1.add(lb_server_ip);
        pn1.add(Box.createRigidArea(new Dimension(20,0)));
        pn1.add(lb_server_port);

        lb_server_ip.setText("IP : ");
        lb_server_port.setText("Port : ");


        btnStart = new JButton("Start");
        btnStart.addActionListener(this);
        btnStart.setActionCommand("btnStart");
        pn1.add(Box.createRigidArea(new Dimension(20, 0)));
        pn1.add(btnStart);

        pn_list_client = new JPanel(new BorderLayout());
        pn_list_client.setBorder(BorderFactory.createTitledBorder("List Client"));
        tf_search_client = new JTextField(null,10);
        pn2 = new JPanel();
        pn2.setLayout(new BoxLayout(pn2,BoxLayout.X_AXIS));
        btnSearchClient = new JButton("Search");
        pn2.add(tf_search_client);
        pn2.add(btnSearchClient);

        model = new DefaultListModel();
        list_client = new JList();
        list_client.setModel(model);



        pn_list_client.add(pn2,BorderLayout.PAGE_START);
        pn_list_client.add(new JScrollPane(list_client),BorderLayout.CENTER);

        pn_action_client = new JPanel();
        pn_action_client.setBorder(BorderFactory.createTitledBorder("Action of clients"));
		DefaultTableModel table_model = new DefaultTableModel(colMedHdr, 0);
		tbAction = new JTable(table_model);
		pn_action_client.add(new JScrollPane(tbAction));


        add(pn1, BorderLayout.PAGE_START);
        add(pn_list_client, BorderLayout.WEST);
        add(pn_action_client,BorderLayout.CENTER);
    }

    public void updateList_Client()
    {
        DefaultListModel model1 = (DefaultListModel) list_client.getModel();
        for(int i = 0 ; i< Main.getServer().getListClient().size();i++)
        {
            model1.addElement(Main.getServer().getListClient().get(i).getSocket().getInetAddress().getHostAddress());
        }
    }

    public void createAndShowGUI() throws UnknownHostException {
        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame frame = new JFrame("Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComponent newContentPane = ServerGUI.this;
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        frame.setMinimumSize(new Dimension(700,400));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();
        if(str.equals("btnStart"))
        {
            if(btnSaveModeServer == false) {
                try {
                    Main.getServer().startServer();

                    lb_server_ip.setText("IP : " + Main.getServer().getIP());
                    lb_server_port.setText("Port : " + Main.getServer().getPort());

                    btnSaveModeServer = true;
                    btnStart.setText("Close");

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,"Failed start server");
                }
            }
            else {
                int choose = JOptionPane.showConfirmDialog(this, "Do you want to disconnect?", "Close Server", JOptionPane.YES_NO_OPTION);
                if (choose == JOptionPane.YES_OPTION) {
                    try {
                        Main.getServer().CloseServer();

                        lb_server_ip.setText("IP : ");
                        lb_server_port.setText("Port : ");

                        btnStart.setText("Start");
                        btnSaveModeServer = false;

                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Failed");
                    }
                }
            }
        }
    }
}
