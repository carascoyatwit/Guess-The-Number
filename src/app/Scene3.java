package app;

import java.util.ArrayList;

import app.misc.SceneLoader;
import app.network.Client;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Scene3 {

	@FXML
	public VBox centerVBox;
	
    @FXML 
    private HBox centerHBox;
    
    @FXML 
    private Button cancelButton; 
   
    private ArrayList<Label> labels = new ArrayList<>();
    
    public void initialize() {
        centerVBox.setSpacing(60);
        centerHBox.setSpacing(50);
        
        new Thread(new Client()).start();
        
        cancelButton.setOnAction(e -> leaveGame());

    }
    
    private void leaveGame() {
    	 Stage stage = (Stage) cancelButton.getScene().getWindow();
    	    SceneLoader.LoadedScene<Scene1> loaded = SceneLoader.load("Scene1");
    	    stage.setScene(loaded.scene);
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
        lbl.setStyle("-fx-text-fill: #50C878; -fx-border-color: #50C878; -fx-background-color: #50C87880;"); // Green
    }

    public void updateDisconnected(int index) {
        if (index < 0 || index >= labels.size()) return;
        Label lbl = labels.get(index);
        lbl.setText("Disconnected");
        lbl.setStyle("-fx-text-fill: #FF0000; -fx-border-color: #FF0000; -fx-background-color: #FF000080;"); // Red
    }
}

