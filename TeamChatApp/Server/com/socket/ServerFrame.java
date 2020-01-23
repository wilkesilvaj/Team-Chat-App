package com.socket;

import java.awt.Color;
import java.io.FileWriter;
import java.io.PrintWriter;

import javax.swing.*;
import javax.swing.UIManager;

public class ServerFrame extends javax.swing.JFrame {

	public SocketServer server;
	public Thread serverThread;

	public ServerFrame() {
		createScreen();
		jTextArea1.setEditable(false);
	}

	private JButton btnConnect;
	private JScrollPane scrollTextArea;
	public JTextArea jTextArea1;

	private void createScreen() {

		btnConnect = new JButton();
		scrollTextArea = new JScrollPane();
		jTextArea1 = new JTextArea();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("TeamChatServer");

		scrollTextArea.setBackground(Color.BLACK);

		btnConnect.setText("Connect");
		btnConnect.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent btnEvent) {
				btnConnectActionPerformed(btnEvent);
			}
		});

		jTextArea1.setColumns(80);
		jTextArea1.setFont(new java.awt.Font("Arial", 0, 12));
		jTextArea1.setRows(40);
		jTextArea1.setBackground(Color.BLACK);
		jTextArea1.setForeground(Color.YELLOW);
		scrollTextArea.setViewportView(jTextArea1);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(scrollTextArea)
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(btnConnect))).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(btnConnect))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				.addComponent(scrollTextArea, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
				.addContainerGap()));

		pack();
			
	}

	private void btnConnectActionPerformed(java.awt.event.ActionEvent btnEvent) {
		
		server = new SocketServer(this);
		btnConnect.setEnabled(false);
	}

	public void RetryStart(int port) {
		if (server != null) {
			server.stop();
		}
		server = new SocketServer(this, port);
	}

	public static void main(String args[]) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new ServerFrame().setVisible(true);
			}
		});
	}
	
	
	

}
