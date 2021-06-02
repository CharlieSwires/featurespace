package featurespace;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Featurespace {

    public static final int MAX_ITEMS = 5;
    public static String top5customersHighestAve[] = new String[MAX_ITEMS];
    public static String top5merchantsHighestAve[] = new String[MAX_ITEMS];
    public static String top5customersGreatestBalance[] = new String[MAX_ITEMS];
    public static String top5merchantsShortestTime[] = new String[MAX_ITEMS];
 
    public static HashMap<String,Merchant> merchantsStore;
    public static HashMap<String,Customer> customersStore;
 
     
    public static void runner() throws Exception{
        Event[] events = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // convert JSON string to Event[] object
            events = mapper.readValue(Paths.get("events.json").toFile(), Event[].class);

 
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }  
        // print events
        System.out.println(events);

        merchantsStore = new HashMap<>();
        customersStore = new HashMap<>();

        //events to merchants and customers
        for (int i = 0; i < events.length; i++) {
            if (events[i].getEventType().equals("transaction")) {
                Customer customer = null;
                if (!customersStore.containsKey(events[i].getCustomerId())) {
                    //not created already
                    customer = new Customer();
                } else {
                    customer = customersStore.get(events[i].getCustomerId());
                }
                customer.setCustomerId(events[i].getCustomerId());
                customer.addTx(events[i].getAmount());
                customersStore.put(customer.getCustomerId(), customer);

                Merchant merchant = null;
                if (!merchantsStore.containsKey(events[i].getMerchantId())) {
                    //not created already
                    merchant = new Merchant();
                } else {
                    merchant = merchantsStore.get(events[i].getMerchantId());
                }
                merchant.setMerchantId(events[i].getMerchantId());
                merchant.addTx(events[i].getAmount());
                merchant.addTime(events[i].getTime());
                merchantsStore.put(merchant.getMerchantId(), merchant);
            } else if (events[i].getEventType().equals("deposit")) {
                Customer customer = null;
                if (!customersStore.containsKey(events[i].getCustomerId())) {
                    //not created already
                    customer = new Customer();
                } else {
                    customer = customersStore.get(events[i].getCustomerId());
                }
                customer.setCustomerId(events[i].getCustomerId());
                customer.addDeposit(events[i].getAmount());
                customersStore.put(customer.getCustomerId(), customer);
               
            } else throw new RuntimeException("Unknown event"+events[i].toString());
        }
        
        List<Customer> tempCustomers = new ArrayList<Customer>();
        for (Customer item :customersStore.values()) {
            tempCustomers.add(item);
        }
        Collections.sort(tempCustomers, new Comparator<Customer>() {

            @Override
            public int compare(Customer o1, Customer o2) {
                if (o1.getRunningAverage() > o2.getRunningAverage()) {
                    return -1;//reverse
                } else if (o1.getRunningAverage() < o2.getRunningAverage()) {
                    return 1;
                }
                return 0;
            }
            
        });
        for (int i = 0; i < MAX_ITEMS; i++) {
            top5customersHighestAve[i] = i < tempCustomers.size()?tempCustomers.get(i).getCustomerId():null;
            System.out.println("Customer["+(i < tempCustomers.size()?tempCustomers.get(i).getCustomerId():null)+"]"
                    + " Running Average = " + (i < tempCustomers.size()?tempCustomers.get(i).getRunningAverage():null));
        }        
        List<Merchant> tempMerchants = new ArrayList<Merchant>();
        for (Merchant item :merchantsStore.values()) {
            tempMerchants.add(item);
        }
        Collections.sort(tempMerchants, new Comparator<Merchant>() {

            @Override
            public int compare(Merchant o1, Merchant o2) {
                if (o1.getRunningAverage() > o2.getRunningAverage()) {
                    return -1;//reverse
                } else if (o1.getRunningAverage() < o2.getRunningAverage()) {
                    return 1;
                }
                return 0;
            }
        });
        for (int i = 0; i < MAX_ITEMS; i++) {
            top5merchantsHighestAve[i] = i < tempMerchants.size()?tempMerchants.get(i).getMerchantId():null;
            System.out.println("Merchant["+(i < tempMerchants.size()?tempMerchants.get(i).getMerchantId():null)+"]"
                    + " Running Average = " + (i < tempMerchants.size()?tempMerchants.get(i).getRunningAverage():null));
        }
        Collections.sort(tempCustomers, new Comparator<Customer>() {

            @Override
            public int compare(Customer o1, Customer o2) {
                if (o1.getAmount() > o2.getAmount()) {
                    return -1;//reverse
                } else if (o1.getAmount() < o2.getAmount()) {
                    return 1;
                }
                return 0;
            }
            
        });
        for (int i = 0; i < MAX_ITEMS; i++) {
            top5customersGreatestBalance[i] = i < tempCustomers.size()?tempCustomers.get(i).getCustomerId():null;
            System.out.println("Customer["+(i < tempCustomers.size()?tempCustomers.get(i).getCustomerId():null)+"]"
                    + " Balance = " + (i < tempCustomers.size()?tempCustomers.get(i).getAmount():null));
        } 
        Collections.sort(tempMerchants, new Comparator<Merchant>() {

            @Override
            public int compare(Merchant o1, Merchant o2) {
                if (o1.getMinTime().getTime() > o2.getMinTime().getTime()) {
                    return 1;
                } else if (o1.getMinTime().getTime() < o2.getMinTime().getTime()) {
                    return -1;
                }
                return 0;
            }
        });
        for (int i = 0; i < MAX_ITEMS; i++) {
            top5merchantsShortestTime[i] = i < tempMerchants.size() && tempMerchants.get(i).isSevenExceeded()?tempMerchants.get(i).getMerchantId():null;
            System.out.println("Merchant["+(i < tempMerchants.size() && tempMerchants.get(i).isSevenExceeded()?tempMerchants.get(i).getMerchantId():null)+"]"
                    + " getMinTime().getTime() (milliseconds) = " + (i < tempMerchants.size() && tempMerchants.get(i).isSevenExceeded()?tempMerchants.get(i).getMinTime().getTime():null));
        }


    }
    public static void main(String[] args) throws Exception{
        runner();
    }

}
