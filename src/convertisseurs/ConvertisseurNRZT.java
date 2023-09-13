package convertisseurs;

import information.Information;
import information.InformationNonConformeException;

public class ConvertisseurNRZT <R,E> extends Convertisseur<Boolean,Float>{

    private float vMin;
    private float vMax;
    private int nbEchantillons;


    /**
     * Une convertisseur qui envoie toujours le même message
     */
    public ConvertisseurNRZT (float vMin, float vMax) {
        super();        
        this.vMin = vMin;
        this.vMax = vMax;
        this.nbEchantillons = 30;
    }

        public ConvertisseurNRZT (float vMin, float vMax, int nbEchantillons) {
        super();        
        this.vMin = vMin;
        this.vMax = vMax;
        this.nbEchantillons = nbEchantillons;
    }
        

    public void recevoir(Information<Boolean> information) throws InformationNonConformeException {
        this.informationRecue = information;
        this.informationEmise = new Information<Float>();
        for (int i = 0; i < information.nbElements(); i++) {
            if (information.iemeElement(i)) {
                for (int j = 0; j < nbEchantillons; j++) {
                    informationEmise.add(vMax);
                }
            } else {
                for (int j = 0; j < nbEchantillons; j++) {
                    informationEmise.add(vMin);
                }
            }
        }
         
        this.emettre();
    }

    @Override
    public Information<Boolean> getInformationRecue() {
        return this.informationRecue;
    }

    @Override
    public Information<Float> getInformationEmise() {
        return this.informationEmise;
    }

}