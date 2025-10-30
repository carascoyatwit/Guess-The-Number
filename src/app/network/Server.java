package app.network;
import java.io.*;
import java.net.*;
import app.*;

import app.Scene2;

public class Server {
    private ServerSocket serverSocket = null;

    
    public Server() {
    	
    	try {
			serverSocket = new ServerSocket(UserConfigs.getServerPort());
			System.out.println("Server started...");
			
			while (UserConfigs.getClients() < UserConfigs.getSetClients()) {
			Socket clientSocket = serverSocket.accept();
		
			int clientID = UserConfigs.getClients();
			Scene2.updateConnected(clientID);
			UserConfigs.incrementClients();
			
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
	
	
	public void idle(BufferedReader input) throws IOException {
		while (input.readLine() != null) {
        }
	  
	}
	
	 public static void main(String[] args) {
		 new Server();
	    }

}