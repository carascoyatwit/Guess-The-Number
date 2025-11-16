package app.network;

import app.AppState;
import app.UserConfigs;
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
                if (!(obj instanceof String)) continue;
                String msg = (String) obj;

                // split into at most 3 parts (so name can contain '|')
                if (msg.startsWith("connected|")) {
                    String[] parts = msg.split("\\|", 3);
                    if (parts.length >= 3) {
                        int id = Integer.parseInt(parts[1]);
                        String name = parts[2];
                        javafx.application.Platform.runLater(() -> {
                            if (AppState.scene2Controller != null) {
                                AppState.scene2Controller.updateConnected(id, name);
                            }
                        });
                    }
                } else if (msg.startsWith("disconnected|")) {
                    String[] parts = msg.split("\\|", 2);
                    if (parts.length >= 2) {
                        int id = Integer.parseInt(parts[1]);
                        javafx.application.Platform.runLater(() -> {
                            if (AppState.scene2Controller != null) {
                                AppState.scene2Controller.updateDisconnected(id);
                            }
                        });
                    }
                } else {
                    // handle other messages
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

