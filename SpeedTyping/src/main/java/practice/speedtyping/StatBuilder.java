package practice.speedtyping;

import java.text.DecimalFormat;
import java.util.List;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class StatBuilder {
    private final LineChart _chart;
    
    public StatBuilder(LineChart chart){  
        _chart = chart;
    }
    
    public void buildChart(List<Object> list, int type){
        _chart.getData().clear();
        XYChart.Series series = new XYChart.Series();
        float multiplier = (float)1.0;
        switch(type){
            case 0:
                series.setName("Скорость печати (зн/мин)");
                multiplier = (float)1.0;
                break;
            case 1:
                series.setName("Процент ошибок в тексте (%)");
                multiplier = (float)100.0;
                break;
        }
        _chart.setAnimated(false);
        for(int i = 0; i<list.size(); i+=2){
            series.getData().add(new XYChart.Data(list.get(i+1).toString(), (float)list.get(i)*multiplier));
            
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
            builder.append("Процент ошибок в тексте (%): ").append(new DecimalFormat("#0.00").format(percent)).append("\n");
        }
        return builder.toString();
    }
    
    public String buildLastStat(Object[] result){
        String resStr;
        resStr = "Количество символов:" + result[0].toString()+"\n";
        resStr+= "Время прохождения:" + result[1].toString()+" секунд \n";
        resStr+= "Количество ошибок:" + result[2].toString()+"\n";
        resStr+= "Скорость:" + result[3].toString()+" (зн/м)\n";
        resStr+= "Процент ошибок:" + Float.parseFloat(result[4].toString())*100.0+" %\n";
        return resStr;
    }
}
