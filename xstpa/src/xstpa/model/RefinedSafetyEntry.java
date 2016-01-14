package xstpa.model;

import java.util.UUID;

import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.controlaction.IValueCombie;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;

public class RefinedSafetyEntry {
	
	private final ProcessModelVariables variable;
	private final String context;
	private final String type;
	private DataModelController model;
	private int number;
	
	private RefinedSafetyEntry(int number,String type,String context,ProcessModelVariables var, DataModelController controller) {
		this.context = context;
		this.type = type;
		this.variable = var;
		this.model = controller;
		this.number = number;
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
		
			for (UUID ucaID : variable.getUcaLinks(type)) {
				tempUcas = tempUcas.concat("UCA-" +model.getUCANumber(ucaID) +",");
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
	

	public static RefinedSafetyEntry getAnytimeEntry(int number,ProcessModelVariables var,DataModelController controller){
		return new RefinedSafetyEntry(number,IValueCombie.TYPE_ANYTIME, IValueCombie.CONTEXT_PROVIDED, var,controller);
		
	}
	public static RefinedSafetyEntry getTooLateEntry(int number,ProcessModelVariables var,DataModelController controller){
		return new RefinedSafetyEntry(number,IValueCombie.TYPE_TOO_LATE, IValueCombie.CONTEXT_PROVIDED, var,controller);
		
	}
	public static RefinedSafetyEntry getTooEarlyEntry(int number,ProcessModelVariables var,DataModelController controller){
		return new RefinedSafetyEntry(number,IValueCombie.TYPE_TOO_EARLY, IValueCombie.CONTEXT_PROVIDED, var,controller);
		
	}
	public static RefinedSafetyEntry getNotProvidedEntry(int number,ProcessModelVariables var,DataModelController controller){
		return new RefinedSafetyEntry(number,IValueCombie.TYPE_NOT_PROVIDED, IValueCombie.CONTEXT_NOT_PROVIDED, var,controller);
		
	}

	public String getRelatedHazards() {

		String tempHazLinks ="";
		for (UUID ucaID : variable.getUcaLinks(type)) {
			for (ITableModel tableModel : model.getLinkedHazardsOfUCA(ucaID)) {
				tempHazLinks = tempHazLinks.concat("H-"+tableModel.getNumber() + ",");
			}
		}
		if(!tempHazLinks.isEmpty()){
			return tempHazLinks.substring(0, tempHazLinks.length() -1);
		}
		return "not hazardous";
	}
	
	public String getRUCA(){
		StringBuffer ruca = new StringBuffer();
		ruca.append(variable.getLinkedControlActionName());
		ruca.append(" command ");
		ruca.append(getType());
		ruca.append(" when ");
		ruca.append(getCriticalCombinations());
		return ruca.toString();
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return this.number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(int number) {
		this.number = number;
	}
}
