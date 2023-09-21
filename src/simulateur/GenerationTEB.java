package simulateur;

import information.Information;
import visualisations.SaveCSV;


public class GenerationTEB {

    public static void main(String[] args) {
    	Simulateur simulateur = null;
        Simulateur simulateur1 = null;
        Simulateur simulateur2 = null;

        Information<String> listeCSV = new Information <String>();
        listeCSV.add("SNRPB;TEB_NRZ;TEB_RZ;TEB_NRZT");



    	try {
            //pour nbRealisation, creer un simulateur et calculer le TEB
            for (int i = -10; i < 40 ; i++) {
                simulateur = new Simulateur(new String[]{"-form", "NRZ","-nbEch","100","-ampl","0f", "1f","-snrpb", Integer.toString(i),"-mess", "50000" });
                simulateur.execute();
                simulateur1 = new Simulateur(new String[]{"-form", "RZ","-nbEch","100","-ampl","0f", "1f","-snrpb", Integer.toString(i),"-mess", "50000" });
                simulateur1.execute();
                simulateur2 = new Simulateur(new String[]{"-form", "NRZT","-nbEch","100","-ampl","0f", "1f","-snrpb", Integer.toString(i),"-mess", "50000" });
                simulateur2.execute();
                System.out.println("\r "+i+Integer.toString(i)+";"+String.format("%.6f", simulateur.calculTauxErreurBinaire())+";"+String.format("%.6f", simulateur1.calculTauxErreurBinaire())+";"+String.format("%.6f", simulateur2.calculTauxErreurBinaire()));
                listeCSV.add(Integer.toString(i)+";"+String.format("%.6f", simulateur.calculTauxErreurBinaire())+";"+String.format("%.6f", simulateur1.calculTauxErreurBinaire())+";"+String.format("%.6f", simulateur2.calculTauxErreurBinaire()));
                
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
