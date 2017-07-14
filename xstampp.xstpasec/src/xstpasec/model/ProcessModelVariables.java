/*******************************************************************************
 * Copyright (c) 2013, 2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstpasec.model;

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
	
	/**
	 * sets the name defined as text of the defined IRectangleComponent 
	 * 
	 * @param name the name of the variable
	 */
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
	
	/**
	 * sets the value ids defined as children of the represented variable to the given list
	 * 
	 * @param valueIds the list of id's of a value components stored in the dataModel
	 */
	public void setValueIds(List<UUID> valueIds) {
		this.valueIds = valueIds;
	}
	
	/**
	 * adds a value id that is defined as child of the represented variable,
	 * this method creates a new list if the valueIds list is null  
	 * @param valueId the id of a value component stored in the dataModel
	 */
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
