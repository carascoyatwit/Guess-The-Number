package app;

import javafx.application.Application;
import app.misc.*;
import javafx.scene.*;
import javafx.scene.text.Font;
import javafx.stage.*;

public class Gui extends Application{

	 public static void main(String[] args) {
	        launch(args);
	    }

	 @Override
	 public void start(Stage stage) throws Exception {
		 	Font.loadFont(getClass().getResource("/app/fonts/Nunito-Regular.ttf").toExternalForm(), 12);
	        Scene scene1 = SceneLoader.sceneLoader("Scene1");
	        stage.setTitle("Guess The Number");
	        stage.setWidth(1200);  
	        stage.setHeight(800);
	        stage.setScene(scene1);
	        stage.show();

	 }

}
