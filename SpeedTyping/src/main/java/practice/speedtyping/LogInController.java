package practice.speedtyping;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LogInController {
    
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
            checkLength(user_field.getText());
            RunMainForm();
            stage.close();
        } catch (Exception ex) {
            new DialogMessage("Ошибка подключения. Проверьте правильность логина и пароля").ErrorMessage();
        }
    }

    @FXML
    void registerUser(ActionEvent event) {
        try {
            checkLength(user_field.getText());
            DataBaseSession.createUser(user_field.getText(),pass_field.getText());
            new DialogMessage("Пользователь "+user_field.getText()+" успешно зарегистрирован!").InfoMessage();
        } catch (Exception ex) {
            new DialogMessage(ex.getMessage()).ErrorMessage();
        }
    }
    
    @FXML
    void initialize() {

    }
    
    private void checkLength(String s) throws Exception{
        if(s.length()>30){
            throw new Exception("Слишком большая длина логина (>30)");
        }
    }

    private void RunMainForm() throws Exception{
        DataBaseSession session = new DataBaseSession(user_field.getText(),pass_field.getText());
        Stage newstage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainForm.fxml"));
        newstage.setScene(new Scene(loader.load()));
        MainController ctrl = loader.getController();
        ctrl.setSession(session);
        newstage.show();
    }
}