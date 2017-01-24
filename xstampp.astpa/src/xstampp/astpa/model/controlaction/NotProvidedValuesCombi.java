/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.model.controlaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import xstampp.model.IValueCombie;

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
@XmlAccessorType(XmlAccessType.NONE)
public class NotProvidedValuesCombi implements IValueCombie{
	
	@XmlElementWrapper(name="processModelValueIDs")
	@XmlElement(name="value")
	private List<UUID> values;


	@XmlElementWrapper(name="processModelVariableIDs")
	@XmlElement(name="variable")
	private List<UUID> variables;

	@XmlElementWrapper(name="relatedUnsafeCOntrolActionIDs")
	@XmlElement(name="ucaID")
	private List<UUID> relatedUCAs;
	
	@XmlElementWrapper(name="refinedSafetyConstraint")
	@XmlElement(name="value")
	private List<UUID> refinedSC;
	
	@XmlElement(name="hazardous")
	private boolean hazardous;
	
	@XmlElement(name="safetyConstraint")
	private String constraint;
	
	@XmlElement(name="id")
	private UUID id;

	@XmlElement(name="refinedRuleId")
	private UUID ruleId;
	
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
	public NotProvidedValuesCombi(ArrayList<UUID> values) {
		this.hazardous = false;
		this.archived = false;
		this.values = values;
		
	}
	
	/**
	 *
	 * @author Lukas
	 *
	 */
	public NotProvidedValuesCombi() {
		this(new ArrayList<UUID>());
	}
	
	@Override
	public List<UUID> getValueList() {
		if(this.values == null){
			return new ArrayList<>();
		}
		return this.values;
	}
	
	/* (non-Javadoc)
	 * @see xstampp.astpa.model.controlaction.IValueCombie#getPMValues()
	 */
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

	/* (non-Javadoc)
	 * @see xstampp.astpa.model.controlaction.IValueCombie#getRefinedSafetyConstraints()
	 */
	@Override
	public List<UUID> getRefinedSafetyConstraints() {
		return new ArrayList<>(this.refinedSC);
	}

	/* (non-Javadoc)
	 * @see xstampp.astpa.model.controlaction.IValueCombie#isCombiHazardous()
	 */
	@Override
	public boolean isCombiHazardous(String type) {
		switch(type){
			case TYPE_ANYTIME:
				return false;
			case TYPE_TOO_EARLY:
				return false;
			case TYPE_TOO_LATE:
				return false;
			case TYPE_NOT_PROVIDED:
				return this.hazardous;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see xstampp.astpa.model.controlaction.IValueCombie#getSafetyConstraint()
	 */
	@Override
	public String getSafetyConstraint() {
		return this.constraint;
	}

	/* (non-Javadoc)
	 * @see xstampp.astpa.model.controlaction.IValueCombie#setValues(java.util.List)
	 */
	@Override
	public void setValues(Map<UUID, UUID> valuesIdsTOvariableIDs) {
		this.values = new ArrayList<>(valuesIdsTOvariableIDs.values());
		this.variables = new ArrayList<>(valuesIdsTOvariableIDs.keySet());
	}

	/* (non-Javadoc)
	 * @see xstampp.astpa.model.controlaction.IValueCombie#setRefinedSC(java.util.List)
	 */
	@Override
	public void setRefinedSC(List<UUID> refinedSC) {
		this.refinedSC = refinedSC;
	}

	/**
	 * @param hazardous the hazardous to set
	 */
	public void setHazardous(boolean hazardous) {
		this.hazardous = hazardous;
	}

	/* (non-Javadoc)
	 * @see xstampp.astpa.model.controlaction.IValueCombie#setConstraint(java.lang.String)
	 */
	@Override
	public void setConstraint(String constraint) {
		this.constraint = constraint;
	}

	/* (non-Javadoc)
	 * @see xstampp.astpa.model.controlaction.IValueCombie#getCombieId()
	 */
	@Override
	public UUID getCombieId() {
		if(this.id == null){
			this.id = UUID.randomUUID();
		}
		return this.id;
	}

	/* (non-Javadoc)
	 * @see xstampp.astpa.model.controlaction.IValueCombie#setId(java.util.UUID)
	 */
	@Override
	public void setId(UUID id) {
		this.id = id;
	}

	@Override
	public void setArchived(boolean archive) {
		this.archived = archive;
		
	}


	@Override
	public List<UUID> getUCALinks(String type) {
		if(this.relatedUCAs == null || !type.equals(TYPE_NOT_PROVIDED)){
			return new ArrayList<>();
		}
		return this.relatedUCAs;
	}

	@Override
	public void setUCALinks(List<UUID> relatedUCAs, String type) {
		if(type.equals(TYPE_NOT_PROVIDED)){
			this.relatedUCAs = relatedUCAs;
		}
	}

	/**
	 * @return the ruleId
	 */
	public UUID getRefinedRuleIdId() {
		return this.ruleId;
	}

	/**
	 * @param ruleId the ruleId to set
	 */
	public void setRefinedRuleId(UUID ruleId) {
		this.ruleId = ruleId;
	}
	/**
	 * @param valueNames the valueNames to set
	 */
	public void setValueNames(List<String> valueNames) {
		this.valueNames = valueNames;
	}
}
