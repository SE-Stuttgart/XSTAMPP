package xstpa.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ProcessModelVariables {

	private List<String> valueNames = new ArrayList<String>();
	private List<UUID> valueIds = new ArrayList<UUID>();
	
	private String name;
	private String linkedControlActionName = "";
	private UUID linkedControlActionID = null;
	private String context;
	private int number;
	
	private boolean isInRSRTable = false;
	private UUID singleVarId;

	
	private UUID controllerID;
	public ProcessModelVariables (List<String> pmVariables,ControlActionEntry linkedControlAction ) {
		this.linkedControlActionName = linkedControlAction.getControlAction();
		this.linkedControlActionID = linkedControlAction.getId();
	}
	
	public ProcessModelVariables() {
		// Empty Constructor for JAXB
	}

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
//********************************************************************************************
// Management of the value name list

	public List<String> getValues() {
		return valueNames;
	}
	public void setValues(List<String> values) {
		this.valueNames = values;
	}
	public void addValue (String value) {
		valueNames.add(value);
		
	}
	public void removeValue (int index) {
		valueNames.remove(index);
	}
	public int getSizeOfValues() {
		return valueNames.size();
	}	
	
	//******************************
	//Management of the pm values which are used to represent the value state
	

	//**********************************
	//Management of the value ids
	
	public List<UUID> getValueIds() {
		return valueIds;
	}

	public void setValueIds(List<UUID> valueIds) {
		this.valueIds = valueIds;
	}
	public void addValueId (UUID valueId) {
		if(this.valueIds == null){
			this.valueIds = new ArrayList<>();
		}
		valueIds.add(valueId);
		
	}
// 
//********************************************************************************************
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	

//
//***********************************************************************************************

	public String getLinkedControlActionName() {
		return linkedControlActionName;
	}
	public void setLinkedControlActionName(String linkedControlActionName, UUID caID) {
		this.linkedControlActionName = linkedControlActionName;
		this.linkedControlActionID = caID;
	}
	
//**************************************************************************************************
//Management of the process model variables

	public UUID getId() {
		return singleVarId;
	}
	
	/**
	 * sets a single variable id to this object<br>
	 * NOTE: an object of ProcessModelVariables can only have one of singleId and variableIdsList
	 * 
	 * @param variableIds the variableIds to set
	 */
	public void setId(UUID id) {
		this.singleVarId = id;
	}
	
//****************************************************************************************************

	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}

	public Boolean getIsInRSRTable() {
		return isInRSRTable;
	}

	public void setIsInRSRTable(Boolean isInRSRTable) {
		this.isInRSRTable = isInRSRTable;
	}

	/**
	 * @return the linkedControlActionID
	 */
	public UUID getLinkedControlActionID() {
		return this.linkedControlActionID;
	}

	/**
	 * @param linkedControlActionID the linkedControlActionID to set
	 */
	public void setLinkedControlActionID(UUID linkedControlActionID) {
		this.linkedControlActionID = linkedControlActionID;
	}

	/**
	 * @return the controllerID
	 */
	public UUID getControllerID() {
		return this.controllerID;
	}

	/**
	 * @param controllerID the controllerID to set
	 */
	public void setControllerID(UUID controllerID) {
		this.controllerID = controllerID;
	}



}
