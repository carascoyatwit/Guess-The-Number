package app;

import java.util.Optional;

import app.*;
import app.misc.SceneLoader;
import app.network.Server;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.*;


public class Scene1 {

	 	@FXML
	    private VBox centerVBox;

	    @FXML
	    private HBox centerHBox;

	    @FXML
	    private Label mainTitle;

	    @FXML
	    private Button createButton;

	    @FXML
	    private Button joinButton;
	    
	    @FXML 
	    private Button configButton;
	    
	    @FXML
	    private HBox topHBox;
	    
	    
	public void initialize() {
		centerVBox.setSpacing(120);
		centerHBox.setSpacing(60);
		emptyNode(centerVBox, 0, 0, true); //(pane you want it in, index, size, true = VBox false = Hbox)
		
		
		Image icon = new Image(getClass().getResourceAsStream("icons/configIcon.png")); 
		ImageView iconView = new ImageView(icon);
		iconView.setFitWidth(30);
		iconView.setFitHeight(30);
		configButton.setGraphic(iconView);
		
		topHBox.setPadding(new Insets(30, 0, 0 ,0));
		emptyNode(topHBox, 1, 350, false);
		
        createButton.setOnAction(e -> createGame());
        joinButton.setOnAction(e -> joinGame());
        configButton.setOnAction(e -> showConfigDialog());
        
	}
	
	private void createGame() {
		
	  new Thread(() -> new Server()).start();
	  
      Stage stage = (Stage) createButton.getScene().getWindow();
      Scene scene = SceneLoader.sceneLoader("Scene2");
      stage.setScene(scene);

		
	}
	
private void joinGame() {

	
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
	
	private void showConfigDialog() {
	    
	    Dialog<Void> dialog = new Dialog<>();
	    dialog.setTitle("Game Configurations");
	    dialog.setHeaderText("Edit your Game Configurations:");

	  
	    DialogPane pane = dialog.getDialogPane();
	    pane.getStylesheets().add(getClass().getResource("guiConfigs/dialog.css").toExternalForm());
	    pane.getStyleClass().add("dialog-pane");

	    
	    TextField nameField = new TextField();
	    nameField.setPromptText("Please Enter Your Name:");

	    Spinner<Integer> setPlayersSpinner = new Spinner<>(2, 10, UserConfigs.getSetClients());

	    TextField hostField = new TextField(UserConfigs.getServerHost());
	    TextField portField = new TextField(String.valueOf(UserConfigs.getServerPort()));

	    // Layout
	    VBox content = new VBox(10);
	    content.getChildren().addAll(
	        new Label("Name:"), nameField,
	        new Label("Max Players:"), setPlayersSpinner,
	        new Label("Server Host:"), hostField,
	        new Label("Server Port:"), portField
	    );
	    pane.setContent(content);

	    ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
	    ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
	    dialog.getDialogPane().getButtonTypes().addAll(saveButton, cancelButton);

	    dialog.setResultConverter(dialogButton -> {
	        if (dialogButton == saveButton) {
	            
	            UserConfigs.setClientName(nameField.getText());
	            UserConfigs.setSetClients(setPlayersSpinner.getValue());
	            UserConfigs.setServerHost(hostField.getText());
	            UserConfigs.setServerPort(Integer.parseInt(portField.getText()));
//
//	            new Thread(() -> new Server()).start();
//
//	            // Switch scene
//	            Stage stage = (Stage) createButton.getScene().getWindow();
//	            Scene scene = SceneLoader.sceneLoader("Scene2");
//	            stage.setScene(scene);
	        }
	        return null;
	    });

	    // Show the dialog
	    dialog.showAndWait();
	}

}
