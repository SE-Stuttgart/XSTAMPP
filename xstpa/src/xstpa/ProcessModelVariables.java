package xstpa;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;


@XmlRootElement(name = "contexttablecombination")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "values", "valueIds", "pmVariables","pmValues", "name", "linkedControlActionName", "refinedSafetyRequirements", "context",
		"number", "hazardous", "hLate", "hEarly", "hAnytime", "conflict", "isInRSRTable", "relatedHazards", "uca", "id" })
public class ProcessModelVariables {

	@XmlElementWrapper(name = "values")
	@XmlElement(name = "value")
	private List<String> values = new ArrayList<String>();
	
	@XmlElementWrapper(name = "valueIds")
	@XmlElement(name = "Id")
	private List<UUID> valueIds = new ArrayList<UUID>();
	
	@XmlElementWrapper(name = "pmVariables")
	@XmlElement(name = "pmVariable")
	private List<String> pmVariables = new ArrayList<String>();
	
	@XmlElementWrapper(name = "pmValues")
	@XmlElement(name = "pmValue")
	private List<String> pmValues = new ArrayList<String>();
	
	private String name;
	

	private String linkedControlActionName = "";
	

	private String refinedSafetyRequirements ="";
	
	private String context;
	
	private int number;
	

	private Boolean hazardous = false;
	
	
	private Boolean hLate = false;
	
	
	private Boolean hEarly = false;
	
	
	private Boolean hAnytime = false;
	
	private Boolean conflict = false;
	
	private Boolean isInRSRTable = false;
	
	private RelatedHazards relatedHazards = new RelatedHazards(View.allHazards);
	
	private UnsafeControlAction uca= new UnsafeControlAction(this);
	
	private UUID id;
	
	public ProcessModelVariables (List<String> pmVariables,String linkedControlActionName ) {
		this.linkedControlActionName = linkedControlActionName;
		this.pmVariables = pmVariables;
	}
	
	public ProcessModelVariables() {
		// Empty Constructor for JAXB
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getValues() {
		return values;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}
	public void addValue (String value) {
		values.add(value);
		
	}
	public void removeValue (int index) {
		values.remove(index);
	}
	public int getSizeOfValues() {
		return values.size();
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public Boolean getHazardous() {
		return hazardous;
	}
	public void setHazardous(Boolean hazardous) {
		this.hazardous = hazardous;
	}
	public Boolean getHLate() {
		return hLate;
	}
	public void setHLate(Boolean hLate) {
		this.hLate = hLate;
	}
	public Boolean getHEarly() {
		return hEarly;
	}
	public void setHEarly(Boolean hEarly) {
		this.hEarly = hEarly;
	}
	public Boolean getHAnytime() {
		return hAnytime;
	}
	public void setHAnytime(Boolean hAnytime) {
		this.hAnytime = hAnytime;
	}
	public Boolean getConflict() {
		return conflict;
	}
	public void setConflict(Boolean conflict) {
		this.conflict = conflict;
	}
	public String getLinkedControlActionName() {
		return linkedControlActionName;
	}
	public void setLinkedControlActionName(String linkedControlActionName) {
		this.linkedControlActionName = linkedControlActionName;
	}
	public List<String> getPmVariables() {
		return pmVariables;
	}
	public void setPmVariables(List<String> pmVariables) {
		this.pmVariables = pmVariables;
	}
	public List<String> getPmValues() {
		return pmValues;
	}
	public void setPmValues(List<String> pmValues) {
		this.pmValues = pmValues;
	}
	public String getRefinedSafetyRequirements() {
		return refinedSafetyRequirements;
	}
	public void setRefinedSafetyRequirements(String refinedSafetyRequirements) {
		this.refinedSafetyRequirements = refinedSafetyRequirements;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public RelatedHazards getRelatedHazards() {
		return relatedHazards;
	}
	public void setRelatedHazards(RelatedHazards relatedHazards) {
		this.relatedHazards = relatedHazards;
	}

	public UnsafeControlAction getUca() {
		return uca;
	}

	public void setUca(UnsafeControlAction uca) {
		this.uca = uca;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Boolean getIsInRSRTable() {
		return isInRSRTable;
	}

	public void setIsInRSRTable(Boolean isInRSRTable) {
		this.isInRSRTable = isInRSRTable;
	}

	public List<UUID> getValueIds() {
		return valueIds;
	}

	public void setValueIds(List<UUID> valueIds) {
		this.valueIds = valueIds;
	}
	public void addValueId (UUID valueId) {
		valueIds.add(valueId);
		
	}


}
