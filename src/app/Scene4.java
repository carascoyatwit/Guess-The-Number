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
	        // You'll need to know *your* clientID to send this packet.
	        // This usually needs to be stored in UserConfigs or AppState after connection.
	        // For now, let's assume clientID is available or passed.
	        // This is a simplification; in a real game, the Client class would likely know its own ID.
	        // For example, the server could send a PlayerConnected packet for YOUR player first,
	        // and you store that ID in UserConfigs.
	        int myClientID = -1; // Placeholder: Get actual client ID
	        // For this example, let's just send the guess as a string for simplicity,
	        // but a PlayerGuessed packet is better for structured data.
	        Client.currentClientInstance.sendToServer(new PlayerGuessed(myClientID, guess));
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
	        // Find the label using its assigned ID or by iterating if IDs are not set
	        // A more robust way would be to ensure playerLabels are in order of clientID
	        // and directly access them by index.
	        if (clientID >= 0 && clientID < playerLabels.size()) {
	             Label lbl = playerLabels.get(clientID);
	             if (lbl != null) {
	                 lbl.setText(lbl.getText().replace(" (Disconnected)", "") + " (Disconnected)"); // Append disconnected status
	                 lbl.setStyle("-fx-text-fill: #FF0000; -fx-border-color: #FF0000; -fx-background-color: #FF000080;"); // Red
	             }
	        }
	    }
	 
	 public void updatePlayerStatus(int clientID, String status, String textColor, String borderColor, String backgroundColor) {
	        if (clientID >= 0 && clientID < playerLabels.size()) {
	            Label lbl = playerLabels.get(clientID);
	            if (lbl != null) {
	                String originalName = lbl.getText().split(" \\(")[0]; // Get original name without status
	                lbl.setText(originalName + " (" + status + ")");
	                lbl.setStyle(String.format("-fx-text-fill: %s; -fx-border-color: %s; -fx-background-color: %s;",
	                                           textColor, borderColor, backgroundColor));
	            }
	        }
	    }

}
