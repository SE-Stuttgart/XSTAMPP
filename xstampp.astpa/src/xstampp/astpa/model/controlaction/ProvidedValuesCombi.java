package xstampp.astpa.model.controlaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * This class is used to store a specific amount of process model values
 * which can lead( depending on the hazardous... booleans)
 * to a list of UCAs which can be referenced by refinedSC and
 * a safetyConstraint 
 * 
 * @author Lukas Balzer
 * @since 2.0
 *
 */
public class ProvidedValuesCombi implements IValueCombie{
	
	@XmlElementWrapper(name="processModelValueIDs")
	@XmlElement(name="value")
	private List<UUID> values;
	
	@XmlElementWrapper(name="processModelVariableIDs")
	@XmlElement(name="variable")
	private List<UUID> variables;
	

	@XmlElementWrapper(name="relatedUCAsAnytime")
	@XmlElement(name="ucaID")
	private List<UUID> relatedUCAsAnytime;
	
	@XmlElementWrapper(name="relatedUCAsTooLate")
	@XmlElement(name="ucaID")
	private List<UUID> relatedUCAsTooLate;

	@XmlElementWrapper(name="relatedUCAsTooEarly")
	@XmlElement(name="ucaID")
	private List<UUID> relatedUCAsTooEarly;
	
	@XmlElementWrapper(name="refinedSafetyConstraint")
	@XmlElement(name="value")
	private List<UUID> refinedSC;

	@XmlElement(name="hazardousAnyTime")
	private boolean hazardousAnyTime;

	@XmlElement(name="hazardousToLate")
	private boolean hazardousToLate;
	
	@XmlElement(name="hazardousifProvidedToEarly")
	private boolean hazardousToEarly;
	
	@XmlElement(name="safetyConstraint")
	private String constraint;

	@XmlElement(name="combieId")
	private UUID id;
	
	@XmlElement(name="anytimeRuleId")
	private UUID anytimeRule;
	
	@XmlElement(name="tooEarlyRuleId")
	private UUID tooEarlyRule;
	
	@XmlElement(name="tooLateRuleId")
	private UUID tooLateRule;

	@XmlElement(name="archived")
	private boolean archived;

	@XmlElementWrapper(name="valueNames")
	@XmlElement(name="name")
	private List<String> valueNames;
	/**
	 * constructs a combination of values.legth PM value ids 
	 * 
	 * @author Lukas Balzer
	 *
	 * @param values the list of process model variable ids 
	 */
	public ProvidedValuesCombi(List<UUID> values) {
		this.hazardousAnyTime = false;
		this.hazardousToEarly = false;
		this.hazardousToLate = false;
		this.archived = false;
		this.values = values;
	}

	 /** 
	 * @author Lukas Balzer
	 *
	 */
	public ProvidedValuesCombi() {
		this(new ArrayList<UUID>());
	}
	
	@Override
	public List<UUID> getValueList() {
		if(this.values == null){
			return new ArrayList<>();
		}
		return this.values;
	}
	
	@Override
	public Map<UUID, UUID> getPMValues() {
		if(this.variables == null){
			return null;
		}
		HashMap<UUID, UUID> valueMap = new HashMap<>();
		for(int i=0;i < this.values.size();i++){
			valueMap.put(this.variables.get(i), this.values.get(i));
		}
		return valueMap;
	}

	/**
	 * @return the refinedSC
	 */
	@Override
	public List<UUID> getRefinedSafetyConstraints() {
		return new ArrayList<>(this.refinedSC);
	}

	/**
	 * @return the constraint
	 */
	@Override
	public String getSafetyConstraint() {
		return this.constraint;
	}

	@Override
	public void setValues(Map<UUID, UUID> valuesIdsTOvariableIDs) {
		this.values = new ArrayList<>();
		this.variables = new ArrayList<>();
		for(Entry<UUID, UUID> entry : valuesIdsTOvariableIDs.entrySet()){
			this.values.add(entry.getValue());
			this.variables.add(entry.getKey());
		}
	}


	@Override
	public void setRefinedSC(List<UUID> refinedSC) {
		this.refinedSC = refinedSC;
	}


	@Override
	public void setConstraint(String constraint) {
		this.constraint = constraint;
	}

	/**
	 * @return the hazardousAnyTime
	 */
	public boolean isHazardousWhenAnyTime() {
		return this.hazardousAnyTime;
	}

	/**
	 * @return the hazardousToLate
	 */
	public boolean isHazardousWhenToLate() {
		return this.hazardousToLate;
	}

	/**
	 * @return the hazardousToEarly
	 */
	public boolean isHazardousWhenToEarly() {
		return this.hazardousToEarly;
	}

	/**
	 * @param hazardousAnyTime the hazardousAnyTime to set
	 */
	public void setHazardousAnyTime(boolean hazardousAnyTime) {
		this.hazardousAnyTime = hazardousAnyTime;
	}

	/**
	 * @param hazardousToLate the hazardousToLate to set
	 */
	public void setHazardousToLate(boolean hazardousToLate) {
		this.hazardousToLate = hazardousToLate;
	}

	/**
	 * @param hazardousToEarly the hazardousToEarly to set
	 */
	public void setHazardousToEarly(boolean hazardousToEarly) {
		this.hazardousToEarly = hazardousToEarly;
	}
	
	@Override
	public UUID getCombieId() {
		if(this.id == null){
			this.id = UUID.randomUUID();
		}
		return this.id;
	}

	@Override
	public void setId(UUID id) {
		this.id = id;
	}


	@Override
	public boolean isCombiHazardous(String type) {
		switch(type){
			case TYPE_ANYTIME:
				return this.hazardousAnyTime;
			case TYPE_TOO_EARLY:
				return this.hazardousToEarly;
			case TYPE_TOO_LATE:
				return this.hazardousToLate;
		}
		return false;
	}

	@Override
	public void setArchived(boolean archive) {
		this.archived = archive;
		
	}
	

	@Override
	public List<UUID> getUCALinks(String type) {
		List<UUID> temp = null;
		switch(type){
			case TYPE_ANYTIME:
				temp = this.relatedUCAsAnytime;
				break;
			case TYPE_TOO_EARLY:
				temp = this.relatedUCAsTooEarly;
				break;
			case TYPE_TOO_LATE:
				temp = this.relatedUCAsTooLate;
				break;
		}
		if(temp == null){
			return new ArrayList<>();
		}
		return temp;
	}

	/**
	 * @param relatedUCAs the relatedUCAs to set
	 */
	@Override
	public void setUCALinks(List<UUID> relatedUCAs, String type) {
		switch(type){
			case TYPE_ANYTIME:
				this.relatedUCAsAnytime = relatedUCAs;
				break;
			case TYPE_TOO_EARLY:
				this.relatedUCAsTooEarly = relatedUCAs;
				break;
			case TYPE_TOO_LATE:
				this.relatedUCAsTooLate = relatedUCAs;
				break;
		}
	}

	/**
	 * @return the anytimeRuleId
	 */
	public UUID getAnytimeRuleId() {
		return this.anytimeRule;
	}

	/**
	 * @param anytimeRuleId the anytimeRuleId to set
	 */
	public void setAnytimeRuleId(UUID anytimeRuleId) {
		this.anytimeRule = anytimeRuleId;
	}

	/**
	 * @return the tooEarlyRuleId
	 */
	public UUID getTooEarlyRuleId() {
		return this.tooEarlyRule;
	}

	/**
	 * @param tooEarlyRuleId the tooEarlyRuleId to set
	 */
	public void setTooEarlyRuleId(UUID tooEarlyRuleId) {
		this.tooEarlyRule = tooEarlyRuleId;
	}

	/**
	 * @return the tooLateRuleId
	 */
	public UUID getTooLateRuleId() {
		return this.tooLateRule;
	}

	/**
	 * @param tooLateRuleId the tooLateRuleId to set
	 */
	public void setTooLateRuleId(UUID tooLateRuleId) {
		this.tooLateRule = tooLateRuleId;
	}


	/**
	 * @param valueNames the valueNames to set
	 */
	public void setValueNames(List<String> valueNames) {
		this.valueNames = valueNames;
	}
}
