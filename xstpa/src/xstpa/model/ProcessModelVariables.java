package xstpa.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.core.runtime.Assert;

import xstampp.astpa.model.controlaction.IValueCombie;


@XmlRootElement(name = "contexttablecombination")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "values", "valueIds", "pmVariables","pmValues", "variableIdsList", "name", "linkedControlActionName",
		"refinedSafetyRequirements", "context",	"number", "hazardous", "hLate", "hEarly", "hAnytime", "conflict",
		"archived","singleVarId", "isInRSRTable", "ucaLinks","relatedUCAsAnytime","relatedUCAsTooEarly","relatedUCAsTooLate"})
public class ProcessModelVariables {

	@XmlElementWrapper(name = "values")
	@XmlElement(name = "value")
	private List<String> values = new ArrayList<String>();
	
	@XmlElementWrapper(name = "valueIds")
	@XmlElement(name = "Id")
	private List<UUID> valueIds = new ArrayList<UUID>();
	
	@XmlElementWrapper(name = "pmVariables")
	@XmlElement(name = "pmVariable")
	private List<String> pmVariables = new ArrayList<String>();
	
	@XmlElementWrapper(name = "pmValues")
	@XmlElement(name = "pmValue")
	private List<String> pmValues = new ArrayList<String>();
	
	@XmlElementWrapper(name = "variableIdsList")
	@XmlElement(name = "varId")
	private List<UUID> variableIdsList;
	
	private String name;
	

	private String linkedControlActionName = "";
	private UUID linkedControlActionID = null;

	private String refinedSafetyRequirements ="";
	
	private String context;
	
	private int number;
	

	private boolean hazardous = false;
	
	
	private boolean hLate = false;
	
	
	private boolean hEarly = false;
	
	
	private boolean hAnytime = false;
	
	private boolean conflict = false;
	
	private boolean archived = false;
	
	private boolean isInRSRTable = false;

	private UUID singleVarId;

	@XmlElementWrapper(name = "ucaLinks")
	@XmlElement(name = "id")
	private List<UUID> ucaLinks= new ArrayList<>();
	
	@XmlElementWrapper(name = "relatedUCAsAnytime")
	@XmlElement(name = "id")
	private List<UUID> relatedUCAsAnytime;

	@XmlElementWrapper(name = "relatedUCAsTooEarly")
	@XmlElement(name = "id")
	private List<UUID> relatedUCAsTooEarly;

	@XmlElementWrapper(name = "relatedUCAsTooLate")
	@XmlElement(name = "id")
	private List<UUID> relatedUCAsTooLate;
	
	public ProcessModelVariables (List<String> pmVariables,ControlActionEntrys linkedControlAction ) {
		this.linkedControlActionName = linkedControlAction.getControlAction();
		this.pmVariables = pmVariables;
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
		return values;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}
	public void addValue (String value) {
		values.add(value);
		
	}
	public void removeValue (int index) {
		values.remove(index);
	}
	public int getSizeOfValues() {
		return values.size();
	}	
	
	//******************************
	//Management of the pm values which are used to represent the value state
	
	public List<String> getPmValues() {
		return pmValues;
	}
	public void setPmValues(List<String> pmValues) {
		this.pmValues = pmValues;
	}
	
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
	
//********************************************************************************************
// Management of the Hazardous state
	public Boolean getHazardous() {
		return hazardous || hLate || hEarly || hAnytime;
	}
	public void setHazardous(Boolean hazardous) {
		this.hazardous = hazardous;
	}
	public Boolean getHLate() {
		return hLate;
	}
	public void setHLate(Boolean hLate) {
		this.hLate = hLate;
	}
	public Boolean getHEarly() {
		return hEarly;
	}
	public void setHEarly(Boolean hEarly) {
		this.hEarly = hEarly;
	}
	public Boolean getHAnytime() {
		return hAnytime;
	}
	public void setHAnytime(Boolean hAnytime) {
		this.hAnytime = hAnytime;
	}
//
//***********************************************************************************************
	public Boolean getConflict() {
		return conflict;
	}
	public void setConflict(Boolean conflict) {
		this.conflict = conflict;
	}
	public String getLinkedControlActionName() {
		return linkedControlActionName;
	}
	public void setLinkedControlActionName(String linkedControlActionName, UUID caID) {
		this.linkedControlActionName = linkedControlActionName;
		this.linkedControlActionID = caID;
	}
	
//**************************************************************************************************
//Management of the process model variables
	
	public List<String> getPmVariables() {
		return pmVariables;
	}
	public void setPmVariables(List<String> pmVariables) {
		this.pmVariables = pmVariables;
	}
	/**
	 * this returns the variableId list which can be defined to 
	 * declare this object as a value combination
	 * 
	 * @return the variableIds
	 */
	public List<UUID> getVariableIds() {
		return variableIdsList;
	}

	/**
	 * sets a list of variable ids to this object<br>
	 * NOTE: an object of ProcessModelVariables can only have one of singleId and variableIdsList
	 * 
	 * @param variableIds the variableIds to set
	 */
	public void setVariableIds(List<UUID> variableIds) {
		Assert.isTrue(this.singleVarId == null,"ProcessModelVariables can only have one of singleId and variableIdsList");
		this.variableIdsList = variableIds;
	}

	/**
	 * adds a variable id to this object<br>
	 * NOTE: an object of ProcessModelVariables can only have one of singleId and variableIdsList
	 * 
	 * @param variableId the variableId to add
	 */
	public void addVariableId (UUID variableId) {
		Assert.isTrue(this.singleVarId == null,"ProcessModelVariables can only have one of singleId and variableIdsList");
		if(this.variableIdsList == null){
			this.variableIdsList = new ArrayList<>();
		}
		variableIdsList.add(variableId);
		
	}
	
	public Map<UUID,UUID> getValueMap(){
		Assert.isTrue(this.singleVarId == null,"ProcessModelVariables can only have one of singleId and variableIdsList");
		Assert.isTrue(this.variableIdsList.size() == this.valueIds.size());
		HashMap<UUID, UUID> valueMap = new HashMap<>();
		for(int i=0;i < this.variableIdsList.size();i++){
			valueMap.put(variableIdsList.get(i), valueIds.get(i));
		}
		return valueMap;
	}
	
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
		Assert.isTrue(this.variableIdsList == null,"ProcessModelVariables can only have one of singleId and variableIdsList");
		this.singleVarId = id;
	}
	
//****************************************************************************************************
	public String getRefinedSafetyRequirements() {
		return refinedSafetyRequirements;
	}
	public void setRefinedSafetyRequirements(String refinedSafetyRequirements) {
		this.refinedSafetyRequirements = refinedSafetyRequirements;
	}
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
	 * @return the archived
	 */
	public boolean isArchived() {
		return this.archived;
	}

	/**
	 * @param archived the archived to set
	 */
	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	/**
	 * @param type TODO
	 * @return the ucaLinks
	 */
	public List<UUID> getUcaLinks(String type) {
		List<UUID> temp = null;
		switch(type){
			case IValueCombie.TYPE_ANYTIME:
				temp = this.relatedUCAsAnytime;
				break;
			case IValueCombie.TYPE_TOO_EARLY:
				temp = this.relatedUCAsTooEarly;
				break;
			case IValueCombie.TYPE_TOO_LATE:
				temp = this.relatedUCAsTooLate;
				break;
			case IValueCombie.TYPE_NOT_PROVIDED:
				temp = this.ucaLinks;
				break;
		}
		if(temp == null){
			return new ArrayList<>();
		}
		return temp;
	}

	/**
	 * @param ucaLinks the ucaLinks to set
	 */
	public void setUcaLinks(List<UUID> ucaLinks,String type) {
		switch(type){
			case IValueCombie.TYPE_ANYTIME:
				this.relatedUCAsAnytime = ucaLinks;
				break;
			case IValueCombie.TYPE_TOO_EARLY:
				this.relatedUCAsTooEarly = ucaLinks;
				break;
			case IValueCombie.TYPE_TOO_LATE:
				this.relatedUCAsTooLate = ucaLinks;
				break;
			case IValueCombie.TYPE_NOT_PROVIDED:
				this.ucaLinks = ucaLinks;
				break;
		}
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





}
