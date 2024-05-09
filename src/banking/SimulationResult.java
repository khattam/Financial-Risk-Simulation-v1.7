package banking;

public class SimulationResult {
    private double failureRate;
    private double gvi;
    private double averageGVI;
    private double totalLoansGiven;
    private int numberOfBanks;
    private double alpha;
    private double beta;
    private double lambda; // Example damping factor

    public SimulationResult(double failureRate, double gvi, double averageGVI, double totalLoansGiven,
                            int numberOfBanks, double alpha, double beta, double lambda) {
        this.failureRate = failureRate;
        this.gvi = gvi;
        this.averageGVI = averageGVI;
        this.totalLoansGiven = totalLoansGiven;
        this.numberOfBanks = numberOfBanks;
        this.alpha = alpha;
        this.beta = beta;
        this.lambda = lambda;
        System.out.println("Created result");
    }

    public double getFailureRate() {
        return failureRate;
    }

    public double getGVI() {
        return gvi;
    }

    public double getAverageGVI() {
        return averageGVI;
    }

    public double getTotalLoansGiven() {
        return totalLoansGiven;
    }

    public int getNumberOfBanks() {
        return numberOfBanks;
    }

    public double getAlpha() {
        return alpha;
    }

    public double getBeta() {
        return beta;
    }

    public double getLambda() {
        return lambda;
    }
    
    
}
