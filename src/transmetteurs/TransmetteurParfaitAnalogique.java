package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;

public class TransmetteurParfaitAnalogique extends Transmetteur <Float,Float> {

	/**
	 * Transmetteur parfait : il transmet exactement ce qu'il recoit
	 * @author morga
	 */
	public TransmetteurParfaitAnalogique() {
		super();
		informationEmise = new Information <Float>();
		informationRecue = new Information <Float>();

	}

	@Override
	public void recevoir(Information<Float> information) throws InformationNonConformeException {
		for (int i = 0 ; information.nbElements() > i ; i++ ) {
			informationRecue.add(information.iemeElement(i));
			informationEmise.add(information.iemeElement(i));
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