package xstpa;

import java.util.ArrayList;
import java.util.List;

import xstampp.astpa.haz.controlaction.interfaces.IUnsafeControlAction;

public class UnsafeControlAction {
	private List<String> linkedDescriptions = new ArrayList<String>();
	private List<String> descriptions = new ArrayList<String>();
	private String title;
	private ProcessModelVariables entryToEdit;
	public UnsafeControlAction (ProcessModelVariables entryToEdit) {
		this.entryToEdit = entryToEdit;
		
	}
	
	
	public List<String> getDescriptions() {
		return descriptions;
	}
	public void setTitle(List<String> descriptions) {
		this.descriptions = descriptions;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}


	public List<String> getLinkedDescriptions() {
		return linkedDescriptions;
	}


	public void setLinkedDescriptions(List<String> linkedDescriptions) {
		this.linkedDescriptions = linkedDescriptions;
	}
	
	public void initialize() {
		List<IUnsafeControlAction> unsafeCA;
		for (int j = 0; j<View.model.getAllControlActions().size();j++) {
			unsafeCA = View.model.getAllControlActions().get(j).getUnsafeControlActions();
			for (int i=0; i<unsafeCA.size();i++){
				if (View.model.getAllControlActions() != null) {
					if (entryToEdit.getLinkedControlActionName().equals(View.model.getAllControlActions().get(j).getTitle())) {				
						descriptions.add(unsafeCA.get(i).getDescription());					
					}
				}
			}
		}
	}
	
}
