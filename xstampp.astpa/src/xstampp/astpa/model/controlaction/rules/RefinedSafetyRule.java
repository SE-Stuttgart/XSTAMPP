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
import xstampp.model.AbstractLTLProvider;
@XmlRootElement(name = "rule")
@XmlAccessorType(XmlAccessType.FIELD)

public class RefinedSafetyRule extends AbstractLTLProvider{

	

	
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
	public int compareTo(AbstractLTLProvider sibling) {
		int sign =(int) Math.signum(getNumber() - sibling.getNumber());
		return sign;
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
}
