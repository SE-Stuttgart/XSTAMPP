package xstampp.astpa.model.controlaction.rules;

import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import xstampp.model.ILTLProvider;
@XmlRootElement(name = "rule")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "id", "number","combies","rule","rUCA", "rSCt", "ltl",
		"type", "relatedUCAs", "caID"})
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
	
	
	@XmlElementWrapper(name="relatedUCAIDs")
	@XmlElement(name="ucaID")
	private List<UUID> relatedUCAs;

	@XmlElement(name="relatedCaID")
	private UUID caID;
	
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
	
	public void setUCALinks(List<UUID> ids){
			this.relatedUCAs = ids;
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
	 */
	public void setSafetyRule(String rule) {
		this.rule = rule;
	}


	@Override
	public String getRefinedUCA() {
		return this.rUCA;
	}

	/**
	 * @param refinedUCA the refinedUCA to set
	 */
	public void setRefinedUCA(String refinedUCA) {
		this.rUCA = refinedUCA;
	}


	@Override
	public String getRefinedSafetyConstraint() {
		return this.rSCt;
	}

	/**
	 * @param refinedSafetyConstraint the refinedSafetyConstraint to set
	 */
	public void setRefinedSafetyConstraint(String refinedSafetyConstraint) {
		this.rSCt = refinedSafetyConstraint;
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
	public void setLtlProperty(String ltlProperty) {
		this.ltl = ltlProperty;
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
	 */
	public void setRelatedControlActionID(UUID controlActionID) {
		this.caID = controlActionID;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param caID the caID to set
	 */
	public void setCaID(UUID caID) {
		this.caID = caID;
	}


	/**
	 * @param criticalCombies the criticalCombies to set
	 */
	public void setCriticalCombies(String criticalCombies) {
		this.combies = criticalCombies;
	}
}
