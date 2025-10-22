import java.io.*;
import java.net.*;

public class Server {
    private ServerSocket serverSocket = null;
    private int port = 8080;
    private int clients = 1;
    
    public Server() {
    	
    	try {
			serverSocket = new ServerSocket(port);
			System.out.println("Server started...");
			
			while (clients <= 5) {
			Socket clientSocket = serverSocket.accept();
			System.out.println("Client accepted...");
			System.out.printf("Amount of clients: %d\n", clients);
		
			int clientID = clients; 
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
			
			String guess = "";
			
			BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // update this when using java client, temp for nectat testing
			
			while ((guess = input.readLine()) != null) {
				System.out.printf("Client %d guessed: %s\n", clientID, guess);
			}
			
			input.close();
			clientSocket.close();
			System.out.printf("Client %d disconnected.\n", clientID);
			
		} catch (IOException e) {
			System.out.println(e);
		}
		
	}

}
