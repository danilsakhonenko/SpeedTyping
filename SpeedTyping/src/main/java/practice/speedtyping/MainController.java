package practice.speedtyping;

import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class MainController {
    private DataBaseConnection _session;
    private int _wordsCount;
    
    @FXML
    private ResourceBundle resources;

    @FXML
    private Button cancel_btn;

    @FXML
    private Button control_btn;

    @FXML
    private TextArea given_text_field;

    @FXML
    private LineChart<?, ?> graphic;

    @FXML
    private ComboBox<String> graphic_cb;

    @FXML
    private ComboBox<String> language_cb;

    @FXML
    private CheckBox punctuation_check;

    @FXML
    private Button reset_btn;

    @FXML
    private TextArea statistics_field;

    @FXML
    private TextField word_count_field;

    @FXML
    void cancelProgress(ActionEvent event) {
        
    }

    @FXML
    void controlPlay(ActionEvent event) {
        //if(not pause)
        reset_btn.setDisable(false);
        cancel_btn.setDisable(false);
        _wordsCount = Integer.parseInt(word_count_field.getText());
    }

    
    @FXML
    void resetProgress(ActionEvent event) {
        _wordsCount = Integer.parseInt(word_count_field.getText());
    }


    @FXML
    void initialize() {
        setButtons();
        setLanguages();
        word_count_field.setText("10");
        setGraphComboBox();
    }
    
    void setSession(DataBaseConnection session){
        _session = session;
    }
        
    void setLanguages(){
        language_cb.getItems().add(0, "Английский");
        language_cb.getItems().add(1, "Русский");
        language_cb.getSelectionModel().select(0);
    }
    
    void setButtons(){
        ImageView imageView = new ImageView(getClass().getResource("images/play.png").toExternalForm());
        control_btn.setGraphic(imageView);
        imageView = new ImageView(getClass().getResource("images/stop.png").toExternalForm());
        cancel_btn.setGraphic(imageView);
        imageView = new ImageView(getClass().getResource("images/reset.png").toExternalForm());
        reset_btn.setGraphic(imageView);
        reset_btn.setDisable(true);
        cancel_btn.setDisable(true);
    }
    
    void setGraphComboBox(){
        graphic_cb.getItems().add(0, "Cкорость печати");
        graphic_cb.getItems().add(1, "Коэффициент ошибок");
        graphic_cb.setOnAction(event -> {
            //вывести график
        });
    }

}
