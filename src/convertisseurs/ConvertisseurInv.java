package convertisseurs;

import information.Information;
import information.InformationNonConformeException;

public class ConvertisseurInv <R,E> extends Convertisseur<Float,Boolean>{

    private float vMin;
    private float vMax;
    private int nbEchantillons;
    private float moyenne;


    /**
     * Une convertisseur qui envoie toujours le même message
     */
    public ConvertisseurInv (float vMin, float vMax) {
        super();        
        this.vMin = vMin;
        this.vMax = vMax;
        this.nbEchantillons = 30;
    }

    public ConvertisseurInv (float vMin, float vMax, int nbEchantillons) {
        super();        
        this.vMin = vMin;
        this.vMax = vMax;
        this.nbEchantillons = nbEchantillons;
    }
        

    public void recevoir(Information<Float> information) throws InformationNonConformeException {
        this.informationRecue = information;
        this.informationEmise = new Information<Boolean>();

        for (int i = 0; i < informationRecue.nbElements() - (nbEchantillons/3); i=i+nbEchantillons) {
            moyenne = 0;
            for (int j = (int) (nbEchantillons/3) + i ; j <= (int) (2*(nbEchantillons/3)) + i ; j++) {
                moyenne = moyenne + informationRecue.iemeElement(j);
            }
            moyenne = moyenne/(nbEchantillons/3);

            if (moyenne > (vMax+vMin)/2) {
                informationEmise.add(true);
            } else {
                informationEmise.add(false);
            }
        }


        this.emettre();
    }

    @Override
    public Information<Float> getInformationRecue() {
        return this.informationRecue;
    }

    @Override
    public Information<Boolean> getInformationEmise() {
        return this.informationEmise;
    }

}