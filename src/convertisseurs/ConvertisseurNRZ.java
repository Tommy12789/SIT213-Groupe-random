package convertisseurs;

import information.Information;
import information.InformationNonConformeException;

public class ConvertisseurNRZ <R,E> extends Convertisseur<Boolean,Float>{

    private float vMin;
    private float vMax;
    private float tpSymbole;
    private float pasEchantillonage;

    /**
     * Une convertisseur qui envoie toujours le même message
     */
    public ConvertisseurNRZ (float vMin, float vMax, float tpSymbole, float pasEchantillonage) {
        super();        
        this.vMin = vMin;
        this.vMax = vMax;
        this.tpSymbole = tpSymbole;
        this.pasEchantillonage = pasEchantillonage;
    }
        

    public void recevoir(Information<Boolean> information) throws InformationNonConformeException {
        this.informationRecue = information;
        this.informationEmise = new Information<Float>();
        for (int i = 0; i < information.nbElements(); i++) {
            if (information.iemeElement(i)) {
                for (int j = 0; j < tpSymbole/pasEchantillonage; j++) {
                    informationEmise.add(vMax);
                }
            } else {
                for (int j = 0; j < tpSymbole/pasEchantillonage; j++) {
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