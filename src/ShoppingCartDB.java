import java.io.File;
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

    // Constructors
    public ShoppingCartDB() {
        this.baseFolder = "db"; // default
        this.setup();
        this.db = new CartDBInMemory(this.baseFolder);
    }

    public ShoppingCartDB(String baseFolder) {
        this.baseFolder = baseFolder;
        this.setup();
        this.db = new CartDBInMemory(this.baseFolder);
    }

    // get the filepath, if folder already exists, skip, else create folder
    public void setup(){
        Path p = Paths.get(this.baseFolder);
        if (Files.isDirectory(p)) {
            // SKIP if directory already exits
        } else {
            try {
            Files.createDirectory(p);
            } catch (IOException e) {
                System.out.println("Error :" + e.getMessage());
            }
        }
    }

    // Start the program
    public void startShell() {
        System.out.println("Welcome to MultiUser Shopping Cart >> ");
        Scanner sc =  new Scanner(System.in);
        String line;
        boolean stop = false;

        while (!stop) {
            line = sc.nextLine().trim(); // Get the input and trims
            System.out.println("=> " + line);
            if (line.equalsIgnoreCase("exit")) {
                // If the line is a single word and is "exit", exit
                System.out.println("Exiting !!!");
                stop = true;
            }
            // Validate Command
            if (!this.ValidateInput(line)) {
                // Check List for valid inputs
                System.out.println("Invalid Input: ^^");
            } else {
                System.out.println("Processing : " + line);
                this.ProcessInput(line);
                // ProcessInput contains switch statements for all the different cases
            }
            
        }
        sc.close();
    }
    public boolean ValidateInput(String input) {
        String[] parts = input.split(" ");
        String command = parts[0].trim();
        // Scanner lsc = new Scanner(input);
        // String commad = lsc.next().trim()
        return VALID_COMMANDS.contains(command);
    }

    // Process command
    public void ProcessInput(String input) {
        Scanner sc  = new Scanner(input);
        String command = sc.next().trim();

        switch (command) {
            case LOGIN:
                String username = sc.nextLine().trim();
                this.LoginAction(username);
                System.out.println("Print - current logged in user" + this.currentUser);
                break;

            case LIST:
                this.ListAction();
                break;

            case ADD:
                String[] items = sc.nextLine().trim().split(",");
                this.AddAction(items);
                break;

            case SAVE:
                this.SaveAction();
                break;

            case USERS:
                this.usersAction();
                break;

            default:
                break;
        }

        sc.close();
    }

    public void LoginAction(String username) {
        // Login adds key value pair into userMap
        if (!this.db.userMap.containsKey(username)) {
            this.db.userMap.put(username, new ArrayList<String>());
        }
        this.currentUser = username;
    }

    public void AddAction(String[] items) {
        for (String item : items) {
            this.db.userMap.get(this.currentUser).add(item.trim());
        }
    }

    public void ListAction() {
        int counter = 1;
        for (String item : this.db.userMap.get(this.currentUser)) {
            System.out.printf("%d. %s\n", counter, item);
            counter++;

            
        }
    }

    public void SaveAction() {   
        // Writes file     
        // Prepare the filePath = "db/<username>.db"
        String outputFilename = String.format("%s/%s.db", 
        this.baseFolder, this.currentUser);

        try {
            FileWriter fw = new FileWriter(outputFilename);
            // Save the contents for this user in Map to a file.
            for (String item : this.db.userMap.get(this.currentUser)) {
                fw.write(item +"\n");
            }
            System.out.println("Shopping Cart saved for user: " + this.currentUser);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        
    }

    public void usersAction() {
        System.out.println("The following users are registered");
        int counter = 1;
        // iterate over the keySet of db
        for(String key : this.db.userMap.keySet()) {
            System.out.printf("%d. %s\n", counter, key);
            counter++;
        

        }

    }

}