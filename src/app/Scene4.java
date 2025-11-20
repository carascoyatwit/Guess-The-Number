package app;

import java.util.ArrayList;

import app.network.Client;
import app.packets.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.Node;

public class Scene4 {

	@FXML
	private VBox centerVBox;
	
	@FXML
	private Label answerlabel;
	
	@FXML
	private HBox centerHBox;
	
	@FXML
	private TextField guessInput;
	
	private ArrayList<Label> playerLabels = new ArrayList<>();
	
	public void initialize() {
		centerVBox.setSpacing(90);
		centerHBox.setSpacing(50);
		
		 guessInput.textProperty().addListener((observable, oldValue, newValue) -> {
	            if (!newValue.matches("\\d*")) { 
	                guessInput.setText(""); 
	                guessInput.setStyle("-fx-border-color: red; -fx-prompt-text-fill: red;");
	            } else {
	                guessInput.setStyle(""); 
	                }
	        });
		 
		 
		 guessInput.setOnAction(event -> {
		        sendGuess(guessInput.getText());
		        guessInput.setDisable(true); // Disable input after guessing
		        guessInput.clear(); // Clear the text field
		    });
		 
	}
	
    private void sendGuess(String guess) {
        if (Client.currentClientInstance != null) {
            int myClientID = UserConfigs.getMyClientID(); // Get the client's stored ID
            if (myClientID != -1) { // Ensure the ID has been set
                Client.currentClientInstance.sendToServer(new PlayerGuessed(myClientID, guess));
            } else {
                System.err.println("Cannot send guess: Client ID not yet assigned.");
            }
        } else {
            System.err.println("Cannot send guess: Client not connected.");
        }
    }
    
	 public void setupPlayerLabels(ArrayList<PlayerData> connectedPlayers) {
	        playerLabels.clear();
	        centerHBox.getChildren().clear();

	        for (PlayerData player : connectedPlayers) {
	            Label lbl = new Label(player.getName());
	            lbl.setStyle(
	                    "-fx-text-fill: #ADD8E6;" + 
	                    "-fx-font-size: 20px;" +
	                    "-fx-font-weight: bold;" + 
	                    "-fx-padding: 30px 40px;" + 
	                    "-fx-background-color: #ADD8E680;" + 
	                    "-fx-background-radius: 15px;" + 
	                    "-fx-border-color: #ADD8E6;" + 
	                    "-fx-border-radius: 15px;" +
	                    "-fx-font-family: \"Nunito\";"
	                );
	            playerLabels.add(lbl);
	            centerHBox.getChildren().add(lbl);
	        }
	    }
	 
	 public void updateDisconnected(int clientID) {
	        if (clientID >= 0 && clientID < playerLabels.size()) {
	             Label lbl = playerLabels.get(clientID);
	             
	             if (lbl != null) {
	            	 
	                 lbl.setText(lbl.getText().replace(" (Disconnected)", "") + " (Disconnected)"); 
	                 lbl.setStyle("-fx-text-fill: #FF0000; -fx-border-color: #FF0000; -fx-background-color: #FF000080;"); 
	             }
	        }
	    }

	 public void updatePlayerGuess(int clientID, String guess) {
	        if (clientID >= 0 && clientID < playerLabels.size()) {
	            Label lbl = playerLabels.get(clientID);
	            if (lbl != null) {
	                
	                String originalName = lbl.getText().split(" \\(")[0]; 
	                lbl.setText(originalName + " (Guessed: " + guess + ")");
	                lbl.setStyle("-fx-text-fill: #50C878; -fx-border-color: #50C878; -fx-background-color: #50C87880;");
	              
	            }
	        }
	 }
	 
	 public void updatePlayerWin(int clientID) {
	        if (clientID >= 0 && clientID < playerLabels.size()) {
	            Label lbl = playerLabels.get(clientID);
	            answerlabel.setText("50");
	            answerlabel.setStyle("-fx-text-fill: #FFD700");
	            
	            
	            
	            if (lbl != null) {
	                String originalName = lbl.getText().split(" \\(")[0]; 
	                lbl.setText(originalName + " (Winner!)");
	                lbl.setStyle("-fx-text-fill: #FFD700; -fx-border-color: #FFD700; -fx-background-color: #FFD70080;");
	              
	            }
	        }
	 }
	 
}
