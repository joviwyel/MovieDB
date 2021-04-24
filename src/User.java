import java.util.ArrayList;


/**
 * This User class only has the username field in this example.
 * You can add more attributes such as the user's shopping cart items.
 */
public class User {

    private final String username;
    private ArrayList<String> myCartList;
    private ArrayList<Integer> myQty;
    public User(String username) {
        this.username = username;

    }
    public User(String username, ArrayList<String> ar, ArrayList<Integer> qty){
        this.username = username;
        this.myCartList = ar;
        this.myQty = qty;
    }
    public String getUsername(){return this.username;}

    public ArrayList<String> getMyCartList(){return this.myCartList;}
    public ArrayList<Integer> getMyQty(){return this.myQty;}

    public boolean addToCart(String id){
        if(myCartList==null) {
            myCartList = new ArrayList<String>();
            myQty = new ArrayList<Integer>();
            myCartList.add(id);
            myQty.add(1);
        }
        else{
            for(int i=0; i<myCartList.size(); i++){
                if(myCartList.get(i).equals(id)){
                    int temp = myQty.get(i);
                    temp++;
                    myQty.set(i, temp);
                    return true;
                }
            }
            myCartList.add(id);
            myQty.add(1);
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
                '}';
    }
}
