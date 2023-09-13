package convertisseurs;

import information.Information;
import information.InformationNonConformeException;

public class ConvertisseurRZ <R,E> extends Convertisseur<Boolean,Float>{

    private float vMin;
    private float vMax;
    private int nbEchantillons;


    /**
     * Une convertisseur qui envoie toujours le même message
     */
    public ConvertisseurRZ (float vMin, float vMax) {
        super();        
        this.vMin = vMin;
        this.vMax = vMax;
        this.nbEchantillons = 30;
    }

        public ConvertisseurRZ (float vMin, float vMax, int nbEchantillons) {
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
                    if(j<nbEchantillons/3){
                        informationEmise.add(0f);
                    }
                    else if(j>nbEchantillons/3 && j<2*nbEchantillons/3){
                        informationEmise.add(vMax);
                    }
                    else{
                        informationEmise.add(0f);
                    }
                }
            } else {
                for (int j = 0; j < nbEchantillons; j++) {
                    if(j<nbEchantillons/3){
                        informationEmise.add(0f);
                    }
                    else if(j>nbEchantillons/3 && j<2*nbEchantillons/3){
                        informationEmise.add(vMin);
                    }
                    else{
                        informationEmise.add(0f);
                    }
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