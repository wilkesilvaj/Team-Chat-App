package com.socket;

import java.io.File;
import java.util.Scanner;

public class LinkedList {

	protected class Node {
		Message message;
		Node next;
		
		/** Constructor #1 - With an index:
		 * msg - holds the instance of the object which contains data
		 * n - the reference to the successor(following) node. 
		 */
		Node(Message msg, Node n)	{
			message = msg;
			next = n;
			}
		
		/** Constructor #2 - Without an index, new data goes to the end
		 * @param msg - holds the object to store in the node 
		 */
		Node(Message msg)	{
			// Calls the first ("main") instructor with a null index
			this(msg, null);
		}
	}// End of the Node class
	
	protected Node first; // Pointer to the header of the list
	protected Node last; // Pointer to the last element of the list

	/** LinkedList Constructor
	*/
	public LinkedList()	{
		first = null;
		last = null;
		
		/** Checks if there's already a chat history from our default file and,
		 * if so, generates the messageList based on the chat history 
		 */
		try	{		
			// Gets the current directory
		    String currentDir = System.getProperty("user.dir");
			// Checks if there's already a chat history
			File messageHistory = new File(currentDir+"/Messages.txt");
			if (messageHistory.exists()) {
				Scanner oldMessages = new Scanner(messageHistory);
				// Iterates through the old messages to add them to the file
				while (oldMessages.hasNext())	{
					String[] data = oldMessages.nextLine().split("\t");
					//System.out.println("Msg found! "+data[0]+data[1]+data[2]+data[3]);
					Message msg = new Message(data[0],data[1],data[2],data[3],data[4]);
					add(msg);
					
				}
				System.out.println("Number of messages in the chat history:"+size());
			}			
		}
		catch (Exception ex)	{
			
		}
		
		
	}
	
	//Method which verifies if the list is empty
	
	public Boolean isEmpty() {
		return first == null;
	}
	
	// Method which gets the size of the list 
	public int size() {
		int count = 0; // Sets the counter to 0
		Node node = first; // Get the first item of the list
		while (node != null) { // If the current item exists, move to the following one and increment count
			count++;
			node = node.next;
		}
		return count;
	}
	
	/** The add method adds an element to the end of the list
	 * @param msg is an instance of Message
	 * and it is added to the last position of the list
	 */
	public void add(Message msg) {
		
		// Verifies if the list is currently empty
		if (isEmpty()) {
			first = new Node(msg);
			last = first;
		}
		else	{ // If there is data inside the list
			last.next = new Node(msg); // Creates the new node inside the reference on the current node
			last = last.next; // Assigns the last node to the following one (in comparison to the current, aka "last")
		}
	}
	
	/** The add method adds an element to the end of the list
	 * @param msg is an instance of Employee
	 * @param index  is the desired position for this Message
	 * on the list
	 */
	
	public void add(int index, Message msg)	{
		
		// Verifies if the index is within acceptable bounds
		if (index < 0 || index > size())	{
			String message = String.valueOf(index);
			throw new IndexOutOfBoundsException(message);			
		}
		
		// If the index is 0
		if (index == 0)	 {
			// The new Employee is added to the beginning
			first = new Node(msg, first);
			if (last == null) // If the list only contains this new Employee 
				last = first; // Assigns the pointer to last to the only current employee
			return;
		}
		
		/**
		 * Set a reference pred to point to the node that
		 * will be the predecessor of the new node
		 * The following codes will "re-arrange" the pointer as to ensure the list's structure
		 */
		Node pred= first;
		for (int i=0; i <= index-1;i++)	 {
			pred = pred.next;
		}
		
		// Connects the newly created element to its predecessor on the list
		pred.next = new Node(msg, pred.next);
		
		if (pred.next.next == null)
			last =pred.next; 
	}
	
	/* This must be further developed */
	public String toString()	{
		String myList = "";
		Node n = first;
		
		while (n != null)	{
			System.out.println("Time" + n.message.time + " Sender:" + n.message.sender + " Content:" + n.message.content + " Recipient:" + n.message.recipient);
			n = n.next;
		}
		return myList.toString();
	}
	
	/** Method which filters the messages displayed based on the messageList and the members of that private chat
	 * @param sender: current user
	 * @param recipient: chosen recipient
	 * @return string to be displayed as the chat history between those 2 people
	 */
	public String getPrivateMessages(String sender, String recipient)	{
		String conversation = "";
		Node n = first;
		
		int countMessage =0;
		while (n != null)	{
			/** Filters the message by checking if they were either:
			 *  sent from the current user to the chosen recipient or
			 *  from the chosen recipient to the current user 
			 */
			if (((n.message.sender.equals(sender) && n.message.recipient.equals(recipient)) ||
				 (n.message.sender.equals(recipient) && n.message.recipient.equals(sender))))	{
				countMessage ++;
				conversation += n.message.getPrivateMessages();
			}
			n = n.next;
		
		}		
		//conversation+= "\nMessages found: "+countMessage;
		return conversation;
	}
	
	public String getMessage(int index)	{
		int count=0;
		Node n = first;
		while (n!= null){
			count++;
			n = n.next;
			if (count == index)
				break;
		}
		return n.message.time+"\t"+n.message.sender+"\t"+
		n.message.content+"\t"+n.message.recipient;
		
	}
	
	
}
