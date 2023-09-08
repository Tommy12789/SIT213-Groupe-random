package convertisseurs;

import information.*;
import destinations.DestinationInterface;

/** 
 * Interface d'un composant ayant le comportement d'une convertisseur
 * d'informations dont les éléments sont de type T
 * @author prou
 */
public interface convertisseurInterface <T>  {
   
    /**
     * pour obtenir la dernière information émise par une convertisseur.
     * @return une information   
     */
    public Information <T>  getInformationEmise();
   
    /**
     * pour connecter une destination à la convertisseur
     * @param destination  la destination à connecter
     */
    public void connecter (DestinationInterface <T> destination);
   
    /**
     * pour émettre l'information contenue dans une convertisseur
     */
    public void emettre() throws InformationNonConformeException; 
}
