package cartdbrevision.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CartDBInMemory {

    // Members
    // A hashmap containing all the users and their items
    public HashMap<String, ArrayList<String>> userMap = new HashMap<String, ArrayList<String>>();

    // Constructor
    // Takes in the baseFolder

    public CartDBInMemory(String baseFolder) {
        this.loadDataFromFiles(baseFolder);

    }

    // Method to load data from database
    public void loadDataFromFiles(String baseFolder) {
        // Create filepath
        File f = new File(baseFolder);

        // Create a filter to select all .db files
        File[] filteredFiles = f.listFiles(new FilenameFilter() {
            
            @Override
            public boolean accept(File dir, String name) {

                return name.endsWith(".db");
            }
        });

        // if there are no .db files, return error message
        if(filteredFiles.length == 0) {
            System.out.println("No .db files, are you sure you are in the correct folder?");
            return;
        }

        for(File file : filteredFiles) {
            // since the file names are names of users, use them to get the Key
            String userKey = file.getName().replace(".db", "");
            this.userMap.put(userKey, readFile(file));

        }
        
    }

    // Method to read files
    public ArrayList<String> readFile(File f) {

        ArrayList<String> dataList = new ArrayList<String>();

        // Read each line and add to dataList
        try {
            BufferedReader bf = new BufferedReader(new FileReader(f));
            String line;

            while(null != (line = bf.readLine())) {
                line = line.trim();
                dataList.add(line);
            }
            bf.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return dataList;
    
    }

   

    
}
