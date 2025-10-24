
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
	    
	    try {
	    
	    Socket socket = new Socket(serverName, port);
	    
	    PrintWriter send = new PrintWriter(socket.getOutputStream(), true);
	    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    
	    send.println(clientName);
	   
	    
	    while(true) {
	    	System.out.println("Enther your guess: ");	
	    	String guess = scan.nextLine();
	    	
	    	if (guess.equals("exit")) {
				System.out.println("Disconnected.");
				break;
	    	}
	    
	    send.println(guess);
	    
	   String recieved = input.readLine();
	   System.out.printf("The server replied: %s\n", recieved);
	   
	    }
	   
	   scan.close();
	   socket.close();
	    
	    }catch (IOException e) {
	    	System.out.println(e);
	    }
	    
	}
	
	 public static void main(String[] args) {
	        new Client();
	    }

}
