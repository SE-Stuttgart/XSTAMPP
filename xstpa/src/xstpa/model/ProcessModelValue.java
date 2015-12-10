package xstpa.model;

import java.util.UUID;

/**
 * this class contains a complete representation of a process model value
 * which consists out of a:<br>
 * <ul>
 * <li>Controller
 * <li>Process Model
 * <li>Process Variable
 * <li>Control Action
 * </ul>
 * @author Janik Sowodnic, LukasBalzer
 *
 */
public class ProcessModelValue {

	  private String controller;

	  private String pm;

	  private String pm_variable;

	  private String pm_value;
	  
	  private String comments;
	  
	  private String controlAction;
	  
	  private Boolean safety_critical;
	  
	  private UUID valueid;
	  
	  private UUID variableID;
	  
	  public ProcessModelValue() {
		  comments = "";
		  
	  }

	  /**
	   * @return Returns the controller.
	   */
	  public String getController() {
	    return controller;
	  }

	  /**
	   * @param controller
	   *            The controller to set.
	   */
	  public void setController(String controller) {
	    this.controller = controller;
	  }

	  /**
	   * @return Returns the Process Model.
	   */
	  public String getPM() {
	    return pm;
	  }

	  /**
	   * @param pm
	   *            The process model to set.
	   */
	  public void setPM(String processmodel) {
	    this.pm = processmodel;
	  }

	  /**
	   * @return Returns the Process Model Variables.
	   */
	  public String getPMV() {
	    return pm_variable;
	  }

	  /**
	   * @param pmv
	   *            The PMV to set.
	   */
	  public void setPMV(String pmv) {
	    this.pm_variable = pmv;
	  }

	  /**
	   * @return Returns the Value of the PMVs.
	   */
	  public String getValueText() {
	    return pm_value;
	  }

	  /**
	   * @param pmvv
	   *            The Value of the PMV to set.
	   */
	  public void setValueText(String pmvv) {
	    this.pm_value = pmvv;
	  }
	  
	  /**
	   * @return Returns the Value of the comments.
	   */
	public String getComments() {
		return comments;
	}
	
	  /**
	   * @param pm_value
	   *            The Value of the comments to set.
	   */
	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getControlAction() {
		return controlAction;
	}

	public void setControlAction(String controlAction) {
		this.controlAction = controlAction;
	}

	public Boolean getSafety_critical() {
		return safety_critical;
	}

	public void setSafety_critical(Boolean safety_critical) {
		this.safety_critical = safety_critical;
	}

	public UUID getId() {
		return valueid;
	}

	public void setId(UUID id) {
		this.valueid = id;
	}

	/**
	 * @return the variableID
	 */
	public UUID getVariableID() {
		return variableID;
	}

	/**
	 * @param variableID the variableID to set
	 */
	public void setVariableID(UUID variableID) {
		this.variableID = variableID;
	}
	}

