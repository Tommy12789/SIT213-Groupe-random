package codeurs;

import information.Information;
import information.InformationNonConformeException;

public class CodeurCanalInv <R,E> extends Codeur<Boolean,Boolean>{

    Boolean a,b,c;
    /**
     * Une convertisseur qui envoie toujours le même message
     */
    public CodeurCanalInv () {
        super();        
    }
        

    public void recevoir(Information<Boolean> information) throws InformationNonConformeException {
        this.informationRecue = information;
        this.informationEmise = new Information<Boolean>();

        for(int i =0 ; i < informationRecue.nbElements(); i=i+3){
            a=informationRecue.iemeElement(i);
            b=informationRecue.iemeElement(i+1);
            c=informationRecue.iemeElement(i+2);

           if((a && !b) || (a && c) || (!b && c)){
                informationEmise.add(true);
           }
           else{
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