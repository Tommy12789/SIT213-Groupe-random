package transmetteurs;

import java.util.Random;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;

public class TransmetteurBruiteAnalogique extends Transmetteur <Float,Float> {
	
	private float SNRPB = 0;
	private int nbEchantillons = 30;
	private double bruit = 0;
	private int seed = 0;
	private boolean useSeed = false;
	private Information<String> listeCSV = new Information <String>();


	/**
	 * Transmetteur parfait : il transmet exactement ce qu'il recoit
	 * @author morga
	 */
	public TransmetteurBruiteAnalogique() {
		super();
		informationEmise = new Information <Float>();
		informationRecue = new Information <Float>();

	}

	public TransmetteurBruiteAnalogique(float SNRPB, int nbEchantillons) {
		super();
		informationEmise = new Information <Float>();
		informationRecue = new Information <Float>();
		this.SNRPB= (float) Math.pow(10,SNRPB/10);
		this.nbEchantillons=nbEchantillons;

	}

	public TransmetteurBruiteAnalogique(float SNRPB, int nbEchantillons, int seed) {
		super();
		informationEmise = new Information <Float>();
		informationRecue = new Information <Float>();
		this.SNRPB= (float) Math.pow(10,SNRPB/10);
		this.nbEchantillons=nbEchantillons;
		this.seed = seed;
		useSeed = true;
	}


	@Override
	public void recevoir(Information<Float> information) throws InformationNonConformeException {

		//calcul de la puissance du signal
		float puissance_signal = 0;
		for (int i = 0 ; information.nbElements() > i ; i++ ) {
			puissance_signal=puissance_signal+information.iemeElement(i)*information.iemeElement(i);
		}
		puissance_signal=puissance_signal/information.nbElements();


		//calcul de la puissance du bruit
		float puissance_bruit = puissance_signal/SNRPB;
		
		//affichage du rapport signal sur bruit
		//System.out.println("Rapport signal sur bruit : "+10*Math.log10(puissance_signal/puissance_bruit)+" dB");

		//ajout du bruit au signal
		double variance = (float) (Math.sqrt((nbEchantillons*puissance_signal)/(2*SNRPB)));
	
		//System.out.println("Puissance du signal : "+puissance_signal);
		//affichage de pbruit
		//System.out.println("Puissance du bruit : "+puissance_bruit);
		//affichage de la variance
		//System.out.println("Variance : "+variance);
		Random a1 = useSeed ? new Random(seed) : new Random();
		Random a2 = useSeed ? new Random(seed) : new Random();

		for (int i = 0 ; information.nbElements() > i ; i++ ) {
			//calcul du bruit
			bruit = variance * Math.sqrt(-2*Math.log(1-a1.nextDouble()))*Math.cos(2*Math.PI*a2.nextDouble());

			//ajout du bruit au signal
			informationRecue.add(information.iemeElement(i));
			informationEmise.add(information.iemeElement(i)+ (float)bruit);

			//ajout du bruit dans une liste
			listeCSV.add(String.valueOf((float) Math.round(bruit * 10) / 10));
		}

		emettre();
	}

	@Override
	public void emettre() throws InformationNonConformeException {
		for (DestinationInterface <Float> destinationConnectees : destinationsConnectees) {
	        destinationConnectees.recevoir(informationEmise);
		}
	}


}