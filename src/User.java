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
        myCartList = null;
        myQty = null;
        myPrice = null;
        myTotal = 0;
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
        System.out.println("getmytotal is being called");
        double myt = 0;
        if(myCartList!=null)
            System.out.println("myCartlist size:" + myCartList.size());
        else{
            System.out.println("null");
        }
        for(int i=0; i<myCartList.size(); i++){
            System.out.println("in my total for");
            double temp;
            System.out.println("in my total for");
            temp = myPrice.get(i);
            System.out.println("myprice is:" + temp);
            System.out.println("qty is:" + myQty.get(i));
            myt += myQty.get(i) * myPrice.get(i);
            System.out.println("total:" + myTotal);
        }
        return myt;
    }
    public boolean minCart(String id){
        for (int i=0; i<myCartList.size(); i++){
            if(myCartList.get(i).equals(id)){
                if(myQty.get(i) > 1){
                    int temp = myQty.get(i);
                    temp = temp -1 ;
                    myQty.set(i, temp);
                    myTotal = getMyTotal();
                    return true;
                }
                else{
                    myQty.remove(id);
                    myCartList.remove(i);
                    myPrice.remove(i);
                    myTotal = getMyTotal();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removeItem(String id){
        for(int i=0; i<myCartList.size(); i++){
            if(myCartList.get(i).equals(id)){
                myCartList.remove(i);
                myQty.remove(i);
                myPrice.remove(i);
                myTotal = getMyTotal();
                return true;
            }
        }
        return false;
    }
    public boolean addToCart(String id){
        double price = Math.random() * 20;
//        BigDecimal b = new BigDecimal(price);
//        // keep only 2 digits after decimal point
//        double f1 = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
        if(myCartList==null) {
            myCartList = new ArrayList<String>();
            myQty = new ArrayList<Integer>();
            myPrice = new ArrayList<Double>();
            myCartList.add(id);
            myQty.add(1);
            myPrice.add(price);
            myTotal = price;
            return true;
        }
        else{
            for(int i=0; i<myCartList.size(); i++){
                if(myCartList.get(i).equals(id)){
                    int temp = myQty.get(i);
                    temp = temp+1;
                    myQty.set(i, temp);
                    myTotal = myTotal + myPrice.get(i);
                    return true;
                }
            }
            myCartList.add(id);
            myQty.add(1);
            myPrice.add(price);
            myTotal = myTotal + price;
        }
        return true;
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
