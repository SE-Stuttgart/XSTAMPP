package xstpa.model;

import java.util.List;
import java.util.UUID;

import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.controlaction.IValueCombie;

public class RefinedSafetyEntry implements Comparable<RefinedSafetyEntry>{
	
	private final ProcessModelVariables variable;
	private final String context;
	private final String type;
	private DataModelController model;
	private int number;
	private UUID dataRef;
	private String constraint;
	private String refinedRule;
	private String ruca;
	private String ltlProperty;
	private RefinedSafetyEntry(int number,String type,String context,ProcessModelVariables var, DataModelController controller) {
		this.context = context;
		this.type = type;
		this.variable = var;
		this.model = controller;
		this.number = number;
	}


	public String getCriticalCombinations(String equalsSeq, String andSeq, boolean useBrackets, boolean parseBoolean){
		String temp ="";
		List<String> valueCombies = variable.getPmValues(equalsSeq, false);
		for (int i=0; i<valueCombies.size(); i++){
			if(useBrackets){
				temp = temp.concat('(' + valueCombies.get(i) + ')');
			}else{
				temp = temp.concat(valueCombies.get(i));
			}

			if (!(i==valueCombies.size()-1)) {
				temp = temp.concat(andSeq);
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
	
	private void update(){
		if(this.getDataRef() != null){
			model.removeSafetyRule(false, dataRef);
			setDataRef(null);
		}
		calcConstraint();
		calcLTL();
		calcRUCA();
		calcRule();
		setDataRef(model.addRefinedRule(getUCALinkIDs(),
				getLTLProperty(), 
				getRefinedRule(), 
				getRefinedUCA(), "", getNumber(),
				variable.getLinkedControlActionID(),
				type));
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
	
	private void calcRUCA(){	
		StringBuffer buffer = new StringBuffer();
		buffer.append(variable.getLinkedControlActionName());
		buffer.append(" command ");
		buffer.append(getType());
		buffer.append(" when ");
		buffer.append(getCriticalCombinations(" == ", ",", false, false));
		ruca = buffer.toString();
	}

	private void calcRule(){
		StringBuffer buffer = new StringBuffer();
		buffer.append(" Control Action ");
		buffer.append(variable.getLinkedControlActionName());
		buffer.append(" must not be ");
		buffer.append(getType());
		buffer.append(" when ");
		buffer.append(getCriticalCombinations(" is ", " and ", false, false));
		refinedRule = buffer.toString();
	}

	private void calcConstraint(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("when Control Action ");
		buffer.append(variable.getLinkedControlActionName());
		buffer.append(" must not be ");
		buffer.append(getType());
		buffer.append(" when ");
		buffer.append(getCriticalCombinations(" is ", " and ", false, false));
		refinedRule = buffer.toString();
	}
	
	
	
	private void calcLTL(){
		final String EQUALS ="==";
		final String IMPLIES = " -> ";
		final String IMPLIES_NOT = " ->! ";
		final String START = "[] ";
		final String UNTIL = " U ";
		final String RELEASE = " R ";
		final char BRACKET_OPEN ='(';
		final char BRACKET_CLOSE =')';
		final String CONTROLACTION = "controlAction" + EQUALS + getVariable().getLinkedControlActionName();
		StringBuffer valueBuffer = new StringBuffer(); 
		
		String values = getCriticalCombinations("==", "&&", true, false);
		/*
		 * for TYPE_ANYTIME rules the LTL can be generated as following: 
		 * 
		 * G((critical combinations set ) !-> (control_action==true))
		 */
		if (type.equals(IValueCombie.TYPE_ANYTIME)) {
			if(values != null && !values.isEmpty()){
				valueBuffer = new StringBuffer();
				valueBuffer.append(START);
				valueBuffer.append(BRACKET_OPEN);
				valueBuffer.append(values);
				valueBuffer.append(IMPLIES_NOT);
				valueBuffer.append(BRACKET_OPEN);
				valueBuffer.append(CONTROLACTION);
				valueBuffer.append(BRACKET_CLOSE);
				valueBuffer.append(BRACKET_CLOSE);
			}
		}
		/*
		 * for too early rules the Timed LTL can be generated as following: 
		 * 
		 * G( <control_action==value> -> (control_action==value> U (critical combinations set ))
		 */
		else if (type.equals(IValueCombie.TYPE_TOO_EARLY)) {
			if(values != null && !values.isEmpty()){
				valueBuffer = new StringBuffer();
				valueBuffer.append(START);
				valueBuffer.append(BRACKET_OPEN);
					valueBuffer.append(BRACKET_OPEN);
					valueBuffer.append(CONTROLACTION);
					valueBuffer.append(BRACKET_CLOSE);
					valueBuffer.append(IMPLIES);
					valueBuffer.append(BRACKET_OPEN);
						valueBuffer.append(BRACKET_OPEN);
						valueBuffer.append(CONTROLACTION);
						valueBuffer.append(BRACKET_CLOSE);
						valueBuffer.append(UNTIL);
						valueBuffer.append(BRACKET_OPEN);
						valueBuffer.append(values);
						valueBuffer.append(BRACKET_CLOSE);
					valueBuffer.append(BRACKET_CLOSE);
				valueBuffer.append(BRACKET_CLOSE);
			}
		}
		/*
		 * for too late rules the Timed LTL can be generated as following:
		 * G(  (critical combinations set ) -> ( (critical combinations set ) U (<control_action==value>) ) )
		 */
		else if (type.equals(IValueCombie.TYPE_TOO_LATE)) {
			if(values != null && !values.isEmpty()){
				valueBuffer = new StringBuffer();
				valueBuffer.append(START);
				valueBuffer.append(BRACKET_OPEN);
					valueBuffer.append(values);
					valueBuffer.append(IMPLIES);
					valueBuffer.append(BRACKET_OPEN);
						valueBuffer.append(values);
						valueBuffer.append(RELEASE);
						valueBuffer.append(CONTROLACTION);
					valueBuffer.append(BRACKET_CLOSE);
				valueBuffer.append(BRACKET_CLOSE);
			}
		}
		/*
		 * for TYPE_NOT_PROVIDED rules the LTL can be generated as following: 
		 * 
		 * G((critical combinations set ) -> (control_action==true))
		 */
		else if (type.equals(IValueCombie.TYPE_NOT_PROVIDED)) {
			if(values != null && !values.isEmpty()){
				valueBuffer = new StringBuffer();
				valueBuffer.append(START);
				valueBuffer.append(BRACKET_OPEN);
					valueBuffer.append(values);
					valueBuffer.append(IMPLIES);
					valueBuffer.append(BRACKET_OPEN);
						valueBuffer.append(CONTROLACTION);
					valueBuffer.append(BRACKET_CLOSE);
				valueBuffer.append(BRACKET_CLOSE);
			}
		}
		
		ltlProperty = valueBuffer.toString();
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
	public List<UUID> getUCALinkIDs(){
		return variable.getUcaLinks(type);
	}
	/**
	 * @return the variable
	 */
	public ProcessModelVariables getVariable() {
		return this.variable;
	}
	public String getRefinedUCA(){
		return ruca;
	}
	public String getRefinedRule(){
		return refinedRule;
	}
	public String Constraint() {
		return this.constraint;
	}
	public String getConstraintID(){
		return "SSR1."+this.number;
	}
	public String getRefinedUCAID(){
		return "RUCA1."+this.number;
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

	@Override
	public int compareTo(RefinedSafetyEntry sibling) {
		int sign =(int) Math.signum(getNumber() - sibling.getNumber());
		return sign;
	}
	
	public String getLTLProperty() {
		return ltlProperty;
	}
	/**
	 * @return the dataRef
	 */
	public UUID getDataRef() {
		return this.dataRef;
	}

	/**
	 * @param dataRef the dataRef to set
	 */
	public void setDataRef(UUID dataRef) {
		this.dataRef = dataRef;
	}
	
	public static RefinedSafetyEntry getAnytimeEntry(int number,ProcessModelVariables var,DataModelController controller){
		RefinedSafetyEntry entry = new RefinedSafetyEntry(number,IValueCombie.TYPE_ANYTIME, IValueCombie.CONTEXT_PROVIDED, var,controller);
		entry.setDataRef(var.getAnytimeRule());
		entry.update();
		var.setAnytimeRule(entry.getDataRef());
		
		return entry;
	}
	public static RefinedSafetyEntry getTooLateEntry(int number,ProcessModelVariables var,DataModelController controller){
		RefinedSafetyEntry entry = new RefinedSafetyEntry(number,IValueCombie.TYPE_TOO_LATE, IValueCombie.CONTEXT_PROVIDED, var,controller);
		entry.setDataRef(var.getTooLateRule());
		entry.update();
		var.setTooLateRule(entry.getDataRef());
		
		return entry;
	}
	public static RefinedSafetyEntry getTooEarlyEntry(int number,ProcessModelVariables var,DataModelController controller){
		RefinedSafetyEntry entry = new RefinedSafetyEntry(number,IValueCombie.TYPE_TOO_EARLY, IValueCombie.CONTEXT_PROVIDED, var,controller);
		entry.setDataRef(var.getTooEarlyRule());
		entry.update();
		var.setTooEarlyRule(entry.getDataRef());
		
		return entry;
	}
	public static RefinedSafetyEntry getNotProvidedEntry(int number,ProcessModelVariables var,DataModelController controller){
		
			RefinedSafetyEntry entry = new RefinedSafetyEntry(number,IValueCombie.TYPE_NOT_PROVIDED, IValueCombie.CONTEXT_NOT_PROVIDED, var,controller);
			entry.setDataRef(var.getNotProvidedRule());
			entry.update();
			var.setNotProvidedRule(entry.getDataRef());
		
		return entry;
		
	}
}
