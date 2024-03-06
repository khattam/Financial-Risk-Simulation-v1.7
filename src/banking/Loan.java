	package banking;
	
	public class Loan {
	        Bank lender;
	        Bank borrower;
	        double amount;
	        int remainingCycles;
	
	        Loan(Bank lender, Bank borrower, double amount, int remainingCycles) {
	            this.lender = lender;
	            this.borrower = borrower;
	            this.amount = amount;
	            this.remainingCycles = remainingCycles;
	        }
	
			public Bank getLender() {
				return lender;
			}
	
			public Bank getBorrower() {
				return borrower;
			}
	    
	
	}
