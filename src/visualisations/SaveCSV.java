package visualisations;

import information.Information;

public class SaveCSV {

    public SaveCSV(String filename, Information<String> listeElements) {
        try {
            java.io.FileWriter fw = new java.io.FileWriter(filename);
            for (int i = 0; i < listeElements.nbElements(); i++) {
                fw.write(listeElements.iemeElement(i).toString()+"\n");
            }
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }

    
}
