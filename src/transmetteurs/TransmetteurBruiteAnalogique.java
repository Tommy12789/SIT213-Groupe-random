package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;

public class TransmetteurBruiteAnalogique extends Transmetteur <Float,Float> {
	
	private float SNRPB = 0;
	private int nbEchantillons = 30;
	private double a1 = 0;
	private double a2 = 0;
	private double bruit = 0;

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
		this.SNRPB= (float) Math.pow(SNRPB/10,10);
		this.nbEchantillons=nbEchantillons;

	}



	@Override
	public void recevoir(Information<Float> information) throws InformationNonConformeException {

		//calcul de la puissance du signal
		float puissance_signal = 0;
		for (int i = 0 ; information.nbElements() > i ; i++ ) {
			puissance_signal=puissance_signal+information.iemeElement(i)*information.iemeElement(i);
		}
		puissance_signal=puissance_signal/information.nbElements();

		System.out.println("Puissance du signal : "+puissance_signal);

		//generation du bruit gaussien avec une valeur de EB/N0 de SNRP
		float puissance_bruit = puissance_signal/SNRPB;
		
		//affichage du EB/N0 en dB
		System.out.println("EB/N0 : "+10*Math.log10(SNRPB)+" dB");

		//ajout du bruit au signal
		double sigma = (float) (Math.sqrt((nbEchantillons*puissance_signal)/(2*SNRPB)));
	
		for (int i = 0 ; information.nbElements() > i ; i++ ) {
			//calcul du bruit
			a1 = Math.random();
			a2 = Math.random();
			bruit = sigma * Math.sqrt(-2*Math.log(1-a1))*Math.cos(2*Math.PI*a2);

			//ajout du bruit au signal
			informationRecue.add(information.iemeElement(i));
			informationEmise.add(information.iemeElement(i)+ (float)bruit);
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