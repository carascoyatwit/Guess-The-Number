package app;

import app.network.Client;
import javafx.fxml.*;
import javafx.scene.layout.*;

public class Scene2 {
	
	@FXML
	private VBox centerVBox;
	
	@FXML 
	private HBox centerHBox;

	public void initialize() {
		new Thread(() -> new Client()).start();
	}

}
