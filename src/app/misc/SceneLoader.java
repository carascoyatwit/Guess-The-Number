package app.misc;

import java.io.*;
import javafx.fxml.*;
import javafx.scene.*;

public class SceneLoader {

    public static class LoadedScene<T> {
        public final Scene scene;
        public final T controller;

        public LoadedScene(Scene scene, T controller) {
            this.scene = scene;
            this.controller = controller;
        }
    }

    public static <T> LoadedScene<T> load(String name) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource("/app/guiConfigs/" + name + ".fxml"));
            Parent root = loader.load();

            String css = SceneLoader.class.getResource("/app/guiConfigs/" + name + ".css").toExternalForm();
            root.getStylesheets().add(css);

            return new LoadedScene<>(
                new Scene(root),
                loader.getController()
            );

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
