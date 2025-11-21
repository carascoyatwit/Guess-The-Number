package app.network;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import app.*;
import app.packets.*;

public class Server {
    private ServerSocket serverSocket = null;
    private ArrayList<HandleClient> clients = new ArrayList<>();
    private List<PlayerData> players = Collections.synchronizedList(new ArrayList<>());
    private int max;
    private final List<String> guesses = Collections.synchronizedList(new ArrayList<>());
    private int targetNumber;
    
    public Server() {
    	
    	this.max = UserConfigs.getSetClients();
    	generateNewTarget();
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

               
                while (guesses.size() <= id) {
                    guesses.add(null);
                }

                
                guesses.set(id, guess);

               
                broadcastToAll(guessedPacket);

                
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
             clients.remove(this);
             
             
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

    ArrayList<Integer> winners = new ArrayList<>();

    for (int i = 0; i < guesses.size(); i++) {
        if (guesses.get(i) != null && Integer.parseInt(guesses.get(i)) == targetNumber) {
            winners.add(i);
        }
    }

    if (!winners.isEmpty()) {
        System.out.println("Winner is player " + winners);
        broadcastToAll(new PlayerWin(true, winners, String.valueOf(targetNumber))); // send all winners;
    } else {
    	 System.out.println("No player guessed correctly.");
         broadcastToAll(new PlayerWin(false, new ArrayList<>(), String.valueOf(targetNumber)));
         broadcastToAll(new NextRound(String.valueOf(targetNumber)));
    }
    
    for (int i = 0; i < guesses.size(); i++) {
        guesses.set(i, null);
    }
    
    generateNewTarget();
}

private void generateNewTarget() {
    targetNumber = 1 + (int)(Math.random() * 20);
    System.out.println("New target number: " + targetNumber);
}

    	
private void broadcastToAll(Message message) {
	synchronized (clients) {
        Iterator<HandleClient> iterator = clients.iterator();
        while (iterator.hasNext()) {
            HandleClient c = iterator.next();

            if (c.socket == null || c.socket.isClosed() || !c.socket.isConnected()) {
                iterator.remove();
                continue;
            }

            try {
                if (c.output != null) {
                    c.output.writeObject(message);
                    c.output.flush();
                }
            } catch (IOException e) {
                System.out.printf("Client %d disconnected during broadcast.\n", c.clientID);
                iterator.remove();
            }
        }
    }
}
	
	 public static void main(String[] args) {
		 new Server();
	    }

}
    