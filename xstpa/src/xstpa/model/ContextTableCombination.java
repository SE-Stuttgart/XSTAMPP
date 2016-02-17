package xstpa.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.core.runtime.Assert;

import xstampp.astpa.model.controlaction.IValueCombie;


public class ContextTableCombination {

	private List<String> valueNames = new ArrayList<String>();
	private List<UUID> valueIds = new ArrayList<UUID>();
	private List<String> variableNames = new ArrayList<String>();
	private List<UUID> variableIds;
	
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

	private UUID notProvidedRule = null;
	private UUID anytimeRule = null;
	private UUID tooEarlyRule = null;
	private UUID tooLateRule = null;
	
	private List<UUID> ucaLinks= new ArrayList<>();
	private List<UUID> relatedUCAsAnytime;
	private List<UUID> relatedUCAsTooEarly;
	private List<UUID> relatedUCAsTooLate;
	
	private String rule;
	
	private String ruca;
	
	public ContextTableCombination (List<String> pmVariables,ControlActionEntry linkedControlAction ) {
		this.linkedControlActionName = linkedControlAction.getControlAction();
		this.variableNames = pmVariables;
		this.linkedControlActionID = linkedControlAction.getId();
	}
	
	public ContextTableCombination() {
		// Empty Constructor for JAXB
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
	
	/**
	 * 
	 * @param equalsSeq the sequence that is used to separate the variable-value tuples
	 * @param parseBoolean whether or not  getPmValues should translate any boolean expressions into natural language
	 * @param useSpaces
	 * @return
	 */
	public List<String> getPmValues(String equalsSeq, boolean parseBoolean, boolean useSpaces) {
		ArrayList<String> list = new ArrayList<>();
		String valueString;
		String prefix;
		for (int i = 0; i < this.valueNames.size(); i++) {
			valueString = this.valueNames.get(i).replaceAll(">|<|=", "").trim();
			prefix = equalsSeq.trim();
			char c = this.valueNames.get(i).trim().charAt(0);
			if(c == '>' || c == '<' || c == '='){
				
				if(!parseBoolean){
					if(c != '='){
						prefix = String.valueOf(this.valueNames.get(i).trim().charAt(0));
					}
				}else{
					if(c == '<'){
						prefix = "is less than";
					}else if(c == '>'){
						prefix = "is greater than";
					}
				}
			}
			if(useSpaces){
				list.add(this.variableNames.get(i).trim() + ' ' +prefix +' '+valueString);
			}else{
				list.add(this.variableNames.get(i).trim() + prefix +valueString);
			}
		}
		return list;
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
	public Boolean getGlobalHazardous() {
		return hazardous || hLate || hEarly || hAnytime;
	}
	public void setGlobalHazardous(Boolean hazardous) {
		this.hazardous = hazardous;
		this.hLate = hazardous;
		this.hEarly = hazardous;
		this.hAnytime = hazardous;
	}
	public Boolean getHazardous() {
		return hazardous;
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
		return variableNames;
	}
	public void setPmVariables(List<String> pmVariables) {
		this.variableNames = pmVariables;
	}
	/**
	 * this returns the variableId list which can be defined to 
	 * declare this object as a value combination
	 * 
	 * @return the variableIds
	 */
	public List<UUID> getVariableIds() {
		if(variableIds == null){
			variableIds = new ArrayList<>();
		}
		return variableIds;
	}

	/**
	 * adds a variable id to this object<br>
	 * 
	 * @param variableId the variableId to add
	 */
	public void addVariableId (UUID variableId) {
		if(this.variableIds == null){
			this.variableIds = new ArrayList<>();
		}
		variableIds.add(variableId);
		
	}
	
	/**
	 * adds a variable to this object
	 * 
	 * @param variable the name of a combined variable
	 */
	public void addVariable (String variable) {
		if(this.variableNames == null){
			this.variableNames = new ArrayList<>();
		}
		variableNames.add(variable);
		
	}

	/**
	 * sets a list of variable ids to this object<br>
	 * @param variableIds the variableIds to set
	 */
	public void setVariableIds(List<UUID> variableIds) {
		this.variableIds = variableIds;
	}
	
	public Map<UUID,UUID> getValueMap(){
		Assert.isTrue(this.variableIds.size() == this.valueIds.size());
		HashMap<UUID, UUID> valueMap = new HashMap<>();
		for(int i=0;i < this.variableIds.size();i++){
			valueMap.put(variableIds.get(i), valueIds.get(i));
		}
		return valueMap;
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

	/**
	 * @return the rule
	 */
	public String getSafetyRule() {
		return this.rule;
	}

	/**
	 * @param rule the rule to set
	 */
	public void setSafetyRule(String rule) {
		this.rule = rule;
	}

	/**
	 * @return the ruca
	 */
	public String getRefinedUnsafeControlAction() {
		return this.ruca;
	}

	/**
	 * @param ruca the ruca to set
	 */
	public void setRefinedUnsafeControlAction(String ruca) {
		this.ruca = ruca;
	}

	/**
	 * @return the notProvidedRule
	 */
	public UUID getNotProvidedRule() {
		return this.notProvidedRule;
	}

	/**
	 * @param notProvidedRule the notProvidedRule to set
	 */
	public void setNotProvidedRule(UUID notProvidedRule) {
		this.notProvidedRule = notProvidedRule;
	}

	/**
	 * @return the anytimeRule
	 */
	public UUID getAnytimeRule() {
		return this.anytimeRule;
	}

	/**
	 * @param uuid the anytimeRule to set
	 */
	public void setAnytimeRule(UUID uuid) {
		this.anytimeRule = uuid;
	}

	/**
	 * @return the tooEarlyRule
	 */
	public UUID getTooEarlyRule() {
		return this.tooEarlyRule;
	}

	/**
	 * @param tooEarlyRule the tooEarlyRule to set
	 */
	public void setTooEarlyRule(UUID tooEarlyRule) {
		this.tooEarlyRule = tooEarlyRule;
	}

	/**
	 * @return the tooLateRule
	 */
	public UUID getTooLateRule() {
		return this.tooLateRule;
	}

	/**
	 * @param tooLateRule the tooLateRule to set
	 */
	public void setTooLateRule(UUID tooLateRule) {
		this.tooLateRule = tooLateRule;
	}

}
