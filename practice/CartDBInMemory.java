package practice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CartDBInMemory {

    // Using a class to hold the Hashmap

    // Members
    public HashMap<String, ArrayList<String>> userMap = new HashMap<String, ArrayList<String>>();

    // Constructors

    public CartDBInMemory(String baseFolder) {
        this.loadDataFromFiles(baseFolder);


    }

    // Method to load data from database

    public void loadDataFromFiles(String baseFolder) {
        // Create filepath
        File f = new File(baseFolder);
        // Create a filter to filter files ending with .db and obtain them
        File[] filteredFiles = f.listFiles(new FilenameFilter() {
            // Override is automatically generated when typing 'accepts'
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".db");

            };
        });

        // if there are no files, return
        if(filteredFiles.length == 0) {
            return;
        }
        // for each file, obtain the key values set
        for(File file :filteredFiles) {
            String userKey = file.getName().replace(".db", "");
            //since file names are the keys, use them to make the keys
            this.userMap.put(userKey, readFile(file));
        }
        
    }

    public ArrayList<String> readFile(File f) {

        ArrayList<String> dataList = new ArrayList<String>();

        try {
            BufferedReader bf = new BufferedReader(new FileReader(f));
            String line;
            while((line = bf.readLine()) != null) {
                line = line.trim();
                dataList.add(line);
                // read each line and add to ArrayList
            }
            bf.close();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }

        return dataList;

    }
    
}
