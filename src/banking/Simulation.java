package banking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;


public class Simulation {
    private List<Bank> banks;
    private double[][] adjacencyMatrix;
    private Graph graph; // Member variable to hold the graph
    double gvi = 0.0;
    private int cycleCount = 0;
    private boolean displayed = false;
    private List<Double> allGVI = new ArrayList<>();
    private Random random = new Random();
    private Map<Integer, List<String>> failuresByCycle = new HashMap<>(); // Stores failures by cycle
    
    

    // Constructor initializes the adjacency matrix based on the number of banks
    public Simulation(int numberOfBanks) {
    	// Try to set the system property to a valid display backend
        try {
            System.setProperty("org.graphstream.ui", "swing");
        } catch (Exception e) {
            System.err.println("Failed to set GraphStream system property for UI: " + e.getMessage());
        }	
        banks = new ArrayList<>(); 
        adjacencyMatrix = new double[numberOfBanks][numberOfBanks];
        graph = new SingleGraph("Banking Network"); // Initialize the graph here
      
    }
    
    
    public void simulateFailures(int currentCycle) {
    	
        for (Bank bank : banks) {
            if (bank.isActive() && bank.getAssets() < bank.getLiabilities()) {
                bank.failBank();
                logFailure(currentCycle, "Bank failed: " + bank);
                System.out.println("This is due to starting failure");
                triggerCascade(currentCycle, bank);
            }
        }
    }


    

    private void triggerCascade(int currentCycle, Bank initialFailedBank) {
        Queue<Bank> queue = new LinkedList<>();
        queue.add(initialFailedBank);
        Set<Bank> visited = new HashSet<>();

        while (!queue.isEmpty()) {
            Bank failedBank = queue.poll();
            if (visited.contains(failedBank)) continue;
            visited.add(failedBank);
            int failedIndex = banks.indexOf(failedBank);

            for (int i = 0; i < banks.size(); i++) {
                if (adjacencyMatrix[failedIndex][i] > 0 && banks.get(i).isActive()) {
                    // Apply a reduced random damping factor to mitigate the cascade's impact
                    double dampingFactor = 0.01 + 0.05 * random.nextDouble();
                    double lossAmount = adjacencyMatrix[failedIndex][i] * dampingFactor;

                    // Adjust assets without allowing them to go negative
                    double newAssets = banks.get(i).getAssets() - lossAmount;
                    if (newAssets < 0) {
                        banks.get(i).adjustAssets(-banks.get(i).getAssets()); // Reset to zero
                    } else {
                        banks.get(i).adjustAssets(-lossAmount);
                    }

                    // Check if the bank's assets fall below its liabilities after adjustment
                    if (banks.get(i).getAssets() < banks.get(i).getLiabilities()) {
                        // Calculate the asset/liability ratio
                        double assetLiabilityRatio = banks.get(i).getAssets() / banks.get(i).getLiabilities();
                        // Log the failure with the asset/liability ratio if it indicates financial instability
                        if (assetLiabilityRatio < 1) {
                            logFailure(currentCycle, "Bank " + banks.get(i).getId() + " failed due to cascade with A/L ratio: " + String.format("%.2f", assetLiabilityRatio));
                            System.out.println("Bank " + banks.get(i).getId() + " failed due to cascade with A/L ratio: " + String.format("%.2f", assetLiabilityRatio));
                            banks.get(i).failBank(); // Mark the bank as failed
                            queue.add(banks.get(i)); // Add to the queue for further propagation of the cascade
                        }
                    }
                }
            }
        }
    }

    

   
    
    
    public double calculateFailureProbability(Bank bank) {
        double assetLiabilityRatio = bank.getAssets() / bank.getLiabilities();
        double baseProbability = 0.05; // Base probability of failure if assets equal liabilities

        if (assetLiabilityRatio > 1.0) {
            
            return baseProbability / assetLiabilityRatio;
        } else {
            // If liabilities are greater than or equal to assets, increase the probability of failure
            return baseProbability * (2 - assetLiabilityRatio);
        }
    }

    
    public void forceFailBank(int bankIndex, int currentCycle) {
        Bank bankToFail = banks.get(bankIndex);
        //System.out.println("BankIndex,    ID"+ bankIndex + "     " + bankToFail.getId());
        if (!bankToFail.isActive()) {
            return; // If the bank is already inactive, do nothing
        }
        System.out.println("Forcibly failing Bank " + bankToFail.getId() + " at cycle " + currentCycle);
        bankToFail.failBank();
        logFailure(currentCycle, "Forcibly failed bank: Bank " + bankToFail.getId());
        triggerCascade(currentCycle, bankToFail); // Trigger cascading failures from this failed bank
        bankToFail.failBank();
    }



  
    void logFailure(int cycle, String message) {
        failuresByCycle.computeIfAbsent(cycle, k -> new ArrayList<>()).add(message);
    }
    
    public int getCycleCount() {
        return cycleCount;
    }

    public void incrementCycleCount() {
        cycleCount++;
    }
    
    
    public List<Double> getAllGVI()
    {
    	System.out.println("***************Printing all GVI***************");
    	for(double key: allGVI) {
    		System.out.println(key);
    	}
    	System.out.println(allGVI.size());
    	return allGVI;
    }
    
    
 // Method to check if a bank can afford to give a loan
    public boolean canBankAffordLoan(int bankId, double loanAmount) {
        Bank bank = banks.get(bankId);
        return bank.getAssets() - loanAmount > bank.getLiabilities(); // Assets should remain greater than liabilities after the loan
    }
    

    // Add a new bank to the simulation
    public void addBank(double assets, double liabilities) {
        banks.add(new Bank(assets, liabilities));
    }

//    
 
    public void createDependency(int bankId1, int bankId2, double amount) {
        // Compose a unique ID for the edge
        String edgeId = bankId1 + "-" + bankId2;

        // Determine the number of cycles for the loan
        Random random = new Random();
        int cycles = random.nextInt(5) + 1; // This gives a number between 1 and 5
        if (random.nextFloat() < 0.5) { // At least 50% chance to be a 1-cycle loan
            cycles = 1;
        }
        if(cycles<1)
        System.out.println("Wanring 0000000000000");

        // Only add the edge if it does not exist
        if (graph.getEdge(edgeId) == null) {
            Bank bank1 = banks.get(bankId1);
            Bank bank2 = banks.get(bankId2);

            // Call the new giveLoan method with the cycles parameter
            bank1.giveLoan(bank2, amount, cycles);

            // Update the adjacency matrix
            adjacencyMatrix[bankId1][bankId2] += amount; // Use '+=' to sum up multiple dependencies

            // Check if nodes exist before adding an edge
            if (graph.getNode(Integer.toString(bankId1)) == null) {
                graph.addNode(Integer.toString(bankId1));
                Node node1 = graph.getNode(Integer.toString(bankId1));
                node1.setAttribute("ui.label", "Bank " + bankId1);
            }
            if (graph.getNode(Integer.toString(bankId2)) == null) {
                graph.addNode(Integer.toString(bankId2));
                Node node2 = graph.getNode(Integer.toString(bankId2));
                node2.setAttribute("ui.label", "Bank " + bankId2);
            }

            // Add the edge to the graph, with the label showing the loan amount
            Edge edge = graph.addEdge(edgeId, Integer.toString(bankId1), Integer.toString(bankId2), true);
            edge.setAttribute("ui.label", String.format("%.2f", amount));
        }
    }
    
   

   



    // Calculate influence spread for each bank
    public void calculateInfluenceSpread() {
        System.out.print("Hello");
    }

    // Calculate vulnerability index for each bank
    public void calculateVulnerabilityIndex() {
        // ... To be implemented
    }


    

    
    public void displayActiveBanks() {
        System.out.println("Active Banks:");
        banks.stream()
             .filter(Bank::isActive)
             .forEach(bank -> System.out.printf("Bank %d: A - %.2f, L - %.2f, IS - %.2f, VI - %.2f%n",
                                               bank.getId(), bank.getAssets(), bank.getLiabilities(),
                                               bank.getInfluenceSpread(), bank.getVulnerabilityIndex()));
    }

    
    public void displayConsole() {
//        System.out.println("Bank Data:");
        for (int i = 0; i < banks.size(); i++) {
            Bank bank = banks.get(i);
            if (!bank.isActive()) {
                continue; // Skip failed banks
            }
            Set<Integer> uniqueConnections = new HashSet<>();
            for (int j = 0; j < banks.size(); j++) {
                if (adjacencyMatrix[i][j] != 0) {
                    uniqueConnections.add(j);
                }
                if (adjacencyMatrix[j][i] != 0) {
                    uniqueConnections.add(j);
                }
            }
            String influenceSpreadStr = (bank.getInfluenceSpread() != 0.0) ? String.format("%.2f", bank.getInfluenceSpread()) : "NaN";
            String vulnerabilityIndexStr = (bank.getVulnerabilityIndex() != 0.0) ? String.format("%.2f", bank.getVulnerabilityIndex()) : "NaN";
            //System.out.printf("Bank %d: A - %.2f, L - %.2f, Connections - %d, IS - %s, VI - %s%n", 
               // i, bank.getAssets(), bank.getLiabilities(), uniqueConnections.size(), influenceSpreadStr, vulnerabilityIndexStr);
        }

        // Print GVI if calculated
        String gviStr = (gvi != 0.0) ? String.format("%.2f", gvi) : "NaN";
        System.out.println("Global Vulnerability Index (GVI): " + gviStr);
    }




    

    
    public void displayGraphical() {
        // Set the stylesheet for the graph
    	 // Set the cycle label
        graph.setAttribute("ui.label", "Cycle " + cycleCount);
        graph.setAttribute("ui.stylesheet", styleSheet());

        for (int i = 0; i < banks.size(); i++) {
            String nodeId = Integer.toString(i);
            if (graph.getNode(nodeId) == null) {
                graph.addNode(nodeId);
                Node node = graph.getNode(nodeId);
                node.setAttribute("ui.label", "Bank " + nodeId);

                // Set random positions
                double x = Math.random();
                double y = Math.random();
                node.setAttribute("xyz", x, y, 0);

                // Apply style directly to the node if needed
                node.setAttribute("ui.style", "size: 20px; fill-color: blue;");
            }
        }
                
     // Initialize viewer only if it has not been displayed
        if (!displayed) {
            Viewer viewer = graph.display();
            viewer.disableAutoLayout();
            displayed = true; // Set to true as the graph is now displayed
        }
    }


    private String styleSheet() {
        // Define your stylesheet here
        return "node { size: 20px; fill-color: blue; text-alignment: above; } " +
               "edge.loan { size: 2px; fill-color: red; } " +
               "edge.credit { size: 2px; fill-color: blue; } ";
    }


    
    // Method to calculate IS and VI for each bank, and then GVI for the sector
    public void calculateIndexes() {
        for (int i = 0; i < banks.size(); i++) {
            banks.get(i).calculateInfluenceSpread(banks, adjacencyMatrix, i);
            banks.get(i).calculateVulnerabilityIndex(banks, adjacencyMatrix, i);
        }
        gvi = calculateGlobalVulnerabilityIndex();
    }
    
 
    
    public double calculateGlobalVulnerabilityIndex() {
        double totalVI = 0.0;
        for (Bank bank : banks) {
            totalVI += bank.getVulnerabilityIndex();
        }
        double gvi = totalVI / banks.size();
        allGVI.add(gvi);
        GVIChartApplication.updateChart(cycleCount, gvi);
        return gvi;
    }
    
    
    public void processCycle() {
        for (Bank bank : banks) {
            if (bank.isActive() && bank.getAssets() < bank.getLiabilities()) {
                bank.failBank();
                triggerCascade(getCycleCount(), bank);
            }
        }
    }

    
// // Method to remove repaid loans from the graph
    private void removeRepaidLoansFromGraph() {
        // Iterate through each bank and remove the edges of repaid loans
        for (int i = 0; i < banks.size(); i++) {
            Bank bank = banks.get(i);
            List<Loan> repaidLoans = bank.getRepaidLoans(); // Get the list of repaid loans from the bank

            for (Loan loan : repaidLoans) {
                // Remove the edge from the graph
                String edgeId = getEdgeId(loan);
                if (graph.getEdge(edgeId) != null) {
                    graph.removeEdge(edgeId);
                }
            }
        }
    }
 
    public boolean anyBankActive() {
        for (Bank bank : banks) {
            if (bank.isActive()) {
                return true; // Return true immediately when an active bank is found
            }
        }
        return false; // Return false if no active banks are found
    }


    
 // Helper method to get edge ID from a loan
    private String getEdgeId(Loan loan) {
        int bankId1 = banks.indexOf(loan.getLender());
        int bankId2 = banks.indexOf(loan.getBorrower());
        return bankId1 + "-" + bankId2;
    }


    public double getFailureRate(int numberOfBanks) {
        if (numberOfBanks == 0) return 0.0; // Prevent division by zero

        long failedBanks = banks.stream()
                                .filter(bank -> !bank.isActive())
                                .count();

        return (double) failedBanks / numberOfBanks * 100.0;
    }

    
}

