package banking;

import javafx.application.Application;
import javafx.stage.Stage;

public class Master extends Application {
    private static int simulationsCount = 1000; // Default number of simulations

    @Override
    public void start(Stage primaryStage) {
        if (getParameters().getRaw().size() > 0) {
            simulationsCount = Integer.parseInt(getParameters().getRaw().get(0)); // Get number of simulations from command line
        }

        System.out.println("Starting simulations...");
        for (int i = 0; i < simulationsCount; i++) {
            SimulationTest test = new SimulationTest();
            test.runSimulation(); // Run each simulation independently
        }
        System.out.println("All simulations completed.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
