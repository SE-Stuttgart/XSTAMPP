package xstpa.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class ControlActionEntry {
	
	private String controller;
	private String controlAction;
	private Boolean safety_critical;
	private String comments;
	
	private UUID id;
	private int number;

	private List<ProcessModelVariables>  linkedItems = new ArrayList<ProcessModelVariables>();

	private List<ProcessModelVariables>  availableItems = new ArrayList<ProcessModelVariables>();
	
	// all combinations generated by ACTS

	private List<ProcessModelVariables>  contextTableCombinations = new ArrayList<ProcessModelVariables>();
	
	public ControlActionEntry() {
		comments = "";
		safety_critical = true;
		
	}
	
	
	public String getController() {
		return controller;
	}
	public void setController(String controller) {
		this.controller = controller;
	}
	public String getControlAction() {
		return controlAction;
	}
	public void setControlAction(String controlAction) {
		this.controlAction = controlAction;
	}
	public Boolean getSafetyCritical() {
		return safety_critical;
	}
	public void setSafetyCritical(Boolean safety_critical) {
		this.safety_critical = safety_critical;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

//======================================================================
//START Linked list
//======================================================================

	/**
	 *  this method returns the list of all process model variables this control Action 
	 *  dependents on</br>
	 *  NOTE: the linkedItems list is not to be confused with the contextTableCombinations!!
	 *   
	 * @return  the list of process model variables objects,which represent the variable 
	 * 			components in the control structure
	 */
	public List<ProcessModelVariables> getLinkedItems() {
		return linkedItems;
	}

	/**
	 *  this method sets the list of all process model variables this control Action 
	 *  dependents on</br>
	 *  NOTE: the linkedItems list is not to be confused with the contextTableCombinations!!
	 *   
	 * @param linkedItems the list of process model variables objects,which represent the variable 
	 * 						components in the control structure
	 */
	public void setLinkedItems(List<ProcessModelVariables> linkedItems) {
		if(linkedItems == null){
			this.linkedItems = new ArrayList<>();
		}else{
			this.linkedItems = linkedItems;
		}
	}
	
	/**
	 *  this method adds an item to the list of all process model variables this control Action 
	 *  dependents on</br>
	 *  NOTE: the linkedItems list is not to be confused with the contextTableCombinations!!
	 *   
	 * @param item a process model variables object,which represents a variable 
	 * 						components in the control structure
	 */
	public void addLinkedItem(ProcessModelVariables item) {
		linkedItems.add(item);
	}
	
	
	public void removeLinkedItem(int id) {
		linkedItems.remove(id);
	}
	
	public void removeLinkedItem(ProcessModelVariables item) {
		linkedItems.remove(item);
		
	}
	
	public void removeAllLinkedItems() {
		linkedItems.clear();
	}
	
	public ProcessModelVariables getLinkedItem(int id) {
		return linkedItems.get(id-1);
	}

//======================================================================
//END linked list
//======================================================================


//======================================================================
//START Available list
//======================================================================

	public List<ProcessModelVariables> getAvailableItems() {
		return availableItems;
	}

	public void setAvailableItems(List<ProcessModelVariables> availableItems) {
		if(availableItems == null){
			this.availableItems = new ArrayList<>();
		}else{
			this.availableItems = availableItems;
		}
		this.availableItems = availableItems;
	}
	
	/**adds a process model variable to the list of variables which can be linked to
	 * this control action Entry in the dependences table
	 * 
	 * @param item an object of the type ProcessModelVariable
	 */
	public void addAvailableItem(ProcessModelVariables item) {
		availableItems.add(item);
		//setNumber(availableItems.indexOf(item)+1);
		//setNumber(item.getNumber());
	}
	
	public void addAvailableItem(int position, ProcessModelVariables item) {
		availableItems.set(position, item);
	}
	
	public void removeAvailableItem(int id) {
		availableItems.remove(id);
	}
	
	public void removeAvailableItem(ProcessModelVariables item) {
		availableItems.remove(item);
		
	}
	
	public void removeAllAvailableItems() {
		availableItems.clear();
		
	}

//======================================================================
//END available list
//======================================================================

	/**
	 * This Function sorts a given List by its ID (Number)
	 * @param list
	 * 		the list to be sorted
	 * @return list
	 * 		the sorted list
	 */
	public List<ProcessModelVariables> sortItems(List<ProcessModelVariables> list) {
		
		ProcessModelVariables[] tempList = new ProcessModelVariables[linkedItems.size() + availableItems.size()];

		
		for (ProcessModelVariables entry : list) {
			if (entry.getNumber()-1 > tempList.length) {
				
			}
			tempList[(entry.getNumber())-1] = entry;
			}
		list.clear();
		for (ProcessModelVariables entry : tempList) {
			if (entry != null) {
				list.add(entry);
			}
		}
			
			
		
		
		return list;
		
	}
	
	public List<ProcessModelVariables> sortnpItems(List<ProcessModelVariables> list) {
		
		ProcessModelVariables[] tempList = new ProcessModelVariables[linkedItems.size() + availableItems.size()];

		
		for (ProcessModelVariables entry : list) {
			if (entry.getNumber()-1 > tempList.length) {
				
			}
			tempList[(entry.getNumber())-1] = entry;
			}
		list.clear();
		for (ProcessModelVariables entry : tempList) {
			if (entry != null) {
				list.add(entry);
			}
		}
			
			
		
		
		return list;
		
	}
	
	@XmlElementWrapper(name = "contexttablecombinations")
	@XmlElement(name = "contexttablecombination")
	public List<ProcessModelVariables> getContextTableCombinations() {
		return contextTableCombinations;
	}

	public void setContextTableCombinations(List<ProcessModelVariables> contextTableCombinations) {
		this.contextTableCombinations = contextTableCombinations;
	}
	
	public void addContextTableCombination(ProcessModelVariables entry) {
		if(this.contextTableCombinations == null){
			this.contextTableCombinations = new ArrayList<>();
		}
		contextTableCombinations.add(entry);
	}
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
}