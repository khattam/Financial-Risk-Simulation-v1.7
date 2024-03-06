package banking;

import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class GVIChartApplication extends Application {
    private static List<Double> gviData;

    public static void setData(List<Double> data) {
        gviData = data; // Static method to set data from simulation
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("GVI Trend Over Cycles");
        
        // Defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Cycles");
        yAxis.setLabel("GVI");
        
        // Creating the line chart
        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("GVI vs Cycles");
        	
        // Defining a series to display data
        XYChart.Series series = new XYChart.Series();
        series.setName("GVI Trend");
        
        // Populate the series with the pre-set data
        for (int i = 0; i < gviData.size(); i++) {
            series.getData().add(new XYChart.Data(i, gviData.get(i)));
        }
        
        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.getData().add(series);
        
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
