package practice.speedtyping;

import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
    private Tracker _tracker;
    private ArrayList<Label> _charLabels;
    private StatBuilder _statBuilder;
    
    @FXML
    private TabPane main_window;
     
    @FXML
    private Button cancel_btn;

    @FXML
    private Button control_btn;

    @FXML
    private TilePane words_area;

    @FXML
    private LineChart<Number, Number> graphic;

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
        cancelTest();
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
            setLetters();
            words_area.requestFocus();
        } catch (Exception ex) {
           new DialogMessage("Во время запуска текста возникла ошибка: "+ex.getMessage()).ErrorMessage();
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
    void initialize() {
        main_window.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                loadStatsTab(t1);
            }
        });
        setButtons();
        setLanguages();
        word_count_field.setText("10");
        setGraphComboBox();
        
    }
    
    void loadStatsTab(Tab newTab) {
        if(_statBuilder == null)
            _statBuilder = new StatBuilder(graphic);
        if(newTab.getId().equals("stats")){
            try {
                String s = _statBuilder.buildLastStat(_session.getLastResult());
                statistics_field.setText(s);
            } catch (Exception ex) {
               
            }
        }
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
        buttonsSetDisabled(true);
    }
    
    private void setGraphComboBox(){
        graphic_cb.getItems().add(0, "Cкорость печати");
        graphic_cb.getItems().add(1, "Процент ошибок");
        graphic_cb.setOnAction(event -> {
            try{
                int index = graphic_cb.getSelectionModel().getSelectedIndex();
                _statBuilder.buildChart(_session.getResults(index),index);
            }catch(Exception ex){
                new DialogMessage("Ошибка загрузки графика: "+ex.getMessage()).ErrorMessage();
            }
        });
    }
    
    private void startTest(){
        try {
            int _wordsCount = Integer.parseInt(word_count_field.getText());
            if(_wordsCount < 5 && _wordsCount> 450)
                new DialogMessage("Недопустимое значение. Условия для количества слов "
                        + "5 < количество слов < 450.").WarningMessage();
            int language = language_cb.getSelectionModel().getSelectedIndex()+1;
            boolean punct = punctuation_check.isSelected();
            _tracker=new Tracker();
            _tracker.start(_wordsCount,punct,language,_session);
            setLetters();
            setControlBtnImage(false);
            buttonsSetDisabled(false);
            optionsSetDisabled(true);
        } catch (Exception ex) {
            new DialogMessage("Во время запуска текста возникла ошибка: "+ex.getMessage()).ErrorMessage();
        }
    }
    
    private void setLetters(){
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
    
    private void cancelTest(){
        words_area.getChildren().clear();
        setControlBtnImage(true);
        optionsSetDisabled(false);
        buttonsSetDisabled(true);
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
    
    private void buttonsSetDisabled(boolean state){
        reset_btn.setDisable(state);
        cancel_btn.setDisable(state);
    }
    
    private void optionsSetDisabled(boolean state){
        language_cb.setDisable(state);
        word_count_field.setDisable(state);
        punctuation_check.setDisable(state);
    }
    
    private void showResult(){
        try {
            Object[] result = _tracker.getResult();
            cancelTest();
            _session.insertResult(result);
            if(_statBuilder == null)
                _statBuilder = new StatBuilder(graphic);
            new DialogMessage(_statBuilder.buildLastStat(result)).InfoMessage();
        } catch (Exception ex) {
            new DialogMessage("Ошибка сохранения результатов: "+ex.getMessage()).ErrorMessage();
        }
    }
    
}
