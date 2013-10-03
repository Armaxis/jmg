package core.data.tonality;

import java.util.List;

public class MultiTonality extends Tonality {
	private List<Tonality> tonalitiesList;
	
	public MultiTonality(List<Tonality> tonalitiesList) {		
		super(tonalitiesList.get(0).getPitch(), tonalitiesList.get(0).getIntervals());
		this.tonalitiesList = tonalitiesList;
		this.degrees = tonalitiesList.get(0).degrees;
		
		this.type = "multi";
	}
	@Override
	public void switchTonality(int id) {
		this.pitch = tonalitiesList.get(id).getPitch();
		this.intervals = tonalitiesList.get(id).getIntervals();
		this.degrees = tonalitiesList.get(id).degrees;
	}

}
