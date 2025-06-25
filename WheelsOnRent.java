import java.util.Scanner;
import java.util.Calendar;
import java.util.Date;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * While running the program for the first time, the cars.csv and RentCars.csv files will already have information of the cars.
 * This is required as per the program.
 * The entry.csv file contains credentials of user(username, password).
 * It will contain one username : project and its password : car123456
 * If you decide to sign up, then the new username and password entered by you will also be stored in this same file.
 * However if you directly want to login, you can do it using the above mentioned username and password which are already there in the file.
 * You can add as many users as you want by signing up multiple times. All their information will be stored in the entry.csv file only.
 * The necessary modifications in the files will be done by the program itself.
 * Like for example, if the quantity of some car XYZ is 2 and you buy one, then the quantity in the cars.csv file will become 1.
 */

class LoginResult {//This class is made separately to return the username after successful login as well as status of login
    private String username;
    private boolean success;//success is true if login was successful.

    public LoginResult(String username, boolean success) {
        this.username = username;
        this.success = success;
    }

    public String getUsername() {//returns username String.
        return username;
    }

    public boolean isSuccess() {//returns true if login was success.
        return success;
    }
}

class SellCar {//This class will handle the selling process.
    String name;
    String uname;

    SellCar(String name, String uname) {
        this.name = name;
        this.uname = uname;
    }

    void sellCar() throws IOException {// This method takes information about the car user wants to sell.
        Scanner s = new Scanner(System.in);

        System.out.println(
                "What is the kind of fuel of the car that you want to put on sale? Enter 'P' for Petrol, 'D' for Diesel, and 'E' for EV");
        String fuel = s.nextLine();
        System.out.println("Now enter the name of the car: ");
        String car = s.nextLine();
        String cost = "";
        System.out.println();

        try {// This block updates the "cars.csv" file to include the newly on-sale car.
            BufferedReader reader = new BufferedReader(new FileReader("cars.csv"));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            int flag = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4 && parts[0].trim().equalsIgnoreCase(car)
                        && parts[1].trim().equalsIgnoreCase(fuel)) {
                    int quantity = Integer.parseInt(parts[3].trim());
                    if (quantity > 0) {// If the car already exists in the list with the exact same price, increase its
                                       // quantity.
                        quantity++;
                    }
                    line = parts[0].trim() + "," + parts[1].trim() + "," + parts[2].trim() + "," + quantity;
                    flag = 1;
                    cost = parts[2];
                }

                stringBuilder.append(line).append("\n");// Append the line to the stringBuilder.
            }
            if (flag == 1)
                System.out.println("We suggest a sale price of: " + cost);

            if (flag == 0) {// This will happen when the user sells a car which was not already in the list.
                System.out.println(
                        "This model is not on our system already. Please enter the selling price you expect: ");
                String sp = s.nextLine();
                line = car + "," + fuel + "," + sp + "," + "1";// Then we make a new line for it and update the file.
                stringBuilder.append(line).append("\n");

            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter("cars.csv"));
            writer.write(stringBuilder.toString());// write to the "cars.csv" file.
            writer.close();
            System.out.println();
            System.out.println(
                    "Our agent will soon contact you to check the condition of your car and finalise the sale price! Thank You for choosing our platform!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            s.close();
        }
    }
}

class BuyCar extends Receipt {
    String name;
    String uname;

    BuyCar(String name, String uname) {
        super(name, uname);// invoke Constructor of superclass Receipt.
    }

    void BuyingTheCar() throws IOException {// This method takes information about what kind of cars does the user want.
        Scanner sc = new Scanner(System.in);
        String line = "";

        BufferedReader br = new BufferedReader(new FileReader("cars.csv"));
        System.out.println("What kind of a car are you looking to buy: \nP. Petrol\nD. Diesel\nE. EV");
        String fuel = sc.nextLine();
        System.out.println();
        System.out.println(
                "Please enter the model which you are interested to buy or enter 'A' to view all the available options: ");
        String car = sc.nextLine();
        if (car.equalsIgnoreCase("a")) {// If user enter 'a' or 'A', the list of all available cars is shown.
            while ((line = br.readLine()) != null) {
                String ar[] = line.split(",");
                if (fuel.equalsIgnoreCase(ar[1])) {// ar[1] is the fuel type of the car. If a given car matches the fuel
                                                   // entered by user, display it.
                    System.out.println(ar[0]);// ar[0] contains car name.
                }
            }
            System.out.println();
            System.out
                    .println("Would you like to purchase any of the available models? Enter Y for Yes and N to exit.");
            char deci = sc.next().charAt(0);// Ask user if he/she would like to purchase any of the models.
            if (deci == 'Y') {
                System.out.println();
                System.out.println("Which model would you like to purchase: ");// If yes, ask which one and continue to
                                                                               // searchFor(String fav).
                sc.nextLine();
                String fav = sc.nextLine();// fav is the model entered by user.
                searchFor(fav);
            }
        } else {
            searchFor(car);// If user directly enters model name instead of 'a', search for that model.
        }
        br.close();
    }

    void searchFor(String carName) throws IOException {// This method searches for a car entered by the user.
        Scanner sc = new Scanner(System.in);
        BufferedReader br = new BufferedReader(new FileReader("cars.csv"));
        String line1 = "";
        String cost = "";
        String car = "";
        int flag = 0;
        while ((line1 = br.readLine()) != null) {
            String parts[] = line1.split(",");
            if (parts.length == 4 && parts[0].trim().equalsIgnoreCase(carName)) {// "fav" is the model name entered by
                                                                                 // user. If a match is found, then
                // display its information.
                cost = parts[2];
                car = parts[0];
                flag = 1;
                System.out.println();
                System.out.println("We found a model as per your request: ");
                System.out.println("Model: " + car + "\nCost: " + cost + " Rupees");
                break;
            }
        }
        if (flag == 1) {// flag = 1 only if a match is found. Then we proceed to generating a bill once
                        // the user confirms the purchase.
                        System.out.println();
                        System.out.println(
                    "Are you sure you would like to purchase this car? Enter Y to generate receipt or N to exit.");
            char fin = sc.next().charAt(0);
            if (fin == 'Y') {
                System.out.println();
                decreaseQuantity(carName);
                display2();
                System.out.println("Model selected - " + car.toUpperCase());
                System.out.println("Cost: " + cost);
                double cst = Double.parseDouble(cost);
                System.out.println("GST payable: " + ((0.28) * cst));
                System.out.println("On-Road Price - " + (cst + (0.28) * cst));// Total Cost = Showroom Price + GST (28%
                                                                              // GST)
                System.out.println(
                        "Congratulations for your new car!!! Our agent will soon contact you for further processing of payments and document verification.");
            } else {
                System.out.println("Exiting...");
            }
        } else {
            System.out.println("Sorry! We do not have that model available on sale. :(");
        }
        br.close();
    }

    void decreaseQuantity(String carName) {// If a car is bought, this method will decrease its quantity by one and
                                           // update the file.
        try {
            BufferedReader reader = new BufferedReader(new FileReader("cars.csv"));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4 && parts[0].trim().equalsIgnoreCase(carName)) {// First identify which car's
                                                                                     // quantity has to be decremented.
                    int quantity = Integer.parseInt(parts[3].trim());
                    if (quantity > 0) {
                        quantity--;// decrement the quantity.
                        if (quantity != 0) {// rebuild and update the file by rebuilding each line one by one.
                            line = parts[0].trim() + "," + parts[1].trim() + "," + parts[2].trim() + "," + quantity;
                        } else {
                            continue;// If quantity becomes zero, we remove that line completely as we don't need to
                                     // show it.
                        }
                    } else {
                        System.out.println("Sorry! The selected model is sold out. :(");
                    }
                }
                stringBuilder.append(line).append("\n");// Append the line to stringBuilder.
            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter("cars.csv"));
            writer.write(stringBuilder.toString());// Write the lines to the file "cars.csv".
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

class RentCar extends Receipt {
    private String model;
    private int days;
    private final double suvRentPerDay = 3000;// The rent per day of each type is fixed. So, we used final keyword.
    private final double sedanRentPerDay = 2000;
    private final double hatchbackRentPerDay = 1800;

    RentCar(String model, int days, String name, String uname) {
        super(name, uname, model);// call Constructor of superclass Receipt.
        this.days = days;
    }

    static void displayRent(String type) throws IOException {// displays all the cars of the type that user entered that
                                                             // are available for rent.
        BufferedReader br = new BufferedReader(new FileReader("RentCars.csv"));
        String line = "";
        while ((line = br.readLine()) != null) {
            String ar[] = line.split(",");
            if (type.equalsIgnoreCase(ar[1].trim())) {// ar[1] contains the type of car('U','S','H').
                System.out.println(ar[0]);// ar[0] contains the car name.
            }
        }
        br.close();
    }

    void getDate() {// Get the date when the car is booked.
        Date date1 = new Date();
        System.out.println("Date of booking: " + date1);
    }

    void getLater(int x) {// Get the date when the car has to be returned.
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, x);
        System.out.println("Date  of returning: " + cal.getTime());
    }

    void FinalRent(String type) {// Calculate the total Rent andPrint the bill after renting a car according with
                                 // the type entered by user.
        double totalRent = 0;
        switch (type) {
            case "U":
                totalRent = suvRentPerDay * days;
                display();
                System.out.println("Rent per Day - " + suvRentPerDay);
                System.out.println("Number of Days - " + days);
                System.out.println("Total Rent - " + totalRent);
                getDate();
                getLater(days);
                System.out.println("You will have to show this receipt at our facility to collect the car.");
                break;

            case "S":
                totalRent = sedanRentPerDay * days;
                display();
                System.out.println("Rent per Day - " + sedanRentPerDay);
                System.out.println("Number of Days - " + days);
                System.out.println("Total Rent - " + totalRent);
                getDate();
                getLater(days);
                System.out.println("You will have to show this receipt at our facility to collect the car.");
                break;

            case "H":
                totalRent = hatchbackRentPerDay * days;
                display();
                System.out.println("Rent per Day - " + hatchbackRentPerDay);
                System.out.println("Number of Days - " + days);
                System.out.println("Total Rent - " + totalRent);
                getDate();
                getLater(days);
                System.out.println("You will have to show this receipt at our facility to collect the car.");
                break;
        }
    }

    boolean CheckCar(String fav) throws IOException {// This function checks if the car "fav" is available or not.
                                                     // Returns true if yes.
        BufferedReader br = new BufferedReader(new FileReader("RentCars.csv"));
        String line1 = "";

        String car = "";
        while ((line1 = br.readLine()) != null) {
            String cr[] = line1.split(",");
            car = cr[0];
            if (fav.equalsIgnoreCase(car)) {
                return true;
            }
        }
        br.close();
        return false;
    }
}

class Receipt {
    String name;
    String uname;
    String model;

    Receipt(String name, String uname, String model) {
        this.name = name;
        this.uname = uname;
        this.model = model;
    }

    Receipt(String name, String uname) {
        this.name = name;
        this.uname = uname;
    }

    Receipt() {
    }

    void display() {// For buying a car.
        System.out.println("Name: " + name + " \tUsername: " + uname);
        System.out.println("=======================================");
        System.out.println("Model selected - " + model.toUpperCase());
    }

    void display2() {// For renting a car.
        System.out.println("Name: " + name + " \tUsername: " + uname);
        System.out.println("=======================================");
    }
}

public class WheelsOnRent {
    static boolean loggedIn = false;
    static String username = "";

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("\033[0;36m____________________________________");
        System.out.println("\033[0;36m|                                   |");
        System.out.println("\033[0;36m| \033[0;34mHello!!! Welcome to \033[0;32mWheelsOnRent\033[0;34m. \033[0;36m|");
        System.out.println("\033[0;36m|___________________________________|");
        System.out.println();

        System.out.println("\033[0;33mPlease enter your name: ");
        String name = s.nextLine();

        while (!loggedIn) {
            System.out.println("Welcome to WheelsOnRent!!!");
            System.out.println("1. Sign up\n2. Login");//prompt the user whether he wants to signup or login.
            System.out.println();
            int choice = s.nextInt();

            if (choice == 1) {
                username = signUp(1);
                loggedIn = true;
            } else if (choice == 2) {
                LoginResult loginResult = Login();
                username = loginResult.getUsername();
                loggedIn = loginResult.isSuccess();
            } else {
                System.out.println("Invalid Choice Entered. Please try again.");
            }
        }

        System.out.println("\033[0;37mWhat brings you here today!!");
        System.out.println("\033[0;37m1. Buy a Car");
        System.out.println("\033[0;37m2. Rent a Car");
        System.out.println("\033[0;37m3. Sell a car");
        System.out.print("\033[0;37mEnter your choice: ");
        int dec = s.nextInt();

        switch (dec) {
            case 1:
                BuyCar buy = new BuyCar(name, username);
                try {
                    buy.BuyingTheCar();//invoke BuyingTheCar() method for further process.
                } catch (IOException e) {
                    System.out.println(e);
                }
                break;

            case 2:
                System.out.println(
                        "\033[0;37mWhat kind of a car do you want?\n\033[0;37m'U' for SUV\n\033[0;37m'S' for Sedan\n\033[0;37m'H' for Hatchback");
                s.nextLine();
                String a = s.nextLine();//a contains the kind of vehicle the user wants to rent.
                System.out.println("\033[0;37mFor how many days do you want to rent the car?");
                int days = s.nextInt();//Ask the duration of rent.
                System.out.println("\033[0;37mWe have the following models currently available to rent: ");
                try {
                    RentCar.displayRent(a);//This method will display all the available cars of type 'a'.
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("\033[0;37mWhich model would you like to rent from the currently available ones?");
                s.nextLine();
                String model = s.nextLine();
                System.out.println();
                RentCar car = new RentCar(model, days, name, username);
                try {
                    if (car.CheckCar(model)) {//If the car entered by the user exists, then calculate final rent and print a receipt.
                        car.FinalRent(a);
                    } else {
                        System.out.println("\033[0;37mThe requested model was not found.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case 3:
                SellCar sell = new SellCar(name, username);
                try {
                    sell.sellCar();//Invoke sellCar() method for further process.
                } catch (IOException e) {
                    System.out.println(e);
                }
                break;

            default:
                System.out.println("\033[0;37mAn invalid choice was entered. Exiting...");
        }
    }

    /* We are using a flag as an argument in signUp method because of the following situation:
     * Suppose user decided to login first and entered a username that does not exist in the file.
     * So, the program will ask him to signUp first.
     * After signing up succesfully, the program will ask him to login not once but twice.
     * This is because signUp() method is called from inside Login() and then, inside signUp() method too, we call Login() method.
     * So, Login() is called two times.
     * So, to avoid logging in two times, we use flag. 
     * When flag = 1, nothing irregular has happened and program will continue as it is.
     * But when flag = 0, it indicates that the method was called from inside Login() method and hence handle it accordingly.
     */

    public static String signUp(int flag) {//This method is for signUp of new user
        Scanner sign = new Scanner(System.in);
        System.out.println("New here?");

        System.out.println("Enter the username");
        String username = sign.nextLine();
        boolean userExists = checkIfUserExists(username);

        if (!userExists) {//If username does not already exists, prompt to enter password, otherwise ask user for another name.
            System.out.println("Username is available. Please enter a password to create a new account.");
            String password = sign.nextLine();
            System.out.println();
            addUser(username, password);
            System.out.println("User added successfully.");
            System.out.println("Now you can Login");
            System.out.println();
            if (flag == 1)
                return Login().getUsername();//Once signUp is succesful, ask user to login.
            else
                return "";
        } else {
            System.out.println("Username already exists!!! Please use a different username");
            System.out.println();
            return signUp(1);
        }
    }

    public static LoginResult Login() {//This method is for login of existing user.
        int loginAttempts = 0;
        int flag = 0;
        while (!loggedIn && loginAttempts < 3) {//Maximum login attempts are 3.
            Scanner login = new Scanner(System.in);

            System.out.print("Enter username: ");
            String username = login.nextLine();

            if (!checkIfUserExists(username)) {//If entered username does not exist, ask user to signUp first.
                System.out.println("User does not exist. Please sign up.");
                System.out.println();
                signUp(flag);
            }

            System.out.print("Enter password: ");//Else ask the password for verification
            String password = login.nextLine();
            System.out.println();
            if (verifyPassword(username, password)) {//If password is correct, login is successful
                System.out.println("Login successful!");
                System.out.println();
                loggedIn = true;
                return new LoginResult(username, true);//this line returns the username once the login is successful.
            } else {//If password is incorrect, ask user to reenter.
                loginAttempts++;
                System.out.println("Incorrect password. Please try again. You have " + (3 - loginAttempts)
                        + " attempts remaining.");
                System.out.println();
            }
        }

        if (!loggedIn) {//Once 3 attempts are used, exit the program.
            System.out.println("Maximum login attempts reached. Exiting program.");
            System.exit(0);
            return new LoginResult("", false);
        }
        return new LoginResult("", false);
    }

    private static boolean checkIfUserExists(String username) {//This method checks is username already exists in our file "entry.csv"
        try (BufferedReader br = new BufferedReader(new FileReader("entry.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");//split line using "," as delimiter.
                if (data.length > 0 && data[0].equals(username)) {//data[0] contains username and if it matches the username entered by user, return true.
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean verifyPassword(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader("entry.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");//split the line using "," as delimiter.
                if (data.length >= 2 && data[0].equals(username) && data[1].equals(password)) {
                    return true;//data[0] and data[1] contain username and password and if they both match user-entered credentials, return true.
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void addUser(String username, String password) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("entry.csv", true))) {
            writer.println(username + "," + password);//upon successful signUp, add new user and its credentials to the "entry.csv" file.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}