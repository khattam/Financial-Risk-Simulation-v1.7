package banking;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javafx.application.Application;

public class SimulationTest {
    private static final int NUMBER_OF_BANKS = 35;
    private static final double MIN_ASSET_MULTIPLIER = 1.5; // Ensures assets are always at least 50% greater than liabilities
    private static final double MAX_LOAN_AMOUNT = 20000; // Max loan amount for the random dependencies
    private Simulation simulation;
    private Random random = new Random();
    private static final int continuousCyclesLimit = 500; // Limit for continuous mode
    

    public SimulationTest() {
        simulation = new Simulation(NUMBER_OF_BANKS);
    }

    public void initialiseBanks() {
        for (int i = 0; i < NUMBER_OF_BANKS; i++) {
            double liabilities = generateRandomLiabilities();
            double assets = liabilities * (1 + MIN_ASSET_MULTIPLIER * random.nextDouble());
            simulation.addBank(assets, liabilities);
        } 
    }

    private double generateRandomLiabilities() {
        // This can be tailored to match realistic values for bank liabilities
        return 50000 + (50000 * random.nextDouble());
    }

    //Milestone 1 version
//    public void createRandomDependencies() {
//        for (int i = 0; i < NUMBER_OF_BANKS - 1; i++) {
//            double amount = random.nextDouble() * MAX_LOAN_AMOUNT;
//            // Ensure that the bank has enough assets before creating a dependency
//            if (simulation.canBankAffordLoan(i, amount)) {
//                simulation.createDependency(i, i + 1, amount);
//            }
//        }
//    }
    

    public void createRandomDependencies() {
        int numberOfDependencies = (NUMBER_OF_BANKS * (NUMBER_OF_BANKS - 1)) / 4; // Adjusted for more connections

        for (int i = 0; i < numberOfDependencies; i++) {
            int bankId1 = random.nextInt(NUMBER_OF_BANKS);
            int bankId2 = random.nextInt(NUMBER_OF_BANKS);
            double amount = random.nextDouble() * MAX_LOAN_AMOUNT;

            // Avoid self-loop and check if the bank can afford this loan
            if (bankId1 != bankId2 && simulation.canBankAffordLoan(bankId1, amount)) {
                simulation.createDependency(bankId1, bankId2, amount);
            }
        }
    }

 
    
//    public void runSimulation() {
//        initialiseBanks();
//        simulation.displayGraphical(); // Display the graph with just nodes
//        simulation.displayConsole(); // Display initial bank data
//
//        Scanner scanner = new Scanner(System.in);
//        while (true) {
//            System.out.println("Press ENTER to proceed to the next cycle, or type 'q' to quit. Also, enter 'd' to dispay GVI list");
//            String input = scanner.nextLine();
//
//            if ("q".equalsIgnoreCase(input)) {
//                break; // Exit the loop if 'q' is entered
//            }
//            
//            if ("d".equalsIgnoreCase(input)) {
//              simulation.getAllGVI();
//              continue;
//              }
//
//            // Create dependencies in every cycle
//            createRandomDependencies();
//
//            // Process the cycle, update indexes and display
//            simulation.processCycle();
//            simulation.calculateIndexes();
//            simulation.displayGraphical(); // Update the graph with current cycle data
//            simulation.displayConsole(); // Display bank data after cycle
//
//            // Increment the cycle count
//            simulation.incrementCycleCount();
//        }
//
//        scanner.close(); // Close the scanner resource
//    }
    
    public void runSimulation() {
        initialiseBanks();
        simulation.displayGraphical(); // Display the graph with just nodes
        simulation.displayConsole(); // Display initial bank data

        Scanner scanner = new Scanner(System.in);
       
        while (true) {
        	System.out.println("Press ENTER to proceed to the next cycle, 'c' for continuous mode, 'd' to display GVI list, 's' to show GVI for a specific cycle, or 'q' to quit.");
            String input = scanner.nextLine();

            if ("q".equalsIgnoreCase(input)) {
                break; // Exit the loop if 'q' is entered
            }

            if ("d".equalsIgnoreCase(input)) {
                List<Double> temp = simulation.getAllGVI();
                continue;
            }
            
            if ("s".equalsIgnoreCase(input)) {
                System.out.println("Enter cycle number to display GVI:");
                int cycleNumber = scanner.nextInt(); // Read cycle number
                // Consume the newline
                scanner.nextLine();
                // Check if the cycle number is within the list bounds
                if (cycleNumber >= 0 && cycleNumber < simulation.getAllGVI().size()) {
                    System.out.println("GVI at cycle " + cycleNumber + ": " + simulation.getAllGVI().get(cycleNumber));
                } else {
                    System.out.println("Cycle number out of bounds.");
                }
                continue;
            }

            int cyclesToRun = 1;
            if ("c".equalsIgnoreCase(input)) {
                cyclesToRun = continuousCyclesLimit;
            }

            for (int i = 0; i < cyclesToRun; i++) {
                if (i > 0) {
                    System.out.println("Cycle " + (simulation.getCycleCount() + 1) + " of " + cyclesToRun);
                }

                // Create dependencies in every cycle
                createRandomDependencies();

                // Process the cycle, update indexes and display
                simulation.processCycle();
                simulation.calculateIndexes();
                simulation.incrementCycleCount();

                if (i < cyclesToRun - 1) {
                    // For continuous mode, delay between cycles for readability
                    try {
                        Thread.sleep(100); // Delay for visibility, can be adjusted
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
            simulation.displayGraphical(); // Update the graph with current cycle data
            simulation.displayConsole(); // Display bank data after cycle
        }
        scanner.close(); // Close the scanner resource
    }

    
   

        
    
//    public static void main(String[] args) {
//        SimulationTest test = new SimulationTest();
//        test.runSimulation();
//    }
    
    public List<Double> runSimulationAndGetGVI() {
        runSimulation(); // This method should run the simulation logic
        return simulation.getAllGVI(); // This method should return the list of GVI values
    }

    
    public static void main(String[] args) {
        // Launch the JavaFX application.
        Application.launch(GVIChartApplication.class, args);
    }
    
 
}
