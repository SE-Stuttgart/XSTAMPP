package xstpa.model;

import java.util.ArrayList;
import java.util.List;

import xstampp.astpa.haz.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.causalfactor.ICausalComponent;
import xstampp.astpa.model.controlaction.ProvidedValuesCombi;
import xstampp.astpa.model.controlstructure.components.Component;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstpa.ui.ControlActionEntrys;

public class XSTPADataController {
	private List<ProcessModelValue> valuesList;
	private List<ControlActionEntrys> dependenciesIFProvided;
	private ArrayList<ProcessModelVariables> variablesList;
	private List<ControlActionEntrys> dependenciesProvided = new ArrayList<ControlActionEntrys>();
	
	private List<ControlActionEntrys> dependenciesNotProvided = new ArrayList<ControlActionEntrys>();

	public XSTPADataController() {
		this.valuesList = new ArrayList<>();
		this.variablesList = new ArrayList<>();
	}
	
	public void clear(DataModelController model) {
		this.valuesList.clear();
		this.variablesList.clear();
		this.fetchProcessComponents(model);
		this.fetchControlActions(model);
	}
	/**
	 * @return the dependenciesIFProvided
	 */
	public List<ControlActionEntrys> getDependenciesIFProvided() {
		return this.dependenciesIFProvided;
	}

	/**
	 * @param dependenciesIFProvided the dependenciesIFProvided to set
	 */
	public void setDependenciesIFProvided(List<ControlActionEntrys> dependenciesIFProvided) {
		this.dependenciesIFProvided = dependenciesIFProvided;
	}

/********************************************************************************************************************
 * Management of the PROCESS MODEL VALUES
 * 																	
 ********************************************************************************************************************/
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
		varible.setNumber(this.variablesList.size());
		this.variablesList.add(varible);
	}
	
	/**
	 * fetches the current available values and variable components from the data model
	 * 
	 * @param model the current data model 
	 */
	private void fetchProcessComponents(DataModelController model){
		List<ICausalComponent> templist = model.getCausalComponents();
		for (int i = 0, n = templist.size(); i < n; i++) {
	    	  
	    	  ProcessModelValue tempCWPME = new ProcessModelValue();
	    	  Component parentComponent =(Component) templist.get(i);
		      if (parentComponent.getComponentType().name().equals("CONTROLLER")) {
		    	  
		    	  tempCWPME.setController(parentComponent.getText());
		    	  // get the process models
		    	  for (IRectangleComponent tempPM :  parentComponent.getChildren()) {
		    		  tempCWPME.setPM(tempPM.getText());
		    		  
		    		  // get the variables
		    		  for (IRectangleComponent tempPMV : tempPM.getChildren()) {
		    			  if(!tempPMV.getComponentType().equals(ComponentType.PROCESS_VARIABLE)){
		    				  System.out.println();
		    			  }
		    			  tempCWPME.setPMV(tempPMV.getText());
		    			  ProcessModelVariables variable = new ProcessModelVariables();
		    			  variable.setName(tempPMV.getText());
		    			  variable.setId(tempPMV.getId());
		    			  variable.addVariableId(tempPMV.getId());
		    			  // get the values and add the new object to the processmodel list
		    			  for (IRectangleComponent tempPMVV : tempPMV.getChildren()) {
		    				  tempCWPME.setComments(tempPMVV.getComment());
		    				  tempCWPME.setValueText(tempPMVV.getText());
		    				  tempCWPME.setId(tempPMVV.getId());
		    				  
		    				  ProcessModelValue pmValueObject = new ProcessModelValue();
		    				  
		    				  pmValueObject.setController(tempCWPME.getController());
		    				  pmValueObject.setPM(tempCWPME.getPM());
		    				  pmValueObject.setPMV(tempCWPME.getPMV());
		    				  pmValueObject.setValueText(tempPMVV.getText());
		    				  pmValueObject.setId(tempPMVV.getId());
		    				  pmValueObject.setVariableID(tempPMV.getId());
		    				  pmValueObject.setComments(tempCWPME.getComments());
		    				  variable.addValue(tempCWPME.getValueText());
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
	}
	
	/**
	 * this method pulls all control actions and fills the 
	 * list of dependent variables with the information from 
	 * this{@link #fetchProcessComponents(DataModelController)}
	 * 
	 * @param model the data model which should be used
	 */
	private void fetchControlActions(DataModelController model){
		List<IControlAction> iControlActions = model.getAllControlActionsU();
		  // get the controlActions
	      for (IControlAction entry : iControlActions) {
	    	  ControlActionEntrys tempCAEProvided = new ControlActionEntrys();
	    	  ControlActionEntrys tempCAENotProvided = new ControlActionEntrys();
	    	  
	    	  tempCAEProvided.setComments(entry.getDescription());
	    	  tempCAEProvided.setControlAction(entry.getTitle());
	    	  tempCAEProvided.setNumber(entry.getNumber());
	    	  tempCAEProvided.setId(entry.getId());
	    	  tempCAEProvided.setSafetyCritical(model.isCASafetyCritical(entry.getId()));
	    	  //tempCAE.setController(entry.);
	    	  tempCAENotProvided.setComments(entry.getDescription());
	    	  tempCAENotProvided.setControlAction(entry.getTitle());
	    	  tempCAENotProvided.setNumber(entry.getNumber());
	    	  tempCAENotProvided.setId(entry.getId());	    	  
	    	  tempCAENotProvided.setSafetyCritical(model.isCASafetyCritical(entry.getId()));
	    	  
	    		  /*
	    		   *  set linkedItems and available items for the control action entry
	    		   */
	    		  for (ProcessModelVariables var : getVariablesList()) {
	    			  if(model.getCANotProvidedVariables(entry.getId()).contains(var.getId())) {
	    				  tempCAENotProvided.addLinkedItem(var);
	    			  }else{
	    				  tempCAENotProvided.addAvailableItem(var);
	    			  }
	    			// set linkedItems for CA Provided
	    			  if(model.getCAProvidedVariables(entry.getId()).contains(var.getId())) {
  					  tempCAEProvided.addLinkedItem(var);
	    			  }else{
	    				  tempCAEProvided.addAvailableItem(var);
	    			  }
	    			  
	    		  }
	    	  
	
	    		  // add all the value combinations for the context table to the two dependencies lists 
	    		  
	    		  ProcessModelVariables contextTableEntry;
  			  for (int i = 0; i<model.getValuesWhenCANotProvided(entry.getId()).size();i++) {
  				  contextTableEntry = new ProcessModelVariables();
  				  for (int n = 0; n<model.getValuesWhenCANotProvided(entry.getId()).get(i).getPMValues().size();n++) {
  					  for (int j = 0; j<getValueCount();j++) {
	    					  if (model.getValuesWhenCANotProvided(entry.getId()).get(i).getPMValues().get(n).equals(getValue(j).getId())) {
	    						  
	    						  contextTableEntry.addValue(getValue(j).getValueText());		    						  
	    						  contextTableEntry.setLinkedControlActionName(entry.getTitle());
	    						  contextTableEntry.addValueId(getValue(j).getId());
	    						  
	    					  }
  					  }
  				  }
  			  
	    			  try{
	    				  contextTableEntry.getUca().setLinkedDescriptionIds(model.getValuesWhenCANotProvided(entry.getId()).
									get(i).getRefinedSafetyConstraints());
	    			  } catch (Exception e) {
	//    					  System.out.println("No stored refined Safety for Not Provided");
					  }
  			  
					  contextTableEntry.setContext("Not Provided");
					  contextTableEntry.setRefinedSafetyRequirements(model.getValuesWhenCANotProvided(entry.getId()).get(i).getSafetyConstraint());
					  contextTableEntry.setHazardous(model.getValuesWhenCANotProvided(entry.getId()).get(i).isCombiHazardous());
					  tempCAENotProvided.addContextTableCombination(contextTableEntry);
  			  }
  			  for (ProcessModelVariables valueCombie : tempCAENotProvided.getContextTableCombinations()) {
	  	   				 
	    				  List<String> tempValuesList = new ArrayList<String>();
	    				  List<String> tempVarialesList = new ArrayList<String>();
	  	    			  for (int z=0;z<tempCAENotProvided.getLinkedItems().size();z++) {
	  	    				  
	  	    				  tempValuesList.add(tempCAENotProvided.getLinkedItems().get(z).getName() + "="
	  	    						  													+ valueCombie.getValues().get(z));
	  	    				  tempVarialesList.add(tempCAENotProvided.getLinkedItems().get(z).getName());
	  	    				  valueCombie.addVariableId(tempCAENotProvided.getLinkedItems().get(z).getId());
	  	    				  
	  	    			  }
	  	    			  valueCombie.setPmValues(tempValuesList);
	    				  valueCombie.setPmVariables(tempVarialesList);
  			  }
	    		  
	    		  
				  
	    		  // set linkedItems for CA Provided
	    		  List<ProvidedValuesCombi> valueCombie=model.getValuesWhenCAProvided(entry.getId());
  			  for (int i = 0; i<valueCombie.size();i++) {
  				  contextTableEntry = new ProcessModelVariables(); 
  				  for (int n = 0; n<valueCombie.get(i).getPMValues().size();n++) {
  					  
  					  for (int j = 0; j<getValueCount();j++) {
  						  if (valueCombie.get(i).getPMValues().get(n).equals(getValue(j).getId())) {
  						  
	    						  contextTableEntry.addValue(getValue(j).getValueText());
	    						  contextTableEntry.setLinkedControlActionName(entry.getTitle());
	    						  contextTableEntry.addValueId(getValue(j).getId());
  						  }
  					 }
  					  
						  
  					  
  				  }

  				  try {
  					  contextTableEntry.getUca().setLinkedDescriptionIds(valueCombie.get(i).getRefinedSafetyConstraints());
  				  }
  				  catch (Exception e) {
//	    					  System.out.println("No stored refined Safety for Not Provided");
  				  }
  				  
  				  contextTableEntry.setContext("Provided");
					  contextTableEntry.setRefinedSafetyRequirements(valueCombie.get(i).getSafetyConstraint());
					  contextTableEntry.setHAnytime(valueCombie.get(i).isHazardousWhenAnyTime());
					  contextTableEntry.setHEarly(valueCombie.get(i).isHazardousWhenToEarly());
					  contextTableEntry.setHLate(valueCombie.get(i).isHazardousWhenToLate());
  				  tempCAEProvided.addContextTableCombination(contextTableEntry); 
  			  }
	    			  for (int j=0; j<tempCAEProvided.getContextTableCombinations().size(); j++) {
		  	    		  

		  	   				  ProcessModelVariables temp = new ProcessModelVariables();
		  	   				  temp = tempCAEProvided.getContextTableCombinations().get(j);

	  	    				  List<String> tempPmValList = new ArrayList<String>();
	  	    				  List<String> tempPmVarList = new ArrayList<String>();
		  	    			  for (int z=0;z<tempCAEProvided.getLinkedItems().size();z++) {
		  	    				  
		  	    				  tempPmValList.add(tempCAEProvided.getLinkedItems().get(z).getName() + "="
		  	    						  + tempCAEProvided.getContextTableCombinations().get(j).getValues().get(z));
		  	    				  tempPmVarList.add(tempCAEProvided.getLinkedItems().get(z).getName());
		  	    				  temp.setPmVariables(tempPmVarList);
		  	    				  temp.addVariableId(tempCAEProvided.getLinkedItems().get(z).getId());
		  	    				  
		  	    			  }
		  	    			  temp.setPmValues(tempPmValList);
	    			  }
	    		
	    	  
		    	  
	    	getDependenciesIFProvided().add(tempCAEProvided);
	    	getDependenciesNotProvided().add(tempCAENotProvided);

	      }
	}

//********************************************************************************************************************
// Management of the control action dependencies																	
		
	
	/**
	 * returns a list with entry objects for each control action
	 * which contain the variables which the control action depends when provided 
	 * 
	 * @return the dependenciesProvided
	 */
	public List<ControlActionEntrys> getDependenciesProvided() {
		return this.dependenciesProvided;
	}

	/**
	 * @param dependenciesProvided the dependenciesProvided to set
	 */
	public void setDependenciesProvided(List<ControlActionEntrys> dependenciesProvided) {
		this.dependenciesProvided = dependenciesProvided;
	}

	/**
	 * @return the dependenciesNotProvided
	 */
	public List<ControlActionEntrys> getDependenciesNotProvided() {
		return this.dependenciesNotProvided;
	}

	/**
	 * @param dependenciesNotProvided the dependenciesNotProvided to set
	 */
	public void setDependenciesNotProvided(List<ControlActionEntrys> dependenciesNotProvided) {
		this.dependenciesNotProvided = dependenciesNotProvided;
	}
}
