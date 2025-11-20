package app.network;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import app.*;
import app.packets.*;

public class Server {
    private ServerSocket serverSocket = null;
    private ArrayList<HandleClient> clients = new ArrayList<>();
    private List<PlayerData> players = Collections.synchronizedList(new ArrayList<>());
    private int max;
    private final List<String> guesses = Collections.synchronizedList(new ArrayList<>());
    
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
        
        System.out.printf("%s has connected (id = %d)\n", name, clientID);
        UserConfigs.incrementClients();
        
        output.writeObject(new MaxPlayers(max));
        output.flush();
        
        for (int i = 0; i < players.size(); i++) {
            PlayerData p = players.get(i);
            output.writeObject(new PlayerConnected(i, p.getName()));
        }
        output.flush();
        broadcastToAll(new PlayerConnected(clientID, name));
        
        while (true) {
            Object obj = input.readObject();
            
            if (obj instanceof String) {
                String msg = (String) obj;
                if (msg.equals("start-game")) {
                	broadcastToAll(new GameStatus(true));
                    System.out.println("Host started game!");
                }
                
                //can handle any other messages that is a string
                
            } else if (obj instanceof PlayerGuessed) { 
                PlayerGuessed guessedPacket = (PlayerGuessed) obj;
                int id = guessedPacket.getClientID();
                String guess = guessedPacket.getGuess();

                System.out.printf("Client %d (%s) guessed: %s\n", id, name, guess);

                // Ensure guess list matches number of players
                while (guesses.size() <= id) {
                    guesses.add(null);
                }

                // Store the guess
                guesses.set(id, guess);

                // Broadcast guess update to all clients (UI update)
                broadcastToAll(guessedPacket);

                // Check if ALL players have guessed
                boolean allGuessed = true;
                for (int i = 0; i < players.size(); i++) {
                    if (i >= guesses.size() || guesses.get(i) == null) {
                        allGuessed = false;
                        break;
                    }
                }

                if (allGuessed) {
                    new Thread(() -> {
                        try {
                            Thread.sleep(1500); 
                        } catch (InterruptedException ignored) {}

                        revealWinners(); 
                    }).start();
                }

            }
        }
        
		}catch (IOException | ClassNotFoundException e) {
			
			//this is where you will go if the connection breaks.
			
			System.out.printf("Client %d has disconnected.\n", clientID);
             players.removeIf(p -> p.getName().equals(name));
             UserConfigs.decrementClients();
             broadcastToAll(new PlayerDisconnect(clientID));
             
             
		}finally {
			try {
                if (input != null) input.close();
                if (output != null) output.close(); // this is cloasing the streams.
                if (socket != null) socket.close();
            } catch (IOException ignored) { } // who cares really.
		}
	}
}

private void revealWinners() {
    String target = "50"; 

    Integer winningID = null;

    for (int i = 0; i < guesses.size(); i++) {
        if (target.equals(guesses.get(i))) {
            winningID = i;
            break;
        }
    }

    if (winningID != null) {
        System.out.println("Winner is player " + winningID);
        broadcastToAll(new PlayerWin(true, winningID, target));
    } else {
        System.out.println("No player guessed correctly.");
        broadcastToAll(new PlayerWin(false, -1, target));
        broadcastToAll(new NextRound(target));
    }
    
    for (int i = 0; i < guesses.size(); i++) {
        guesses.set(i, null);
    }
}
    	
private void broadcastToAll(Message message) {
    for (HandleClient c : clients) {
        try {
            if (c.output != null) {
                c.output.writeObject(message);
                c.output.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
	
	 public static void main(String[] args) {
		 new Server();
	    }

}
    