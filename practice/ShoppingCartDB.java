package practice;

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

    // Constants (name is in caps)
    public static final String LOGIN = "login";
    public static final String ADD = "add";
    public static final String LIST = "list";
    public static final String SAVE = "save";
    public static final String EXIT = "exit";
    public static final String USERS = "users";
    // Create a list of those constants
    public static final List<String> VALID_COMMANDS = Arrays.asList(
            LOGIN, SAVE, ADD, LIST, USERS, EXIT);

    private CartDBInMemory db;
    private String currentUser;
    private String baseFolder;


    public ShoppingCartDB() { // Constructor when the user doesn't specify a base folder
        this.baseFolder = "db";
        
        // When instance is created, run the setupFiles to create a directory (or skip if folder exists)
        this.setupFiles();
        this.db = new CartDBInMemory(this.baseFolder);

    }

    public ShoppingCartDB(String baseFolder) { // Constructor when the user specifies a base folder
        this.baseFolder = baseFolder;
        this.db = new CartDBInMemory(this.baseFolder);
        // When instance is created, run the setupFiles to create a directory (or skip if folder exists)
        this.setupFiles();


    }

    public void setupFiles() {
        //Create a Path and then create a directory if it does not exist

        Path p = Paths.get(this.baseFolder);
        if(Files.isDirectory(p)) {
            // SKIP if directionary already exists
        } else {
            try {

                Files.createDirectory(p);
                
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }

    }

    // Method to start program
    public void startCart() {
        System.out.println("Welcome to the shopping cart");
        boolean isQuit = false;
        String line;
        Scanner sc = new Scanner(System.in);

        while(!isQuit) {

            System.out.printf("> ");
            // Get the input from the console
            line = sc.nextLine().trim();
            System.out.println(">>> " + line);

            if(line.equalsIgnoreCase(EXIT)) {
                System.out.println("Exiting...");
                isQuit = true;

            // Validate commands by checking with the list of constants
            if(!this.validateInput(line)) {
                System.out.println("Invalid Input. Please type a valid command.");
            } else {
                System.out.println("Processing: " + line);
                this.processInput(line);
            }

            }


        }
        sc.close();
    }

    public boolean validateInput(String input) {
        String[] splitString = input.split(" ");
        String command = splitString[0].trim();

        return VALID_COMMANDS.contains(command);
    }

    public void processInput(String input) {
        Scanner sc = new Scanner(input);
        String command = sc.next().trim(); // Gets the first word of the line

        switch (command) {
            case LOGIN:
                // Get the next line after the command
                String username = sc.nextLine().trim();
                this.loginAction(username);
                System.out.println("Currenly logged in as " + this.currentUser);
                break;

            case ADD:
                String[] items = sc.nextLine().trim().split(" ");
                this.addAction(items);
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

    public void loginAction(String username) {

        //Login adds key value pair into userMap database
        // If the Hashmap does not include the pair, create one and add to it
        if(!this.db.userMap.containsKey(username)) {
            this.db.userMap.put(username, new ArrayList<String>());
        }
        this.currentUser = username; // Set currentUser as input name

    }

    public void addAction(String[] items) {
        // iterate the items in the list and add it to the hashmap under current user
        for(String item : items) {
            this.db.userMap.get(this.currentUser).add(item.trim());
        }

    }

    public void listAction() {
        int counter = 1;

        for(String item : this.db.userMap.get(this.currentUser)) {
            System.out.printf("%d. %s\n", counter, item);
            counter++;
        }

    }

    public void saveAction() {
        // Writes file when saved
        // Allow user to specify a basefolder to save to, or it'll be a default one provided
        // e.g. basecart/name.db
        String outputFile = String.format("%s/%s.db", this.baseFolder, this.currentUser);

        // Start writing files
        try {

            FileWriter fw = new FileWriter(outputFile);

            for(String item : this.db.userMap.get(this.currentUser)) {
                fw.write(item + "\n");
            }

            fw.flush();
            fw.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void usersAction() {

        System.out.println("The following users are registered: ");
        int counter = 1;
        for(String key : this.db.userMap.keySet()) {
            System.out.printf("%d. %s", counter, key);
            counter++;
        }

    }
    
}
