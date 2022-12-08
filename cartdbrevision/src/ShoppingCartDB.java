package cartdbrevision.src;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ShoppingCartDB {

    // Constants

    // Constants (name is in caps)
    public static final String LOGIN = "login";
    public static final String ADD = "add";
    public static final String LIST = "list";
    public static final String SAVE = "save";
    public static final String EXIT = "exit";
    public static final String USERS = "users";
    public static final String DELETE = "delete";
    // Create a list of those constants
    public static final List<String> VALID_COMMANDS = Arrays.asList(
            LOGIN, SAVE, ADD, LIST, USERS, EXIT, DELETE);

    // Members

    private String currentUser;
    private String baseFolder;
    private CartDBInMemory db;


    // Constructor no input
    public ShoppingCartDB() {
        // if the user does not specify which directory, db is default
        this.baseFolder = "db";
        // Create an instance of CartDBInMemory to read and write files
        this.db = new CartDBInMemory(this.baseFolder);
    }

    // Constructor with file input
    public ShoppingCartDB(String baseFolder) {
        this.baseFolder = baseFolder;
        // Create an instance of CartDBInMemory to read and write files
        this.db = new CartDBInMemory(this.baseFolder);
    }



    // setupFiles method to create a directory if it does not exist

    public void setupFiles() {
        // Use the input String baseFolder to create a directory if it does not exist
        Path p = Paths.get(this.baseFolder);
        if(Files.isDirectory(p)) {
            System.out.println("Directory already exists");

        } else {
            try {
                Files.createDirectory(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Method to start program
    public void startCart() {
        System.out.println("Welcome to the multiuser shopping cart");
        String line;
        Scanner sc = new Scanner(System.in);
        boolean isQuit = false;

        while(!isQuit) {
            System.out.printf("> ");
            line = sc.nextLine().trim();
            System.out.println(">>> " + line);

            if(line.equalsIgnoreCase(EXIT)) {
                System.out.println("Exiting... ");
                isQuit = true;
            }

            if(!this.validateInput(line)) {
                System.out.println("Invalid input. Please enter a valid command.");
            } else {
                System.out.println("Processing command: " + line);
                this.processInput(line);
            }


        }
        sc.close();

    }

    // Method to validate input

    public boolean validateInput(String input) {

        String[] splitString = input.split(" ");
        String command = splitString[0].trim();

        return VALID_COMMANDS.contains(command);

    }

    // Method to process the command using switch
    public void processInput(String input) {
        Scanner sc = new Scanner(input);
        String command = sc.next().trim();

        switch(command) {
            case LOGIN:
            String username = sc.nextLine().trim();
            this.loginAction(username);
            System.out.println("Currently logged in as " + this.currentUser);
            break;

            case ADD:
            String stripSpace = sc.nextLine().replaceAll(" ", "");
                String[] items = stripSpace.trim().split(",");
                this.addAction(items);
                break;
            case DELETE:
                String dstripSpace = sc.nextLine().replaceAll(" ", "");
                String[] ditems = dstripSpace.trim().split(",");
                this.deleteAction(ditems);
                break;
            case LIST:
                this.listAction();
                break;
            case SAVE:
                this.saveAction();
                break;
            case USERS:
                this.usersAction();
                break;
            default:
                break;
        }

        sc.close();
    }

    // Methods for "login", "add", "list", "save", "user", "exit"

    public void loginAction(String username) {
        // if user is new, add user to the hashmap database
        if(!this.db.userMap.containsKey(username)) {
            this.db.userMap.put(username, new ArrayList<String>());
        }
        // set currentUser as the username
        this.currentUser = username;

    }

    public void addAction(String[] items) {
        // iterate over the items in the list and add it to the value under the key
        for(String item : items) {
            this.db.userMap.get(currentUser).add(item);
        }

    }

    public void deleteAction(String[] items) {
        for(String item : items) {
            try {

                int index = Integer.parseInt(item);

                try {
                    System.out.println(this.db.userMap.get(this.currentUser).get(index - 1) + " removed from cart");
                    this.db.userMap.get(this.currentUser).remove(index - 1);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                
            } catch (NumberFormatException e) {
                System.out.println(item + " removed from cart");
                this.db.userMap.get(this.currentUser).remove(item);
                
            }
        }


    }

    public void listAction() {
        // for current user, print a list of all the items
        int counter = 1;
        for(String item : this.db.userMap.get(this.currentUser)) {
            System.out.printf("%d. %s\n", counter, item);
            counter++;
        }

    }

    public void saveAction() {
        // Write files into directory
        // Format the filename of the output file
        // Pass the filename into the filewriter

        String outputFile = String.format("%s/%s.db",this.baseFolder, this.currentUser);

        // Start writing files
        try {
            FileWriter fw = new FileWriter(outputFile);

            for(String item : this.db.userMap.get(this.currentUser)) {
                fw.write(item + "\n");
            }

            System.out.println("Shopping cart saved for user: " + this.currentUser);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void usersAction() {
        // list all users (list all keys in the hashmap)
        int counter = 1;
        System.out.println("Current registered users: ");
        for(String user : this.db.userMap.keySet()) {
            System.out.printf("%d. %s\n", counter, user);
            counter++;

        }
        

    }

    
}
