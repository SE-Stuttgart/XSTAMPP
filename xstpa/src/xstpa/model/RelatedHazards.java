package xstpa.model;

import java.util.ArrayList;
import java.util.List;

public class RelatedHazards {
	private List<Hazard>  linkedItems = new ArrayList<Hazard>();

	private List<Hazard>  availableItems = new ArrayList<Hazard>();
	
	public RelatedHazards(List<Hazard> availableHazards) {
		for (Hazard entry : availableHazards) {
			availableItems.add(new Hazard(entry.getNumber(), entry.getDescription(), entry.getName()));
		}
		
	}

	public List<Hazard> getLinkedItems() {
		return linkedItems;
	}

	public void setLinkedItems(List<Hazard> linkedItems) {
		this.linkedItems = linkedItems;
	}

	public List<Hazard> getAvailableItems() {
		return availableItems;
	}

	public void setAvailableItems(List<Hazard> availableItems) {
		this.availableItems = availableItems;
	}

}
