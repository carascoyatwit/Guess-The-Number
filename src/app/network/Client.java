package app.network;

import app.AppState;
import app.UserConfigs;
import app.Scene2;
import app.Scene3;
import app.Scene4; 
import app.packets.*; 
import app.misc.SceneLoader; 
import javafx.application.Platform;
import javafx.stage.Stage; 

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Client implements Runnable {

    private ObjectOutputStream output;
    private ObjectInputStream input;
    public static Client currentClientInstance = null; // this is used to identify a specific client instance.
    private ArrayList<PlayerData> connectedPlayers = new ArrayList<>();
    
    public Client() { }

    @Override
     public void run() {
       try (Socket socket = new Socket(UserConfigs.getServerHost(), UserConfigs.getServerPort())) {
            System.out.println("Connected to server at " + UserConfigs.getServerHost() + ":" + UserConfigs.getServerPort());

          Client.currentClientInstance = this; // Assign this client instance to the static field
          
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
              
                    if (finalReceivedObject instanceof GameStatus) {
                        GameStatus gameStatusMsg = (GameStatus) finalReceivedObject;
                        if (gameStatusMsg.isGameStarted()) { 
                               System.out.println("Client received Game Started message. Switching to Scene4.");
                            AppState.gameStarted = true; // Update global game state

                           
                               Stage stage = null;
                            
                            if (AppState.waitingRoomController instanceof Scene2) {
                                 Scene2 controller = (Scene2) AppState.waitingRoomController;
                               
                             if (controller.centerVBox != null && controller.centerVBox.getScene() != null) {
                                    stage = (Stage) controller.centerVBox.getScene().getWindow();
                                }
                            } else if (AppState.waitingRoomController instanceof Scene3) {
                                Scene3 controller = (Scene3) AppState.waitingRoomController;
                                
                              if (controller.centerVBox != null && controller.centerVBox.getScene() != null) {
                                    stage = (Stage) controller.centerVBox.getScene().getWindow();
                                }
                            }
                         
                            if (stage != null) {
                              SceneLoader.LoadedScene<Scene4> loadedScene4 = SceneLoader.load("Scene4");
                                stage.setScene(loadedScene4.scene);
                                
                                AppState.gameController = loadedScene4.controller;
                                
                                ArrayList<PlayerData> activePlayers = new ArrayList<>();
                                for (PlayerData p : connectedPlayers) {
                                    if (p != null) { 
                                        activePlayers.add(p);
                                    }
                                }
                                
                                loadedScene4.controller.setupPlayerLabels(activePlayers);
                                AppState.waitingRoomController = null;
                            } else {
                                System.err.println("there is no stage?");
                            }
                        }
                     return; 
                    }
                    
                    if (AppState.gameStarted && AppState.gameController != null) { 
                        handleMessageForController(AppState.gameController, finalReceivedObject);
                        return; 
                    }
                  
                    if (AppState.waitingRoomController == null) {
                        System.err.println("AppState.waitingRoomController is null. Cannot update UI.");
                        return;
                    }

                    if (AppState.waitingRoomController instanceof Scene2) {
                    	
                        Scene2 controller = (Scene2) AppState.waitingRoomController;
                        handleMessageForController(controller, finalReceivedObject); 
                        
                    } else if (AppState.waitingRoomController instanceof Scene3) {
                        Scene3 controller = (Scene3) AppState.waitingRoomController;
                        handleMessageForController(controller, finalReceivedObject); 
                        
                  } else {
                     System.err.println("A different controller? Type: " + AppState.waitingRoomController.getClass().getName());
                       }
                }); 
         }

        } catch (Exception e) {
            System.out.println(e); 
            
        }finally {
        	if (Client.currentClientInstance == this) {
        		
                Client.currentClientInstance = null; 
                
            }
        	try {
        		
                if (output != null) output.close();
                if (input != null) input.close();
                
            } catch (IOException ignored) {}
        }
    }
 
    public void sendToServer(Serializable packet) {
    	
    	if (output != null) {
    		try {
    			
                output.writeObject(packet);
                output.flush();
                
              System.out.println("sent: " + packet.getClass().getSimpleName()); 
            } catch (IOException e) {
                System.err.println("failed: " + packet.getClass().getSimpleName());
            } 
    	} else {
            System.err.println("failed"); 
        }
    }

    
    private void handleMessageForController(Object controllerInstance, Object receivedPacket) {
    	
        // First check for Scene4-specific updates if game has started
        if (AppState.gameStarted && controllerInstance instanceof Scene4) {
            Scene4 gameController = (Scene4) controllerInstance;

            if (receivedPacket instanceof PlayerDisconnect) {
                PlayerDisconnect msg = (PlayerDisconnect) receivedPacket;
                int id = msg.getClientID();
                // Update connectedPlayers list (set to null)
                if (id >= 0 && id < connectedPlayers.size()) {
                    connectedPlayers.set(id, null);
                }
                Platform.runLater(() -> gameController.updateDisconnected(id)); // Update Scene4 UI on FX thread
                return; // Handled
                
            }else if (receivedPacket instanceof PlayerGuessed) { // New: Handle PlayerGuessed
            	
                PlayerGuessed msg = (PlayerGuessed) receivedPacket;
                
                int id = msg.getClientID();
                String guess = msg.getGuess();
                Platform.runLater(() -> gameController.updatePlayerGuess(id, guess)); // make an update player guess
                return;
            }
   
        }
        
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
            
            while (connectedPlayers.size() <= id) {
                connectedPlayers.add(null);
            }
            connectedPlayers.set(id, new PlayerData(name)); 
            
            if (controllerInstance instanceof Scene2) {
                ((Scene2) controllerInstance).updateConnected(id, name);
           } else if (controllerInstance instanceof Scene3) {
                ((Scene3) controllerInstance).updateConnected(id, name);
           }

        } else if (receivedPacket instanceof PlayerDisconnect) {
            PlayerDisconnect msg = (PlayerDisconnect) receivedPacket;
            int id = msg.getClientID();
            
            if (id >= 0 && id < connectedPlayers.size()) {
                connectedPlayers.set(id, null);
            }
            
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
//            System.out.println("Client received String: " + msg); // Added a print for general strings
        } else {
            System.out.println("Client received an unknown object: " + receivedPacket.getClass().getName());
        }
    }

    public static void main(String[] args) {
        new Thread(new Client()).start();
    }
}
