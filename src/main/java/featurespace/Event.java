package featurespace;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class Event {

    private String eventType;
    private String transactionId;
    private String customerId;
    private String depositId;
    private String merchantId;
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date time;
    private Double amount;
    
    public Event(String eventType, String transactionId, String customerId, String merchantId,
            Date time, Double amount, String depositId) {
        super();
        this.eventType = eventType;
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.merchantId = merchantId;
        this.time = time;
        this.amount = amount;
        this.depositId = depositId;
    }
    public Event() {

    }
    public String getEventType() {
        return eventType;
    }
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    public String getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public String getMerchantId() {
        return merchantId;
    }
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
    public Date getTime() {
        return time;
    }
    public void setTime(Date time) {
        this.time = time;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public String getDepositId() {
        return depositId;
    }
    public void setDepositId(String depositId) {
        this.depositId = depositId;
    }
    @Override
    public String toString() {
        return "Event [eventType=" + eventType + ", transactionId=" + transactionId
                + ", customerId=" + customerId + ", depositId=" + depositId + ", merchantId="
                + merchantId + ", time=" + time + ", amount=" + amount + "]";
    }
}
