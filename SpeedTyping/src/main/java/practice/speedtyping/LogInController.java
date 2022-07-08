package practice.speedtyping;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LogInController {
    private String _username;
    private String _pass;
    private static Stage _stage;
    
    @FXML
    private ResourceBundle resources;;

    @FXML
    private Button login_btn;

    @FXML
    private PasswordField pass_field;

    @FXML
    private Button signup_btn;

    @FXML
    private TextField user_field;

    
    @FXML
    void logInUser(ActionEvent event) {
        try {
            Stage stage = (Stage) login_btn.getScene().getWindow();
            setUserData();
            DataBaseConnection session = new DataBaseConnection(_username, _pass);
            RunMainForm(session);
            stage.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    


    @FXML
    void registerUser(ActionEvent event) {
        try {
            setUserData();
            DataBaseConnection.createUser(_username, _pass);
            //вывод окна успех
        } catch (Exception ex) {
            //вывод окна ошибки
        }
    }
    
    @FXML
    void initialize() {

    }
    
    private void setUserData(){
        _username = user_field.getText();
        _pass = pass_field.getText();
    }

    private void RunMainForm(DataBaseConnection session) throws Exception{
        Stage newstage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainForm.fxml"));
        newstage.setScene(new Scene(loader.load()));
        MainController ctrl = loader.getController();
        ctrl.setSession(session);
        newstage.show();
    }
}