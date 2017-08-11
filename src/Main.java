import javax.swing.*; //Here we import all needed packages
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.stream.*;

public class Main {

    public static void main(String[] args) {
        //Here we finally specify our window's settings and finally call it with .setVisible(true)
        Gui prod = new Gui();
        prod.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        prod.setSize(570, 520); //setting window size
        prod.setLocationRelativeTo(null);

        prod.setVisible(true); //calling our JFrame Panel

    }
}
class Gui extends JFrame { //We must extend JFrame to be able to use Swing and create a GUI
    //Here we declare all JFrame elements to be added to our panel
    private JMenuBar menuBar;
    private JMenu file;
    private JMenuItem newOrder;
    private JLabel labelName;
    private JLabel labelSize;
    private JLabel labelPrice;
    private JTextField fieldName;
    private JTextField fieldSize;
    private JTextField fieldPrice;
    private JButton button1;
    private JButton button2;
    private JTextArea userResult;
    private JTextArea userShipping;
    private JScrollPane userResultScroll;
    private JScrollPane userShippingScroll;
    private JLabel alertArea;
    int pressed = 0;
    int totalPressed = 0;
    String userOrder = "Product Name" + "\t" + "Product Seats" + "\t" + "Price" + "\n";
    double sumPrice = 0;
    double avgPrice = 0;
    private double shippingPrice = 0;
    private double totalPrice = 0;
    NumberFormat formatter = new DecimalFormat("$#.00");
    double totalSumOfOrders = 0;
    boolean shipPress;

    public Gui() {//This class sets up our GUI, constructs all elements, and adds them to the panel.
        super("Furniture"); //Changes title of our window
        JPanel master = new JPanel(new BorderLayout()); //Here we set up a master panel in order to use multiple layout types
        JPanel L1 = new JPanel(new GridLayout(4, 2, 5, 5));//We construct our first layout for the labels and fields
        JPanel L2 = new JPanel(new GridLayout(1, 1));//constructing our second layout housing our JTextArea
        menuBar = new JMenuBar();
        file = new JMenu("Order Options");
        newOrder = new JMenuItem("Start New Order");
        labelName = new JLabel("  Couch Name:");            //We begin constructing all of our elements
        labelSize = new JLabel("  Couch Seats:");
        labelPrice = new JLabel("  Couch Price:");
        fieldName = new JTextField(null, 10);
        fieldSize = new JTextField(null, 10);
        fieldPrice = new JTextField(null, 10);
        button1 = new JButton("Submit");
        button2 = new JButton("Shipping");
        userResult = new JTextArea(19, 10);
        userResultScroll = new JScrollPane(userResult);
        userShipping = new JTextArea(19, 0);
        userShippingScroll = new JScrollPane(userShipping);
        userResult.setEditable(false); //Set JTextArea so that it cannot be edited by the user at runtime
        userShipping.setEditable(false);
        userResult.setLineWrap(true);
        userShipping.setLineWrap(true);//Allow our JTextArea to word-wrap
        userResult.setWrapStyleWord(true);
        userShipping.setWrapStyleWord(true);//Forces JTextArea to wrap on a complete word
        button1.setDefaultCapable(true);
        userResult.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black));
        userShipping.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.black));
        file.add(newOrder);//add intermenu button
        menuBar.add(file);//add file to menu bar
        this.setJMenuBar(menuBar);//add a menu bar to master panel.
        alertArea = new JLabel("");

        L1.add(labelName); //Here we add all of our elements to the panel
        L1.add(fieldName);
        L1.add(labelSize);
        L1.add(fieldSize);
        L1.add(labelPrice);
        L1.add(fieldPrice);
        L1.add(button1);
        L1.add(button2);
        L2.add(userResultScroll);
        L2.add(userShipping);
        master.add(L1, BorderLayout.PAGE_START);//Adding first layout to master panel
        master.add(L2, BorderLayout.CENTER);//adding second layout to master panel
        this.getContentPane().add(master);//Forcing master panel to the set layout.
        userResult.setText(userOrder);
        master.add(alertArea, BorderLayout.PAGE_END); //adding alert to bottom of the application

        shipping zip = new shipping();
        button1.addActionListener(new ActionListener() {//Here we add button functionality which listens for a user click on the submit button
            @Override
            public void actionPerformed(ActionEvent e) {//This method overrides actionListener and tells it what to do after the click

                try { //here we begin our main user input validation. We use multiple catches to ensure the user doesn't enter the wrong data type, and that there are no array index issues.
                    if (fieldName.getText().matches("(\\D{1,12}\\s*)") && !fieldName.getText().matches("(\\s*)") && !fieldSize.getText().isEmpty() && !fieldPrice.getText().isEmpty()) {
                        //here we create a new object instance of our product class. We pass in all necessary variable references.
                        product userProduct = new product(fieldName.getText(), Integer.parseInt(fieldSize.getText()), Double.parseDouble(fieldPrice.getText()), pressed);
                        userResult.append(userProduct.getName(pressed) + "\t" + userProduct.getSize(pressed) + "\t" + formatter.format(userProduct.getPrice(pressed)) + "\n"); //appending new items to order
                        sumPrice += userProduct.getPrice(pressed); //incrementing sum of all items entered.
                        totalSumOfOrders += userProduct.getPrice(pressed);
                    } else {
                        JOptionPane.showMessageDialog(null, "Please fill all fields in correctly!"); //if data field is empty.
                        pressed--;
                        totalPressed--;
                    }
                } catch (NumberFormatException f) {
                    JOptionPane.showMessageDialog(null, "Please fill all fields in correctly!"); //catch all incorrect data types
                    pressed--;
                    totalPressed--;
                } catch (ArrayIndexOutOfBoundsException f) { //catch any array index issues.
                    JOptionPane.showMessageDialog(null, "Due to shipping constraints we can only ship 15 pieces of furniture at a time.\nPlease create another order if necessary.");
                    pressed--;
                    totalPressed--;
                }
                pressed++;
                totalPressed++;
                if (shipPress) { //if zipcode has been entered correctly, automatically press button2 to autoupdate totals.
                    button2.doClick();
                }
            }
        });

        button2.addActionListener(new ActionListener() { //This is the functionality for the shipping button.
            @Override
            public void actionPerformed(ActionEvent e) {

                if (zip.getZipcode() == "" || zip.getZipcode() == null && pressed != 0) { //if there was no zipcode previously entered, ask for zipcode in prompt.

                    zip.setZipcode(JOptionPane.showInputDialog(null, "Please enter zip code"));

                    if (zip.getZipcode().length() == 5 && zip.getZipcode().matches("\\d*")) { //if zipcode is 5 characters long and numerals, set the zipcode variable to user input.
                        computeAverageSales(pressed); //call average sales method
                        computeShippingCharges(zip.getZipcode()); //call shipping method
                        computeTotals();
                        shipPress = true;
                    } else {
                        zip.setZipcode(null); //if incorrectly entered, reset the zipcode to null, and alert user to reenter zipcode correctly.
                        JOptionPane.showMessageDialog(null, "Please make sure you enter a 5 digit zipcode.");
                    }
                } else if (pressed == 0) {
                    zip.setZipcode(null); //if incorrectly entered, reset the zipcode to null, and alert user to reenter zipcode correctly.
                    JOptionPane.showMessageDialog(null, "Please make sure you enter items into order!");
                } else {
                    computeAverageSales(pressed); //if zipcode was already entered correctly, update information
                    computeShippingCharges(zip.getZipcode());
                    computeTotals();
                }
                if ((totalSumOfOrders / totalPressed) > 200) {//Show dialog if the average price is over 200 dollars.
                    alertArea.setText("Congrats! the average price per item was over $200!"); //if over $200, turn green and congratulate
                    alertArea.setOpaque(true);
                    alertArea.setBackground(Color.GREEN);
                } else {
                    alertArea.setText("The average price per item is below $200"); //stay white, but note that sales are low.
                    alertArea.setOpaque(false);
                }
            }
        });

        newOrder.addActionListener(new ActionListener() { //Button listener for Start New Order. Clears fields, and order variables but stores data needed for totals.
            @Override
            public void actionPerformed(ActionEvent e) {
                zip.setZipcode(null);//set the zipcode to null
                userResult.setText(userOrder); //reset order TextArea
                userShipping.setText("");
                sumPrice = 0;
                pressed = 0;
                shipPress = false; //reset counter so that zipcode promt is asked.

            }
        });

    }

    public void computeShippingCharges(String zip) { //Here we take the shipping and zipcode data, figure out shipping cost and print it to shipping JTextArea
        shippingPrice = pressed * 20;
        totalPrice = sumPrice + shippingPrice;
        userShipping.append("The estimated shipping cost \nto " + zip + " will be: " + formatter.format(shippingPrice) + "\n\n");
        userShipping.append("The total cost of this order is: " + formatter.format(totalPrice) + "\n\n");
        button2.setText("Update Shipping Cost");
    }

    public void computeAverageSales(int pressed2) { //Here we take the data, and figure out the average price. We then print this to the shipping JTextArea
        avgPrice = sumPrice / (pressed2);
        userShipping.setText(String.format("The Average product price this order is: %s \n\n", formatter.format(avgPrice)));
        userShipping.append("The number of items purchased this order is: " + pressed2 + "\n\n");
    }

    public void computeTotals() {//here we print out the totals for all orders.

        userShipping.append("--------------------------------\n\n");
        userShipping.append("The average of all items sold is: " + formatter.format(totalSumOfOrders / totalPressed) + "\n\n");
        userShipping.append("The total amount of items sold is: " + totalPressed + "\n\n");
        userShipping.append("The total of all items sold is: " + formatter.format(totalSumOfOrders));
    }

}