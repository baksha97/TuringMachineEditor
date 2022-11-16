package fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class FXApplicationWrapper extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL editor = Objects.requireNonNull(getClass().getResource("editor.fxml"));
        Parent root = FXMLLoader.load(editor);
        primaryStage.setTitle("Turing Machine Editor v3.17.2019");
        primaryStage.getIcons().add(new Image("computer_icon.png"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
