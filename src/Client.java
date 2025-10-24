
import java.util.Scanner;
import java.io.*;
import java.net.*;

public class Client {
	
	private String clientName;
	private String serverName = "localhost";
	private int port = 8080;
	private int status;  // 0 is host and 1 is join for now
	

	public Client() {
		
	    Scanner scan = new Scanner(System.in);
	    
	    System.out.println("Please enter your name: ");
	    clientName = scan.nextLine();
	    
	    System.out.println("Would you like to create a game or join a game: ");
	    status = scan.nextInt();
	    scan.nextLine();
	    
	    if(status == 0) {
	    	new Thread(() -> startServer()).start();
	    }
	    
	    try {
	    
	    Socket socket = new Socket(serverName, port);
	    
	    PrintWriter send = new PrintWriter(socket.getOutputStream(), true);
	    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    
	    send.println(clientName);
	   
	    
	    while(true) {
	    	System.out.println("Enter your guess: ");	
	    	String guess = scan.nextLine();
	    	
	    	if (guess.equals("exit")) {
				System.out.println("Disconnected.");
				break;
	    	}
	    
	   send.println(guess);
	    }
	   
	   scan.close();
	   socket.close();
	    
	    }catch (IOException e) {
	    	System.out.println(e);
	    }
	    
	}
	
	public void startServer() {
		Server server = new Server();
	}
	
	 public static void main(String[] args) {
	        new Client();
	    }

}
