package com.socket;

import java.io.*;
import java.net.*;

import gui.ChatFrame;

public class SocketClient implements Runnable{
    
    public int port;
    public String serverAddr;
    public Socket socket;
    public ChatFrame gui;
    public ObjectInputStream In;
    public ObjectOutputStream Out;
    
    public SocketClient(ChatFrame frame) throws IOException{
        gui = frame; 
        this.serverAddr = gui.serverAddr; 
        this.port = gui.port;
        socket = new Socket(InetAddress.getByName(serverAddr), port);
        Out = new ObjectOutputStream(socket.getOutputStream());
        Out.flush();
        In = new ObjectInputStream(socket.getInputStream());
        
    }

    @Override
    public void run() {
        boolean keepRunning = true;
        while(keepRunning){
            try {
                Message msg = (Message) In.readObject();
                //System.out.println(msg.toString());
         
                if(msg.type.equals("message")){
                    if(msg.recipient.equals(gui.username)){
                        gui.jTextArea1.append("\n"+msg.sender +": " + msg.content + "\n");
                    	//gui.jTextArea1.setText(SocketServer.messageList.getPrivateMessages(msg.sender, msg.recipient));
                    }
                    else{
                        gui.jTextArea1.append("\n"+msg.sender +": " + msg.content + "\n");
                    }
                                            
                }
                else if(msg.type.equals("login")){
                    if(msg.content.equals("TRUE")){
                        gui.btnLogin.setEnabled(false);
                        gui.tfPort.setEnabled(false);
                        gui.btnSend.setEnabled(true);
                        gui.tfTypemessage.setEditable(true); 
                        gui.tfUsername.setEnabled(false);
                        gui.pfPassword.setEnabled(false);
                        gui.jTextArea1.setEditable(false);
                        gui.jTextArea1.append("Server: Login Successful\n");
                    }
                    else{
                        gui.jTextArea1.append("Server: Login Failed\n");
                    }
                }
                else if(msg.type.equals("test")){
                    gui.btnConnect.setEnabled(false);
                    gui.btnLogin.setEnabled(true);
                    gui.tfUsername.setEnabled(true); 
                    gui.pfPassword.setEnabled(true);
                    gui.tfTypemessage.setEditable(false);
                    gui.btnSend.setEnabled(false);
                    gui.jTextArea1.setEditable(false);
                }
                else if(msg.type.equals("newuser")){
                    if(!msg.content.equals(gui.username)){
                        boolean exists = false;
                        for(int i = 0; i < gui.model.getSize(); i++){
                            if(gui.model.getElementAt(i).equals(msg.content)){
                                exists = true; 
                                break;
                            }
                        }
                        if(!exists){ gui.model.addElement(msg.content); }
                    }
                }

                else{
                    gui.jTextArea1.append("Server: Unknown message type\n");
                }
            }
            catch(Exception ex) {
                keepRunning = false;
                gui.jTextArea1.append("TeamChatApp: Connection Failure\n");
                gui.btnConnect.setEnabled(true); 
                gui.tfUsername.setEditable(true);
                gui.btnSend.setEnabled(false);
                
                for(int i = 1; i < gui.model.size(); i++){
                    gui.model.removeElementAt(i);
                }
                
                
                System.out.println("Exception SocketClient run()");
                ex.printStackTrace();
            }
        }
    }
    
    public void send(Message msg){
        try {
        	Out.writeObject(msg);
            Out.flush();
                        
        } 
        catch (IOException ex) {
            System.out.println("Exception SocketClient send()");
        }
    }
    
    public void closeThread(Thread t){
        t = null;
    }
}
