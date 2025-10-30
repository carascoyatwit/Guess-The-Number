package app;

import app.misc.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.control.*;
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
	    
	    
	public void initialize() {
		centerVBox.setSpacing(120);
		centerHBox.setSpacing(60);
		insertSpacerVBox(centerVBox, 0, 30);

        createButton.setOnAction(e -> System.out.println("Create Game clicked!"));
        joinButton.setOnAction(e -> System.out.println("Join Game clicked!"));
        
	}
	
	public static Region insertSpacerVBox(VBox vbox, int index, double height) {
	    Region spacer = new Region();
	    spacer.setMinHeight(height);
	    spacer.setPrefHeight(height);
	    spacer.setMaxHeight(height);
	    vbox.getChildren().add(index, spacer);
	    return spacer;
	}

	public static Region insertSpacerHBox(HBox hbox, int index, double width) {
	    Region spacer = new Region();
	    spacer.setMinWidth(width);
	    spacer.setPrefWidth(width);
	    spacer.setMaxWidth(width);
	    hbox.getChildren().add(index, spacer);
	    return spacer;
	}

}
