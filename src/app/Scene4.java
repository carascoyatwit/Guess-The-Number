package app;

import java.util.ArrayList;

import app.network.Client;
import app.packets.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class Scene4 {

	@FXML
	private VBox centerVBox;
	
	@FXML
	private Label answerlabel;
	
	@FXML
	private HBox centerHBox;
	
	@FXML
	private TextField guessInput;
	
	@FXML
	private HBox bottomHBox;
	
	@FXML 
	private HBox utilHBox;
	
	@FXML
	private Button backButton;
	
	private ArrayList<Label> playerLabels = new ArrayList<>();
	private Button exitButton = new Button("Exit");
	
	public void initialize() {
		centerVBox.setSpacing(90);
		centerHBox.setSpacing(50);
		
		exitButton.setOnAction(e ->	 {
		    if (Client.currentClientInstance != null) {
		        Client.currentClientInstance.disconnectAndReturnHome();
		    }
		});
		
		Image icon = new Image(getClass().getResourceAsStream("icons/backIcon.png")); 
		ImageView iconView = new ImageView(icon);
		iconView.setFitWidth(30);
		iconView.setFitHeight(30);
		backButton.setGraphic(iconView);
		
		utilHBox.setPadding(new Insets(30, 0, 0 ,0));
		emptyNode(utilHBox, 0, 150, false);
		
		backButton.setOnAction(e -> {
		    if (Client.currentClientInstance != null) {
		        Client.currentClientInstance.disconnectAndReturnHome();
		    }
		});
		
		String normalStyle = 
			    "-fx-text-fill: #FFFFFF;" +
			    "-fx-border-color: #FFFFFF;" +
			    "-fx-background-color: #FFFFFF80;" +
			    "-fx-background-radius: 15px;" +
			    "-fx-border-radius: 15px;" +
			    "-fx-font-size: 20px;" +
			    "-fx-pref-width: 200px;" + 
			    "-fx-pref-height: 50px;";

			String hoverStyle = 
			    "-fx-text-fill: #FFFFFF;" +
			    "-fx-border-color: #FFFFFF;" +
			    "-fx-background-color: #FFFFFFAA;" +
			    "-fx-background-radius: 15px;" +
			    "-fx-border-radius: 15px;" +
			    "-fx-font-size: 20px;" +
			    "-fx-pref-width: 200px;" + 
			    "-fx-pref-height: 50px;";

			exitButton.setStyle(normalStyle);

			exitButton.setOnMouseEntered(e -> exitButton.setStyle(hoverStyle));
			exitButton.setOnMouseExited(e -> exitButton.setStyle(normalStyle));

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
	 
	 public static Region emptyNode(Pane pane, int index, double size, boolean isVertical) {
		    Region spacer = new Region();
		    if (isVertical) { // set true if you want a Vbox spacer or a false if you want an Hbox spacer
		        spacer.setMinHeight(size);
		        spacer.setPrefHeight(size);
		        spacer.setMaxHeight(size);
		    } else {
		        spacer.setMinWidth(size);
		        spacer.setPrefWidth(size);
		        spacer.setMaxWidth(size);
		    }
		    pane.getChildren().add(index, spacer);
		    return spacer;
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
	        
	            if (lbl != null) {
	                String originalName = lbl.getText().split(" \\(")[0]; 
	                lbl.setText(originalName + " (Winner!)");
	                lbl.setStyle("-fx-text-fill: #FFD700; -fx-border-color: #FFD700; -fx-background-color: #FFD70080;");
	              
	            }
	        }
	        
	        int index = bottomHBox.getChildren().indexOf(guessInput);
	        if (index != -1) {
	            // Replace the old label with the new label
	            bottomHBox.getChildren().set(index, exitButton);
	        }
	        
	 }
	 
	 public void updateAnswerLabel(String target) {
		 
		 answerlabel.setText(target);
         answerlabel.setStyle("-fx-text-fill: #FFD700");
		 
	 }
	 
	 public void flashRed() {
		 for (Label lbl : playerLabels) {
			 lbl.setStyle("-fx-text-fill: #FF0000; -fx-border-color: #FF0000; -fx-background-color: #FF000080;");
		 }
	 }
	 
	 public void resetRound() {
		    guessInput.setDisable(false);
		    guessInput.clear();

		    for (Label lbl : playerLabels) {
		        if (lbl != null) {
		            String originalName = lbl.getText().split(" \\(")[0]; 
		            lbl.setText(originalName);
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
		     }
		        }
		    
		    
            answerlabel.setText("???");
            answerlabel.setStyle("#E0E0E0");
            
		}

}
