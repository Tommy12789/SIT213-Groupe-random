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

}