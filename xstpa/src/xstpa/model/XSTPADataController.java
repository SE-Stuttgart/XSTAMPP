package xstpa.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
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
import xstampp.model.ObserverValue;

public class XSTPADataController extends Observable implements Observer{

	private static final String CONTEXT_PROVIDED ="provided";
	private static final String CONTEXT_NOT_PROVIDED ="not provided";
	private Map<UUID,ProcessModelValue> valuesList;
	private Map<UUID,ControlActionEntry> dependenciesIFProvided;
	private Map<UUID,ProcessModelVariables> variablesList;
	private Map<UUID,ControlActionEntry> dependenciesNotProvided;
	private ControlActionEntry linkedCAE;
	private ProcessModelVariables linkedPMV;
	private DataModelController model;
	private boolean controlActionProvided;
	private List<RefinedSafetyEntry> refinedEntrys;
	
	public XSTPADataController(DataModelController model) {
		this.valuesList = new HashMap<>();
		this.variablesList = new HashMap<>();
		this.refinedEntrys = new ArrayList<>();
		this.dependenciesIFProvided  = new HashMap<>();
		this.dependenciesNotProvided = new HashMap<>();
		this.model = model;
		clear();
		
		
	}
	
	public void clear() {
		this.linkedCAE = null;
		this.linkedPMV = null;
		this.dependenciesIFProvided.clear();
		this.dependenciesNotProvided.clear();
		if(model != null){
			this.fetchProcessComponents();
			this.fetchControlActions();
		}
	}
	
//********************************************************************************************************************
// Management of the PROCESS MODEL VALUES
 
	/**
	 * @return the valuesList
	 * @see ProcessModelValue
	 */
	public List<ProcessModelValue> getValuesList() {
		return new ArrayList<ProcessModelValue>(this.valuesList.values());
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
		this.valuesList.put(value.getId(),value);
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
		return new ArrayList<ProcessModelVariables>(this.variablesList.values());
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
		this.variablesList.put(varible.getId(),varible);
	}
	
	
	/**
	 * fetches the current available values and variable components from the data model
	 * 
	 * @param model the current data model 
	 */
	private void fetchProcessComponents(){

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
	private void fetchControlActions(){
		this.dependenciesIFProvided.clear();
		this.dependenciesNotProvided.clear();
		  // get the controlActions
	      for (IControlAction entry : model.getAllControlActionsU()) {
	    	  this.dependenciesIFProvided.put(entry.getId(),getEntryFor(entry, model.getIvaluesWhenCAProvided(entry.getId()),CONTEXT_PROVIDED));
	    	  this.dependenciesNotProvided.put(entry.getId(),getEntryFor(entry, model.getIValuesWhenCANotProvided(entry.getId()),CONTEXT_NOT_PROVIDED));
	      }
	}
	
	private ControlActionEntry getEntryFor(IControlAction entry,List<IValueCombie> combies,String context){
		ControlActionEntry tempCAEntry = new ControlActionEntry(context);
  	  
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
			if(context.equals(CONTEXT_PROVIDED)){
				contextTableEntry.setAnytimeRule(((ProvidedValuesCombi)valueCombie).getAnytimeRuleId());
	  	  	}else{
	  	  		linkedIDs = ((ControlAction)entry).getNotProvidedVariables();
	  	  	}
			if(valueCombie.getPMValues() == null){
				Map<UUID, UUID> valuesIdsTOvariableIDs= new HashMap<>();
				for(ProcessModelValue value : getValuesList()){
					if(valueCombie.getValueList().contains(value.getId())){
						valuesIdsTOvariableIDs.put(value.getVariableID(), value.getId());
					}
				}
				valueCombie.setValues(valuesIdsTOvariableIDs);
			}
			if(valueCombie.getPMValues() == null){
				HashMap<UUID, UUID> map = new HashMap<>();
				for(UUID valueId : valueCombie.getValueList()){
					map.put(valuesList.get(valueId).getVariableID(),valueId);
				}
				valueCombie.setValues(map);
			}
			for (UUID valId : valueCombie.getPMValues().values()) {
				contextTableEntry.addVariableId(valuesList.get(valId).getVariableID());
				tempVarialesList.add(model.getComponent(valuesList.get(valId).getVariableID()).getText());
				
				tempValuesList.add(model.getComponent(valuesList.get(valId).getVariableID()).getText()+ "="
						+ valuesList.get(valId).getValueText());
				contextTableEntry.addValue(valuesList.get(valId).getValueText());		    						  
				contextTableEntry.setLinkedControlActionName(entry.getTitle(), entry.getId());
				contextTableEntry.addValueId(valId);
  					
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

	
	public Map<String,ArrayList<RefinedSafetyEntry>> getHazardousCombinations(UUID caID){
		HashMap<String,ArrayList<RefinedSafetyEntry>> combiesToContextID = new HashMap<>();

		combiesToContextID.put(IValueCombie.HAZ_IF_NOT_PROVIDED, new ArrayList<RefinedSafetyEntry>());
		combiesToContextID.put(IValueCombie.HAZ_IF_PROVIDED, new ArrayList<RefinedSafetyEntry>());
		combiesToContextID.put(IValueCombie.HAZ_IF_WRONG_PROVIDED, new ArrayList<RefinedSafetyEntry>());
		if(getModel() == null){
			return combiesToContextID;
		}

		model.lockUpdate();
		ArrayList<ControlActionEntry> allCAEntrys = new ArrayList<>();
  	    allCAEntrys.addAll(getDependenciesIFProvided());
  	    allCAEntrys.addAll(getDependenciesNotProvided());

		int count = 0;
		boolean consider;
		ArrayList<UUID> currentRSR= new ArrayList<>();
		for (IControlAction ca : getModel().getAllControlActions()) {
  	    	consider = (caID == null) || ca.getId().equals(caID);
  	    	if(getControlActionEntry(true, ca.getId()) == null){
  	    		fetchControlActions();
  	    	}
			RefinedSafetyEntry entry;
			for(ProcessModelVariables variable : getControlActionEntry(true, ca.getId()).getContextTableCombinations()){
				if(variable.getHAnytime()){
					count++;
					if(consider){
						entry = RefinedSafetyEntry.getAnytimeEntry(count,variable,getModel());
						currentRSR.add(entry.getDataRef());
						combiesToContextID.get(IValueCombie.HAZ_IF_PROVIDED)
												.add(entry);
					}
				}
				if(variable.getHEarly()){
					count++;
					if(consider){
						entry = RefinedSafetyEntry.getTooEarlyEntry(count,variable,getModel());
						currentRSR.add(entry.getDataRef());
						combiesToContextID.get(IValueCombie.HAZ_IF_WRONG_PROVIDED)
												.add(entry);
					}
				}
				if(variable.getHLate()){
					count++;
					if(consider){
						entry = RefinedSafetyEntry.getTooLateEntry(count,variable,getModel());
						currentRSR.add(entry.getDataRef());
						combiesToContextID.get(IValueCombie.HAZ_IF_WRONG_PROVIDED).
											add(entry);
					}
				}
			}
			
			for (ProcessModelVariables variable : getControlActionEntry(false, ca.getId()).
					getContextTableCombinations()) {
				if(variable.getGlobalHazardous()){
					count++;
					if(consider){
						entry = RefinedSafetyEntry.getNotProvidedEntry(count,variable,getModel());
						currentRSR.add(entry.getDataRef());
						combiesToContextID.get(IValueCombie.HAZ_IF_NOT_PROVIDED).
											add(entry);
					}
				}
			}
		}
		int total =model.getLTLPropertys().size()-1;
		for (int i = total; i >= 0; i--) {
			if(!currentRSR.contains(model.getLTLPropertys().get(i).getRuleId())){
				model.removeSafetyRule(false, model.getLTLPropertys().get(i).getRuleId());
			}
		}
		model.releaseLockAndUpdate(ObserverValue.Extended_DATA);
		return combiesToContextID;
	}
//=====================================================================
//START Save function
//=====================================================================

	/**
	 * Store the Boolean Data (from the Context Table) in the Datamodel
	 * @param caEntry the ControlActionEntrys or null for the currently linked which should be stored in the data model
	 */
	public void storeBooleans(ControlActionEntry caEntry) {
		ControlActionEntry temp = caEntry;
		if(temp == null){
			temp = this.linkedCAE;
		}
		if (this.dependenciesIFProvided.containsValue(temp)) {
			syncCombiesWhenProvided(temp);
		}
		else {
	    	syncCombiesWhenNotProvided(temp);
		}
		
		setChanged();
		notifyObservers(ObserverValue.CONTROL_ACTION);
	}
	
	private void syncCombiesWhenProvided(ControlActionEntry caEntry){
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
	
	private void syncCombiesWhenNotProvided(ControlActionEntry caEntry){
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
  			  val.setHazardous(combie.getGlobalHazardous());
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
			  for (ProcessModelValue value :valuesList.values()) {
				  
				  //2 if cases are used to find find the right variable-value combination 
				  if (value.getId().equals(model.getIgnoreLTLValue().getId()) || value.getVariableID().equals(variableID)) {
					  
					  String tempValue =value.getValueText().trim();
					  
					  if ((tempValue.equals(sValueName))) {
						  combis.put(variableID,value.getId());
						  variables.addValueId(value.getId());
						  variables.addVariableId(value.getVariableID());
					  }
					  else if ("(don't care)".equals(sValueName)) {
						  combis.put(variableID,model.getIgnoreLTLValue().getId());
						  variables.addValueId(model.getIgnoreLTLValue().getId());
						  variables.addVariableId(value.getVariableID());
					  }
				  }
			  }
		  }
		 return combis;
	}
	
//=====================================================================
//END Save function
//=====================================================================

	
	
//********************************************************************************************************************
// Management of the control action dependencies																	
		

	/**
	 * @return the dependenciesIFProvided
	 */
	public Collection<ControlActionEntry> getDependenciesIFProvided() {
		return this.dependenciesIFProvided.values();
	}
	
	/**
	 * @return the dependenciesNotProvided
	 */
	public Collection<ControlActionEntry> getDependenciesNotProvided() {
		return this.dependenciesNotProvided.values();
	}

	/**
	 * 
	 * @param providedContext whether the context is 'provided' or not
	 * @param id the id which is provided by astpa for the requested controlAction
	 * @return the control action entry stored in the context map for the given id
	 */
	public ControlActionEntry getControlActionEntry(boolean providedContext,UUID id){
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
	public ControlActionEntry getLinkedCAE() {
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
	 * @param i the index in either the {@link #dependenciesIFProvided} or the {@link #dependenciesNotProvided} list or null if 
	 * 			there is no linked control action at the time
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

	@Override
	public void update(Observable arg0, Object updatedValue) {
		final ObserverValue value= (ObserverValue) updatedValue; 
		switch(value){
			case CONTROL_ACTION:
			case CONTROL_STRUCTURE:
				new Runnable() {
					@Override
					public void run() {
						clear();
						setChanged();
						notifyObservers(value);
					}
				}.run();
			default:
				break;
			
		}
	}

	/**
	 * @return the refinedEntrys
	 */
	public List<RefinedSafetyEntry> getRefinedEntrys() {
		return this.refinedEntrys;
	}

	/**
	 * @param refinedEntrys the refinedEntrys to set
	 */
	public void setRefinedEntrys(List<RefinedSafetyEntry> refinedEntrys) {
		this.refinedEntrys = refinedEntrys;
	}
}
