package server;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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

import client.Message;

public class ServerGUI extends JPanel implements ActionListener {
	JPanel pn1, pn2, pn3, pn4, pn_list_client, pn_action_client, panelGridBagLayout;
	boolean btnSaveModeServer = false;

	JTextField tf_search_client;
	JTable tbAction;
	String[] colMedHdr = { "ID", "Time", "Action", "Description" };
	JList list_client;
	JLabel lb_server_ip;
	JLabel lb_server_port;
	JLabel lb3, lb_title;

	private DefaultListModel model;
	CardLayout clMain;

	JButton btnStart, btnSearchClient;

	BoxLayout bl1;

	public ServerGUI() throws UnknownHostException {
		super(new BorderLayout());

		panelGridBagLayout = new JPanel();
		pn1 = new JPanel();

		lb_title = new JLabel();
		lb_title.setText("Monitoring");
		lb_title.setForeground(Color.BLACK);
		lb_title.setFont(new Font("Arial", Font.BOLD, 20));

		// Set up layout for panel
		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		panelGridBagLayout.setLayout(gridBagLayout);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 2;
		// gridBagConstraints.gridheight = 0;
		gridBagConstraints.insets = new Insets(10, 50, 10, 50);
		panelGridBagLayout.add(lb_title, gridBagConstraints);
		gridBagConstraints.gridwidth = 1;

		lb_server_ip = new JLabel();
		lb_server_ip.setForeground(Color.BLACK);
		lb_server_port = new JLabel();
		lb_server_port.setForeground(Color.BLACK);
		pn1.add(lb_server_ip);
		pn1.add(Box.createRigidArea(new Dimension(20, 0)));
		pn1.add(lb_server_port);

		lb_server_ip.setText("IP : ");
		lb_server_port.setText("Port : ");

		btnStart = new JButton("Start");
		btnStart.addActionListener(this);
		btnStart.setActionCommand("btnStart");
		pn1.add(Box.createRigidArea(new Dimension(20, 0)));
		pn1.add(btnStart);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new Insets(0, 10, 5, 10);
		panelGridBagLayout.add(pn1, gridBagConstraints);

		pn_list_client = new JPanel(new BorderLayout());
		pn_list_client.setBorder(BorderFactory.createTitledBorder("List Client"));
		tf_search_client = new JTextField(null, 10);
		pn2 = new JPanel();
		pn2.setLayout(new BoxLayout(pn2, BoxLayout.X_AXIS));
		btnSearchClient = new JButton("Search");
		pn2.add(tf_search_client);
		pn2.add(btnSearchClient);

		model = new DefaultListModel();
		list_client = new JList();
		list_client.setModel(model);

		pn_list_client.add(pn2, BorderLayout.PAGE_START);
		pn_list_client.add(new JScrollPane(list_client), BorderLayout.CENTER);

		pn_action_client = new JPanel();
		pn_action_client.setBorder(BorderFactory.createTitledBorder("Action of clients"));
		DefaultTableModel table_model = new DefaultTableModel(colMedHdr, 0);
		tbAction = new JTable(table_model);
		pn_action_client.add(new JScrollPane(tbAction));

		add(panelGridBagLayout, BorderLayout.PAGE_START);
		add(pn_list_client, BorderLayout.WEST);
		add(pn_action_client, BorderLayout.CENTER);
	}

	public void updateList_Client() {
		DefaultListModel model1 = (DefaultListModel) list_client.getModel();
		for (int i = 0; i < Main.getServer().getListClient().size(); i++) {
			String ipString = Main.getServer().getListClient().get(i).getSocket().getInetAddress().getHostAddress();
			if(!model1.contains(ipString))
			{
				model1.addElement(ipString);
			}
			
		}
	}

	public void createAndShowGUI() throws UnknownHostException {
		JFrame.setDefaultLookAndFeelDecorated(true);

		JFrame frame = new JFrame("Server");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JComponent newContentPane = ServerGUI.this;
		newContentPane.setOpaque(true);
		frame.setContentPane(newContentPane);

		frame.setMinimumSize(new Dimension(700, 400));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void fillTable(Message message) {
		DefaultTableModel model = (DefaultTableModel) tbAction.getModel();
		//model.setRowCount(0);

		Object[] rowdata = new Object[4];
		rowdata[0] = (model.getRowCount()+1);
		rowdata[1] = message.getTime();
		rowdata[2] = message.getKind();
		rowdata[3] = message.getAction();

		model.addRow(rowdata);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String str = e.getActionCommand();
		if (str.equals("btnStart")) {
			if (btnSaveModeServer == false) {
				try {
					Main.getServer().startServer();

					lb_server_ip.setText("IP : " + Main.getServer().getIP());
					lb_server_port.setText("Port : " + Main.getServer().getPort());

					btnSaveModeServer = true;
					btnStart.setText("Close");

				} catch (IOException ex) {
					JOptionPane.showMessageDialog(this, "Failed start server");
				}
			} else {
				int choose = JOptionPane.showConfirmDialog(this, "Do you want to disconnect?", "Close Server",
						JOptionPane.YES_NO_OPTION);
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
