package convertisseurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;

public class ConvertisseurNRZ <R,E> extends Convertisseur<Boolean,Double>{

    private double vMin;
    private double vMax;
    private double tpSymbole;
    private double pasEchantillonage;

    /**
     * Une convertisseur qui envoie toujours le même message
     */
    public ConvertisseurNRZ (double vMin, double vMax, double tpSymbole, double pasEchantillonage) {
        super();        
        this.vMin = vMin;
        this.vMax = vMax;
        this.tpSymbole = tpSymbole;
        this.pasEchantillonage = pasEchantillonage;
    }
        

    public void recevoir(Information<Boolean> information) throws InformationNonConformeException {
        this.informationRecue = information;
        this.informationEmise = new Information<Double>();
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
    public Information<Double> getInformationEmise() {
        return this.informationEmise;
    }

}