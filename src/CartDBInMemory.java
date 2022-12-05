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

    public HashMap<String, ArrayList<String>> userMap = new HashMap<String, ArrayList<String>>();


    // Constructors
    public CartDBInMemory(String baseFolder) {
        //passes the folder name to load data
        this.loadDataFromFiles(baseFolder);
    }

    public void loadDataFromFiles(String baseFolder) {
        File f = new File(baseFolder);
        File[] filteredFiles = f.listFiles(new FilenameFilter() {
            // Override generates automatically when typing accepts
            // gets an array of files ending with .db

            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".db");
            }

            
        });
        if (filteredFiles.length == 0) {
            return;
        }
        for (File file : filteredFiles) {
            String userKey = file.getName().replace(".db", "");
            // Since filename == username, use filename (minus .db for Key)
            // Read the content of the file
            // ReadFile(file) returns an ArrayList of Strings
            this.userMap.put(userKey, ReadFile(file));
        }
    }

    /**
     * @param f
     * @return
     */
    public ArrayList<String> ReadFile(File f) {
        // Read files
        ArrayList<String> dataList = new ArrayList<String>();
        try {
            BufferedReader bf = new BufferedReader(new FileReader(f));
            String line;
            while ((line = bf.readLine()) != null) {
                line = line.trim();
                dataList.add(line);
                // read each line and add to ArrayList
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
