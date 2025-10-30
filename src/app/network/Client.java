package app.network;

import java.util.Scanner;

import app.*;
import java.io.*;
import java.net.*;


public class Client{
	
	public Client() {
		
	    Scanner scan = new Scanner(System.in);
	        
	    try {
	    
	    Socket socket = new Socket(UserConfigs.getServerHost(), UserConfigs.getServerPort());
	    PrintWriter send = new PrintWriter(socket.getOutputStream(), true);
	    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    
	    
	    send.println(UserConfigs.getClientName());
	    System.out.println("Enter your guess: ");	
	    String guess = scan.nextLine();
	    send.println(guess);
	   
	   idle(); // this is kind of like a just hold on a second method for editing and testing purposes, whenever you reach a point where you want it to "stop" use this method
	   
	   scan.close();
	   socket.close();
	   send.close();
	   input.close();
	   
	    }catch (IOException e) {
	    	System.out.println(e);
	    }
	    
	}
	
	public void idle() {
		 while (true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		 }

	}
	
	 public static void main(String[] args) {
	        new Client();
	    }
	 
}
