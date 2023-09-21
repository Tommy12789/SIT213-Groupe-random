package sources;

import information.Information;
import java.util.Random;

public class SourceAleatoire extends Source<Boolean>{
    
    
    /**
     * Une source qui envoie un message aléatoire
     */
    public SourceAleatoire () {
    	Random random = new Random();
    	
        informationGeneree = new Information<Boolean>();
        for (int i=0;i<10;i++) {
            informationGeneree.add(random.nextBoolean());  

        }
        
    }

        public SourceAleatoire (int taille) {
    	Random random = new Random();
    	
        informationGeneree = new Information<Boolean>();
        for (int i=0;i<taille;i++) {
            informationGeneree.add(random.nextBoolean());  

        }
        
    }


    public SourceAleatoire(int taille, int seed) {
        super();
        this.informationGeneree = new Information<Boolean>();
        Random rd = new Random(seed); // creating Random object
        for (int i = 0; i < taille; i++) {
            this.informationGeneree.add(rd.nextBoolean());
        }
        this.informationEmise = this.informationGeneree;
    }

}