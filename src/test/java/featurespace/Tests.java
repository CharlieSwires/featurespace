package featurespace;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class Tests {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test() throws Exception {
        Path path = Paths.get("events.json");
        
        try (BufferedWriter writer = Files.newBufferedWriter(path)) 
        {
            Random rnd = new Random(0);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'");
            int transactionId = 0;
            int depositId = 0;
            writer.write("[");
            for (int i = 0; i < 2000; i++) {
                switch((int)(rnd.nextDouble()*2.0)) {
                case 0://deposit
                    writer.write("{ \"eventType\" : \"deposit\", "
                            + "\"depositId\" : \""+(depositId++)+"\", "
                            + "\"customerId\" : \"Cust"+(int)Math.round(rnd.nextDouble()*10.0)+"\", "
                            + "\"time\" : \""+formatter.format(new Date())+"\", "
                            + "\"amount\" : "+(int)Math.round(rnd.nextDouble()*100.0)+ " }");
                    break;
                case 1://transaction
                    writer.write("{ \"eventType\" : \"transaction\", "
                            + "\"transactionId\" : \""+(transactionId++)+"\", "
                            + "\"customerId\" : \"Cust"+(int)Math.round(rnd.nextDouble()*10.0)+"\", "
                            + "\"merchantId\" : \"Merch"+(int)Math.round(rnd.nextDouble()*10.0)+"\", "
                            + "\"time\" : \""+formatter.format(new Date())+"\", "
                            + "\"amount\" : "+(int)Math.round(rnd.nextDouble()*100.0)+ " }");
                    break;
                    default:
                        break;
                }
                Thread.sleep(Math.round(rnd.nextDouble()*10.0));
                if (i < 1999) {
                    writer.write(",\n");
                }
            }
            writer.write("]");

        }        
        Featurespace.runner();
        Assert.assertEquals("Cust5", Featurespace.top5customersHighestAve[0]);
        Assert.assertEquals("Cust2", Featurespace.top5customersHighestAve[1]);
        Assert.assertEquals("Cust9", Featurespace.top5customersHighestAve[2]);
        Assert.assertEquals("Cust10", Featurespace.top5customersHighestAve[3]);
        Assert.assertEquals("Cust4", Featurespace.top5customersHighestAve[4]);
        Assert.assertEquals("Merch4",Featurespace.top5merchantsHighestAve[0]);
        Assert.assertEquals("Merch8",Featurespace.top5merchantsHighestAve[1]);
        Assert.assertEquals("Merch3",Featurespace.top5merchantsHighestAve[2]);
        Assert.assertEquals("Merch6",Featurespace.top5merchantsHighestAve[3]);
        Assert.assertEquals("Merch0",Featurespace.top5merchantsHighestAve[4]);
        Assert.assertEquals("Cust8",Featurespace.top5customersGreatestBalance[0]);
        Assert.assertEquals("Cust1",Featurespace.top5customersGreatestBalance[1]);
        Assert.assertEquals("Cust5",Featurespace.top5customersGreatestBalance[2]);
        Assert.assertEquals("Cust2",Featurespace.top5customersGreatestBalance[3]);
        Assert.assertEquals("Cust4",Featurespace.top5customersGreatestBalance[4]);
        Assert.assertEquals("Merch4",Featurespace.top5merchantsShortestTime[0]);
        Assert.assertEquals("Merch8",Featurespace.top5merchantsShortestTime[1]);
        Assert.assertEquals("Merch3",Featurespace.top5merchantsShortestTime[2]);
        Assert.assertEquals("Merch6",Featurespace.top5merchantsShortestTime[3]);
        Assert.assertEquals("Merch0",Featurespace.top5merchantsShortestTime[4]);

    }

}
