package codeurs;

import information.Information;
import information.InformationNonConformeException;

public class CodeurCanal <R,E> extends Codeur<Boolean,Boolean>{


    /**
     * Une convertisseur qui envoie toujours le même message
     */
    public CodeurCanal () {
        super();        
    }

        

    public void recevoir(Information<Boolean> information) throws InformationNonConformeException {
        this.informationRecue = information;
        this.informationEmise = new Information<Boolean>();
        for (int i = 0; i < information.nbElements(); i++) {
            if (information.iemeElement(i)) {
                    informationEmise.add(true);
                    informationEmise.add(false);
                    informationEmise.add(true);
            } else {
                    informationEmise.add(false);
                    informationEmise.add(true);
                    informationEmise.add(false);
            }
        }
        this.emettre();
    }

    @Override
    public Information<Boolean> getInformationRecue() {
        return this.informationRecue;
    }

    @Override
    public Information<Boolean> getInformationEmise() {
        return this.informationEmise;
    }


}