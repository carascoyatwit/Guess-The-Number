package app;

import java.util.ArrayList;

import app.packets.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
	
	private ArrayList<Label> playerLabels = new ArrayList<>();
	
	public void initialize() {
		centerVBox.setSpacing(30);
		centerHBox.setSpacing(50);
		
		 guessInput.textProperty().addListener((observable, oldValue, newValue) -> {
	            if (!newValue.matches("\\d*")) { 
	                guessInput.setText(""); 
	                guessInput.setStyle("-fx-border-color: red; -fx-prompt-text-fill: red;");
	            } else {
	                guessInput.setStyle(""); 
	                }
	        });
		 
		 
		 //uses a sendGuess method to actually send the guess on enter
		 
//		 guessInput.setOnAction(event -> {
//	            sendGuess();
//		 guessInput.setDisable(true);
//	        });
		 
	}
	
	 public void setupPlayerLabels(ArrayList<PlayerData> connectedPlayers) {
	        playerLabels.clear();
	        centerHBox.getChildren().clear();

	        for (PlayerData player : connectedPlayers) {
	            Label lbl = new Label(player.getName());
	            lbl.setStyle("-fx-text-fill: #E0E0E0; -fx-font-size: 20px;"); // Default style for names
	            playerLabels.add(lbl);
	            centerHBox.getChildren().add(lbl);
	        }
	    }

}
