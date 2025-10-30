package app;

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

	public void initialize() {
		new Thread(() -> new Client()).start();
		
		for(int i = 0; i < Server.maxClients; i++) {
			centerHBox.getChildren().add(new Label());
		}
		centerHBox.setSpacing(50);
	}

}
