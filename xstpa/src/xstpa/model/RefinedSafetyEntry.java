package xstpa.model;

import java.util.List;
import java.util.UUID;

import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.controlaction.IValueCombie;

/**
 * an object of this class is used to generate the refined data of a hazardous process state.
 * An object of this class can only be created by using one of the static methods provided.
 * 
 * @author Lukas Balzer
 * @since 1.0.1
 *
 *@see RefinedSafetyEntry#getTooLateEntry(int, ProcessModelVariables, DataModelController)
 *@see RefinedSafetyEntry#getTooEarlyEntry(int, ProcessModelVariables, DataModelController)
 *@see RefinedSafetyEntry#getAnytimeEntry(int, ProcessModelVariables, DataModelController)
 *@see RefinedSafetyEntry#getNotProvidedEntry(int, ProcessModelVariables, DataModelController)
 */
public class RefinedSafetyEntry implements Comparable<RefinedSafetyEntry>{
	
	public static final String Literal ="SR"; //$NON-NLS-1$
	private final ContextTableCombination variable;
	private final String context;
	private final String type;
	private DataModelController model;
	private int number;
	private UUID dataRef;
	private String constraint;
	private String refinedRule;
	private String ruca;
	private String ltlProperty;
	
	/**
	 * This private constructor is only called by one of the four static methods provided by this class
	 * 
	 * @param number the number of the created Refined Data must be a positive nr > 0
	 * @param type the Type of the context the rule should be generated for one of the <code>TYPE</code> constants
	 * 				Defined in IValueCombie
	 * @param context the context of the refinement
	 * @param var the variable object which represents a hazardous value combination
	 * @param controller the astpa data model in which the refined data should be stored
	 * 
	 * @see IValueCombie
	 *@see RefinedSafetyEntry#getTooLateEntry(int, ProcessModelVariables, DataModelController)
	 *@see RefinedSafetyEntry#getTooEarlyEntry(int, ProcessModelVariables, DataModelController)
	 *@see RefinedSafetyEntry#getAnytimeEntry(int, ProcessModelVariables, DataModelController)
	 *@see RefinedSafetyEntry#getNotProvidedEntry(int, ProcessModelVariables, DataModelController)
	 */
	private RefinedSafetyEntry(int number,String type,String context,ContextTableCombination var, DataModelController controller) {
		this.context = context;
		this.type = type;
		this.variable = var;
		this.model = controller;
		this.number = number;
	}

	/**
	 * 
	 * @param equalsSeq the sequence that is used to separate the variable-value tuples
	 * @param andSeq the sequence that is used to separate the variable string from the value string<p>
	 * 					NOTE: all Whitespaces are ignored
	 * @param useBrackets whether or not the tuples should put into brackets
	 * @param parseBoolean whether or not  getPmValues should translate any boolean expressions into natural language
	 * @param useSpaces TODO
	 * @return A string containing the critical combinations separated by the equalsSeq
	 */
	public String getCriticalCombinations(String equalsSeq, String andSeq, boolean useBrackets, boolean parseBoolean, boolean useSpaces){
		String temp ="";
		
		List<String> valueCombies = variable.getPmValues(model, equalsSeq, parseBoolean, useSpaces);
		String andLiteral = andSeq.trim();
		if(useSpaces){
			andLiteral = andLiteral +' ';
		}
		if(!andLiteral.equals(",") && useSpaces){
			andLiteral = ' ' + andLiteral;
		}
		for (int i=0; i<valueCombies.size(); i++){
			if(!valueCombies.get(i).contains(model.getIgnoreLTLValue().getText())){
				if(useBrackets){
					temp = temp.concat('(' + valueCombies.get(i) + ')');
				}else{
					temp = temp.concat(valueCombies.get(i));
				}
	
				temp = temp.concat(andLiteral);
				
			}
		}
		try{
			temp = temp.substring(0, temp.length() - andLiteral.length());
		}catch(Exception e){
			
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
		
		calcConstraint();
		calcLTL();
		calcRUCA();
		calcRule();
		if(this.getDataRef() != null){
			setDataRef(model.updateRefinedRule(getDataRef(), 
									getUCALinkIDs(), 
									getCriticalCombinations("==", ",", false, false, false),
									getLTLProperty(), 
									getRefinedRule(),
									getRefinedUCA(), 
									constraint,
									getNumber(), 
									variable.getLinkedControlActionID(), 
									getType()));
		}else{
			setDataRef(model.addRefinedRule(getUCALinkIDs(),
											getCriticalCombinations("==", ",", false, false, false),
											getLTLProperty(), 
											getRefinedRule(), 
											getRefinedUCA(), constraint, getNumber(),
											variable.getLinkedControlActionID(),
											type));
		}
		
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
		return "";
	}
	
	private void calcRUCA(){	
		StringBuffer buffer = new StringBuffer();
		buffer.append("The "+variable.getLinkedControlActionName());
		buffer.append(" command is ");
		buffer.append(getContext());//$NON-NLS-1$
		if(!getType().equals(IValueCombie.TYPE_NOT_PROVIDED)){
			buffer.append(" " + getType());
		}
		String criticalString =getCriticalCombinations("is", "and", false, true, true);
		if(!criticalString.isEmpty()){
			buffer.append(" when ");
			buffer.append(criticalString);
		}
		ruca = buffer.toString();
	}

	private void calcRule(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("The " + variable.getLinkedControlActionName());
		buffer.append(" command");
		if(getType().equals(IValueCombie.TYPE_NOT_PROVIDED)){
			buffer.append(" must be provided");
		}else{
			buffer.append(" must not be provided ");
			buffer.append(getType());
		}
		String criticalString =getCriticalCombinations("=", "and", false, false, true);
		if(!criticalString.isEmpty()){
			buffer.append(" when ");
			buffer.append(criticalString);
		}
		refinedRule = buffer.toString();
	}

	private void calcConstraint(){
		StringBuffer buffer = new StringBuffer();
		buffer.append(variable.getLinkedControlActionName());
		if(getType().equals(IValueCombie.TYPE_NOT_PROVIDED)){
			buffer.append(" must be provided");
		}else{
			buffer.append(" must not be provided ");
			buffer.append(getType());
		}
		String criticalString =getCriticalCombinations("is", "and", false, true, true);
		if(!criticalString.isEmpty()){
			buffer.append(" when ");
			buffer.append(criticalString);
		}
		constraint = buffer.toString();
	}
	
	
	
	private void calcLTL(){
		final String EQUALS ="==";
		final String IMPLIES = " -> ";
		final String IMPLIES_NOT = " ->! ";
		final String START = "[] ";
		final String UNTIL = " U ";
		final char BRACKET_OPEN ='(';
		final char BRACKET_CLOSE =')';
		final String CONTROLACTION = "(controlAction" + EQUALS + getCombination().getLinkedControlActionName().replaceAll(" ", "")+")";
		StringBuffer valueBuffer = new StringBuffer(); 
		
		String values = getCriticalCombinations("==", "&&", true, false, false);
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
						valueBuffer.append(CONTROLACTION);
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
		 * G(  (critical combinations set ) -> !( !(critical combinations set ) U !(<controlAction==value>) ) )
		 */
		else if (type.equals(IValueCombie.TYPE_TOO_LATE)) {
			if(values != null && !values.isEmpty()){
				valueBuffer = new StringBuffer();
				valueBuffer.append(START);
				valueBuffer.append(BRACKET_OPEN);
					valueBuffer.append(values);
					valueBuffer.append(IMPLIES);
					valueBuffer.append("!"+BRACKET_OPEN);
						valueBuffer.append("!"+BRACKET_OPEN);
							valueBuffer.append(values);
						valueBuffer.append(BRACKET_CLOSE);
						valueBuffer.append(UNTIL);
						valueBuffer.append('!'+CONTROLACTION);
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
					valueBuffer.append(CONTROLACTION);
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
	public ContextTableCombination getCombination() {
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
	
	
	public static RefinedSafetyEntry getAnytimeEntry(int number,ContextTableCombination var,DataModelController controller){
		
		RefinedSafetyEntry entry = new RefinedSafetyEntry(number,IValueCombie.TYPE_ANYTIME, IValueCombie.CONTEXT_PROVIDED, var,controller);
		entry.setDataRef(var.getAnytimeRule());
		entry.update();
		var.setAnytimeRule(entry.getDataRef());
		
		return entry;
	}
	public static RefinedSafetyEntry getTooLateEntry(int number,ContextTableCombination var,DataModelController controller){
		RefinedSafetyEntry entry = new RefinedSafetyEntry(number,IValueCombie.TYPE_TOO_LATE, IValueCombie.CONTEXT_PROVIDED, var,controller);
		entry.setDataRef(var.getTooLateRule());
		entry.update();
		var.setTooLateRule(entry.getDataRef());
		
		return entry;
	}
	public static RefinedSafetyEntry getTooEarlyEntry(int number,ContextTableCombination var,DataModelController controller){
		RefinedSafetyEntry entry = new RefinedSafetyEntry(number,IValueCombie.TYPE_TOO_EARLY, IValueCombie.CONTEXT_PROVIDED, var,controller);
		entry.setDataRef(var.getTooEarlyRule());
		entry.update();
		var.setTooEarlyRule(entry.getDataRef());
		
		return entry;
	}
	public static RefinedSafetyEntry getNotProvidedEntry(int number,ContextTableCombination var,DataModelController controller){
		
			RefinedSafetyEntry entry = new RefinedSafetyEntry(number,IValueCombie.TYPE_NOT_PROVIDED, IValueCombie.CONTEXT_NOT_PROVIDED, var,controller);
			entry.setDataRef(var.getNotProvidedRule());
			entry.update();
			var.setNotProvidedRule(entry.getDataRef());
		
		return entry;
		
	}
}
