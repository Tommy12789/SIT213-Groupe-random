package sources;

import information.Information;

public class SourceFixe extends Source<Boolean>{
    
    
    /**
     * Une source qui envoie toujours le même message
     */
    public SourceFixe (Boolean[] message) {
        informationGeneree = new Information<Boolean>(message);
    }

}