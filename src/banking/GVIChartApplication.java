//package banking;
//
//import javafx.application.Application;
//import javafx.application.Platform;
//import javafx.scene.Scene;
//import javafx.scene.chart.LineChart;
//import javafx.scene.chart.NumberAxis;
//import javafx.scene.chart.XYChart;
//import javafx.stage.Stage;
//
//public class GVIChartApplication extends Application {
//    private static XYChart.Series<Number, Number> series;
//
//    @Override
//    public void start(Stage stage) {
//        final NumberAxis xAxis = new NumberAxis();
//        final NumberAxis yAxis = new NumberAxis();
//        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
//        xAxis.setLabel("Number of Cycles");
//        yAxis.setLabel("GVI");
//        
//        series = new XYChart.Series<>();
//        series.setName("GVI Trend");
//        lineChart.getData().add(series);
//        
//
//        Scene scene = new Scene(lineChart, 800, 600);
//        scene.getStylesheets().add(getClass().getResource("chart-styles.css").toExternalForm());
//
//        stage.setTitle("GVI Trend Over Cycles");
//        stage.setScene(scene);
//        stage.show();
//
//        new Thread(() -> {
//            SimulationTest test = new SimulationTest();
//            test.runSimulation();
//        }).start();
//    }
//
//    public static void updateChart(int cycle, double gviValue) {
//        Platform.runLater(() -> {
//            series.getData().add(new XYChart.Data<>(cycle, gviValue));
//        });
//    }
//    
//    public static void main(String[] args) {
//        launch(args);
//    }
//}

package banking;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;


//public class GVIChartApplication extends Application {
//    private static XYChart.Series<Number, Number> series = new XYChart.Series<>();
//    private static XYChart.Series<Number, Number> badDataSeries = new XYChart.Series<>();
//
//    @Override
//    public void start(Stage stage) {
//        final NumberAxis xAxis = new NumberAxis();
//        final NumberAxis yAxis = new NumberAxis();
//        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
//        
//        xAxis.setLabel("Number of Cycles");
//        yAxis.setLabel("GVI");
//        
//        series.setName("GVI Trend");
//        badDataSeries.setName("Bad Data Points");
//
//        lineChart.getData().addAll(series, badDataSeries);
//        
//        Scene scene = new Scene(lineChart, 800, 600);
//        scene.getStylesheets().add(getClass().getResource("chart-styles.css").toExternalForm());
//        
//        stage.setTitle("GVI Trend Over Cycles");
//        stage.setScene(scene);
//        stage.show();
//
//        new Thread(() -> {
//            SimulationTest test = new SimulationTest();
//            test.runSimulation();
//        }).start();
//    }
    
    public class GVIChartApplication extends Application {
        private static XYChart.Series<Number, Number> series = new XYChart.Series<>();
        private static XYChart.Series<Number, Number> badDataSeries = new XYChart.Series<>();
        private LineChart<Number, Number> lineChart;

        @Override
        public void start(Stage stage) {
            // Initialize axes and line chart
            final NumberAxis xAxis = new NumberAxis();
            final NumberAxis yAxis = new NumberAxis();
            lineChart = new LineChart<>(xAxis, yAxis);

            // Setup ComboBox for view mode selection
            ComboBox<String> viewModeComboBox = new ComboBox<>(FXCollections.observableArrayList("All Data", "Only Good Data", "Only Bad Data"));
            viewModeComboBox.setValue("All Data"); // Default value
            viewModeComboBox.setOnAction(event -> updateViewMode(viewModeComboBox.getValue()));

            // Setup chart
            setupChart();

            // Layout
            VBox vbox = new VBox(viewModeComboBox, lineChart);
            Scene scene = new Scene(vbox, 800, 600);
            scene.getStylesheets().add(getClass().getResource("chart-styles.css").toExternalForm());

            stage.setTitle("GVI Trend Over Cycles");
            stage.setScene(scene);
            stage.show();

            // Start simulation on a separate thread
            new Thread(() -> {
                SimulationTest simulationTest = new SimulationTest();
                simulationTest.runSimulation(); // Make sure this method is properly managing threads for UI update
            }).start();
        }


        private void setupChart() {
            series.setName("GVI Trend");
            badDataSeries.setName("Bad Data Points");
            lineChart.getData().addAll(series, badDataSeries);
        }

        private void updateViewMode(String viewMode) {
            lineChart.getData().clear();
            switch (viewMode) {
                case "Only Good Data":
                    lineChart.getData().add(series);
                    break;
                case "Only Bad Data":
                    lineChart.getData().add(badDataSeries);
                    break;
                default:
                    lineChart.getData().addAll(series, badDataSeries);
                    break;
            }
        }
        
//        private void updateViewMode(String viewMode) {
//            Platform.runLater(() -> {
//                // Clear all series first to reset state
//                lineChart.getData().clear();
//
//                // Then add series back based on the selected mode
//                switch (viewMode) {
//                    case "Only Good Data":
//                        if (!series.getChart().equals(lineChart)) {
//                            lineChart.getData().add(series);
//                        }
//                        break;
//                    case "Only Bad Data":
//                        if (!badDataSeries.getChart().equals(lineChart)) {
//                            lineChart.getData().add(badDataSeries);
//                        }
//                        break;
//                    default:
//                        if (!series.getChart().equals(lineChart)) {
//                            lineChart.getData().add(series);
//                        }
//                        if (!badDataSeries.getChart().equals(lineChart)) {
//                            lineChart.getData().add(badDataSeries);
//                        }
//                        break;
//                }
//            });
//        }


    public static void updateChart(int cycle, double gviValue) {
        // Example condition for bad data point (e.g., gviValue < 0 or gviValue > threshold)
        boolean isBadData = gviValue < 0 || gviValue > 100; // Adjust the threshold as per your criteria

        Platform.runLater(() -> {
            if (!isBadData) {
                series.getData().add(new XYChart.Data<>(cycle, gviValue));
            } else {
                XYChart.Data<Number, Number> badDataPoint = new XYChart.Data<>(cycle, gviValue);
                badDataSeries.getData().add(badDataPoint);
                // Apply style class directly to the bad data point
                badDataPoint.getNode().getStyleClass().add("bad-data-symbol");
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}

