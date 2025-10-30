package app;

import java.util.Optional;
import javafx.fxml.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;


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
        
	}
	
	private void createGame() {
//		
//		TextInputDialog dialog = new TextInputDialog();
//		dialog.setTitle("Enter Name");
//		dialog.setHeaderText("Please provide your name");
//		dialog.setContentText("Name:");
//		
//		DialogPane pane = dialog.getDialogPane();
//		pane.getStylesheets().add(getClass().getResource("guiConfigs/dialog.css").toExternalForm());
//		pane.getStyleClass().add("dialog-pane");
//		
//	    Optional<String> result = dialog.showAndWait();
//	    
//	    result.ifPresent(name -> {
//	    	Client.setClientName(name);
//	    	Client.setStatus(true);
//	    	new Thread(() -> new Server()).start();
//	        Stage stage = (Stage) createButton.getScene().getWindow();
//	        Scene scene = SceneLoader.sceneLoader("Scene2");
//	        stage.setScene(scene);
//	    });
		
		
		
		
	}
	
private void joinGame() {
//		
//		TextInputDialog dialog = new TextInputDialog();
//		dialog.setTitle("Enter Name");
//		dialog.setHeaderText("Please provide your name");
//		dialog.setContentText("Name:");
//		
//		DialogPane pane = dialog.getDialogPane();
//		pane.getStylesheets().add(getClass().getResource("guiConfigs/dialog.css").toExternalForm());
//		pane.getStyleClass().add("dialog-pane");
//		
//	    Optional<String> result = dialog.showAndWait();
//	    
//	    result.ifPresent(name -> {
//	    	Client.setClientName(name);
//	    	Client.setStatus(false);
//	        Stage stage = (Stage) createButton.getScene().getWindow();
//	        Scene scene = SceneLoader.sceneLoader("Scene2");
//	        stage.setScene(scene);
//	    });
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

}
