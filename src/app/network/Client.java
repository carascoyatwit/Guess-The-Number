package app.network;

import app.AppState;
import app.UserConfigs;
import app.packets.MaxPlayers;
import app.packets.PlayerConnected;
import app.packets.PlayerDisconnect;
import javafx.application.Platform;

import java.io.*;
import java.net.*;

public class Client implements Runnable {

    private ObjectOutputStream output;
    private ObjectInputStream input;

    public Client() { }

    @Override
    public void run() {
        try (Socket socket = new Socket(UserConfigs.getServerHost(), UserConfigs.getServerPort())) {
            System.out.println("Connected to server at " + UserConfigs.getServerHost() + ":" + UserConfigs.getServerPort());

            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            // send our name to server
            output.writeObject(UserConfigs.getClientName());
            output.flush();

            // listen loop
            Object obj;
            while ((obj = input.readObject()) != null) {
                if (obj instanceof MaxPlayers) {
                MaxPlayers msg = (MaxPlayers) obj;
                    int maxPlayers = msg.getMax();
                    Platform.runLater(() -> {
                        if (AppState.scene2Controller != null) {
                            AppState.scene2Controller.setupLabels(maxPlayers);
                        }
                    });
                
                } else if (obj instanceof PlayerConnected) {
                    PlayerConnected msg = (PlayerConnected) obj;
                    int id = msg.getClientID();
                    String name = msg.getName();
                        Platform.runLater(() -> {
                            if (AppState.scene2Controller != null) {
                                AppState.scene2Controller.updateConnected(id, name);
                            }
                        });
                
                } else if (obj instanceof PlayerDisconnect) {
                		PlayerDisconnect msg = (PlayerDisconnect) obj;
                        int id = msg.getClientID();
                        javafx.application.Platform.runLater(() -> {
                            if (AppState.scene2Controller != null) {
                                AppState.scene2Controller.updateDisconnected(id);
                            }
                        });
                    
                } else if(obj instanceof String) {
                    // handle other messages
                	String msg = (String) obj;
                	if(msg.equals("start-game")) {
                		Platform.runLater(() -> {
                            System.out.println("Game started!");
                            // AppState.gameStarted = true; // Update global state
                            // AppState.scene2Controller.displayGameStarted();
                        });
                	}
                } else {
                	System.out.println("Client Has recieved an unknown object." + obj.getClass().getName());
                }
            }

        } catch (Exception e) {
            System.out.println("Client error: " + e);
        }
    }

    public static void main(String[] args) {
        new Thread(new Client()).start();
    }
}


