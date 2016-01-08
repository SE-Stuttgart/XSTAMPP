package xstampp.astpa.model.controlaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

	@XmlElement(name="archived")
	private boolean archived;
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
		this.values = new ArrayList<>(valuesIdsTOvariableIDs.values());
		this.variables = new ArrayList<>(valuesIdsTOvariableIDs.keySet());
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
}
