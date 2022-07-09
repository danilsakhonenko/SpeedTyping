package practice.speedtyping;

import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

public class MainController {
    private DataBaseSession _session;
    private int _wordsCount;
    private Tracker _tracker =null;
    private ArrayList<Label> _charLabels;
    

    @FXML
    private Button cancel_btn;

    @FXML
    private Button control_btn;

    @FXML
    private TilePane words_area;

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
        stopTest();
        
    }

    @FXML
    void controlPlay(ActionEvent event) {
        if(_tracker==null){
            startTest();
        }else if (_tracker.isPaused()){
            continueTest();
        }else if (!_tracker.isPaused()){
            pauseTest();
        }
        words_area.requestFocus();
    }
   
    @FXML
    void resetProgress(ActionEvent event){
        try {
            _tracker.reset();
            words_area.getChildren().clear();
            setLabels();
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void keyCheck(KeyEvent event) {
        if(_tracker==null || _tracker.isPaused())
            return;
        Label label;
        if(_tracker.isCorrectChar(event.getCharacter().charAt(0))){
            label = _charLabels.get(_tracker.getCurrentIndex()-1);
            label.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(4), new Insets(0))));
            if(_tracker.isFinished())
                showResult();
        }else{
            label = _charLabels.get(_tracker.getCurrentIndex());
            label.setBackground(new Background(new BackgroundFill(Color.LIGHTCORAL, new CornerRadii(4), new Insets(0))));
        }
    }
    
    @FXML
    void tabChange(ActionEvent event) {
        if(_tracker!=null){
            stopTest();
            _tracker=null;
        }
    }

    @FXML
    void initialize() {
        setButtons();
        setLanguages();
        word_count_field.setText("10");
        setGraphComboBox();
    }
    
    void setSession(DataBaseSession session){
        _session = session;
    }
        
    private void setLanguages(){
        language_cb.getItems().add(0, "Английский");
        language_cb.getItems().add(1, "Русский");
        language_cb.getSelectionModel().select(0);
    }
    
    private void setButtons(){
        ImageView imageView = new ImageView(getClass().getResource("images/play.png").toExternalForm());
        control_btn.setGraphic(imageView);
        imageView = new ImageView(getClass().getResource("images/stop.png").toExternalForm());
        cancel_btn.setGraphic(imageView);
        imageView = new ImageView(getClass().getResource("images/reset.png").toExternalForm());
        reset_btn.setGraphic(imageView);
        buttonsSetDisable(true);
    }
    
    private void setGraphComboBox(){
        graphic_cb.getItems().add(0, "Cкорость печати");
        graphic_cb.getItems().add(1, "Коэффициент ошибок");
        graphic_cb.setOnAction(event -> {
            //_session.getStatistics();
        });
    }
    
    private void startTest(){
        
        try {
            _wordsCount = Integer.parseInt(word_count_field.getText());
            if(_wordsCount < 5 && _wordsCount> 450)
                throw new Exception();
            int language = language_cb.getSelectionModel().getSelectedIndex()+1;
            boolean punct = punctuation_check.isSelected();
            _tracker=new Tracker();
            _tracker.startTest(_wordsCount,punct,language,_session);
            setLabels();
            setControlBtnImage(false);
            buttonsSetDisable(false);
            optionsSetDisable(true);
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void setLabels(){
        _charLabels = new ArrayList<>();
        char[] chars = _tracker.getChars();
        for (char c: chars){
            Label label = new Label(String.valueOf(c));
            label.setFont(Font.font ("Courier New", 26));
            _charLabels.add(label);
            words_area.getChildren().add(label);
        }
    }
    
    private void continueTest(){
        setControlBtnImage(false);
        _tracker.unpause();
    }
    
    private void pauseTest(){
        setControlBtnImage(true);
        _tracker.pause();
    }
    
    private void stopTest(){
        words_area.getChildren().clear();
        setControlBtnImage(true);
        optionsSetDisable(false);
        buttonsSetDisable(true);
        _tracker = null;
    }

    private void setControlBtnImage(boolean state){
        ImageView imageView;
        if(state)
            imageView = new ImageView(getClass().getResource("images/play.png").toExternalForm());
        else
            imageView = new ImageView(getClass().getResource("images/pause.png").toExternalForm());
        control_btn.setGraphic(imageView);
    }
    
    private void buttonsSetDisable(boolean state){
        reset_btn.setDisable(state);
        cancel_btn.setDisable(state);
    }
    
    private void optionsSetDisable(boolean state){
        language_cb.setDisable(state);
        word_count_field.setDisable(state);
        punctuation_check.setDisable(state);
    }
    
    private void showResult(){
        try {
            Object[] result = _tracker.getResult();
            stopTest();
            for(Object o : result){
                System.out.println(o.toString());
            }
            _session.insertResult(result);
            //вывод окна результата, удаление всего установка дизейбла на кнопки и енабла на контролы
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
