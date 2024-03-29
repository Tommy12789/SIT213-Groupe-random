package simulateur;
import convertisseurs.ConvertisseurNRZ;
import codeurs.Codeur;
import codeurs.CodeurCanal;
import codeurs.CodeurCanalInv;
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
import sources.SourceFixe;
import transmetteurs.Transmetteur;
import transmetteurs.TransmetteurBruiteAnalogique;
import transmetteurs.TransmetteurBruiteAnalogiqueTrajetsMultiples;
import transmetteurs.TransmetteurParfait;
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

    /** indique si un codage de canal est utilisé */
    private boolean codeurCanal = false;
    
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

    /** le composant codeur de la chaine pour le codage canal*/
    private Codeur <Boolean,Boolean> codeur = null;

    /** le composant codeur de la chaine pour le codage canal*/
    private Codeur <Boolean,Boolean> codeurInv = null;

    /** le  composant ConvertisseurNRZInv de la chaine de transmission */
    private ConvertisseurInv <Float,Boolean> convertisseurInv = null;
    
    /** le  composant Transmetteur parfait anal logique de la chaine de transmission */
    private Transmetteur<Float, Float>  transmetteurLogique = null;
    
    /** le  composant Transmetteur parfait logique de la chaine de transmission */
    private Transmetteur<Boolean, Boolean>  transmetteurLogiqueLeVrai = null;

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
    private String form = "RZ";

    /** Analogique */
    private boolean analog = false;


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
      
        if(aleatoireAvecGerme && messageAleatoire){
            source = new SourceAleatoire(nbBitsMess, seed);
        }
        else if (messageAleatoire){
            source = new SourceAleatoire(nbBitsMess);
        }
        else{
            //convertir la chaîne de caractères en un tableau de booléens
            Boolean[] message = new Boolean[messageString.length()];
            for (int i = 0; i < messageString.length(); i++) {
                if (messageString.charAt(i) == '0') {
                    message[i] = false;
                } else {
                    message[i] = true;
                }
            }
            source = new SourceFixe(message);
        }

        destination = new DestinationFinale();


        if (analog){
            convertisseurInv = new ConvertisseurInv(vMin, vMax,nbEchantillons);

            if (trajetsMultiples.nbElements() != 0){
                if(aleatoireAvecGerme){
                    transmetteurLogique = new TransmetteurBruiteAnalogiqueTrajetsMultiples(SNRPB,nbEchantillons,trajetsMultiples,seed);
                }else{
                    transmetteurLogique = new TransmetteurBruiteAnalogiqueTrajetsMultiples(SNRPB,nbEchantillons,trajetsMultiples);
 
                }
            }
            else{
                if(aleatoireAvecGerme){
                    transmetteurLogique = new TransmetteurBruiteAnalogique(SNRPB,nbEchantillons,seed);
                }else{
                    transmetteurLogique = new TransmetteurBruiteAnalogique(SNRPB,nbEchantillons);

                }
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

        
            if (codeurCanal){
                codeur = new CodeurCanal();
                codeurInv = new CodeurCanalInv();

                source.connecter(codeur);
                codeur.connecter(convertisseur);
                convertisseur.connecter(transmetteurLogique);
                transmetteurLogique.connecter(convertisseurInv);
                convertisseurInv.connecter(codeurInv);
                codeurInv.connecter(destination);
            }else{
                source.connecter(convertisseur);
                convertisseur.connecter(transmetteurLogique);
                transmetteurLogique.connecter(convertisseurInv);
                convertisseurInv.connecter(destination);
            }
        
            if (affichage && !codeurCanal){
                source.connecter(new SondeLogique("Source",100 ));
                convertisseur.connecter(new SondeAnalogique("Convertisseur"));
                transmetteurLogique.connecter(new SondeAnalogique("Transmetteur"));
                convertisseurInv.connecter(new SondeLogique("Destination ",100 ));
            }

            if (affichage && codeurCanal){
                source.connecter(new SondeLogique("Source",100 ));
                convertisseur.connecter(new SondeAnalogique("Convertisseur"));
                transmetteurLogique.connecter(new SondeAnalogique("Transmetteur"));
                codeurInv.connecter(new SondeLogique("Destination ",100 ));  
            }
        }
        else{
            transmetteurLogiqueLeVrai = new TransmetteurParfait();
            codeur = new CodeurCanal<>();
            codeurInv = new CodeurCanalInv<>();

            if (codeurCanal){
                source.connecter(codeur);
                codeur.connecter(transmetteurLogiqueLeVrai);
                transmetteurLogiqueLeVrai.connecter(codeurInv);
                codeurInv.connecter(destination);
            }else{
                source.connecter(transmetteurLogiqueLeVrai);
                transmetteurLogiqueLeVrai.connecter(destination);
            }

            if (affichage && !codeurCanal){
                source.connecter(new SondeLogique("Source",100 ));
                transmetteurLogiqueLeVrai.connecter(new SondeLogique("Destination",100) );
            }

            if (affichage && codeurCanal){
                source.connecter(new SondeLogique("Source",100 ));
                transmetteurLogiqueLeVrai.connecter(new SondeLogique("Transmetteur",100 ));
                codeurInv.connecter(new SondeLogique("Destination",100 ));
            }
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
    		}else

            //ajout de la commande -codeur 
            if (args[i].matches("-codeur")){
    			codeurCanal = true;
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
                analog = true;
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
                analog = true;
                try {
                    nbEchantillons = Integer.valueOf(args[i]);
                    //si nbEch < 10 on leve une erreur
                    if (nbEchantillons < 10){
                        throw new ArgumentsException("Valeur du parametre -nbEch invalide : nombre d'échantillons trop faible");
                    }
                }
                catch (Exception e) {
                    throw new ArgumentsException(e.getMessage());
                }
            }

            //ajout de  l'option -ampl min max
            else if (args[i].matches("-ampl")){
                analog = true;
                i++;
                try {
                    vMin = Float.valueOf(args[i]);
                    i++;
                    vMax = Float.valueOf(args[i]);

                    //leve une erreur si min>max
                    if (vMin > vMax){
                        throw new ArgumentsException("Valeur du parametre -ampl invalide : min > max");
                    }
                }
                catch (Exception e) {
                    throw new ArgumentsException(e.getMessage());
                }
            }


            //ajout du paramentre NRZ,RZ ou RNZT dans form
            else if (args[i].matches("-form")){
                analog = true;
                i++;
                try {
                    form = args[i];
                    if (!form.matches("RZ|NRZ|NRZT")){
                        throw new ArgumentsException("Valeur du parametre -form invalide :" + args[i]);
                    }
                }
                catch (Exception e) {
                    throw new ArgumentsException("Valeur du parametre -form invalide :" + args[i]);
                }
            }


            //ajout de la commande -ti
            else if (args[i].matches("-ti")){
                analog = true;
                try {
                    for (int j = i+1; j < args.length; j++) {
                        //si l'argument est un nombre
                        if (args[j].matches("[-+]?\\d*\\.?\\d+")){
                            i++;
                            //System.out.println(args[j]);
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

                    //pour toutes les valeurs de la liste on vérifie qu'elles sont positives
                    for (int j = 0; j < trajetsMultiples.nbElements(); j++) {
                        if (trajetsMultiples.iemeElement(j) < 0){
                            throw new ArgumentsException("Valeur du parametre -ti invalide : valeur negative");
                        }
                    }

                    //on vérifie que les amplitudes sont inferieures à 1
                    for (int j = 1; j < trajetsMultiples.nbElements(); j=j+2) {
                        if (trajetsMultiples.iemeElement(j) > 1){
                            throw new ArgumentsException("Valeur du parametre -ti invalide : amplitude superieure à 1");
                        }
                    }

                    //on vérifie que les valeurs sont des entiers
                    for (int j = 0; j < trajetsMultiples.nbElements(); j=j+2) {
                        if (trajetsMultiples.iemeElement(j) != Math.round(trajetsMultiples.iemeElement(j))){
                            throw new ArgumentsException("Valeur du parametre -ti invalide : décalage non entier");
                        }
                    }

                    
                }
                catch (Exception e) {
                    throw new ArgumentsException(e.getMessage());
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
   		for( int i = 0 ; i < informationEmise.nbElements() ; i++) {
    		if (!informationEmise.iemeElement(i).equals(informationRecue.iemeElement(i))){
    			nbErreur ++;
    		}
		}    		
        return (float) nbErreur / (float) informationRecue.nbElements(); //calcul du TEB
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

