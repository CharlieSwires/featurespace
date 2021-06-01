package featurespace;

public class Customer {
    private String customerId;
    private Double amount;
    private Double runningTotal = 0.0;
    private Double runningAverage = 0.0;
    private int numberOfTransactions =0;
    
    public Double addDeposit(Double amount) {
        this.amount += amount;
        return this.amount;
    }
    public Double addTx(Double amount) {
        this.amount -= amount;
        runningTotal += amount;
        return setRunningAverage(runningTotal / ((double) ++numberOfTransactions));
    }
    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public Double getRunningAverage() {
        return runningAverage;
    }
    public Double setRunningAverage(Double runningAverage) {
        this.runningAverage = runningAverage;
        return runningAverage;
    }
}
