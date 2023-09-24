package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;

public class TransmetteurBruiteAnalogiqueTrajetsMultiples extends Transmetteur <Float,Float> {
	
	private float SNRPB = 0;
	private int nbEchantillons = 30;
	private double a1 = 0;
	private double a2 = 0;
	private double bruit = 0;
	private Information<String> listeCSV = new Information <String>();
	private Information<Float> trajetsMultiples = null;


	/**
	 * Transmetteur parfait : il transmet exactement ce qu'il recoit
	 * @author morga
	 */

	public TransmetteurBruiteAnalogiqueTrajetsMultiples(float SNRPB, int nbEchantillons, Information<Float> trajetsMultiples) {
		super();
		informationEmise = new Information <Float>();
		informationRecue = new Information <Float>();
		this.SNRPB= (float) Math.pow(10,SNRPB/10);
		this.nbEchantillons=nbEchantillons;
		this.trajetsMultiples=trajetsMultiples;

	}


	@Override
	public void recevoir(Information<Float> information) throws InformationNonConformeException {

		informationRecue = information;
		informationEmise = new Information <Float>();

		//calcul du decalage max multiple de nbEchantillon
		int decalageMax = 0;
		for (int i = 0 ; i < trajetsMultiples.nbElements(); i=i+2) {
			if (trajetsMultiples.iemeElement(i).intValue() > decalageMax) {
				decalageMax = trajetsMultiples.iemeElement(i).intValue();
			}
		}
		if (decalageMax%nbEchantillons != 0) {
			decalageMax = decalageMax + (nbEchantillons - decalageMax%nbEchantillons);
		}

		//pour chaque information émise, on ajoute les trajets multiples
		for (int i = 0; i < informationRecue.nbElements(); i++) {
			informationEmise.add(informationRecue.iemeElement(i));
		}
		//on calcul le decalage max
		for (int i = 0; i < decalageMax; i++) {
			informationEmise.add(0f);
		}
		//on ajoute les trajets multiples
		for (int i = 0; i < informationRecue.nbElements(); i++) {
			for (int j = 0 ; j < trajetsMultiples.nbElements(); j=j+2) {
				int valeurDecalage = i+trajetsMultiples.iemeElement(j).intValue();
				float nouvelleValeur = informationEmise.iemeElement(valeurDecalage)+informationRecue.iemeElement(i)*trajetsMultiples.iemeElement(j+1);
				informationEmise.setIemeElement(valeurDecalage,nouvelleValeur );
			}
		}

		//calcul de la puissance du signal
		float puissance_signal = 0;
		for (int i = 0 ; informationEmise.nbElements() > i ; i++ ) {
			puissance_signal=puissance_signal+informationEmise.iemeElement(i)*informationEmise.iemeElement(i);
		}
		puissance_signal=puissance_signal/information.nbElements();
		
		//calcul de la puissance du bruit
		float puissance_bruit = puissance_signal/SNRPB;

		//ajout du bruit au signal
		double variance = (float) (Math.sqrt((nbEchantillons*puissance_signal)/(2*SNRPB)));
	


		for (int i = 0 ; informationEmise.nbElements() > i ; i++ ) {
			//calcul du bruit
			a1 = Math.random();
			a2 = Math.random();
			bruit = variance * Math.sqrt(-2*Math.log(1-a1))*Math.cos(2*Math.PI*a2);

			//ajout du bruit au signal
			informationEmise.setIemeElement(i,informationEmise.iemeElement(i)+ (float)bruit);
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