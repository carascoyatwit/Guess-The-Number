import java.io.*;
import java.net.*;

public class Server {

	private Socket socket = null;
    private ServerSocket serverSocket = null;
    private BufferedReader input = null;
    private int port = 8080;
    private int clients = 1;
    
    public Server() {
    	
    	try {
			serverSocket = new ServerSocket(port);
			System.out.println("Server started...");
			while (clients <= 5) {
			handleClient(serverSocket);
			clients++;
			}
			
			serverSocket.close();
			
		} catch (IOException e) {
			System.out.println(e);
		}
    
    
    
    }
	
	private void handleClient(ServerSocket serverSocket) {
		
		try {
			socket = serverSocket.accept();
			System.out.println("Client accepted...");
			System.out.printf("Amount of clients: %d\n", clients);
			
			String guess = "";
			
			input = new BufferedReader(new InputStreamReader(socket.getInputStream())); // update this when using java client, temp for nectat testing
		
			guess = input.readLine();
			System.out.printf("The client Guessed: %s\n", guess);
			
			
			input.close();
			socket.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		
	}

}
