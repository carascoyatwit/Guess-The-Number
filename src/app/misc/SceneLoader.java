package app.misc;

import java.io.*;
import javafx.fxml.*;
import javafx.scene.*;


//This class is mostly unimportant you can ignore it, It is jujst for loading stuff from the /Gui folder.

public class SceneLoader {

	public static Scene sceneLoader(String name) {
        try {
        	FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource("/app/guiConfigs/" + name + ".fxml"));
			Parent root = loader.load();
			String cssPath = SceneLoader.class.getResource("/app/guiConfigs/" + name + ".css").toExternalForm();
			root.getStylesheets().add(cssPath);
	        return new Scene(root);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
        
	}

}
