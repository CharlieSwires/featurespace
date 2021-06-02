package featurespace;

import java.time.Instant;
import java.util.Date;

public class Merchant {
    private static final int MIN_LIST = 7;
    private String merchantId;
    private Double amount = 0.0;
    private Date[] time = new Date[MIN_LIST]; // Cyclic list
    private int timeIndexLast = 0;
    private Date minTime = Date.from(Instant.parse("4000-06-02T14:20:56.000Z"));
    private boolean sevenExceeded = false;
    private Double runningTotal = 0.0;
    private Double runningAverage = 0.0;
    private int numberOfTransactions =0;
    
    public Double addTx(Double amount) {
        this.amount += amount;
        runningTotal += amount;
        return setRunningAverage(runningTotal / ((double) ++numberOfTransactions));
    }

    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public String getMerchantId() {
        return merchantId;
    }
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
    public Date[] getTime() {
        return time;
    }
    public void setTime(Date[] time) {
        this.time = time;
    }
    public int getTimeIndex() {
        return timeIndexLast;
    }
    public void setTimeIndex(int timeIndex) {
        this.timeIndexLast = timeIndex;
    }
    public Date addTime(Date time) {
       this.time[timeIndexLast] = time;
       if (!sevenExceeded && timeIndexLast >= MIN_LIST -1) {
           timeIndexLast++;
           sevenExceeded = true;
           timeIndexLast = 0;
           for(int i = 0; i < 3;i++) {
               if (minTime.getTime() > this.time[i+4].getTime() - this.time[i].getTime()) {
                   minTime = new Date(this.time[i+4].getTime() - this.time[i].getTime());
               }
           }
       } else if (sevenExceeded) {
           int previous = (timeIndexLast - 4) < 0 ? timeIndexLast - 4 + MIN_LIST : timeIndexLast - 4;
           if (minTime.getTime() > this.time[timeIndexLast].getTime() - this.time[previous].getTime()) {
               minTime = new Date(this.time[timeIndexLast].getTime() - this.time[previous].getTime());
           }
           timeIndexLast = timeIndexLast+1 >= MIN_LIST ? 0 : timeIndexLast + 1;

       } else {
           timeIndexLast++;
       }
       if (sevenExceeded) {
          return minTime; 
       } else {
           return null;
       }
    }

    public Double getRunningAverage() {
        return runningAverage;
    }

    public Double setRunningAverage(Double runningAverage) {
        this.runningAverage = runningAverage;
        return runningAverage;
    }

    public int getTimeIndexLast() {
        return timeIndexLast;
    }

    public void setTimeIndexLast(int timeIndexLast) {
        this.timeIndexLast = timeIndexLast;
    }

    public Date getMinTime() {
        return minTime;
    }

    public void setMinTime(Date minTime) {
        this.minTime = minTime;
    }

    public boolean isSevenExceeded() {
        return sevenExceeded;
    }

    public void setSevenExceeded(boolean sevenExceeded) {
        this.sevenExceeded = sevenExceeded;
    }

    public Double getRunningTotal() {
        return runningTotal;
    }

    public void setRunningTotal(Double runningTotal) {
        this.runningTotal = runningTotal;
    }

    public int getNumberOfTransactions() {
        return numberOfTransactions;
    }

    public void setNumberOfTransactions(int numberOfTransactions) {
        this.numberOfTransactions = numberOfTransactions;
    }

    public static int getMinList() {
        return MIN_LIST;
    }
}
