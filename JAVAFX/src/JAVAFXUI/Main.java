package JAVAFXUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    public static void main(String[] args) {

        launch(Main.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Evolutionary Time Table");
        Parent load = FXMLLoader.load(getClass().getResource("/JAVAFXUI/ETTJAVAFX.fxml"));
        Scene scene = new Scene(load);
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();
    }
}
