package banking;

public class Loan {
    private Bank lender;
     Bank borrower;
    double amount;
    int remainingCycles;

    // Existing constructor...
    public Loan(Bank lender, Bank borrower, double amount, int remainingCycles) {
        this.lender = lender;
        this.borrower = borrower;
        this.amount = amount;
        this.remainingCycles = remainingCycles;
    }

    // Getter for the lender
    public Bank getLender() {
        return lender;
    }

    // Getter for the borrower
    public Bank getBorrower() {
        return borrower;
    }

    // Getter for the amount
    public double getAmount() {
        return amount;
    }

    // Getter for remaining cycles if necessary
    public int getRemainingCycles() {
        return remainingCycles;
    }
}
