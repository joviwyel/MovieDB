
package models;

import java.sql.Date;
import java.util.Scanner;

public class Prompt {
    
    private static Scanner in;
    
    static {
        in = new Scanner(System.in);
    }
    
    public static String forWord(String prompt) {
        System.out.print(prompt + ": ");
        String response = in.next().trim();
        in.nextLine();
        return response;
    }
    
    public static String forString(String prompt) {
        System.out.print(prompt + ": ");
        // TODO: Will fail on empty password from user.
        return in.nextLine();
    }
    
    public static int forInt(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            if (in.hasNextInt()) {
                int response = in.nextInt();
                in.nextLine();
                return response;
            } else {
                System.out.println("Please enter an integer.");
            }
        }
    }
    
    public static Customer forCustomer() {
        
        /*
          # `id` INT NOT NULL AUTO_INCREMENT,
          `first_name` VARCHAR(50) NOT NULL,
          `last_name` VARCHAR(50) NOT NULL,
          `cc_id` VARCHAR(20) NOT NULL,
          `address` VARCHAR(200) NOT NULL,
          `email` VARCHAR(50) NOT NULL,
          `password` VARCHAR(20) NOT NULL,
        */
        Customer customer = new Customer();
        while (true) {
            
            System.out.println("\nPlease enter the customer's information:");
            // First and last name
            String fullNameInput = Prompt.forString("\tFull name");
            Utils.FullName fullName = new Utils.FullName();
            fullName.parse(fullNameInput);
            
            String firstName = fullName.firstName;
            String lastName = fullName.lastName;
            
            if (lastName.isEmpty()) {
                System.out.println("ERROR - Please provide at least one name.");
                continue;
            }
            
            
            // cc_id
            String ccId = Prompt.forString("\tCredit card id number");
            
            // address
            String address = Prompt.forString("\tAddress");
            
            // email
            String email = Prompt.forString("\tEmail");
            
            // password
            String password = Prompt.forString("\tPassword");
            
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            customer.setAddress(address);
            customer.setCcId(ccId);
            customer.setEmail(email);
            customer.setPassword(password);
            
            return customer;    
        }
    }
    
    
    public static Star forStar() {
        
        Star star = new Star();
        while (true) {
            
            // Handle first and last name.
            String fullName = Prompt.forString("\nEnter the star's first and last name");
            String[] fullNameSplit = fullName.trim().split("\\s+");
            String firstName = "";
            String lastName = "";
            String photoUrl = null;
            Date dob;
            
            if (fullNameSplit.length == 0) {
                System.out.println("ERROR - Stars must have at least one name.");
                continue;
            }
            
            // If star has only one name (e.g. Char or Mango),
            // sets it as last name and default first name to empty string.
            for (int i = 0; i < fullNameSplit.length - 1; i++) {
                firstName += fullNameSplit[i] + " ";
            }
            lastName = fullNameSplit[fullNameSplit.length - 1].trim();
            firstName = firstName.trim();
            
    
            dob = Prompt.forDate("Enter the star's DOB");
            
            // Handle the photo url
            String photoUrlFromUser = Prompt.forString("Enter the url of star's photo or \"null\" if unknown").trim();
            if (!photoUrlFromUser.equalsIgnoreCase("null")) {
                photoUrl = photoUrlFromUser;
            }
            
            star.setFirstName(firstName);
            star.setLastName(lastName);
            star.setDob(dob);
            star.setPhotoUrl(photoUrl);
            
            return star;
        }
    }
    
    // Allows null values if user enters "null"
    public static Date forDate(String prompt) {
        Date date = null;
        while (true) {
            try {
                String dateString = Prompt.forString(prompt + " or \"null\" if unknown");  // Format must be yyyy-[m]m-[d]d
                dateString = dateString.toLowerCase().trim();
                if (dateString.equals("null")) {
                    date = null;
                } else {
                    date = Date.valueOf(dateString);
                }
                return date;
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR - Date's must be of the form yyyy-[m]m-[d]d");
            }
        }
    }
}
