package xstpa;

import java.util.UUID;

public class ControllerWithPMEntry {

	  private String controller;

	  private String pm;

	  private String pmv;

	  private String pmvv;
	  
	  private String comments;
	  
	  private String controlAction;
	  
	  private Boolean safety_critical;
	  
	  private UUID id;
	  
	  public ControllerWithPMEntry() {
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
	    return pmv;
	  }

	  /**
	   * @param pmv
	   *            The PMV to set.
	   */
	  public void setPMV(String pmv) {
	    this.pmv = pmv;
	  }

	  /**
	   * @return Returns the Value of the PMVs.
	   */
	  public String getValues() {
	    return pmvv;
	  }

	  /**
	   * @param pmvv
	   *            The Value of the PMV to set.
	   */
	  public void setValues(String pmvv) {
	    this.pmvv = pmvv;
	  }
	  
	  /**
	   * @return Returns the Value of the comments.
	   */
	public String getComments() {
		return comments;
	}
	
	  /**
	   * @param pmvv
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
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	}

