import java.io.*;
import java.net.*;

public class Server {
    private ServerSocket serverSocket = null;
    private int port = 8080;
    private int clients = 1;
    private int maxClients = 4;
    
    public Server() {
    	
    	try {
			serverSocket = new ServerSocket(port);
			System.out.println("Server started...");
			
			while (clients <= maxClients) {
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
			
			BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			//PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
			//use above printwriter when you want to send stuff to client. 
			
			String clientName = input.readLine();
			if (clientName == null || clientName.isEmpty()) {
                clientName = "Unknown";
            }
			
            System.out.printf("Client %d connected as [%s]\n", clientID, clientName);
            
        	String guess = input.readLine();
			if (guess != null) {
				System.out.printf("[%s] guessed: %s\n", clientName, guess);
				
			}
		
			  idle(input); // this basically just keeps connection open until the client disconnects different form idle on client side.
			  
			  input.close();
	          clientSocket.close();
	          System.out.printf("%s (Client %d) disconnected.\n", clientName, clientID);
			
		} catch (IOException e) {
			
			System.out.println(e);
		}
		
	}
	
	public int getMaxClients() {
		return this.maxClients;
	}
	
	public void setMaxClients(int maxClients) {
		this.maxClients = maxClients;
	}
	
	public void idle(BufferedReader input) throws IOException {
		while (input.readLine() != null) {
        }
	  
	}
	
	 public static void main(String[] args) {
	        new Server();
	    }

}