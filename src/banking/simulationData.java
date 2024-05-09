package banking;

public class simulationData {
    private double failureRate;
    private double gvi;
    private double avgGVI;
    private double maxGVI;
    private double minGVI;
    private double totalLoans;
    private double totalAssets;
    private double totalLiabilities;
    private int numberOfBanks;
    private int activeBankCount;
    private int cycleCount;

    public simulationData(double failureRate, double gvi, double avgGVI, double maxGVI, double minGVI, double totalLoans, double totalAssets, double totalLiabilities, int numberOfBanks, int activeBankCount, int cycleCount) {
        this.failureRate = failureRate;
        this.gvi = gvi;
        this.avgGVI = avgGVI;
        this.maxGVI = maxGVI;
        this.minGVI = minGVI;
        this.totalLoans = totalLoans;
        this.totalAssets = totalAssets;
        this.totalLiabilities = totalLiabilities;
        this.numberOfBanks = numberOfBanks;
        this.activeBankCount = activeBankCount;
        this.cycleCount = cycleCount;
    }

    @Override
    public String toString() {
        return String.format("%f,%f,%f,%f,%f,%f,%f,%d,%d,%d",
            failureRate, gvi, avgGVI, maxGVI, minGVI, totalLoans, totalAssets, totalLiabilities, numberOfBanks, activeBankCount, cycleCount);
    }
}
