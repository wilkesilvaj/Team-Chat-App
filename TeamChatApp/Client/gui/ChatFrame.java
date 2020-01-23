package gui;

import com.socket.Message;
import com.socket.SocketClient;
import com.socket.SocketServer;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.*;
import javax.swing.UIManager;

public class ChatFrame extends javax.swing.JFrame {

	public SocketClient client;
	public int port;
	public String serverAddr, username, password;
	public Thread clientThread;
	public DefaultListModel<String> model;

	public ChatFrame() {
		createScreen();
		this.setTitle("TeamChat");
		model.addElement("All");
		userList.setSelectedIndex(0);
	}

	public JButton btnConnect, btnLogin, btnSend;
	public JLabel lblUsername, lblPort, lblPassword;
	public JList<String> userList;
	public JPasswordField pfPassword;
	public JScrollPane scrollTextArea, scrollUserList;
	public JSeparator jSeparator1, jSeparator2;
	public JTextArea jTextArea1;
	public JTextField tfPort, tfUsername, tfTypemessage;

	private void createScreen() {
		
		lblUsername = new JLabel();
		lblPort = new JLabel();
		lblPassword = new JLabel();
		tfPort = new JTextField();
		tfUsername = new JTextField();
		tfTypemessage = new JTextField();
		btnConnect = new JButton();
		btnLogin = new JButton();
		btnSend = new JButton();
		pfPassword = new JPasswordField();
		scrollTextArea = new JScrollPane();
		scrollUserList = new JScrollPane();
		jTextArea1 = new JTextArea();
		userList = new JList();
		jSeparator1 = new JSeparator();
		jSeparator2 = new JSeparator();
		

		lblPort.setText("Port: ");
		tfPort.setText("8001");

		btnConnect.setText("Connect");
		btnConnect.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent btnEvent) {
				btnConnectActionPerformed(btnEvent);
			}
		});

		lblUsername.setText("Username: ");
		tfUsername.setText("username");
		tfUsername.setEnabled(false);

		lblPassword.setText("Password :");

		pfPassword.setText("password");
		pfPassword.setEchoChar('*');
		pfPassword.setEnabled(false);
		
		btnLogin.setText("Login");
		btnLogin.setEnabled(false);
		btnLogin.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent btnEvent) {
				btnLoginActionPerformed(btnEvent);
			}
		});
		
		/** Event handler for the picking of a desired recipient for 
		 *  messages 
		 */
		userList.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
			String recipient = userList.getSelectedValue().toString();
			jTextArea1.setText(SocketServer.messageList.getPrivateMessages(username, recipient));
			//jTextArea1.append("Sender: "+username+ "\nRecipient:"+recipient+"\n");
			}
		});
		

		jTextArea1.setColumns(70);
		jTextArea1.setFont(new java.awt.Font("Arial", 0, 12));
		jTextArea1.setRows(30);
		jTextArea1.setEditable(false);
		scrollTextArea.setViewportView(jTextArea1);

		userList.setModel((model = new DefaultListModel<String>()));
		scrollUserList.setViewportView(userList);
		userList.setBackground(Color.WHITE);
		jTextArea1.setBackground(Color.WHITE);

		tfTypemessage.setText("Type a message");
		tfTypemessage.setEditable(false);

		btnSend.setText("Send");
		btnSend.setEnabled(false);
		btnSend.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent btnEvent) {
				btnSendActionPerformed(btnEvent);
			}
		});

		
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(jSeparator2).addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
										.addComponent(lblUsername).addComponent(lblPassword))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(layout.createSequentialGroup()
												.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(pfPassword).addComponent(tfUsername))
												.addGap(25, 25, 25)
												.addGroup(layout
														.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
														.addComponent(lblPort).addComponent(lblPassword))
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
												.addGroup(layout
														.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(tfPort).addComponent(pfPassword))))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
										.addComponent(btnConnect, javax.swing.GroupLayout.Alignment.TRAILING,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
												layout.createSequentialGroup().addGroup(layout
														.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
														.addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE,
																70, javax.swing.GroupLayout.PREFERRED_SIZE))
														.addPreferredGap(
																javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addGroup(layout.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING, false)))))
						.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
								layout.createSequentialGroup().addComponent(scrollTextArea).addGap(25, 25, 25)
										.addComponent(scrollUserList, javax.swing.GroupLayout.PREFERRED_SIZE, 108,
												javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
								layout.createSequentialGroup()
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(tfTypemessage).addGap(25, 25, 25).addComponent(btnSend,
												javax.swing.GroupLayout.PREFERRED_SIZE, 108,
												javax.swing.GroupLayout.PREFERRED_SIZE)))
				.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lblUsername)
						.addComponent(tfUsername, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPort)
						.addComponent(tfPort, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(btnConnect))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(pfPassword, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPassword).addComponent(lblPassword).addComponent(btnLogin))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
						javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				.addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
						.addComponent(scrollTextArea)
						.addComponent(scrollUserList, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(btnSend)
						.addComponent(tfTypemessage, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				.addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addContainerGap()));

		pack();
	}

	private void btnConnectActionPerformed(java.awt.event.ActionEvent btnEvent) {
		port = Integer.parseInt(tfPort.getText());

		if (!tfPort.getText().isEmpty()) {
			try {
				client = new SocketClient(this);
				clientThread = new Thread(client);
				clientThread.start();
				client.send(new Message("time", "test", "testUser", "testContent", "Server"));
			} catch (Exception ex) {
				jTextArea1.append("Application: Server not found\n");
			}
		}
	}

	private void btnLoginActionPerformed(java.awt.event.ActionEvent btnEvent) {
		username = tfUsername.getText();
		password = pfPassword.getText();

		if (!username.isEmpty() && !password.isEmpty()) {
			client.send(new Message("time", "login", username, password, "Server"));
		}
			
	}

	private void btnSendActionPerformed(java.awt.event.ActionEvent btnEvent) {
		String msg = tfTypemessage.getText();
		String target = userList.getSelectedValue().toString();

		if (!msg.isEmpty() && !target.isEmpty()) {
			tfTypemessage.setText("");
			client.send(new Message("time", "message", username, msg, target));
		}
	}

	public static void main(String args[]) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			//System.out.println(ex.getMessage());
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new ChatFrame().setVisible(true);
			}
		});
	}
}
