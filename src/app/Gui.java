package app;

import javafx.application.Application;
import app.misc.*;
import javafx.scene.*;
import javafx.stage.*;

public class Gui extends Application{

	 public static void main(String[] args) {
	        launch(args);
	    }

	 @Override
	 public void start(Stage stage) throws Exception {
//	        Label label = new Label("Yan's Java FX Project");
//
//	        startButton.setOnAction(e -> {
//	            new Thread(() -> {Client client = new Client();}).start();
//	        });
//	        
//	        StackPane root = new StackPane(label);
	        Scene scene = SceneLoader.sceneLoader("Scene1");
	        stage.setTitle("Guess The Number");
	        stage.setWidth(800);   // desired width
	        stage.setHeight(600);
	        stage.setScene(scene);
	        stage.show();
		
	 }

}
