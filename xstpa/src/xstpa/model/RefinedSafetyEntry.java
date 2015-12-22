package xstpa.model;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.controlaction.IValueCombie;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;

public class RefinedSafetyEntry {
	
	private final ProcessModelVariables variable;
	private final String context;
	private final String type;
	private DataModelController model;
	
	
	private RefinedSafetyEntry(String type,String context,ProcessModelVariables var, DataModelController controller) {
		this.context = context;
		this.type = type;
		this.variable = var;
		this.model = controller;
	}

	/**
	 * @return the context
	 */
	public String getContext() {
		return this.context;
	}


	/**
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	public String getCriticalCombinations(){
		String temp ="";

		for (int i=0; i<variable.getPmValues().size(); i++){
	
			temp = temp.concat(variable.getPmValues().get(i));

			if (!(i==variable.getPmValues().size()-1)) {
				temp = temp.concat(",");
			}
		}
		return temp;
	}
	
	public String getUCALinks(){
		String tempUcas ="";
		
			for(int i= 0 ; i< model.getAllUnsafeControlActions().size();i++){
				if(variable.getUcaLinks(type).contains(model.getAllUnsafeControlActions().get(i).getId())){
					tempUcas = tempUcas.concat("UCA-" +i +",");
				}
			}
		
		if(tempUcas.endsWith(",")){
			tempUcas = tempUcas.substring(0, tempUcas.length()-1);
		}
		return tempUcas;
		
	}
	/**
	 * @return the variable
	 */
	public ProcessModelVariables getVariable() {
		return this.variable;
	}
	

	public static RefinedSafetyEntry getAnytimeEntry(ProcessModelVariables var,DataModelController controller){
		return new RefinedSafetyEntry(IValueCombie.TYPE_ANYTIME, IValueCombie.CONTEXT_PROVIDED, var,controller);
		
	}
	public static RefinedSafetyEntry getTooLateEntry(ProcessModelVariables var,DataModelController controller){
		return new RefinedSafetyEntry(IValueCombie.TYPE_TOO_LATE, IValueCombie.CONTEXT_PROVIDED, var,controller);
		
	}
	public static RefinedSafetyEntry getTooEarlyEntry(ProcessModelVariables var,DataModelController controller){
		return new RefinedSafetyEntry(IValueCombie.TYPE_TOO_EARLY, IValueCombie.CONTEXT_PROVIDED, var,controller);
		
	}
	public static RefinedSafetyEntry getNotProvidedEntry(ProcessModelVariables var,DataModelController controller){
		return new RefinedSafetyEntry(IValueCombie.TYPE_NOT_PROVIDED, IValueCombie.CONTEXT_NOT_PROVIDED, var,controller);
		
	}

	public String getRelatedHazards() {
		// TODO Auto-generated method stub
		return null;
	}
}
