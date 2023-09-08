package destinations;

import information.Information;
import information.InformationNonConformeException;

public class DestinationFinale extends Destination <Boolean>{

	/**
	 * Destination finale
	 * @author morga
	 */
	public DestinationFinale() {
		super();
		informationRecue = new Information <Boolean>();
	}

	@Override
	public void recevoir(Information<Boolean> information) throws InformationNonConformeException {
		for (int i = 0 ; information.nbElements() > i ; i++ ) {
			informationRecue.add(information.iemeElement(i));
		}
		
	}


}
