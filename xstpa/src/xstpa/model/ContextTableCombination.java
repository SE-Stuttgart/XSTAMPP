package xstpa.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IExtendedDataModel;
import xstampp.model.IValueCombie;


public class ContextTableCombination {
	
	private Map<UUID,UUID> valueIdTOvariableId = new HashMap<>();
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
	
	public ContextTableCombination() {
		// Empty Constructor for JAXB
	}

//********************************************************************************************
// Management of the value name list

	/**
	 * removes all entries from the {@link #valueIdTOvariableId} map
	 */
	public void clearIDsMap(){
		valueIdTOvariableId.clear();
	}
	
	/**
	 * removes all entries from the {@link #valueNameTOvariableId} map
	 */
	public void clearNameMap(){
		valueIdTOvariableId.clear();
	}
	/**
	 * adds an entry in the {@link #valueIdTOvariableId} map
	 * that maps the given valueID to the variableId 
	 * 
	 * @param variableId the UUID with which a variable component is stored in the data model
	 * @param valueId the UUID with which a value component is stored in the data model
	 * 
	 * @return {@link HashMap#put(Object, Object)}
	 * @see HashMap#put(Object, Object)
	 */
	public UUID addValueMapping(UUID variableId,UUID valueId){
		if(variableId != null && valueId != null){
			return valueIdTOvariableId.put(variableId, valueId);
		}
		return null;
	}
	
	/**
	 * 
	 * @param variableId should be the id of a variable stored in the data Model
	 * @return the value which is contained in the {@link #valueIdTOvariableId} for the given UUID or 
	 * 				null if there is no stored mapping or the variableId is null
	 */
	public UUID getValueIDForVariable(UUID variableId){
		if(variableId == null || !valueIdTOvariableId.containsKey(variableId)){
			return null;
		}
		return valueIdTOvariableId.get(variableId);
	}
	
	/**
	 * 
	 * @return a copy of {@link #valueIdTOvariableId}
	 */
	public HashMap<UUID,UUID> getValueIDTOVariableIdMap(){
		new HashMap<>(this.valueIdTOvariableId);
		return new HashMap<>(this.valueIdTOvariableId);
	}
	
	/**
	 * 
	 * @return the size of {@link #valueIdTOvariableId}
	 * 
	 * @see HashMap#size()
	 */
	public int getSizeOfValues() {
		return valueIdTOvariableId.size();
	}	
	
	//******************************
	//Management of the pm values which are used to represent the value state
	
	/**
	 * 
	 * @param root 
	 * @param equalsSeq the sequence that is used to separate the variable-value tuples
	 * @param parseBoolean whether or not  getPmValues should translate any boolean expressions into natural language
	 * @param useSpaces
	 * @return
	 */
	public List<String> getPmValues(IExtendedDataModel controller, String equalsSeq, boolean parseBoolean, boolean useSpaces) {
		ArrayList<String> list = new ArrayList<>();
		String valueString;
		String prefix;
		for (Entry<UUID, UUID> valueMapping : this.valueIdTOvariableId.entrySet()) {
			IRectangleComponent value = controller.getComponent(valueMapping.getValue());
			IRectangleComponent variable = controller.getComponent(valueMapping.getKey());
			valueString = value.getText().replaceAll(">|<|=", "").trim();
			prefix = equalsSeq.trim();
			char c = value.getText().trim().charAt(0);
			if(c == '>' || c == '<' || c == '='){
				
				if(!parseBoolean){
					if(c != '='){
						prefix = String.valueOf(c);
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
				list.add(variable.getText().trim() + ' ' +prefix +' '+valueString);
			}else{
				list.add(variable.getText().trim() + prefix +valueString);
			}
		}
		return list;
	}
	//**********************************
	//Management of the value ids
	
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
