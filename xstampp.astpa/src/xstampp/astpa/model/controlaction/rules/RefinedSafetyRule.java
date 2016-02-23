package xstampp.astpa.model.controlaction.rules;

import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import xstampp.astpa.model.controlaction.IValueCombie;
import xstampp.model.ILTLProvider;
@XmlRootElement(name = "rule")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "id",
					   "number",
					   "combies",
					   "rule",
					   "rUCA", 
					   "rSCt", 
					   "ltl",
					   "type",
					   "controlAction",
					   "links", 
					   "relatedUCAs", 
					   "caID"})
public class RefinedSafetyRule implements ILTLProvider{

	
	@XmlElement(name="ruleID")
	private UUID id;

	@XmlElement(name="ruleNR")
	private int number;

	@XmlElement(name="criticalCombies")
	private String combies;
	
	@XmlElement(name="RefinedSafetyRule")
	private String rule;

	@XmlElement(name="refinedUCA")
	private String rUCA;

	@XmlElement(name="refinedSC")
	private String rSCt;

	@XmlElement(name="ltlProp")
	private String ltl;
	
	@XmlElement(name="type")
	private String type;

	@XmlElement(name="controlAction")
	private String controlAction;
	
	@XmlElement(name="links")
	private String links;
	
	@XmlElementWrapper(name="relatedUCAIDs")
	@XmlElement(name="ucaID")
	private List<UUID> relatedUCAs;

	@XmlElement(name="relatedCaID")
	private UUID caID;
	
	/**
	 * 
	 * @param ucaLinks
	 * @param caId
	 * @param ltlExp
	 * @param rule
	 * @param ruca
	 * @param constraint
	 * @param nr
	 * @param combies
	 * @param type the Type of the context the rule should be generated for one of the <code>TYPE</code> constants
	 * 				Defined in IValueCombie
	 * 
	 * @see IValueCombie
	 */
	public RefinedSafetyRule(List<UUID> ucaLinks,UUID caId,String ltlExp,String rule,String ruca,String constraint,String type, int nr, String combies) {
		id = UUID.randomUUID();
		this.number = nr;
		this.combies = combies;
		this.rule = rule;
		this.rSCt = constraint;
		this.rUCA = ruca;
		this.ltl = ltlExp;
		this.relatedUCAs = ucaLinks;
		this.caID = caId;
		this.type = type;
		
	}

	public RefinedSafetyRule() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean setUCALinks(List<UUID> ids){
		if(this.relatedUCAs != null && this.relatedUCAs.equals(ids)){
			return false;
		}
		this.relatedUCAs = ids;
		return true;
	}
	
	public void removeUCALink(UUID id) {
		this.relatedUCAs.remove(id);
		
	}
	
	public String getType() {
		return this.type;
	}
	
	@Override
	public List<UUID> getUCALinks(){
		return this.relatedUCAs;
	}

	@Override
	public String getSafetyRule() {
		return this.rule;
	}

	/**
	 * @param rule the rule to set
	 * @return 
	 */
	public boolean setSafetyRule(String rule) {
		if(this.rule != null && this.rule.equals(rule)){
			return false;
		}
		this.rule = rule;
		return true;
	}


	@Override
	public String getRefinedUCA() {
		return this.rUCA;
	}

	/**
	 * @param refinedUCA the refinedUCA to set
	 * @return 
	 */
	public boolean setRefinedUCA(String refinedUCA) {
		if(this.rUCA != null && this.rUCA.equals(refinedUCA)){
			return false;
		}
		this.rUCA = refinedUCA;
		return true;
	}


	@Override
	public String getRefinedSafetyConstraint() {
		return this.rSCt;
	}

	/**
	 * @param refinedSafetyConstraint the refinedSafetyConstraint to set
	 * @return 
	 */
	public boolean setRefinedSafetyConstraint(String refinedSafetyConstraint) {
		if(this.rSCt != null && this.rSCt.equals(refinedSafetyConstraint)){
			return false;
		}
		this.rSCt = refinedSafetyConstraint;
		return true;
	}

	@Override
	public UUID getRuleId() {
		return this.id;
	}

	@Override
	public String getLtlProperty() {
		return this.ltl;
	}

	/**
	 * @param ltlProperty the ltlProperty to set
	 */
	public boolean setLtlProperty(String ltlProperty) {
		if(this.ltl != null && this.ltl.equals(ltlProperty)){
			return false;
		}
		this.ltl = ltlProperty;
		return true;
	}

	@Override
	public int getNumber() {
		return this.number;
	}
	
	@Override
	public int compareTo(ILTLProvider sibling) {
		int sign =(int) Math.signum(getNumber() - sibling.getNumber());
		return sign;
	}

	@Override
	public UUID getRelatedControlActionID() {
		return this.caID;
	}

	/**
	 * @return the criticalCombies
	 */
	public String getCriticalCombies() {
		return this.combies;
	}
	
	/**
	 * @param controlActionID the controlActionID to set
	 * @return 
	 */
	public boolean setRelatedControlActionID(UUID controlActionID) {
		if(this.caID != null && this.caID.equals(controlActionID)){
			return false;
		}
		this.caID = controlActionID;
		return true;
	}

	/**
	 * @param number the number to set
	 * @return 
	 */
	public boolean setNumber(int number) {
		if(this.number == number){
			return false;
		}
		this.number = number;
		return true;
	}

	/**
	 * @param type the Type of the context the rule should be generated for one of the <code>TYPE</code> constants
	 * 				Defined in IValueCombie
	 * @return 
	 * @see IValueCombie
	 */
	public boolean setType(String type) {
		if(this.type != null && this.type.equals(type)){
			return false;
		}
		this.type = type;
		return true;
	}

	/**
	 * @param caID the caID to set
	 */
	public boolean setCaID(UUID caID) {
		if(this.caID != null && this.caID.equals(caID)){
			return false;
		}
		this.caID = caID;
		return true;
	}


	/**
	 * @param criticalCombies the criticalCombies to set
	 * @return 
	 */
	public boolean setCriticalCombies(String criticalCombies) {
		if(this.combies != null && this.combies.equals(criticalCombies)){
			return false;
		}
		this.combies = criticalCombies;
		return true;
	}
	/**
	 * @param controlAction the controlAction to set
	 * @return 
	 */
	public boolean setControlAction(String controlAction) {
		if(this.controlAction != null && this.controlAction.equals(controlAction)){
			return false;
		}
		this.controlAction = controlAction;
		return true;
	}

	/**
	 * @param links the links to set
	 * @return 
	 */
	public boolean setLinks(String links) {
		if(this.links != null && this.links.equals(links)){
			return false;
		}
		this.links = links;
		return true;
	}
	public String getLinks() {
		return this.links;
	}
}
