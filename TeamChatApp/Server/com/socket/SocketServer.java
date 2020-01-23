package com.socket;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import javax.swing.JFrame;


class ServerThread extends Thread {

	/** This class is responsible for the actual instantiation of the server,
	 *  its configurations and user & message handling
	 */
	
	public SocketServer server = null;
	public static Socket socket = null;
	public int ID = -1;
	public String username = "";
	public ObjectInputStream streamIn = null;
	public ObjectOutputStream streamOut = null;
	public ServerFrame gui;
	

	public ServerThread(SocketServer _server, Socket _socket) {
		super();
		server = _server;
		socket = _socket;
		ID = socket.getPort();
		gui = _server.gui;	
			
	}

	public void send(Message msg) {
		try {
			streamOut.writeObject(msg);
			streamOut.flush();
		} catch (IOException ex) {
			System.out.println("Exception SocketClient : send(...)");
		}
	}

	public int getID() {
		return ID;
	}


	public void run() {
		gui.jTextArea1.append("\nServer Thread " + ID + " running.");
		while (true) {
			try {
				Message msg = (Message) streamIn.readObject();
				//TODO REMOVE COMMENT
				//System.out.println(msg);
				server.handle(ID, msg);
			} catch (Exception ioe) {
				System.out.println(ID + " ERROR reading: " + ioe.getMessage());
				server.remove(ID);
				stop();
			}
		}
	}

	public void open() throws IOException {
		streamOut = new ObjectOutputStream(socket.getOutputStream());
		streamOut.flush();
		streamIn = new ObjectInputStream(socket.getInputStream());
	}

	public void close() throws IOException {
		if (socket != null)
			socket.close();
		if (streamIn != null)
			streamIn.close();
		if (streamOut != null)
			streamOut.close();
	}
}

public class SocketServer implements Runnable {

	
	public ServerThread clients[];
	public ServerSocket server = null;
	public Thread thread = null;
	public int clientCount = 0, port = 8001;
	public ServerFrame gui;
	
	// Gets the current directory
    String currentDir = System.getProperty("user.dir");
	
	// LinkedList which handles the messages exchanged within the chat
	public static LinkedList messageList = new LinkedList();	
		
	public SocketServer(ServerFrame frame) {

		clients = new ServerThread[50];
		gui = frame;

		try {
			server = new ServerSocket(port);
			port = server.getLocalPort();
			gui.jTextArea1
					.append("IP : " + InetAddress.getLocalHost() + ", Port : " + server.getLocalPort());
			start();
				
		} catch (IOException ioe) {
			gui.jTextArea1.append("Can not bind to port : " + port + "\nRetrying");
			gui.RetryStart(0);
		}
	}

	public SocketServer(ServerFrame frame, int Port) {

		clients = new ServerThread[50];
		gui = frame;
		port = Port;

		try {
			server = new ServerSocket(port);
			port = server.getLocalPort();
			gui.jTextArea1
					.append("Server startet. IP : " + InetAddress.getLocalHost() + ", Port : " + server.getLocalPort());
			start();
		} catch (IOException ioe) {
			gui.jTextArea1.append("\nCan not bind to port " + port + ": " + ioe.getMessage());
		}
	}

	public void run() {
		while (thread != null) {
			try {
				gui.jTextArea1.append("\nServer running...");
				addThread(server.accept());
	
			} 
			catch (Exception ioe) {
				gui.jTextArea1.append("\nServer accept error: \n");
				gui.RetryStart(0);
			}
		}
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
				
				
		}
	}

	public void stop() {
		if (thread != null) {
			thread.stop();
			thread = null;
		}
	}

	private int findUser(int ID) {
		for (int i = 0; i < clientCount; i++) {
			if (clients[i].getID() == ID) {
				return i;
			}
		}
		return -1;
	}

	// Method which handles the directing of the messages
	public synchronized void handle(int ID, Message msg) {

		// If message is bye (intent to leave)
		if (msg.content.equals(".bye")) {
			Announce(msg.time, "signout", "Server", msg.sender);
			remove(ID);
		} else { // Checks for the "login" messages in which the user attempts to login
			if (msg.type.equals("login")) {
				if (findUserThread(msg.sender) == null) {
						clients[findUser(ID)].username = msg.sender;
						clients[findUser(ID)].send(new Message(msg.time, "login", "Server", "TRUE", msg.sender));
						Announce(msg.time, "newuser", "Server", msg.sender);
						SendUserList(msg.sender);

				} else {
					clients[findUser(ID)].send(new Message(msg.time, "login", "Server", "FALSE", msg.sender));
				}
				// If the message comes from an user, aka an actual text message
			} else if (msg.type.equals("message")) {
				
				// Adds the new message to the chat history
				try	{
				 	// Gets the chatHistory file
			        FileWriter fw = new FileWriter(currentDir+"/Messages.txt",true);
			        PrintWriter pw = new PrintWriter(fw);
			        pw.println(msg.toFile());
			      
			        // Closes the PrintWriter and FileWriter
			        pw.close();
			        fw.close();
				}
				catch (Exception ex){
					System.out.println("ERROR WHILE TRYING TO WRITE TO FILE "+ex.getMessage());
				}
				
				// Adds the message to the messageList
				messageList.add(msg);
				// TODO REMOVE THE FOLLOWING DEBUGGING LINES
				System.out.println("The Number of messages: "+messageList.size());
				//messageList.toString();

				/** If the message is meant for everyone, the server broadcasts it to all.
				 *  However, if that is not the case, it locates the user thread and sends the message
				 *  directly to him/her - jvws & fo
				*/
				if (msg.recipient.equals("All")) {
					Announce(msg.time, "message", msg.sender, msg.content);
				} else {
					findUserThread(msg.recipient).send(new Message(msg.time, msg.type, msg.sender, msg.content, msg.recipient));
					clients[findUser(ID)].send(new Message(msg.time, msg.type, msg.sender, msg.content, msg.recipient));
				}
			} else if (msg.type.equals("test")) {
				clients[findUser(ID)].send(new Message(msg.time, "test", "Server", "Ok", msg.sender));
			} 
		}
	}

	public void Announce(String time, String type, String sender, String content) {
		Message msg = new Message(time, type, sender, content, "All");
		for (int i = 0; i < clientCount; i++) {
			clients[i].send(msg);
		}
	}

	public void SendUserList(String toWhom) {
		for (int i = 0; i < clientCount; i++) {
			findUserThread(toWhom).send(new Message(" ", "newuser", "Server", clients[i].username, toWhom));
		}
	}

	public ServerThread findUserThread(String usr) {
		for (int i = 0; i < clientCount; i++) {
			if (clients[i].username.equals(usr)) {
				return clients[i];
			}
		}
		return null;
	}

	public synchronized void remove(int ID) {
		int pos = findUser(ID);
		if (pos >= 0) {
			ServerThread toTerminate = clients[pos];
			gui.jTextArea1.append("\nRemoving client thread " + ID + " at " + pos);
			if (pos < clientCount - 1) {
				for (int i = pos + 1; i < clientCount; i++) {
					clients[i - 1] = clients[i];
				}
			}
			clientCount--;
			try {
				toTerminate.close();
			} catch (IOException ioe) {
				gui.jTextArea1.append("\nError closing thread: " + ioe);
			}
			toTerminate.stop();
		}
	}

	private void addThread(Socket socket) {
		if (clientCount < clients.length) {
			gui.jTextArea1.append("\nClient connected: " + socket);
			clients[clientCount] = new ServerThread(this, socket);
			try {
				clients[clientCount].open();
				clients[clientCount].start();
				clientCount++;
			} catch (IOException ioe) {
				gui.jTextArea1.append("\nError opening thread: " + ioe);
			}
		} else {
			gui.jTextArea1.append("\nClient disconnected: maximum " + clients.length + " reached.");
		}
	}
}
