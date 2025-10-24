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
			
			
			BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // update this when using java client, temp for nectat testing
			PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
			
			String clientName = input.readLine();
			
			if (clientName == null || clientName.isEmpty()) {
                clientName = "Unknown";
            }
            System.out.printf("Client %d connected as [%s]\n", clientID, clientName);
            
        	String guess = "";
			while ((guess = input.readLine()) != null) {
				System.out.printf("[%s] guessed: %s\n", clientName, guess);
			}
			
			input.close();
			clientSocket.close();
			 System.out.printf("%s (Client %d) disconnected.\n", clientName, clientID);
			
		} catch (IOException e) {
			System.out.println(e);
		}
		
	}
	
	 public static void main(String[] args) {
	        new Server();
	    }

}