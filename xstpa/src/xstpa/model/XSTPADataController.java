package xstpa.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import xstampp.astpa.haz.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.causalfactor.ICausalComponent;
import xstampp.astpa.model.controlaction.ControlAction;
import xstampp.astpa.model.controlaction.IValueCombie;
import xstampp.astpa.model.controlaction.NotProvidedValuesCombi;
import xstampp.astpa.model.controlaction.ProvidedValuesCombi;
import xstampp.astpa.model.controlstructure.components.Component;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;

public class XSTPADataController {

	private static final String CONTEXT_PROVIDED ="provided";
	private static final String CONTEXT_NOT_PROVIDED ="not provided";
	private List<ProcessModelValue> valuesList;
	private Map<UUID,ControlActionEntrys> dependenciesIFProvided;
	private ArrayList<ProcessModelVariables> variablesList;
	private Map<UUID,ControlActionEntrys> dependenciesNotProvided;
	private ControlActionEntrys linkedCAE;
	private ProcessModelVariables linkedPMV;
	private DataModelController model;
	private boolean controlActionProvided;
	
	public XSTPADataController() {
		this.valuesList = new ArrayList<>();
		this.variablesList = new ArrayList<>();
		this.dependenciesIFProvided  = new HashMap<>();
		this.dependenciesNotProvided = new HashMap<>();
	}
	
	public void clear(DataModelController model) {
		this.linkedCAE = null;
		this.linkedPMV = null;
		this.dependenciesIFProvided.clear();
		this.dependenciesNotProvided.clear();
		this.fetchProcessComponents(model);
		this.fetchControlActions(model);
	}
	
//********************************************************************************************************************
// Management of the PROCESS MODEL VALUES
 
	/**
	 * @return the valuesList
	 * @see ProcessModelValue
	 */
	public List<ProcessModelValue> getValuesList() {
		return this.valuesList;
	}

	public int getValueCount() {
		return this.valuesList.size();
	}
	/**
	 * returns a ProcessModelValue 
	 * 
	 * @param index the index in the list of values
	 * @return the ProcessModelValue stored at the given index
	 * @see ProcessModelValue
	 */
	public ProcessModelValue getValue(int index) {
		return this.valuesList.get(index);
	}
	/**
	 * @param value the value which is to add to the valuesList
	 */
	public void addValue(ProcessModelValue value) {
		this.valuesList.add(value);
	}

	public boolean removeValue(int index) {
		return this.valuesList.remove(index) != null;
	}
//********************************************************************************************************************
// Management of the PROCESS MODEL VARIABLES																		
	
	/**
	 * @return the variablesList
	 */
	public ArrayList<ProcessModelVariables> getVariablesList() {
		return this.variablesList;
	}

	/**
	 * returns a ProcessModelVariables 
	 * 
	 * @param index the index in the list of values
	 * @return the ProcessModelVariable stored at the given index
	 * @see ProcessModelVariables
	 */
	public ProcessModelVariables getVariable(int index) {
		return this.variablesList.get(index);
	}
	
	public int getVariablesCount() {
		return this.variablesList.size();
	}
	/**
	 * @param varible the value which is to add to the valuesList
	 */
	public void addVariable(ProcessModelVariables varible) {
		varible.setNumber(this.variablesList.size()+1);
		this.variablesList.add(varible);
	}
	
	
	/**
	 * fetches the current available values and variable components from the data model
	 * 
	 * @param model the current data model 
	 */
	private void fetchProcessComponents(DataModelController model){

		this.valuesList.clear();
		this.variablesList.clear();
		List<ICausalComponent> templist = model.getCausalComponents();
		for (int i = 0, n = templist.size(); i < n; i++) {
			
	    	  Component parentComponent =(Component) templist.get(i);
		      if (parentComponent.getComponentType().name().equals("CONTROLLER")) {
		    	  
		    	  // get the process models
		    	  for (IRectangleComponent tempPM :  parentComponent.getChildren()) {
		    		  
		    		  // get the variables
		    		  for (IRectangleComponent tempPMV : tempPM.getChildren()) {
		    			  ProcessModelVariables variable = new ProcessModelVariables();
		    			  variable.setName(tempPMV.getText());
		    			  variable.setId(tempPMV.getId());
		    			  // get the values and add the new object to the processmodel list
		    			  for (IRectangleComponent tempPMVV : tempPMV.getChildren()) {
		    				  
		    				  ProcessModelValue pmValueObject = new ProcessModelValue();
		    				  
		    				  pmValueObject.setController(parentComponent.getText());
		    				  pmValueObject.setPM(tempPM.getText());
		    				  pmValueObject.setPMV(tempPMV.getText());
		    				  pmValueObject.setValueText(tempPMVV.getText());
		    				  pmValueObject.setId(tempPMVV.getId());
		    				  pmValueObject.setVariableID(tempPMV.getId());
		    				  pmValueObject.setComments(tempPMVV.getComment());
		    				  variable.addValue(tempPMVV.getText());
		    				  variable.addValueId(tempPMVV.getId());
		    				  addValue(pmValueObject);
		    				  
		    			  }
		    			  if (!variable.getValues().isEmpty()) {
		    				  addVariable(variable);
		    			  }
		    		  }
		    		  
		    	  }
			  }
	      }
		// Add the dontcare obj
		ProcessModelValue finalObj = new ProcessModelValue();
		IRectangleComponent dontCare = model.getIgnoreLTLValue();
		finalObj.setValueText(dontCare.getText());
		finalObj.setId(dontCare.getId());
		addValue(finalObj);
	}
	
	/**
	 * this method pulls all control actions and fills the 
	 * list of dependent variables with the information from 
	 * this{@link #fetchProcessComponents(DataModelController)}
	 * 
	 * @param model the data model which should be used
	 */
	private void fetchControlActions(DataModelController model){
		  // get the controlActions
	      for (IControlAction entry : model.getAllControlActionsU()) {
	    	  this.dependenciesIFProvided.put(entry.getId(),getEntryFor(entry, model.getValuesWhenCAProvided(entry.getId()),CONTEXT_PROVIDED));
	    	  this.dependenciesNotProvided.put(entry.getId(),getEntryFor(entry, model.getValuesWhenCANotProvided(entry.getId()),CONTEXT_NOT_PROVIDED));
	      }
	}
	
	private ControlActionEntrys getEntryFor(IControlAction entry,List<IValueCombie> combies,String context){
		ControlActionEntrys tempCAEntry = new ControlActionEntrys();
  	  
		//tempCAE.setController(entry.);
		tempCAEntry.setComments(entry.getDescription());
		tempCAEntry.setControlAction(entry.getTitle());
		tempCAEntry.setNumber(entry.getNumber());
		tempCAEntry.setId(entry.getId());	    	  
		tempCAEntry.setSafetyCritical(model.isCASafetyCritical(entry.getId()));
		List<UUID> linkedIDs;
  	  	if(context.equals(CONTEXT_PROVIDED)){
  	  		linkedIDs = ((ControlAction)entry).getProvidedVariables();
  	  	}else{
  	  		linkedIDs = ((ControlAction)entry).getNotProvidedVariables();
  	  	}
		/*
		 *  set linkedItems and available items for the control action entry
		 */
		for (ProcessModelVariables var : getVariablesList()) {
			if(linkedIDs.contains(var.getId())) {
				tempCAEntry.addLinkedItem(var);
			}else{
				tempCAEntry.addAvailableItem(var);
			}
		}
  	  

		// add all the value combinations for the context table to the two dependencies lists 
  		  
		ProcessModelVariables contextTableEntry;
		for (IValueCombie valueCombie :  combies) {
			List<String> tempValuesList = new ArrayList<String>();
			List<String> tempVarialesList = new ArrayList<String>();
			contextTableEntry = new ProcessModelVariables();
			
			contextTableEntry.setUcaLinks(valueCombie.getUCALinks(IValueCombie.TYPE_NOT_PROVIDED),IValueCombie.TYPE_NOT_PROVIDED);
			contextTableEntry.setUcaLinks(valueCombie.getUCALinks(IValueCombie.TYPE_ANYTIME),IValueCombie.TYPE_ANYTIME);
			contextTableEntry.setUcaLinks(valueCombie.getUCALinks(IValueCombie.TYPE_TOO_EARLY),IValueCombie.TYPE_TOO_EARLY);
			contextTableEntry.setUcaLinks(valueCombie.getUCALinks(IValueCombie.TYPE_TOO_LATE),IValueCombie.TYPE_TOO_LATE);
			
			if(valueCombie.getPMValues() == null){
				Map<UUID, UUID> valuesIdsTOvariableIDs= new HashMap<>();
				for(ProcessModelValue value : getValuesList()){
					if(valueCombie.getValueList().contains(value.getId())){
						valuesIdsTOvariableIDs.put(value.getVariableID(), value.getId());
					}
				}
				valueCombie.setValues(valuesIdsTOvariableIDs);
			}
			for (UUID varId : valueCombie.getPMValues().keySet()) {
				contextTableEntry.addVariableId(varId);
				tempVarialesList.add(model.getComponent(varId).getText());
				for (int j = 0; j<getValueCount();j++) {
					if (valueCombie.getPMValues().get(varId).equals(getValue(j).getId())) {
						tempValuesList.add(model.getComponent(varId).getText()+ "="
								+ getValue(j).getValueText());
						contextTableEntry.addValue(getValue(j).getValueText());		    						  
						contextTableEntry.setLinkedControlActionName(entry.getTitle(), entry.getId());
						contextTableEntry.addValueId(getValue(j).getId());
  						  
					}
				}
			}
		  
			contextTableEntry.setPmValues(tempValuesList);
			contextTableEntry.setPmVariables(tempVarialesList);
			contextTableEntry.setContext(context);
			contextTableEntry.setRefinedSafetyRequirements(valueCombie.getSafetyConstraint());
			contextTableEntry.setHazardous(valueCombie.isCombiHazardous(IValueCombie.TYPE_NOT_PROVIDED));
			contextTableEntry.setHAnytime(valueCombie.isCombiHazardous(IValueCombie.TYPE_ANYTIME));
			contextTableEntry.setHEarly(valueCombie.isCombiHazardous(IValueCombie.TYPE_TOO_EARLY));
			contextTableEntry.setHLate(valueCombie.isCombiHazardous(IValueCombie.TYPE_TOO_LATE));
			tempCAEntry.addContextTableCombination(contextTableEntry);
		}
		
		return tempCAEntry;
	}

//=====================================================================
//START 
//=====================================================================

	/**
	 * Store the Boolean Data (from the Context Table) in the Datamodel
	 * @param caEntry the ControlActionEntrys or null for the currently linked which should be stored in the data model
	 */
	public void storeBooleans(ControlActionEntrys caEntry) {
		ControlActionEntrys temp = caEntry;
		if(temp == null){
			temp = this.linkedCAE;
		}
		if (this.dependenciesIFProvided.containsValue(temp)) {
			syncCombiesWhenProvided(temp);
		}
		else {
	    	syncCombiesWhenNotProvided(temp);
		}
	}
	
	private void syncCombiesWhenProvided(ControlActionEntrys caEntry){
  		  List<ProvidedValuesCombi> valuesIfProvided = new ArrayList<ProvidedValuesCombi>();
  		  ProvidedValuesCombi val = new ProvidedValuesCombi();
  		  //iteration over all value combinations registered for the linked control action
  		  for (ProcessModelVariables combie :caEntry.getContextTableCombinations()) {
  			  val = new ProvidedValuesCombi();
  			  if(combie.getValueIds().isEmpty() || combie.getVariableIds() == null){
   				 val.setValues(getCombieUUIDs(combie));
  			  }else{
  				  val.setValues(combie.getValueMap());
  			  }

  			  val.setUCALinks(combie.getUcaLinks(IValueCombie.TYPE_ANYTIME),IValueCombie.TYPE_ANYTIME);
  			  val.setUCALinks(combie.getUcaLinks(IValueCombie.TYPE_TOO_EARLY),IValueCombie.TYPE_TOO_EARLY);
  			  val.setUCALinks(combie.getUcaLinks(IValueCombie.TYPE_TOO_LATE),IValueCombie.TYPE_TOO_LATE);
  			  
  			  val.setArchived(combie.isArchived());
  			  val.setConstraint(combie.getRefinedSafetyRequirements());
  			  val.setHazardousAnyTime(combie.getHAnytime());
  			  val.setHazardousToEarly(combie.getHEarly());
  			  val.setHazardousToLate(combie.getHLate());
  			  valuesIfProvided.add(val);
  		  }
  		  model.setValuesWhenCAProvided(caEntry.getId(),valuesIfProvided);
	}
	
	private void syncCombiesWhenNotProvided(ControlActionEntrys caEntry){
  		  List<NotProvidedValuesCombi> valuesIfProvided = new ArrayList<NotProvidedValuesCombi>();
  		  NotProvidedValuesCombi val = new NotProvidedValuesCombi();
  		  //iteration over all value combinations registered for the linked control action
  		  for (ProcessModelVariables combie : caEntry.getContextTableCombinations()) {
  			  val = new NotProvidedValuesCombi();
  			  if(combie.getValueIds().isEmpty() || combie.getVariableIds() == null){
  				 val.setValues(getCombieUUIDs(combie));
  			  }else{
  				  val.setValues(combie.getValueMap());
  			  }
  			  val.setUCALinks(combie.getUcaLinks(IValueCombie.TYPE_NOT_PROVIDED),IValueCombie.TYPE_NOT_PROVIDED);
  				  
  			  val.setArchived(combie.isArchived());
  			  val.setConstraint(combie.getRefinedSafetyRequirements());
  			  val.setHazardous(combie.getHazardous());
  			  valuesIfProvided.add(val);
  		  }
  		  model.setValuesWhenCANotProvided(caEntry.getId(),valuesIfProvided);
		
	}
	
	
	public Map<UUID,UUID> getCombieUUIDs(ProcessModelVariables variables){
		Map<UUID,UUID> combis = new HashMap<>();
		  //iterate over all values stored in that combination
		  for (int z = 0; z<variables.getValues().size();z++) {
			  String sVarName =variables.getPmVariables().get(z);
			  String sValueName =variables.getValues().get(z);
			  UUID variableID =null;
			  for(ProcessModelVariables variable : getVariablesList()){
				  String variableString = variable.getName();
				  if(sVarName.contains("_")){
					  variableString = variableString.replace(" ", "_");
				  }
				  if(variableString.equals(sVarName)){
					  variableID = variable.getId();
				  }
			  }
			  
			  //iteration over all available and in the data model stored values
			  //to get the uuids mapped to the components
			  for (int n = 0; n<getValueCount();n++) {
				  
				  //2 if cases are used to find find the right variable-value combination 
				  if (getValue(n).getId().equals(model.getIgnoreLTLValue().getId()) || getValue(n).getVariableID().equals(variableID)) {
					  
					  String tempValue =getValue(n).getValueText().trim();
					  
					  if ((tempValue.equals(sValueName))) {
						  combis.put(variableID,getValue(n).getId());
						  variables.addValueId(getValue(n).getId());
						  variables.addVariableId(getValue(n).getVariableID());
					  }
					  else if ("(don't care)".equals(sValueName)) {
						  combis.put(variableID,model.getIgnoreLTLValue().getId());
						  variables.addValueId(model.getIgnoreLTLValue().getId());
						  variables.addVariableId(getValue(n).getVariableID());
					  }
				  }
			  }
		  }
		 return combis;
	}
	
//=====================================================================
//END 
//=====================================================================

	
	
//********************************************************************************************************************
// Management of the control action dependencies																	
		

	/**
	 * @return the dependenciesIFProvided
	 */
	public Collection<ControlActionEntrys> getDependenciesIFProvided() {
		return this.dependenciesIFProvided.values();
	}
	
	/**
	 * @return the dependenciesNotProvided
	 */
	public Collection<ControlActionEntrys> getDependenciesNotProvided() {
		return this.dependenciesNotProvided.values();
	}

	/**
	 * 
	 * @param providedContext whether the context is 'provided' or not
	 * @param id the id which is provided by astpa for the requested controlAction
	 * @return the control action entry stored in the context map for the given id
	 */
	public ControlActionEntrys getControlActionEntry(boolean providedContext,UUID id){
		if(providedContext){
			return this.dependenciesIFProvided.get(id);
		}
		return this.dependenciesNotProvided.get(id);
	}
	
	/**
	 * @return the linkedPMV
	 */
	public ProcessModelVariables getLinkedPMV() {
		return this.linkedPMV;
	}

	/**
	 * @param linkedPMV the linkedPMV to set
	 */
	public void setLinkedPMV(ProcessModelVariables linkedPMV) {
		this.linkedPMV = linkedPMV;
	}

	/**
	 * @return the linkedCAE
	 */
	public ControlActionEntrys getLinkedCAE() {
		return this.linkedCAE;
	}

	/**
	 * 
	 * @param provided whether the control action entry should be
	 * 					chosen out of the provided list or not 
	 * @param i the linkedCAE to set
	 */
	public boolean setLinkedCAE(boolean provided,UUID i) {
		
		this.controlActionProvided = provided;
		return setLinkedCAE(i);
	}
	
	/**
	 * this method sets the linked control action to the entry stored <br>
	 * at the index i either in the {@link #dependenciesIFProvided} or the {@link #dependenciesNotProvided} list<br>
	 * depending on the {@link #controlActionProvided} choice
	 * @param i the index in either the {@link #dependenciesIFProvided} or the {@link #dependenciesNotProvided} list
	 * 
	 * @return whether the linked control action has changed
	 */
	public boolean setLinkedCAE(UUID i) {
		if(i == null || !(this.dependenciesIFProvided.containsKey(i) || this.dependenciesNotProvided.containsKey(i))){
			this.linkedCAE = null;
			return true;
		}
		if(this.controlActionProvided && !this.dependenciesIFProvided.get(i).equals(this.linkedCAE)){
			this.linkedCAE = this.dependenciesIFProvided.get(i);
			return true;
		}
		if(!this.controlActionProvided && !this.dependenciesNotProvided.get(i).equals(this.linkedCAE)){
			this.linkedCAE = this.dependenciesNotProvided.get(i);
			return true;
		}
		return false;
	}
	public boolean isControlActionProvided(){
		return controlActionProvided;
	}

	/**
	 * @return the model
	 */
	public DataModelController getModel() {
		return this.model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(DataModelController model) {
		this.model = model;
	}
}
