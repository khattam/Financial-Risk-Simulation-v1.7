package banking;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Bank {
    private double assets;
    private double liabilities;
    private double influenceSpread;
    private double vulnerabilityIndex;
    
    private double loansGivenSum;
    private double loansTakenSum;
    
    private List<Loan> loansGiven;
    private List<Loan> loansReceived;

    
    
 // Constants for the calculation (can be adjusted as needed)
    private static final double ALPHA = 1.0;
    private static final double BETA = 1.0;
    private static final double LAMBDA = 0.5; // Example damping factor

    // Constructor to set assets and liabilities
    public Bank(double assets, double liabilities) {
        this.assets = assets;
        this.liabilities = liabilities;
        this.influenceSpread = 0.0;
        this.vulnerabilityIndex = 0.0;
        this.loansGivenSum = 0.0;
        this.loansTakenSum = 0.0;
        
        this.loansGiven = new ArrayList<>();
        this.loansReceived = new ArrayList<>();
    }

    // Getters and setters for the properties
    // ... (To be implemented)

    // Method to give a loan
//    public void giveLoan(Bank receiver, double amount) {
//        this.assets -= amount;
//        this.loansGivenSum += amount;
//        receiver.receiveLoan(amount);
//    }
    
    // Method to give a loan
    public void giveLoan(Bank receiver, double amount, int cycles) {
        // Decrease assets and increase loansGivenSum
        this.assets -= amount;
        this.loansGivenSum += amount;
        
        // Add the loan to the list of given loans
        loansGiven.add(new Loan(this, receiver, amount, cycles));

        // The receiver processes the received loan
        receiver.receiveLoan(this, amount, cycles);
    }

//    // Method to receive a loan
//    public void receiveLoan(double amount) {
//        this.assets += amount;
//        this.loansTakenSum += amount;
//    }
    
    //Method to receive a loan
    public void receiveLoan(Bank lender, double amount, int cycles) {
        // Increase assets and loansTakenSum
        this.assets += amount;
        this.loansTakenSum += amount;
        
        // Add the loan to the list of received loans
        loansReceived.add(new Loan(lender, this, amount, cycles));
    }
    
    // Process the cycle: repay loans and decrement cycles
    public void processCycle() {
        // Repay loans where remainingCycles is 0
        loansGiven.removeIf(loan -> {
            if (loan.remainingCycles <= 0) {
                repayLoan(loan.borrower, loan.amount);
                return true; // Remove the loan from the list
            }
            return false;
        });

        // Decrement remainingCycles for all loans
        loansGiven.forEach(loan -> loan.remainingCycles--);
        loansReceived.forEach(loan -> loan.remainingCycles--);
    }

    
   
    
    
// // Repay a loan Medhansh
//    private void repayLoan(Bank receiver, double amount) {
//        // Adjust assets and loansGivenSum for the lender
//        this.assets += amount;
//        this.loansGivenSum -= amount;
//
//        // Adjust assets and loansTakenSum for the borrower
//        receiver.assets -= amount;
//        receiver.loansTakenSum -= amount;
//        
//        
//        // Print the repayment information
//        System.out.printf("Loan to %s from %s worth %.2f is repaid!\n", 
//                          receiver.getClass().getSimpleName(), 
//                          this.getClass().getSimpleName(), 
//                          amount);
//    }
    
    private void repayLoan(Bank receiver, double amount) {
      // Adjust assets and loansGivenSum for the lender
      this.assets += amount;
      this.loansGivenSum -= amount;

      // Adjust assets and loansTakenSum for the borrower
      receiver.assets -= amount;
      receiver.loansTakenSum -= amount;
      
      
      // Print the repayment information
//      System.out.printf("Loan to %s from %s worth %.2f is repaid!\n", 
//                        receiver.getClass().getSimpleName(), 
//                        this.getClass().getSimpleName(), 
//                        amount);
  }

 //

	public double getAssets() { 
		return this.assets;
	}

	public double getLiabilities() {
		// TODO Auto-generated method stub
		return this.liabilities;
	}
	
	
	
	public void calculateInfluenceSpread(List<Bank> banks, double[][] adjacencyMatrix, int bankId) {
	    double directInfluence = 0.0;
	    double indirectInfluence = 0.0;

	    // Loop through all banks to calculate the direct and indirect influence
	    for (int j = 0; j < banks.size(); j++) {
	        // Add the weight of the direct financial connection to direct influence
	        directInfluence += adjacencyMatrix[bankId][j]; // This adds up the total loan amount or financial connection bank 'i' has given to other banks

	        // Now, calculate the indirect influence
	        for (int k = 0; k < banks.size(); k++) {
	            // Add up the product of the financial connections 'i-j' and 'j-k' to represent the indirect influence from 'i' to 'k' through 'j'
	            indirectInfluence += adjacencyMatrix[bankId][j] * adjacencyMatrix[j][k];
	        }
	    }

	    // Normalize the direct and indirect influence to the same scale
	    double totalInfluence = directInfluence + indirectInfluence;
	    if (totalInfluence > 0) {
	        // Normalize the influences to ensure they are proportionate to their actual values
	        directInfluence = (directInfluence / totalInfluence) * directInfluence;
	        indirectInfluence = (indirectInfluence / totalInfluence) * indirectInfluence;
	    }

	    // Scale the direct and indirect influence to contribute 55% and 45% respectively to the total influence
	    double scaledDirectInfluence = directInfluence * 0.55;
	    double scaledIndirectInfluence = indirectInfluence * 0.45;

	    // The final influence spread is the sum of scaled direct and indirect influences
	    influenceSpread = scaledDirectInfluence + scaledIndirectInfluence;
	}
	
	
    public void calculateVulnerabilityIndex(List<Bank> banks, double[][] adjacencyMatrix, int bankId) {
        double networkRisk = 0.0;
        double totalWeight = 0.0; // Track the sum of all outgoing connections weights
        
        // Calculate the total weight of outgoing connections
        for (int j = 0; j < banks.size(); j++) {
            totalWeight += adjacencyMatrix[bankId][j];
        }

        // Calculate the network risk based on connections to other banks, normalized by the total weight
        for (int j = 0; j < banks.size(); j++) {
            double liabilityRatio = banks.get(j).getLiabilities() / banks.get(j).getAssets();
            networkRisk += (adjacencyMatrix[bankId][j] * liabilityRatio) / (totalWeight > 0 ? totalWeight : 1);
        }

        // Combine the bank's own liabilities/assets ratio with the normalized network risk
        vulnerabilityIndex = ALPHA * (this.liabilities / this.assets) + BETA * networkRisk;
    }
    
    
	
    public double getInfluenceSpread() {
        return this.influenceSpread;
    }
    
    public double getVulnerabilityIndex() {
        return this.vulnerabilityIndex;
    }
    
    // Method to get a list of repaid loans
    public List<Loan> getRepaidLoans() {
        return loansGiven.stream()
                         .filter(loan -> loan.remainingCycles <= 0)
                         .collect(Collectors.toList());
    }
}
