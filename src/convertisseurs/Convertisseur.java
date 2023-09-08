package convertisseurs;

import information.*;
import destinations.DestinationInterface;
import java.util.*;

/** 
 * Classe Abstraite d'un composant convertisseur d'informations dont les
 * éléments sont de type T
 * @author prou
 */
public  abstract class Convertisseur <R,E> implements  ConvertisseurInterface <R,E> {
   
    /** 
     * la liste des composants destination connectés
     */
    protected LinkedList <DestinationInterface <E>> destinationsConnectees;
   
    /** 
     * l'information générée par la convertisseur
     */
    protected Information<R>  informationRecue;
   	
    /** 
     * l'information émise par la convertisseur
     */
    protected Information<E>  informationEmise;
   	
    /** 
     * un constructeur factorisant les initialisations communes aux
     * réalisations de la classe abstraite convertisseur
     */
    public Convertisseur () {
	destinationsConnectees = new LinkedList <DestinationInterface <E>> ();
	informationRecue = null;
	informationEmise = null;
    }
    
    /**
     * retourne la dernière information émise par la convertisseur
     * @return une information   
     */
    public Information <E>  getInformationEmise() {
	return this.informationEmise;
    }
   
    /**
     * connecte une destination à la convertisseur
     * @param destination  la destination à connecter
     */
    public void connecter (DestinationInterface <E> destination) {
	destinationsConnectees.add(destination); 
    }
   
    /**
     * déconnecte une destination de la convertisseur
     * @param destination  la destination à déconnecter
     */
    public void deconnecter (DestinationInterface <E> destination) {
	destinationsConnectees.remove(destination); 
    }
   
    /**
     * émet l'information convertie
     */
    public   void emettre() throws InformationNonConformeException {
       	// émission vers les composants connectés
        for (DestinationInterface <E> destinationConnectee : destinationsConnectees) {
                destinationConnectee.recevoir(informationEmise);
        } 			 			      
    }
}
