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
	
	public static ArrayList<Label> list = new ArrayList<>();

	public void initialize() {
		new Thread(() -> new Client()).start();
		
		centerVBox.setSpacing(60);
		
		for(int i = 0; i < UserConfigs.getSetClients(); i++) {
			Label lbl = new Label();
			list.add(lbl);
			centerHBox.getChildren().add(lbl);
		}
		centerHBox.setSpacing(50);
		
		
	}
	
	public static void updateConnected(int index) {
		list.get(index).setStyle("-fx-text-fill: #50C878; -fx-border-color: #50C878; -fx-background-color: #50C87880;");
	}
	public static void updateDisconnected(int index) {
		list.get(index).setStyle("-fx-text-fill: #FF0000; -fx-border-color: #FF0000; -fx-background-color: #FF000080;");
	}


}
