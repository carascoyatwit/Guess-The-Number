package app.network;

import app.AppState;
import app.UserConfigs;
import app.Scene2; 
import app.Scene3; 
import app.packets.*;
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
            Object receivedRawObject; 
          while ((receivedRawObject = input.readObject()) != null) {
            	
            	//needs to be effectivly final?
                final Object finalReceivedObject = receivedRawObject; 
                
                Platform.runLater(() -> {
                    if (AppState.waitingRoomController == null) {
                        System.err.println("Look into this, it is null");
                        return;
                      }

                    if (AppState.waitingRoomController instanceof Scene2) {
                    	
                        Scene2 controller = (Scene2) AppState.waitingRoomController;
                        handleMessageForController(controller, finalReceivedObject); 
                        
                    } else if (AppState.waitingRoomController instanceof Scene3) {
                        Scene3 controller = (Scene3) AppState.waitingRoomController;
                        handleMessageForController(controller, finalReceivedObject); 
                        
                  } else {
                     System.err.println("A different controller?" + AppState.waitingRoomController.getClass().getName());
                       }
                }); 
         }

        } catch (Exception e) {
            System.out.println(e);
            
        }
    }

    
    private void handleMessageForController(Object controllerInstance, Object receivedPacket) {
        
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

            //This can handle any specific strings you need to send.
        } else if (receivedPacket instanceof String) {
            
            String msg = (String) receivedPacket;
//            if("Something".equalsIgnoreCase(msg)) {
//            	
//            }
//            
        } else {
            System.out.println("Client received an unknown object: " + receivedPacket.getClass().getName());
        }
    }

    public static void main(String[] args) {
        new Thread(new Client()).start();
    }
}
