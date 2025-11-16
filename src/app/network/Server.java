package app.network;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.*;

public class Server {
    private ServerSocket serverSocket = null;
    private ArrayList<HandleClient> clients = new ArrayList<>();
    private List<PlayerData> players = Collections.synchronizedList(new ArrayList<>());
    private int max;
    
    public Server() {
    	
    	this.max = UserConfigs.getSetClients();
    	
    	try {
			serverSocket = new ServerSocket(UserConfigs.getServerPort());
			System.out.println("Server started on port " + UserConfigs.getServerPort());
			
			while (players.size() < max) {
				
			Socket clientSocket = serverSocket.accept();
			HandleClient client = new HandleClient(clientSocket, players.size());
			clients.add(client);
			new Thread(client).start();
			}
			
	System.out.println("Max clients connected. Server will no longer accept new connections.");
			
		} catch (IOException e) {
			System.out.println(e);
		}
   
    
    }
    

private  class HandleClient implements Runnable{
	
	private Socket socket;
    private int clientID;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String name;

	public HandleClient(Socket socket, int id) {
		 this.socket = socket;
         this.clientID = id;
	}
	
	public void run() {
		
		try {
		output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());

        // Receive player name from client
        name = (String) input.readObject();
        if (name == null || name.isEmpty()) {
            name = "Unknown";
        }
        PlayerData player = new PlayerData(name);
        players.add(player);
        
        System.out.printf("%s has connected (id=%d)\n", name, clientID);
        UserConfigs.incrementClients();
        Scene2.updateConnected(clientID);
        
        while (true) {
            Object obj = input.readObject();
            if (obj instanceof String) {
                String msg = (String) obj;
                if (msg.equals("start-game")) {
                    System.out.println("Host started game!");
                    broadcastStart();
                }
            }
        }
        
		}catch (IOException | ClassNotFoundException e) {
			
			System.out.printf("Client %d has disconnected.\n", clientID);
			 Scene2.updateDisconnected(clientID);
             players.removeIf(p -> p.getName().equals(name));
             UserConfigs.decrementClients();
            
		}finally {
			try {
                if (input != null) input.close();
                if (output != null) output.close();
                if (socket != null) socket.close();
            } catch (IOException ignored) { }
		}
	}
}
    	
private void broadcastStart() {
    for (HandleClient c : clients) {
        try {
            c.output.writeObject("start-game");
            c.output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


	
	
	 public static void main(String[] args) {
		 new Server();
	    }

}
    