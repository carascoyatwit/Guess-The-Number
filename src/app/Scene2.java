package app;

import java.util.ArrayList;

import app.network.Client;
import app.network.Server;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Scene2 {
	
	@FXML
	private VBox centerVBox;
	
	@FXML 
	private HBox centerHBox;
	
	@FXML
	private Button startButton;
	
	private ArrayList<Label> labels = new ArrayList<>();

	public void initialize() {
		centerVBox.setSpacing(60);
		 centerHBox.setSpacing(50);
        
        new Thread(new app.network.Client()).start();
	}
	
	   public void setupLabels(int maxPlayers) {
	        labels.clear();
	        centerHBox.getChildren().clear();

	        for (int i = 0; i < maxPlayers; i++) {
	            Label lbl = new Label("...");
	            labels.add(lbl);
	            centerHBox.getChildren().add(lbl);
	        }
	        
	        centerHBox.setSpacing(50);
	    }

	
	public void updateConnected(int index, String name) {
		if (index < 0 || index >= labels.size()) return;
        Label lbl = labels.get(index);
        lbl.setText(name);
        lbl.setStyle("-fx-text-fill: #50C878; -fx-border-color: #50C878; -fx-background-color: #50C87880;");
	}
	public void updateDisconnected(int index) {
		if (index < 0 || index >= labels.size()) return;
        Label lbl = labels.get(index);
        lbl.setText("Disconnected");
        lbl.setStyle("-fx-text-fill: #FF0000; -fx-border-color: #FF0000; -fx-background-color: #FF000080;");
	}


}
