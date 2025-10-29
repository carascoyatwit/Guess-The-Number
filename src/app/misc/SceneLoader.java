package app.misc;

import java.io.*;
import javafx.fxml.*;
import javafx.scene.*;


//This class is mostly unimportant you can ignore it, It is jujst for loading stuff from the /Gui folder.

public class SceneLoader {

	public SceneLoader(String name) {
        try {
        	FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource("/Gui/" + name + ".fxml"));
			Parent root = loader.load();
			root.getStylesheets().add(SceneLoader.class.getResource("/Gui/" + name + ".css").toExternalForm());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
        
	}

}
