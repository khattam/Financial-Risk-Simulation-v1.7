package banking;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import javafx.application.Application;

public class SimulationTest {
    private static final int NUMBER_OF_BANKS = 80;
    private static final double MIN_ASSET_MULTIPLIER = 1.5;
    private static final double MAX_LOAN_AMOUNT = 20000;
    private static final int continuousCyclesLimit = 500;
    private static final double FAILURE_PROBABILITY = 0.005;
    private Simulation simulation;
    private Random random = new Random();
    private Map<Integer, List<String>> failuresByCycle = new HashMap<>(); // To store the record of failures by cycle

    public SimulationTest() {
        simulation = new Simulation(NUMBER_OF_BANKS);
    }

//    public void initialiseBanks() {
//        for (int i = 0; i < NUMBER_OF_BANKS; i++) {
//            double liabilities = generateRandomLiabilities();
//            double assets = liabilities * (1 + MIN_ASSET_MULTIPLIER * random.nextDouble());
//            simulation.addBank(assets, liabilities);
//        }
//    }

    private void collectData() {
        // Collect data at the end of each simulation run
        String data = String.format("%f,%f,%f,%f,%f,%f,%f,%f,%d,%d,%d,%f,%f,%f",
            this.simulation.getFailureRate(NUMBER_OF_BANKS),
            this.simulation.getGVI(),
            this.simulation.getAvgGVI(),
            this.simulation.getMaxGVI(),
            this.simulation.getMinGVI(),
            this.simulation.getTotalLoans(),
            this.simulation.getTotalAssets(),
            this.simulation.getTotalLiabilities(),
            NUMBER_OF_BANKS,
            this.simulation.getActiveBankCount(),
            this.simulation.getCycleCount(),
            Bank.ALPHA,
            Bank.BETA,
            Bank.LAMBDA
        );
        this.simulationData.add(data);
        this.exportResults(); // Export results after each run or collect to export later
    }

    
    private void initialiseBanks() {
        this.simulation = new Simulation(NUMBER_OF_BANKS); // Re-create simulation instance to reset
        for (int i = 0; i < NUMBER_OF_BANKS; i++) {
            double liabilities = this.generateRandomLiabilities();
            double assets = liabilities * (1 + MIN_ASSET_MULTIPLIER * this.random.nextDouble());
            this.simulation.addBank(assets, liabilities);
        }
    }
    
    private double generateRandomLiabilities() {
        return 50000 + (50000 * random.nextDouble());
    }

    public void createRandomDependencies() {
        int numberOfDependencies = (NUMBER_OF_BANKS * (NUMBER_OF_BANKS - 1)) / 4;
        for (int i = 0; i < numberOfDependencies; i++) {
            int bankId1 = random.nextInt(NUMBER_OF_BANKS);
            int bankId2 = random.nextInt(NUMBER_OF_BANKS);
            double amount = random.nextDouble() * MAX_LOAN_AMOUNT;
            if (bankId1 != bankId2 && simulation.canBankAffordLoan(bankId1, amount)) {
                simulation.createDependency(bankId1, bankId2, amount);
            }
        }
    }

    private List<String> simulationData = new ArrayList<>();
    private static final String outputFile = "C:\\Users\\khattam\\Documents\\ResearchData\\ResultsHQLAF4.csv";
  
 

    private void exportResults() {
        File file = new File(outputFile);
        boolean isNewFile = !file.exists();

        try (FileWriter writer = new FileWriter(file, true)) {
            if (isNewFile) {
                writer.append("FailureRate,GVI,AvgGVI,MaxGVI,MinGVI,TotalLoans,TotalAssets,TotalLiabilities,NumberOfBanks,ActiveBankCount,CycleCount,Alpha,Beta,Lambda\n");
            }
            for (String data : this.simulationData) {
                writer.append(data + "\n");
            }
            System.out.println("Results exported to CSV.");
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }
    

 
 
    
//    public void runSimulation() {
//        initialiseBanks();
//        simulation.displayGraphical();
//        simulation.displayConsole();
//        Scanner scanner = new Scanner(System.in);
//        boolean continuousMode = false;
//
//        while (true) {
//            System.out.println("Press 'ENTER' to proceed to the next cycle, 'c' for continuous mode, 'd' to display GVI list, 's' to show GVI for a specific cycle, 'p' to print all active banks and GVI, or 'q' to quit.");
//            String input = scanner.nextLine().trim();
//
//            if ("q".equalsIgnoreCase(input)) {
//                exportResults();
//                break;
//            } else if ("p".equalsIgnoreCase(input)) {
//                simulation.displayActiveBanks();
//                simulation.displayConsole(); // This method already prints the GVI.
//                continue;
//            }
//            
//            if("l".equalsIgnoreCase(input)) {
//                double ans = simulation.getFailureRate(NUMBER_OF_BANKS);
//                System.out.println("Failure Rate"+ans);
//                System.out.println("GVI is: "+simulation.gvi);
//            }
//
//            int cyclesToRun = 1;
//            if ("c".equalsIgnoreCase(input)) {
//                continuousMode = true;
//                cyclesToRun = continuousCyclesLimit;
//                System.out.println("Continuous mode: running " + cyclesToRun + " cycles.");
//            }
//
//            for (int i = 0; i < cyclesToRun; i++) {
//                createRandomDependencies();
//                simulation.processCycle();
//                maybeInduceFailure(simulation.getCycleCount());
//                simulation.calculateIndexes();
//                simulation.displayGraphical();
//                simulation.incrementCycleCount();
//                if (!simulation.anyBankActive()) {
//                    System.out.println("All banks have failed. Ending simulation at cycle " + simulation.getCycleCount());
//                    break; // Exit the loop and thus the method
//                }
//            }
//
//            // Record the data only after a full set of cycles have completed
//            String data = String.format("%f,%f,%f,%f,%f,%f,%f,%f,%d,%d,%d,%f,%f,%f",
//                simulation.getFailureRate(NUMBER_OF_BANKS),  // double - %f
//                simulation.getGVI(),                        // double - %f
//                simulation.getAvgGVI(),                     // double - %f
//                simulation.getMaxGVI(),                     // double - %f
//                simulation.getMinGVI(),                     // double - %f
//                simulation.getTotalLoans(),                 // double - %f
//                simulation.getTotalAssets(),                // double - %f
//                simulation.getTotalLiabilities(),           // double - %f
//                NUMBER_OF_BANKS,                            // int - %d
//                simulation.getActiveBankCount(),            // int - %d
//                simulation.getCycleCount(),                 // int - %d
//                Bank.ALPHA,                                 // double - %f
//                Bank.BETA,                                  // double - %f
//                Bank.LAMBDA);                               // double - %f
//            simulationData.add(data);
//        }
//        
//        scanner.close();
//    }

    public int CYCLES_PER_RUN = 500;
    public void runSimulation() {
        this.initialiseBanks(); // Initialize or reset banks
        this.simulation.displayGraphical();
        this.simulation.displayConsole();

        // Run a fixed number of cycles for this simulation instance
        for (int cycle = 0; cycle < CYCLES_PER_RUN; cycle++) {
            this.createRandomDependencies();
            this.simulation.processCycle();
            this.maybeInduceFailure(this.simulation.getCycleCount());
            this.simulation.calculateIndexes();
            this.simulation.displayGraphical();
            this.simulation.incrementCycleCount();
        }

        // After completing the cycles, collect and possibly export the data
        this.collectData();
    }



    private void maybeInduceFailure(int currentCycle) {
        if (currentCycle > 50) { // Ensure the first forced failure happens after 50 cycles
            double randomProbability = Math.random();
            if (randomProbability < FAILURE_PROBABILITY) {
                int bankIndex = random.nextInt(NUMBER_OF_BANKS); // Randomly select a bank
                System.out.println("Forcibly failing bank: " + bankIndex + " at cycle " + currentCycle);
                simulation.forceFailBank(bankIndex, currentCycle);
            }
        }
    }


    private void reportFailures() {
        System.out.println("Comprehensive Failure Report:");
        if (failuresByCycle.isEmpty()) {
            System.out.println("No failures reported.");
        } else {
            failuresByCycle.forEach((cycle, failures) -> {
                System.out.println("Cycle " + cycle + ": " + failures);
            });
        }
    }

    public static void main(String[] args) {
        Application.launch(GVIChartApplication.class, args);
    }

	public double getFailureRate() {
		return simulation.getFailureRate(NUMBER_OF_BANKS);
		
	}

	public double getGVI() {
		return simulation.gvi;
	}

	public double getAverageGVI() {
		return simulation.getAvgGVI();
	}

	public double getTotalLoansGiven() {
		return simulation.getTotalLoans();
		
	}

	public int getNumberOfBanks() {
		return NUMBER_OF_BANKS;
	}
    
    
    
}
