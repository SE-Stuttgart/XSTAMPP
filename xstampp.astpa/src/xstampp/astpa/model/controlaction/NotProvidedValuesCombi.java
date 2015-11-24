package xstampp.astpa.model.controlaction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.sun.xml.txw2.annotation.XmlAttribute;

/**
 * This class is used to store a specific amount of process model values
 * which can lead( depending on the hazardous boolean)
 * to a list of UCAs which can be referenced by refinedSC and
 * a safetyConstraint 
 * 
 * @author Lukas Balzer
 * @since 2.0
 *
 */
public class NotProvidedValuesCombi{
	
	@XmlElementWrapper(name="processModelValueIDs")
	@XmlElement(name="value")
	private List<UUID> values;
	
	@XmlElementWrapper(name="refinedSafetyConstraint")
	@XmlElement(name="value")
	private List<UUID> refinedSC;
	
	@XmlElement(name="hazardous")
	private boolean hazardous;
	
	@XmlElement(name="safetyConstraint")
	private String constraint;
	
	@XmlElement(name="id")
	private UUID id;
	/**
	 * constructs a combination of values.legth PM value ids 
	 * 
	 * @author Lukas Balzer
	 *
	 * @param values the list of process model variable ids 
	 */
	public NotProvidedValuesCombi(List<UUID> values) {
		this.hazardous = false;
		this.values = values;
	}
	
	/**
	 *
	 * @author Lukas
	 *
	 */
	public NotProvidedValuesCombi() {
		this.hazardous = false;
		this.values = new ArrayList<>();
	}
	
	/**
	 * @return a copie of the list of process model value ids
	 */
	public List<UUID> getPMValues() {
		return new ArrayList<>(this.values);
	}

	/**
	 * @return the refinedSC
	 */
	public List<UUID> getRefinedSafetyConstraints() {
		return new ArrayList<>(this.refinedSC);
	}

	/**
	 * @return the hazardous
	 */
	public boolean isCombiHazardous() {
		return this.hazardous;
	}

	/**
	 * @return the constraint
	 */
	public String getSafetyConstraint() {
		return this.constraint;
	}

	/**
	 * @param values the values to set
	 */
	public void setValues(List<UUID> values) {
		this.values = values;
	}

	/**
	 * @param refinedSC the refinedSC to set
	 */
	public void setRefinedSC(List<UUID> refinedSC) {
		this.refinedSC = refinedSC;
	}

	/**
	 * @param hazardous the hazardous to set
	 */
	public void setHazardous(boolean hazardous) {
		this.hazardous = hazardous;
	}

	/**
	 * @param constraint the constraint to set
	 */
	public void setConstraint(String constraint) {
		this.constraint = constraint;
	}

	public UUID getCombieId() {
		if(this.id == null){
			this.id = UUID.randomUUID();
		}
		return this.id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	
	
}
