package app.network;

import app.AppState;
import app.UserConfigs;
import app.Scene2; 
import app.Scene3; 
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
            Object receivedRawObject; // Renamed to avoid confusion with the final variable below
            while ((receivedRawObject = input.readObject()) != null) {
                // --- FIX FOR "EFFECTIVELY FINAL" ERROR ---
                // Create a new effectively final variable for each iteration
                final Object finalReceivedObject = receivedRawObject; // This is the crucial line!
                // --- END FIX ---

                // *** IMPORTANT: All UI updates must be inside Platform.runLater() ***
                Platform.runLater(() -> {
                    // 1. Check if there's any active waiting room controller registered in AppState
                    if (AppState.waitingRoomController == null) {
                        System.err.println("AppState.waitingRoomController is null. Cannot update UI.");
                        return; // No active scene to update, exit.
                    }

                    // 2. Determine which specific type of controller is currently active
                    //    and then call a helper method to handle the message for that controller.
                    if (AppState.waitingRoomController instanceof Scene2) {
                        Scene2 controller = (Scene2) AppState.waitingRoomController;
                        handleMessageForController(controller, finalReceivedObject); // Use the final variable
                    } else if (AppState.waitingRoomController instanceof Scene3) {
                        Scene3 controller = (Scene3) AppState.waitingRoomController;
                        handleMessageForController(controller, finalReceivedObject); // Use the final variable
                    } else {
                        System.err.println("AppState.waitingRoomController holds an unknown type: " + AppState.waitingRoomController.getClass().getName());
                    }
                }); // End of Platform.runLater block
            }

        } catch (Exception e) {
            System.out.println("Client error: " + e);
            // Optionally add more specific error handling or UI feedback for connection loss.
        }
    }

    // --- NEW HELPER METHOD ---
    // This method takes the specific controller (Scene2 or Scene3) and the received object (packet)
    // It then checks the type of the received object and calls the appropriate update method on the controller.
    private void handleMessageForController(Object controllerInstance, Object receivedPacket) {
        // --- Packet Type Checks (Your existing logic, adapted for the controllerInstance) ---
        if (receivedPacket instanceof MaxPlayers) {
            MaxPlayers msg = (MaxPlayers) receivedPacket;
            int maxPlayers = msg.getMax();
            if (controllerInstance instanceof Scene2) {
                ((Scene2) controllerInstance).setupLabels(maxPlayers);
            } else if (controllerInstance instanceof Scene3) {
                ((Scene3) controllerInstance).setupLabels(maxPlayers);
            }

        } else if (receivedPacket instanceof PlayerConnected) {
            PlayerConnected msg = (PlayerConnected) receivedPacket;
            int id = msg.getClientID();
            String name = msg.getName();
            if (controllerInstance instanceof Scene2) {
                ((Scene2) controllerInstance).updateConnected(id, name);
            } else if (controllerInstance instanceof Scene3) {
                ((Scene3) controllerInstance).updateConnected(id, name);
            }

        } else if (receivedPacket instanceof PlayerDisconnect) {
            PlayerDisconnect msg = (PlayerDisconnect) receivedPacket;
            int id = msg.getClientID();
            if (controllerInstance instanceof Scene2) {
                ((Scene2) controllerInstance).updateDisconnected(id);
            } else if (controllerInstance instanceof Scene3) {
                ((Scene3) controllerInstance).updateDisconnected(id);
            }

        } else if (receivedPacket instanceof String) {
            // handle specific String messages if needed, e.g., "start-game"
            String msg = (String) receivedPacket;
            if (msg.equals("start-game")) {
                System.out.println("Game started (via String message)! Controller: " + controllerInstance.getClass().getSimpleName());
                // You could add methods to Scene2 and Scene3 to handle game start visuals
                // if (controllerInstance instanceof Scene2) { ((Scene2) controllerInstance).displayGameStarted(); }
                // else if (controllerInstance instanceof Scene3) { ((Scene3) controllerInstance).displayGameStarted(); }
            }
        } else {
            System.out.println("Client received an unknown object: " + receivedPacket.getClass().getName());
        }
    }

    public static void main(String[] args) {
        new Thread(new Client()).start();
    }
}
