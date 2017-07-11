package xstampp.stpapriv.model.results;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import xstampp.stpapriv.model.controlaction.ControlAction;


public class ConstraintResult implements Comparable<ConstraintResult>{

	@XmlElement(name="secstep")
	private String stpasecstep;

	private UUID Id;
	
	@XmlElement(name="secConstraint")
	private String secConstraint;
	
	@XmlElement(name="secid")
	private String secId;
	
	@XmlElement(name="corrSecConstraint")
	private String relatedSecId;
	
	@XmlTransient
	public String getScId() {
		return secId;
	}

	public void setScId(String scId) {
		this.secId = scId;
	}
	
	@XmlElement(name="safRelated")
	private Boolean isSafe;
	
	@XmlElement(name="secRelated")
	private Boolean isSecure;
	
	@XmlElement(name="privRelated")
	private Boolean isPrivate;
	
	@XmlTransient
	private ControlAction temp;
	
	@XmlTransient
	private List<String> relatedConstraints = new ArrayList<>();
	
	public List<String> getRelatedConstraints() {
		return relatedConstraints;
	}
	
	@XmlTransient
	public void setRelatedConstraints(List<String> relatedConstraints) {
		this.relatedConstraints = relatedConstraints;
	}
	
	public void addRelatedConstraints(String constraint){
		this.relatedConstraints.add(constraint);
	}
	
	public void addAllRelatedConstraints(List<String> constraint){
		this.relatedConstraints.addAll(constraint);
	}

	@XmlTransient
	public ControlAction getTemp() {
		return temp;
	}

	public void setTemp(ControlAction temp) {
		this.temp = temp;
	}

	@XmlTransient
	public String getStpastep() {
		return stpasecstep;
	}

	public void setStpastep(String stpastep) {
		this.stpasecstep = stpastep;
	}

	@XmlTransient
	public UUID getId() {
		return Id;
	}

	public void setId(UUID id) {
		Id = id;
	}

	@XmlTransient
	public String getSecurityConstraint() {
		return secConstraint;
	}

	public void setSecurityConstraint(String securityConstraint) {
		this.secConstraint = securityConstraint;
	}

	@XmlTransient
	public Boolean isSafe() {
		return isSafe;
	}

	public void setSafe(Boolean isSafe) {
		this.isSafe = isSafe;
	}

	@XmlTransient
	public Boolean isSecure() {
		return isSecure;
	}

	public void setSecure(Boolean isSecure) {
		this.isSecure = isSecure;
	}

	@XmlTransient
	public Boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	
	@Override
	public int compareTo(ConstraintResult o) {
		
		try{
			int returnValue=0;
			if(this.getIdCompare().length>=0||o.getIdCompare().length>=0){
				returnValue=this.getIdCompare()[0]-o.getIdCompare()[0];
				if(returnValue!=0){
					return returnValue;
				}
			}
			return this.getIdCompare()[1]-o.getIdCompare()[1];
		}catch(NullPointerException e){
			
		}
		
		return 0;
	}
	
	
public int[] getIdCompare(){
	String[] splitone= this.secId.split("SC");

	String[] splittwo= splitone[1].split("\\.");
	int[] ids= new int[2];
	
	ids[0]=Integer.parseInt(splittwo[0]);
	ids[1]=Integer.parseInt(splittwo[1]);
	return ids;
}

@XmlTransient
public String getRelatedId() {
	return relatedSecId;
}

public void setRelatedId(String relatedId) {
	this.relatedSecId = relatedId;
}
}
