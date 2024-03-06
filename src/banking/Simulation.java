package banking;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

//    // Create a dependency between two banks
//    public void createDependency(int bankId1, int bankId2, double amount) {
//        Bank bank1 = banks.get(bankId1);
//        Bank bank2 = banks.get(bankId2);
//        bank1.giveLoan(bank2, amount);
//        adjacencyMatrix[bankId1][bankId2] = amount;
//        
//        // Check if nodes exist before adding an edge
//        if (graph.getNode(Integer.toString(bankId1)) == null) {
//            graph.addNode(Integer.toString(bankId1));
//            Node node1 = graph.getNode(Integer.toString(bankId1));
//            node1.setAttribute("ui.label", "Bank " + bankId1);
//        }
//        if (graph.getNode(Integer.toString(bankId2)) == null) {
//            graph.addNode(Integer.toString(bankId2));
//            Node node2 = graph.getNode(Integer.toString(bankId2));
//            node2.setAttribute("ui.label", "Bank " + bankId2);
//        }
//        
//        Edge edge = graph.addEdge(bankId1 + "-" + bankId2, Integer.toString(bankId1), Integer.toString(bankId2), true);
//        edge.setAttribute("ui.label", String.format("%.2f", amount));
//    }
    
//    public void createDependency(int bankId1, int bankId2, double amount) {
//        // Compose a unique ID for the edge
//        String edgeId = bankId1 + "-" + bankId2;
//
//        // Only add the edge if it does not exist
//        if (graph.getEdge(edgeId) == null) {
//            Bank bank1 = banks.get(bankId1);
//            Bank bank2 = banks.get(bankId2);
//            bank1.giveLoan(bank2, amount);
//            adjacencyMatrix[bankId1][bankId2] += amount; // Use '+=' to sum up multiple dependencies
//
//            if (graph.getNode(Integer.toString(bankId1)) == null) {
//                graph.addNode(Integer.toString(bankId1));
//                Node node1 = graph.getNode(Integer.toString(bankId1));
//                node1.setAttribute("ui.label", "Bank " + bankId1);
//            }
//            if (graph.getNode(Integer.toString(bankId2)) == null) {
//                graph.addNode(Integer.toString(bankId2));
//                Node node2 = graph.getNode(Integer.toString(bankId2));
//                node2.setAttribute("ui.label", "Bank " + bankId2);
//            }
//
//            Edge edge = graph.addEdge(edgeId, Integer.toString(bankId1), Integer.toString(bankId2), true);
//            edge.setAttribute("ui.label", String.format("%.2f", amount));
//        }
//    }
    
 
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

    // Display function to show the graph
    // ... To be implemented
//    public void displayConsole() {
//        System.out.println("Bank Data:");
//        for (int i = 0; i < banks.size(); i++) {
//            Bank bank = banks.get(i);
//            // Check if IS and VI have been calculated (are not zero)
//            String influenceSpreadStr = (bank.getInfluenceSpread() != 0.0) ? String.format("%.2f", bank.getInfluenceSpread()) : "NaN";
//            String vulnerabilityIndexStr = (bank.getVulnerabilityIndex() != 0.0) ? String.format("%.2f", bank.getVulnerabilityIndex()) : "NaN";
//
//            System.out.printf("Bank %d: A - %.2f L - %.2f IS - %s VI - %s%n",
//                i, bank.getAssets(), bank.getLiabilities(), influenceSpreadStr, vulnerabilityIndexStr);
//        }
//        
//        // Only print GVI if it's been calculated
//        
//            System.out.println("Global Vulnerability Index (GVI):"+gvi);
//       }
    
//    public void displayConsole() {
//        System.out.println("Bank Data:");
//        for (int i = 0; i < banks.size(); i++) {
//            Bank bank = banks.get(i);
//            // Count the number of connections for the current bank
//            int connections = 0;
//            for (int j = 0; j < banks.size(); j++) {
//                if (adjacencyMatrix[i][j] != 0) {
//                    connections++;
//                }
//                if (adjacencyMatrix[j][i] != 0) { // Assuming undirected graph
//                    connections++;
//                }
//            }
//
//            // Prepare strings for IS and VI
//            String influenceSpreadStr = (bank.getInfluenceSpread() != 0.0) ? String.format("%.2f", bank.getInfluenceSpread()) : "NaN";
//            String vulnerabilityIndexStr = (bank.getVulnerabilityIndex() != 0.0) ? String.format("%.2f", bank.getVulnerabilityIndex()) : "NaN";
//
//            // Print out the bank details including the number of connections
//            System.out.printf("Bank %d: A - %.2f, L - %.2f, Connections - %d, IS - %s, VI - %s%n", 
//                i, bank.getAssets(), bank.getLiabilities(), connections, influenceSpreadStr, vulnerabilityIndexStr);
//        }
//
//        // Print GVI if it's been calculated
//        String gviStr = (gvi != 0.0) ? String.format("%.2f", gvi) : "NaN";
//        System.out.println("Global Vulnerability Index (GVI): " + gviStr);
//    }
    
    public void displayConsole() {
        System.out.println("Bank Data:");
        for (int i = 0; i < banks.size(); i++) {
            Bank bank = banks.get(i);

            // Calculate the number of unique connections for the current bank
            Set<Integer> uniqueConnections = new HashSet<>();
            for (int j = 0; j < banks.size(); j++) {
                if (adjacencyMatrix[i][j] != 0) {
                    uniqueConnections.add(j);
                }
                if (adjacencyMatrix[j][i] != 0) {
                    uniqueConnections.add(j);
                }
            }

            // Prepare strings for IS and VI
            String influenceSpreadStr = (bank.getInfluenceSpread() != 0.0) ? String.format("%.2f", bank.getInfluenceSpread()) : "NaN";
            String vulnerabilityIndexStr = (bank.getVulnerabilityIndex() != 0.0) ? String.format("%.2f", bank.getVulnerabilityIndex()) : "NaN";

            // Print out the bank details including the number of unique connections
            System.out.printf("Bank %d: A - %.2f, L - %.2f, Connections - %d, IS - %s, VI - %s%n", 
                i, bank.getAssets(), bank.getLiabilities(), uniqueConnections.size(), influenceSpreadStr, vulnerabilityIndexStr);
        }

        // Print GVI if it's been calculated
        String gviStr = (gvi != 0.0) ? String.format("%.2f", gvi) : "NaN";
        System.out.println("Global Vulnerability Index (GVI): " + gviStr);
    }



    
//    
//    public void displayGraphical() {
//        for (int i = 0; i < banks.size(); i++) {
//            String nodeId = Integer.toString(i);
//            if (graph.getNode(nodeId) == null) {
//                graph.addNode(nodeId);
//                Node node = graph.getNode(nodeId);
//                node.setAttribute("ui.label", "Bank " + nodeId);
//
//                // Set random positions
//                double x = Math.random();
//                double y = Math.random();
//                node.setAttribute("xyz", x, y, 0);
//            }
//        }
//
//        // Viewer configuration
//        Viewer viewer = graph.display();
//        viewer.disableAutoLayout();
//
//
//    }
    
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
    
 // Method to calculate the GVI for the banking sector
    public double calculateGlobalVulnerabilityIndex() {
        double totalVI = 0.0;
        for (Bank bank : banks) {
            totalVI += bank.getVulnerabilityIndex();
        }
        allGVI.add(1.0*(totalVI/banks.size()));
        return totalVI / banks.size();
    }
    
    
 // Method to process a cycle
    public void processCycle() {
        // Process the cycle for each bank
        for (Bank bank : banks) {
            bank.processCycle();
        }
        // Remove repaid loans from the graph
        removeRepaidLoansFromGraph();
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
    
//    private void removeRepaidLoansFromGraph() {
//        for (int i = 0; i < banks.size(); i++) {
//            Bank bank = banks.get(i);
//            List<Loan> repaidLoans = bank.getRepaidLoans();
//
//            for (Loan loan : repaidLoans) {
//                String edgeId = getEdgeId(loan);
//                Edge edge = graph.getEdge(edgeId);
//                if (edge != null) {
//                    // Highlight the edge
//                    edge.setAttribute("ui.style", "fill-color: red; size: 3px;");
//                    try {
//                        Thread.sleep(500); // Delay for visibility, can be adjusted
//                    } catch (InterruptedException e) {
//                        Thread.currentThread().interrupt(); // Handle interruption
//                    }
//                    // Remove the edge
//                    graph.removeEdge(edgeId);
//                }
//            }
//        }
//    }
    
    

    
 // Helper method to get edge ID from a loan
    private String getEdgeId(Loan loan) {
        int bankId1 = banks.indexOf(loan.getLender());
        int bankId2 = banks.indexOf(loan.getBorrower());
        return bankId1 + "-" + bankId2;
    }
    
}

