package xstpa.model;

import java.util.ArrayList;
import java.util.List;

public class ContextTableEntry {
	private String linkedControlAction;
	private List<String> items = new ArrayList<String>();

	public List<String> getItems() {
		return items;
	}

	public void setItems(List<String> items) {
		this.items = items;
	}
	
	public void setItem (String item) {
		items.add(item);
	}

	public String getLinkedControlAction() {
		return linkedControlAction;
	}

	public void setLinkedControlAction(String linkedControlAction) {
		this.linkedControlAction = linkedControlAction;
	}
}
