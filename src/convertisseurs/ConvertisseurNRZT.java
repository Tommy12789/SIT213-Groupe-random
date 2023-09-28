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
        this.nbEchantillons = 0;
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




        //on integre l'information emise
        float integrale = 0;
        for (int i = 0; i < informationEmise.nbElements(); i++) {
            integrale += informationEmise.iemeElement(i);
            informationEmise.setIemeElement(i, integrale);
        }
        
        for (int i = 0; i < informationRecue.nbElements(); i++) {
            if (informationRecue.iemeElement(i)) {
                for (int j = 0; j < nbEchantillons; j++) {
                    if (j < nbEchantillons / 3) {
                        if (i != 0 && informationRecue.iemeElement(i - 1))
                            informationEmise.add(vMax);
                        else
                            informationEmise.add((float) j / (nbEchantillons / 3) * vMax);
                    } else if (j < 2 * nbEchantillons / 3) {
                        informationEmise.add(vMax);
                    } else {
                        if (i + 1 < informationRecue.nbElements() && informationRecue.iemeElement(i + 1))
                            informationEmise.add(vMax);
                        else
                            informationEmise.add((float) (nbEchantillons - j) / (nbEchantillons / 3) * vMax);
                    }
                }
            } else {
                for (int j = 0; j < nbEchantillons; j++) {
                    if (j < nbEchantillons / 3) {
                        if (i != 0 && !informationRecue.iemeElement(i - 1))
                            informationEmise.add(vMin);
                        else
                            informationEmise.add((float) j / (nbEchantillons / 3) * vMin);
                    } else if (j < 2 * nbEchantillons / 3) {
                        informationEmise.add(vMin);
                    } else {
                        if (i + 1 < informationRecue.nbElements() && !informationRecue.iemeElement(i + 1))
                            informationEmise.add(vMin);
                        else
                            informationEmise.add((float) (nbEchantillons - j) / (nbEchantillons / 3) * vMin);
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