package app.network;

import app.*;
import java.io.*;
import java.net.*;
import java.util.List;


public class Client{
	
	 private ObjectOutputStream output;
	 private ObjectInputStream input;
	
	public Client() {
		
	    try {
	    
	    Socket socket = new Socket(UserConfigs.getServerHost(), UserConfigs.getServerPort());
	    System.out.println("Connected to server at " + UserConfigs.getServerHost() + ":" + UserConfigs.getServerPort());
	    
	    output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());

        output.writeObject(UserConfigs.getClientName());
        output.flush();
        
        
	   
	    }catch (IOException e) {
	    	System.out.println(e);
	    }
	    
	}
	
	
	
	 public static void main(String[] args) {
	        new Client();
	    }
	 
}
