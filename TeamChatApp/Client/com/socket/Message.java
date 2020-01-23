package com.socket;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable{
    
    private static final long serialVersionUID = 1L;
    public String time, type, sender, content, recipient;
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    
    public Message(String time, String type, String sender, String content, String recipient){
    	Date date = new Date(); 
    	time = this.formatter.format(date);
        this.time = time; this.type = type; this.sender = sender; this.content = content; this.recipient = recipient;
    }
    
    @Override
    public String toString(){
        return "{time='"+time+"', sender='"+sender+"', content='"+content+"', recipient='"+recipient+"'}";
    }
    
    public String getPrivateMessages()	{
    	return sender +": " + content + "\n";
    }
    
    public String toFile()	{
    	return time+"\t"+type+"\t"+sender+"\t"+content+"\t"+recipient;
    }
}
