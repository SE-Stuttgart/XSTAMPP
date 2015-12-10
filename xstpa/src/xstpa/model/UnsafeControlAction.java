package xstpa.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import xstampp.astpa.haz.controlaction.interfaces.IUnsafeControlAction;
import xstpa.ui.View;

public class UnsafeControlAction {
	private List<String> linkedDescriptions = new ArrayList<String>();
	private List<String> descriptions = new ArrayList<String>();
	
	private List<UUID> linkedDescriptionIds = new ArrayList<UUID>();
	private List<UUID> descriptionIds = new ArrayList<UUID>();
	private String relatedHazards = "No Related Hazards";
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
	
	public void addDescription(String description) {
		this.descriptions.add(description);
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
	public void addLinkedDescription(String description) {
		this.linkedDescriptions.add(description);
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


	public String getRelatedHazards() {
		return relatedHazards;
	}


	public void setRelatedHazards(String relatedHazards) {
		this.relatedHazards = relatedHazards;
	}


	public List<UUID> getLinkedDescriptionIds() {
		return linkedDescriptionIds;
	}


	public void setLinkedDescriptionIds(List<UUID> linkedDescriptionIds) {
		this.linkedDescriptionIds = linkedDescriptionIds;
	}

	public void addLinkedDescriptionId(UUID descriptionId) {
		this.linkedDescriptionIds.add(descriptionId);
	}

	public List<UUID> getDescriptionIds() {
		return descriptionIds;
	}


	public void setDescriptionIds(List<UUID> descriptionIds) {
		this.descriptionIds = descriptionIds;
	}
	
	public void addDescriptionId(UUID descriptionId) {
		this.descriptionIds.add(descriptionId);
	}
	
}
