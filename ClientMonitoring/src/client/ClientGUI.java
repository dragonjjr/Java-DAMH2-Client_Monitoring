package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ClientGUI extends JPanel implements ActionListener {
	JPanel panelGridBagLayout,pn1;
	boolean btnSaveModeClient = false;
	JLabel lb_title, lb_client_ip, lb_client_port;
	JTextField tf_client_ip, tf_client_port;
	JTable tbAction;
	String[] colMedHdr = { "ID", "Time", "Action", "Description" };
	JButton btnConnect;

	Client client = new Client(this);

	public ClientGUI() {
		super(new BorderLayout());

		panelGridBagLayout = new JPanel();
		pn1 = new JPanel();
		
		// Set up layout for panel
		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		panelGridBagLayout.setLayout(gridBagLayout);

		lb_title = new JLabel();
		lb_title.setForeground(Color.BLACK);
		lb_title.setFont(new Font("Arial", Font.BOLD, 20));
		lb_title.setText("Monitoring");

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 2;
		// gridBagConstraints.gridheight = 0;
		gridBagConstraints.insets = new Insets(10, 50, 10, 50);
		panelGridBagLayout.add(lb_title, gridBagConstraints);
		gridBagConstraints.gridwidth = 1;
		
		lb_client_ip = new JLabel();
		lb_client_ip.setForeground(Color.BLACK);
		lb_client_ip.setText("IP : ");

		lb_client_port = new JLabel();
		lb_client_port.setForeground(Color.BLACK);
		lb_client_port.setText("Port : ");

		tf_client_ip = new JTextField(null, 10);
		tf_client_ip.setFont(new Font("Arial", Font.BOLD, 12));
		tf_client_ip.setPreferredSize(new Dimension(110, 25));
		tf_client_port = new JTextField();
		tf_client_port.setFont(new Font("Arial", Font.BOLD, 12));
		tf_client_port.setPreferredSize(new Dimension(110, 25));

		// pn1.add(lb_title);
		pn1.add(Box.createRigidArea(new Dimension(10, 0)));
		pn1.add(lb_client_ip);
		pn1.add(Box.createRigidArea(new Dimension(5, 0)));
		pn1.add(tf_client_ip);
		pn1.add(Box.createRigidArea(new Dimension(10, 0)));
		pn1.add(lb_client_port);
		pn1.add(Box.createRigidArea(new Dimension(5, 0)));
		pn1.add(tf_client_port);

		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(this);
		btnConnect.setActionCommand("btnConnect");
		pn1.add(Box.createRigidArea(new Dimension(15, 0)));
		pn1.add(btnConnect);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new Insets(0, 10, 5, 10);

		panelGridBagLayout.add(pn1, gridBagConstraints);

		
		DefaultTableModel table_model = new DefaultTableModel(colMedHdr, 0);
		tbAction = new JTable(table_model);
		// Box
		JPanel pn7 = new JPanel();
		pn7.setPreferredSize(new Dimension(20, tbAction.getHeight()));

		JPanel pn8 = new JPanel();
		pn8.setPreferredSize(new Dimension(20, tbAction.getHeight()));

		JPanel pn9 = new JPanel();
		pn9.setPreferredSize(new Dimension(tbAction.getWidth(), 20));

		add(panelGridBagLayout, BorderLayout.PAGE_START);
		add(pn7, BorderLayout.WEST);
		add(pn8, BorderLayout.EAST);
		add(pn9, BorderLayout.SOUTH);
		add(new JScrollPane(tbAction), BorderLayout.CENTER);

	}

	public void createAndShowGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);

		JFrame frame = new JFrame("Client");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JComponent newContentPane = ClientGUI.this;
		newContentPane.setOpaque(true);
		frame.setContentPane(newContentPane);

		frame.setMinimumSize(new Dimension(600, 300));
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
		if (str.equals("btnConnect")) {
			if (btnSaveModeClient == false) {
				try {
					client.startClient(tf_client_ip.getText(), Integer.parseInt(tf_client_port.getText()));
					btnConnect.setText("Disconnect");
					btnSaveModeClient = true;
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(this, "Connect failed");
				} catch (NumberFormatException nex) {
					JOptionPane.showMessageDialog(this, "Please input valid Port");
				}
			} else {
				int choose = JOptionPane.showConfirmDialog(this, "Do you want to disconnect?", "Close Client",
						JOptionPane.YES_NO_OPTION);
				if (choose == JOptionPane.YES_OPTION) {
					try {
						client.closeSocket();
						btnSaveModeClient = false;
						btnConnect.setText("Connect");
					} catch (IOException ex) {
						JOptionPane.showMessageDialog(this, "Failed");
					}
				}
			}
		}
	}
}
