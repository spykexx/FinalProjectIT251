import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Spykexx on 8/10/2017.
 */
public class product {//My product class which houses all of the attributes of the couches.
    private String[] eName = new String[15];
    private int[] eSize = new int[15];//Encapsulating and setting the attributes to private so that the rest of the application cannot access them without calling the mutator/accessor.
    private double[] ePrice = new double[15];

    product(String name, int size, double price, int itemNumber) { //class constructor passing in 3 parameters to set all of the attributes to user input
        setName(name, itemNumber);
        setSize(size, itemNumber);
        setPrice(price, itemNumber);

    }

    public String getName(int i) {//All methods to get attributes
        return eName[i];
    }

    public int getSize(int i) {
        return eSize[i];
    }

    public double getPrice(int i) {
        NumberFormat formatter = new DecimalFormat("#0.00"); //formatting number to 2 decimal places.
        return Double.parseDouble(formatter.format(ePrice[i]));

    }

    public void setName(String name, int i) {//all attributes to set attributes
        eName[i] = name;
    }

    public void setSize(int size, int i) {
        eSize[i] = size;
    }

    public void setPrice(double price, int i) {
        ePrice[i] = price;
    }


}
