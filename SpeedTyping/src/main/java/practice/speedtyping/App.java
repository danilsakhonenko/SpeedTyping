package practice.speedtyping;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Calendar;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage){
        try {
            Calendar calendar = Calendar.getInstance();
            System.out.println(calendar.getTime());
            Scene scene = new Scene(new FXMLLoader(getClass().getResource("LogInForm.fxml")).load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            
        }
    }

    public static void main(String[] args) {
        launch();
    }
}