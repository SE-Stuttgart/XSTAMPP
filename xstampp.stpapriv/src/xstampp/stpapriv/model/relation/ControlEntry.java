package xstampp.stpapriv.model.relation;

import java.util.UUID;



public class ControlEntry implements Comparable<ControlEntry>{
	
	private String controller;
	private String controlAction;
	private String unsecureControlAction;
	private Boolean privacy_critical;
	private Boolean security_critical;
	private Boolean safety_critical;
	private String comments;
	
	private UUID id;
	private UUID idCA;
	private int number;


	
	public ControlEntry() {
		comments = "";
		safety_critical = true;
		security_critical = true;
		privacy_critical = true;
		
	}


	
	public String getController() {
		return controller;
	}
	public void setController(String controller) {
		this.controller = controller;
	}
	public String getControlAction() {
		return controlAction;
	}
	public void setControlAction(String controlAction) {
		this.controlAction = controlAction;
	}
	public Boolean getSafetyCritical() {
		return safety_critical;
	}
	public void setSafetyCritical(Boolean safety_critical) {
		this.safety_critical = safety_critical;
	}
	public Boolean getSecurityCritical() {
		return security_critical;
	}
	public void setSecurityCritical(Boolean security_critical) {
		this.security_critical = security_critical;
	}
	
	public Boolean getPrivacyCritical() {
		return privacy_critical;
	}

	public void setPrivacyCritical(Boolean privacy_critical) {
		this.privacy_critical = privacy_critical;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}


	

	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}


	@Override
	public int compareTo(ControlEntry sibling) {
		try{
			int returnValue = 0;
			if(!(getControlAction() == null || sibling.getControlAction() == null)){
				returnValue = getControlAction().compareTo(sibling.getControlAction());
			}else if(getControlAction() != null){
				returnValue = 1;
			}else if(sibling.getControlAction() != null){
				returnValue = -1;
			}
			
			if(returnValue == 0 && !(getUnsecureControlAction() == null || sibling.getUnsecureControlAction() == null)){
				returnValue = getUnsecureControlAction().compareTo(sibling.getUnsecureControlAction());
			}else if(returnValue == 0 && getUnsecureControlAction() != null){
				returnValue = 1;
			}else if(returnValue == 0 && sibling.getUnsecureControlAction() != null){
				returnValue = -1;
			}
			
			
			return returnValue;
		}catch(NullPointerException e){
			return 0;
		}
	}


	public String getUnsecureControlAction() {
		return unsecureControlAction;
	}


	public void setUnsecureControlAction(String unsecureControlAction) {
		this.unsecureControlAction = unsecureControlAction;
	}

	public UUID getIdCA() {
		return idCA;
	}

	public void setIdCA(UUID idCA) {
		this.idCA = idCA;
	}
}
