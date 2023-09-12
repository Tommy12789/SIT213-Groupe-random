package destinations;

import information.Information;
import information.InformationNonConformeException;

public class DestinationFinaleAnalogique extends Destination <Float>{

	/**
	 * Destination finale
	 * @author morga
	 */
	public DestinationFinaleAnalogique() {
		super();
		informationRecue = new Information <Float>();
	}

	@Override
	public void recevoir(Information<Float> information) throws InformationNonConformeException {
		for (int i = 0 ; information.nbElements() > i ; i++ ) {
			informationRecue.add(information.iemeElement(i));
		}
		
	}


}
