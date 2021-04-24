import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * This User class only has the username field in this example.
 * You can add more attributes such as the user's shopping cart items.
 */
public class User {

    private final String username;
    private ArrayList<String> myCartList;
    private ArrayList<Integer> myQty;
    private ArrayList<Double> myPrice;
    private double myTotal;

    public User(String username) {
        this.username = username;

    }
    public User(String username, ArrayList<String> ar, ArrayList<Integer> qty, ArrayList<Double> price){
        this.username = username;
        this.myCartList = ar;
        this.myQty = qty;
        this.myPrice = price;

    }
    public String getUsername(){return this.username;}

    public ArrayList<String> getMyCartList(){return this.myCartList;}
    public ArrayList<Integer> getMyQty(){return this.myQty;}
    public ArrayList<Double> getMyPrice(){return this.myPrice;}
    public double getMyTotal(){
        myTotal = 0;
        for(int i=0; i<myCartList.size(); i++){
            double temp = myPrice.get(i);
            System.out.println("myprice is:" + temp);
            System.out.println("qty is:" + myQty.get(i));
            myTotal += myQty.get(i) * myPrice.get(i);
            System.out.println("total:" + myTotal);
        }
        return myTotal;
    }

    public boolean addToCart(String id){
        double price = Math.random() * 20;
        BigDecimal b = new BigDecimal(price);

        // keep only 2 digits after decimal point
        double f1 = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
        if(myCartList==null) {
            myCartList = new ArrayList<String>();
            myQty = new ArrayList<Integer>();
            myPrice = new ArrayList<Double>();
            myCartList.add(id);
            myQty.add(1);
            myPrice.add(f1);
            myTotal = f1;
        }
        else{
            for(int i=0; i<myCartList.size(); i++){
                if(myCartList.get(i).equals(id)){
                    int temp = myQty.get(i);
                    temp++;
                    myQty.set(i, temp);
                    myTotal += temp;
                    return true;
                }
            }
            myCartList.add(id);
            myQty.add(1);
            myPrice.add(f1);
            myTotal += f1;
        }
        return true;
    }

    public boolean ifEmpty(){
        if(myCartList == null)
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", myCartList=" + myCartList +
                ", myQty=" + myQty +
                ", myPrice=" + myPrice +
                ", myTotal=" + myTotal +
                '}';
    }
}
