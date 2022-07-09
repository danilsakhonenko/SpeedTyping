package practice.speedtyping;
import java.util.ResourceBundle;
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
            DataBaseSession session = new DataBaseSession(_username, _pass);
            RunMainForm(session);
            stage.close();
        } catch (Exception ex) {
            new DialogMessage("Ошибка подключения. Проверьте правильность логина и пароля").ErrorMessage();
        }
    }
    


    @FXML
    void registerUser(ActionEvent event) {
        try {
            setUserData();
            DataBaseSession.createUser(_username, _pass);
            new DialogMessage("Пользователь "+_username+" успешно зарегистрирован!").InfoMessage();
        } catch (Exception ex) {
            new DialogMessage(ex.getMessage()).ErrorMessage();
        }
    }
    
    @FXML
    void initialize() {

    }
    
    private void setUserData() throws Exception{
        if(user_field.getText().length()>30){
            throw new Exception("Слишком большая длина логина (>30)");
        }
        _username = user_field.getText();
        _pass = pass_field.getText();
    }

    private void RunMainForm(DataBaseSession session) throws Exception{
        Stage newstage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainForm.fxml"));
        newstage.setScene(new Scene(loader.load()));
        MainController ctrl = loader.getController();
        ctrl.setSession(session);
        newstage.show();
    }
}