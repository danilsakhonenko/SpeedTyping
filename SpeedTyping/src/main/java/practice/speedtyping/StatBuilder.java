package practice.speedtyping;

import java.text.DecimalFormat;
import java.util.List;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class StatBuilder {
    private LineChart _chart;
    public StatBuilder(LineChart chart){  
        _chart = chart;
    }
    
    public void buildChart(List<Object> list, int type){
        _chart.getData().clear();
        XYChart.Series series = new XYChart.Series();
        switch(type){
            case 0:
                series.setName("Скорость печати (зн/мин)");
                break;
            case 1:
                series.setName("Процент ошибок в тексте (%)");
                break;
        }
        _chart.setAnimated(false);
        for(int i = 0; i<list.size(); i+=2){
            series.getData().add(new XYChart.Data(list.get(i+1).toString(), (float)list.get(i)*100.0));
            
        }
        _chart.getData().add(series);
        _chart.requestFocus();
    }
    
    public String buildLastStat(List<String> list){
        StringBuilder builder = new StringBuilder();
        if(!list.isEmpty()){
            builder.append("Пользователь: ").append(list.get(0)).append("\n");
            builder.append("Дата: ").append(list.get(3)).append("\n");
            builder.append("Скорость печати (зн/м): ").append(list.get(1)).append("\n");
            float percent = Float.parseFloat(list.get(2))*(float)100.0;
            builder.append("Часть ошибок при вводе (%): ").append(new DecimalFormat("#0.00").format(percent)).append("\n");
        }
        return builder.toString();
    }
}
