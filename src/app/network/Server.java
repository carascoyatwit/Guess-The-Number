package app.network;
import java.io.*;
import java.net.*;

import app.Scene2;

public class Server {
    private ServerSocket serverSocket = null;
    private int port = 8081;
    public static int clients;
    public static int setClients = 4;
    
    public Server() {
    	
    	try {
			serverSocket = new ServerSocket(port);
			System.out.println("Server started...");
			
			while (clients < setClients) {
			Socket clientSocket = serverSocket.accept();
		
			int clientID = clients;
			Scene2.updateConnected(clientID);
			clients++;
			
			new Thread(() -> handleClient(clientSocket, clientID)).start();
			
			}
			
			serverSocket.close();
			
		} catch (IOException e) {
			System.out.println(e);
		}
    
    
    
    }
	
	private void handleClient(Socket clientSocket, int clientID) {
		
		try {
			
			BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			//PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
			//use above printwriter when you want to send stuff to client. 
			
			String clientName = input.readLine();
			if (clientName == null || clientName.isEmpty()) {
                clientName = "Unknown";
            }
			
            System.out.printf("%s Has Connected...\n", clientName);
            
        	String guess = input.readLine();
			if (guess != null) {
				System.out.printf("[%s] guessed: %s\n", clientName, guess);
				
			}
		
			  idle(input); // this basically just keeps connection open until the client disconnects different form idle on client side.
			  
			  input.close();
	          clientSocket.close();
	          System.out.printf("%s Has Disconnected.\n", clientName);
	          Scene2.updateDisconnected(clientID);
			
		} catch (IOException e) {
			
			System.out.println(e);
		}
		
	}
	
	public static int getClients() {
		return setClients;
	}
	
	public static void setClients(int maxClients) {
		setClients = maxClients;
	}
	
	
	
	
	public void idle(BufferedReader input) throws IOException {
		while (input.readLine() != null) {
        }
	  
	}
	
	 public static void main(String[] args) {
		 new Server();
	    }

}