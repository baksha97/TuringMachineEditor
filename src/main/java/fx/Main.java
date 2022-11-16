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
        Wrapper.main(args);
    }

    // Required, see:
    // 1) https://stackoverflow.com/questions/52578072/gradle-openjfx11-error-javafx-runtime-components-are-missing
    // 2) https://github.com/javafxports/openjdk-jfx/issues/236
    private static class Wrapper extends Application {

        public static void main(String[] args) {
            launch(args);
        }

        @Override
        public void start(Stage primaryStage) throws Exception {
            URL editor = Objects.requireNonNull(getClass().getClassLoader().getResource("editor.fxml"));
            URL icon = Objects.requireNonNull(getClass().getClassLoader().getResource("computer_icon.png"));

            System.out.println(editor);
            Parent root = FXMLLoader.load(editor);
            primaryStage.setTitle("Turing Machine Editor v3.17.2019");
            primaryStage.getIcons().add(new Image(String.valueOf(icon)));
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
    }

}
