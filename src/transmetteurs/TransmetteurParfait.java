package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;

public class TransmetteurParfait extends Transmetteur <Boolean,Boolean> {

	/**
	 * Transmetteur parfait : il transmet exactement ce qu'il recoit
	 * @author morga
	 */
	public TransmetteurParfait() {
		super();
		informationEmise = new Information <Boolean>();
		informationRecue = new Information <Boolean>();

	}

	@Override
	public void recevoir(Information<Boolean> information) throws InformationNonConformeException {
		for (int i = 0 ; information.nbElements() > i ; i++ ) {
			informationRecue.add(information.iemeElement(i));
			informationEmise.add(information.iemeElement(i));
		}
		emettre();
	}

	@Override
	public void emettre() throws InformationNonConformeException {
		for (DestinationInterface <Boolean> destinationConnectees : destinationsConnectees) {
	        destinationConnectees.recevoir(informationEmise);
		}
	}

}