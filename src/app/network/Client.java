package app.network;

import java.util.Scanner;
import java.io.*;
import java.net.*;


public class Client{
	
	public static  String clientName = "defualt";
	private String serverName = "localhost";
	private int port = 8080;
	public static boolean status = true;  // true is for host false is for join
	
	public Client() {
		
	    Scanner scan = new Scanner(System.in);
	    
//	    System.out.println("Please enter your name: ");
//	    clientName = scan.nextLine();
//	    
//	    System.out.println("Would you like to create a game or join a game: "); // ignore this and just hit 1 when it prompts
//	    status = scan.nextInt();
	    
	    
	    
	    if(status) { // ignore this for now, and just type in 1 when it prompts.
	    	new Thread(() -> startServer()).start();
	    }
	    
	    try {
	    
	    Socket socket = new Socket(serverName, port);
	    PrintWriter send = new PrintWriter(socket.getOutputStream(), true);
	    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    
	    
	    send.println(clientName);
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
	
	public void startServer() {
		Server server = new Server();
	}
	
	 public static void setClientName(String name) {
	        clientName = name;
	    }

	    public static void setStatus(boolean status) {
	        status = status;
	    }
	
	 public static void main(String[] args) {
	        new Client();
	    }
	 
}
