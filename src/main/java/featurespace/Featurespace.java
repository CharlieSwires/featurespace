package featurespace;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class Featurespace {

    private static final int MAX_ITEMS = 5;
    private static String top5customersHighestAve[] = new String[MAX_ITEMS];
    private static String top5merchantsHighestAve[] = new String[MAX_ITEMS];
    private static String top5customersGreatestBalance[] = new String[MAX_ITEMS];
    private static String top5merchantsShortestTime[] = new String[MAX_ITEMS];
 
    private static HashMap<String,Merchant> merchantsStore;
    private static HashMap<String,Customer> customersStore;
 
    public class CustomDateDeserializer extends StdDeserializer<Date> {

        public CustomDateDeserializer() {
            this(null);
        }

        public CustomDateDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Date deserialize(JsonParser jsonparser, DeserializationContext context)
          throws IOException, JsonProcessingException {
            String date = jsonparser.getText();
            try {
                return Date.from( Instant.parse( date));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void main(String[] args) throws Exception{
        Event[] events = null;
        try {
            // create object mapper instance
            ObjectMapper mapper = new ObjectMapper();

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
        
        List<Customer> tempCustomers = (List<Customer>) customersStore.values();
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
        for (int i = 0; i < MAX_ITEMS && i < tempCustomers.size(); i++) {
            top5customersHighestAve[i] = tempCustomers.get(i).getCustomerId();
            System.out.println("Customer["+tempCustomers.get(i).getCustomerId()+"]"
                    + " Running Average = " + tempCustomers.get(i).getRunningAverage());
        }        
        List<Merchant> tempMerchants = (List<Merchant>) merchantsStore.values();
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
        for (int i = 0; i < MAX_ITEMS && i < tempMerchants.size(); i++) {
            top5merchantsHighestAve[i] = tempMerchants.get(i).getMerchantId();
            System.out.println("Merchant["+tempMerchants.get(i).getMerchantId()+"]"
                    + " Running Average = " + tempMerchants.get(i).getRunningAverage());
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
        for (int i = 0; i < MAX_ITEMS && i < tempCustomers.size(); i++) {
            top5customersGreatestBalance[i] = tempCustomers.get(i).getCustomerId();
            System.out.println("Customer["+tempCustomers.get(i).getCustomerId()+"]"
                    + " Balance = " + tempCustomers.get(i).getAmount());
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
        for (int i = 0; i < MAX_ITEMS && i < tempMerchants.size() && tempMerchants.get(i).isSevenExceeded(); i++) {
            top5merchantsShortestTime[i] = tempMerchants.get(i).getMerchantId();
            System.out.println("Merchant["+tempMerchants.get(i).getMerchantId()+"]"
                    + " getMinTime().getTime() (milliseconds) = " + tempMerchants.get(i).getMinTime().getTime());
        }


    }
}
