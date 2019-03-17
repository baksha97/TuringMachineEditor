package fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("editor.fxml"));
        primaryStage.setTitle("Turing Machine Editor v3.17.2019");
        primaryStage.getIcons().add(new Image("computer_icon.png"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
