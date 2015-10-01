package xstampp.astpa.model.controlaction;

import java.util.ArrayList;
import java.util.List;
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
public class ProvidedValuesCombi {
	
	@XmlElementWrapper(name="processModelValueIDs")
	@XmlElement(name="value")
	private List<UUID> values;
	
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
		this.values = values;
	}

	 /** 
	 * @author Lukas Balzer
	 *
	 */
	public ProvidedValuesCombi() {
		this.hazardousAnyTime = false;
		this.hazardousToEarly = false;
		this.hazardousToLate = false;
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
	public List<UUID> getRefinedSafetyConstraint() {
		return new ArrayList<>(this.refinedSC);
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
	 * @param constraint the constraint to set
	 */
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
}
