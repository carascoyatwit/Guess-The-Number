package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Gui extends Application{

	 public static void main(String[] args) {
	        launch(args);
	    }

	 @Override
	 public void start(Stage primaryStage) throws Exception {
	        Label label = new Label("Yan's Java FX Project");

//	        startButton.setOnAction(e -> {
//	            new Thread(() -> {Client client = new Client();}).start();
//	        });
//	        
	        StackPane root = new StackPane(label);
	        Scene scene = new Scene(root, 400, 200);

	        primaryStage.setTitle("JavaFX Barebones");
	        primaryStage.setScene(scene);
	        primaryStage.show();
		
	 }

}
