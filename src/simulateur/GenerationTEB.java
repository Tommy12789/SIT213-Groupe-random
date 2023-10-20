package simulateur;

import information.Information;
import visualisations.SaveCSV;


public class GenerationTEB {

    public static void main(String[] args) {
    	Simulateur simulateur = null;
        //Simulateur simulateur1 = null;
        //Simulateur simulateur2 = null;
        String ligne = null;

        Information<String> listeCSV = new Information <String>();
        listeCSV.add("TP_symbl;TEB_RZ");



    	try {
            //pour nbRealisation, creer un simulateur et calculer le TEB
            for (int i = 10 ; i < 300 ; i=i+2) {
                simulateur = new Simulateur(new String[]{"-form", "RZ","-nbEch",Integer.toString(i),"-ampl","0f", "5f","-snrpb", "15","-mess", "200000","-ti" , "100" , "0.5"});
                simulateur.execute();
                //simulateur1 = new Simulateur(new String[]{"-form", "NRZT","-nbEch","100","-ampl","0f", "5f","-snrpb", "12","-mess", "200000","-ti" , Integer.toString(i) , "0.5"});
                //simulateur1.execute();
                //simulateur2 = new Simulateur(new String[]{"-form", "NRZ","-nbEch","100","-ampl","0f", "5f","-snrpb", "12","-mess", "200000","-ti" , Integer.toString(i) , "0.5"});
                //simulateur2.execute();
                ligne = Integer.toString(i)+";"+String.format("%.7f", simulateur.calculTauxErreurBinaire());
                System.out.println(ligne);
                listeCSV.add(ligne);
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
