package xstampp.model;

import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
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
public abstract class AbstractLTLProvider implements Comparable<AbstractLTLProvider>{
	
	@XmlElement(name="ruleID")
	protected UUID id;

	@XmlElement(name="ruleNR")
	protected int number;

	@XmlElement(name="criticalCombies")
	protected String combies;
	
	@XmlElement(name="RefinedSafetyRule")
	protected String rule;

	@XmlElement(name="refinedUCA")
	protected String rUCA;

	@XmlElement(name="refinedSC")
	protected String rSCt;

	@XmlElement(name="ltlProp")
	protected String ltl;
	
	@XmlElement(name="type")
	protected String type;

	@XmlElement(name="controlAction")
	protected String controlAction;
	
	@XmlElement(name="links")
	protected String links;
	
	@XmlElementWrapper(name="relatedUCAIDs")
	@XmlElement(name="ucaID")
	protected List<UUID> relatedUCAs;

	@XmlElement(name="relatedCaID")
	protected UUID caID;
	
	/**
	 * @return the ltlProperty
	 */
	public String getLtlProperty() {
		return this.ltl;
	}	
	/**
	 * @return the refinedUCA
	 */
	public String getRefinedUCA() {
		return this.rUCA;
	}	
	/**
	 * @return the rule
	 */
	public String getSafetyRule() {
		return this.rule;
	}
	
	public int getNumber() {
		return this.number;
	}	
	/**
	 * @return the controlActionID
	 */
	public UUID getRelatedControlActionID() {
		return this.caID;
	}
	
	public List<UUID> getUCALinks(){
		return this.relatedUCAs;
	}	
	/**
	 * @return the refinedSafetyConstraint
	 */
	public String getRefinedSafetyConstraint() {
		return this.rSCt;
	}	
	/**
	 * @return the id
	 */
	public UUID getRuleId() {
		return this.id;
	}	

	public String getType() {
		return this.type;
	}

	public String getLinks() {
		return this.links;
	}}
