package practice.speedtyping;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage){
        try {
            Scene scene = new Scene(new FXMLLoader(getClass().getResource("LogInForm.fxml")).load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            //new DialogMessage("Во время выполнения программы возникла ошибка").ShowConsoleMessage();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}