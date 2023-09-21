package simulateur;

import information.Information;
import visualisations.SaveCSV;


public class GenerationTEB {

    public static void main(String[] args) {
    	Simulateur simulateur = null;

        Information<String> listeCSV = new Information <String>();
        listeCSV.add("SNRPB;TEB");



    	try {
            //pour nbRealisation, creer un simulateur et calculer le TEB
            for (int i = -40; i < 21 ; i++) {
                simulateur = new Simulateur(new String[]{"-form", "NRZ","-nbEch","100","-ampl","0f", "1f","-snrpb", Integer.toString(i),"-mess", "10000" });
                simulateur.execute();
                System.out.println("\r SNRPB : "+Integer.toString(i) +"  TEB : " + simulateur.calculTauxErreurBinaire());
                listeCSV.add(Integer.toString(i)+";"+String.format("%.6f", simulateur.calculTauxErreurBinaire()));
                
            }
    	}
    	catch (Exception e) {
    		System.out.println(e); 
    		System.exit(-1);
    	}  

        //sauvegarde du bruit dans un fichier CSV
		SaveCSV save = new SaveCSV("test.csv", listeCSV);
    }
}
