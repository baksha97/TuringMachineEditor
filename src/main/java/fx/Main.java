package fx;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Main {

    public static void main(String[] args) {
        FXApplicationWrapper1.main(args);
    }

    public static class FXApplicationWrapper1 extends Application {

        @FXML
        private ResourceBundle resources;

        public static void main(String[] args) {
            launch(args);
        }

        @Override
        public void start(Stage primaryStage) throws Exception {
            URL editor = Objects.requireNonNull(getClass().getClassLoader().getResource("editor.fxml"));
            URL icon = Objects.requireNonNull(getClass().getClassLoader().getResource("computer_icon.png"));

            System.out.println(editor);
            Parent root = FXMLLoader.load(editor, resources);
            primaryStage.setTitle("Turing Machine Editor v3.17.2019");
            primaryStage.getIcons().add(new Image(String.valueOf(icon)));
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
    }

}
