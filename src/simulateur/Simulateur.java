package simulateur;
import convertisseurs.ConvertisseurNRZ;
import convertisseurs.Convertisseur;
import convertisseurs.ConvertisseurInv;
import convertisseurs.ConvertisseurNRZT;
import convertisseurs.ConvertisseurRZ;
import destinations.Destination;
import destinations.DestinationFinale;
import destinations.DestinationFinaleAnalogique;
import information.Information;
import information.InformationNonConformeException;
import sources.Source;
import sources.SourceAleatoire;
import transmetteurs.Transmetteur;
import transmetteurs.TransmetteurBruiteAnalogique;
import transmetteurs.TransmetteurBruiteAnalogiqueTrajetsMultiples;
import transmetteurs.TransmetteurParfaitAnalogique;
import visualisations.SondeAnalogique;
import visualisations.SondeLogique;
import simulateur.Simulateur;


/** La classe Simulateur permet de construire et simuler une chaîne de
 * transmission composée d'une Source, d'un nombre variable de
 * Transmetteur(s) et d'une Destination.
 * @author cousin
 * @author prou
 *
 */
public class Simulateur {
      	
    /** indique si le Simulateur utilise des sondes d'affichage */
    private boolean affichage = false;
    
    /** indique si le Simulateur utilise un message généré de manière aléatoire (message imposé sinon) */
    private boolean messageAleatoire = true;
    
    /** indique si le Simulateur utilise un germe pour initialiser les générateurs aléatoires */
    private boolean aleatoireAvecGerme = false;
    
    /** la valeur de la semence utilisée pour les générateurs aléatoires */
    private Integer seed = null; // pas de semence par défaut
    
    /** la longueur du message aléatoire à transmettre si un message n'est pas imposé */
    private int nbBitsMess = 100; 
    
    /** la chaîne de caractères correspondant à m dans l'argument -mess m */
    private String messageString = "100";
   
    /** le  composant Source de la chaine de transmission */
    private Source <Boolean>  source = null;

    /**le composant convertisseur de la chaine de transmission */
    private Convertisseur <Boolean,Float> convertisseur = null;

    /** le  composant ConvertisseurNRZInv de la chaine de transmission */
    private ConvertisseurInv <Float,Boolean> convertisseurInv = null;
    
    /** le  composant Transmetteur parfait logique de la chaine de transmission */
    private Transmetteur<Float, Float>  transmetteurLogique = null;
    
    /** le  composant Destination de la chaine de transmission */
    private Destination<Boolean>  destination = null;

    /** SNRB par défaut : 0 */
    private float SNRPB = 100;

    /** Nombre d'échantillons par défaut : 30 */
    private int nbEchantillons = 30;

    /** Vmin et vmax */
    private float vMin = 0f;
    private float vMax = 1.0f;

    /** Forme du signal */
    private String form = "NRZ";


    /** liste des retards */
    private Information<Float> trajetsMultiples = new Information <Float>();

    /** Le constructeur de Simulateur construit une chaîne de
     * transmission composée d'une Source Boolean, d'une Destination
     * Boolean et de Transmetteur(s) [voir la méthode
     * analyseArguments]...  <br> Les différents composants de la
     * chaîne de transmission (Source, Transmetteur(s), Destination,
     * Sonde(s) de visualisation) sont créés et connectés.
     * @param args le tableau des différents arguments.
     *² 
     * @throws ArgumentsException si un des arguments est incorrect
     * @throws InformationNonConformeException si l'infomration n'est pas conforme
     *
     */   
    public  Simulateur(String [] args) throws ArgumentsException, InformationNonConformeException {
    	// analyser et récupérer les arguments   	
    	analyseArguments(args);
      
        if(seed!=null){
            source=new SourceAleatoire(nbBitsMess, seed);
        }
        else{
            source=new SourceAleatoire(nbBitsMess);
        }

        convertisseurInv = new ConvertisseurInv(vMin, vMax,nbEchantillons);
        destination = new DestinationFinale();

        if (trajetsMultiples.nbElements() != 0){
            transmetteurLogique = new TransmetteurBruiteAnalogiqueTrajetsMultiples(SNRPB,nbEchantillons);
        }
        else{
            transmetteurLogique = new TransmetteurBruiteAnalogique(SNRPB,nbEchantillons);
        }


        if (form.matches("NRZ")){
            convertisseur = new ConvertisseurNRZ(vMin, vMax,nbEchantillons);
        }
        else if (form.matches("RZ")){
            convertisseur = new ConvertisseurRZ(vMin, vMax,nbEchantillons);
        }
        else if (form.matches("NRZT")){
            convertisseur = new ConvertisseurNRZT(vMin, vMax,nbEchantillons);
        }

        
        source.connecter(convertisseur);
        convertisseur.connecter(transmetteurLogique);
        transmetteurLogique.connecter(convertisseurInv);
        convertisseurInv.connecter(destination);

        
        if (affichage){
            source.connecter(new SondeLogique("Source",100 ));
            convertisseur.connecter(new SondeAnalogique("Convertisseur"));
            transmetteurLogique.connecter(new SondeAnalogique("Transmetteur"));
            convertisseurInv.connecter(new SondeLogique("Destination ",100 ));
        }


    }
   
   
   
    /** La méthode analyseArguments extrait d'un tableau de chaînes de
     * caractères les différentes options de la simulation.  <br>Elle met
     * à jour les attributs correspondants du Simulateur.
     *
     * @param args le tableau des différents arguments.
     * <br>
     * <br>Les arguments autorisés sont : 
     * <br> 
     * <dl>
     * <dt> -mess m  </dt><dd> m (String) constitué de 7 ou plus digits à 0 | 1, le message à transmettre</dd>
     * <dt> -mess m  </dt><dd> m (int) constitué de 1 à 6 digits, le nombre de bits du message "aléatoire" à transmettre</dd> 
     * <dt> -s </dt><dd> pour demander l'utilisation des sondes d'affichage</dd>
     * <dt> -seed v </dt><dd> v (int) d'initialisation pour les générateurs aléatoires</dd> 
     * </dl>
     *
     * @throws ArgumentsException si un des arguments est incorrect.
     *
     */   
    public  void analyseArguments(String[] args)  throws  ArgumentsException {

    	for (int i=0;i<args.length;i++){ // traiter les arguments 1 par 1

    		if (args[i].matches("-s")){
    			affichage = true;
    		}
    		
    		else if (args[i].matches("-seed")) {
    			aleatoireAvecGerme = true;
    			i++; 
    			// traiter la valeur associee
    			try { 
    				seed = Integer.valueOf(args[i]);
    			}
    			catch (Exception e) {
    				throw new ArgumentsException("Valeur du parametre -seed  invalide :" + args[i]);
    			}           		
    		}

    		else if (args[i].matches("-mess")){
    			i++; 
    			// traiter la valeur associee
    			messageString = args[i];
    			if (args[i].matches("[0,1]{7,}")) { // au moins 7 digits
    				messageAleatoire = false;
    				nbBitsMess = args[i].length();
    			} 
    			else if (args[i].matches("[0-9]{1,6}")) { // de 1 à 6 chiffres
    				messageAleatoire = true;
    				nbBitsMess = Integer.valueOf(args[i]);
    				if (nbBitsMess < 1) 
    					throw new ArgumentsException ("Valeur du parametre -mess invalide : " + nbBitsMess);
    			}
    			else 
    				throw new ArgumentsException("Valeur du parametre -mess invalide : " + args[i]);
    		}
    	
            else if (args[i].matches("-snrpb")){
                i++;
                try {
                    SNRPB = Float.valueOf(args[i]);
                }
                catch (Exception e) {
                    throw new ArgumentsException("Valeur du parametre -snrpb invalide :" + args[i]);
                }
            }

            //ajout de l'option nbEchantillons
            else if (args[i].matches("-nbEch")){
                i++;
                try {
                    nbEchantillons = Integer.valueOf(args[i]);
                }
                catch (Exception e) {
                    throw new ArgumentsException("Valeur du parametre -nbEch invalide :" + args[i]);
                }
            }

            //ajout de  l'option -ampl min max
            else if (args[i].matches("-ampl")){
                i++;
                try {
                    vMin = Float.valueOf(args[i]);
                    i++;
                    vMax = Float.valueOf(args[i]);
                }
                catch (Exception e) {
                    throw new ArgumentsException("Valeur du parametre -ampl invalide :" + args[i]);
                }
            }


            //ajout du paramentre NRZ,RZ ou RNZT dans form
            else if (args[i].matches("-form")){
                i++;
                try {
                    form = args[i];
                }
                catch (Exception e) {
                    throw new ArgumentsException("Valeur du parametre -form invalide :" + args[i]);
                }
            }


            //ajout de la commande -ti
            else if (args[i].matches("-ti")){
                try {
                    for (int j = i+1; j < i+10; j++) {
                        //si l'argument est un nombre
                        if (args[j].matches("[-+]?\\d*\\.?\\d+")){
                            i++;
                            float nb = Float.valueOf(args[j]);
                            trajetsMultiples.add(nb);

                        }
                        else {
                            break;
                        }

                    }
                    //si la liste est vide ou impairement remplie
                    if (trajetsMultiples.nbElements() == 0 || trajetsMultiples.nbElements()%2 != 0){
                        throw new ArgumentsException("Valeur du parametre -ti invalide : nombre de parametres invalide");
                    }
                    
                }
                catch (Exception e) {
                    throw new ArgumentsException("Valeur du parametre -ti invalide : ");
                }
            }
            
            

            



    		else throw new ArgumentsException("Option invalide :"+ args[i]);
    	}
      
    }
     
    
   	
    /** La méthode execute effectue un envoi de message par la source
     * de la chaîne de transmission du Simulateur.
     *
     * @throws Exception si un problème survient lors de l'exécution
     *
     */ 
    public void execute() throws Exception {      
         
        source.emettre();
      	     	      
    }
   
   	   	
   	
    /** La méthode qui calcule le taux d'erreur binaire en comparant
     * les bits du message émis avec ceux du message reçu.
     *
     * @return  La valeur du Taux dErreur Binaire.
     */   	   
    public float  calculTauxErreurBinaire() {
    	int nbErreur = 0;
    	Information <Boolean> informationEmise = source.getInformationEmise();
    	Information <Boolean> informationRecue = destination.getInformationRecue();
    	
    	//verification de la longueur des mots binaires
    	if (informationEmise.nbElements() == informationRecue.nbElements()){ 
    		for( int i = 0 ; i < informationEmise.nbElements() ; i++) {
    			if (!informationEmise.iemeElement(i).equals(informationRecue.iemeElement(i))){
    				nbErreur ++;
    			}
    		}
    		return (float) nbErreur / (float) informationRecue.nbElements(); //calcul du TEB
    	}
    	else{ // si les mots binaires ne sont pas de la même longeur, le message est corrompu !
    		System.out.println("Les mots binaires ne sont pas de la même longeur !");
    		return  1.0f;
    	}
 
    	
    }
   
   
   
   
    /** La fonction main instancie un Simulateur à l'aide des
     *  arguments paramètres et affiche le résultat de l'exécution
     *  d'une transmission.
     *  @param args les différents arguments qui serviront à l'instanciation du Simulateur.
     */
    public static void main(String [] args) { 

    	Simulateur simulateur = null;

    	try {
    		simulateur = new Simulateur(args);
    	}
    	catch (Exception e) {
    		System.out.println(e); 
    		System.exit(-1);
    	} 
 
    	try {
    		simulateur.execute();
    		String s = "java  Simulateur  ";
    		for (int i = 0; i < args.length; i++) { //copier tous les paramètres de simulation
    			s += args[i] + "  ";
    		}
    		System.out.println(s + "  =>   TEB : " + simulateur.calculTauxErreurBinaire());
    	}
    	catch (Exception e) {
    		System.out.println(e);
    		e.printStackTrace();
    		System.exit(-2);
    	}              	
    }
}

