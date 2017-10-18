/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
 * Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian Sieber, Fabian Toth, Patrick Wickenhäuser,
 * Aliaksei Babkovich, Aleksander Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.model.sds;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import xstampp.astpa.model.ATableModel;
import xstampp.astpa.model.NumberedArrayList;
import xstampp.astpa.model.causalfactor.ICausalController;
import xstampp.astpa.model.controlaction.IControlActionController;
import xstampp.astpa.model.hazacc.IHazAccController;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.model.service.UndoDesignReqChangeCallback;
import xstampp.model.ObserverValue;

/**
 * Class for managing safety constraints, system goals and design requirements.
 * 
 * @author Jaqueline Patzek, Fabian Toth
 * @since 2.0
 * 
 */
public class SDSController extends Observable implements ISDSController {

  @XmlElementWrapper(name = "safetyConstraints")
  @XmlElement(name = "safetyConstraint")
  private NumberedArrayList<SafetyConstraint> safetyConstraints;

  @XmlElementWrapper(name = "systemGoals")
  @XmlElement(name = "systemGoal")
  private NumberedArrayList<SystemGoal> systemGoals;

  @XmlElementWrapper(name = "designRequirements")
  @XmlElement(name = "designRequirement")
  private NumberedArrayList<DesignRequirement> designRequirements;

  @XmlElementWrapper(name = "designRequirementsStep1")
  @XmlElement(name = "designRequirement")
  private NumberedArrayList<DesignRequirementStep1> designRequirementsStep1;

  @XmlElementWrapper(name = "designRequirementsStep2")
  @XmlElement(name = "designRequirement")
  private NumberedArrayList<DesignRequirementStep2> designRequirementsStep2;

  /**
   * 
   * Constructor of the SDSCotnroller
   * 
   * @author Fabian Toth
   * 
   */
  public SDSController() {
    this.safetyConstraints = new NumberedArrayList<>();
    this.systemGoals = new NumberedArrayList<>();
    this.designRequirements = new NumberedArrayList<>();
    this.designRequirementsStep1 = new NumberedArrayList<>();
    this.designRequirementsStep2 = new NumberedArrayList<>();
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.sds.ISDSController#addSafetyConstraint(java.lang.String,
   * java.lang.String, java.util.UUID)
   */
  @Override
  public UUID addSafetyConstraint(String title, String description, UUID createdBy) {
    SafetyConstraint safetyConstraint = new SafetyConstraint(title, description);
    safetyConstraint.setCreatedBy(createdBy);
    this.getSafetyConstraints().add(safetyConstraint);
    return safetyConstraint.getId();
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.sds.ISDSController#addSafetyConstraint(xstampp.astpa.model.interfaces.
   * ITableModel)
   */
  @Override
  public UUID addSafetyConstraint(ITableModel model) {
    SafetyConstraint safetyConstraint = new SafetyConstraint(model, -1);
    this.getSafetyConstraints().add(safetyConstraint);
    return safetyConstraint.getId();
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.sds.ISDSController#getAllSafetyConstraints()
   */
  @Override
  public List<ITableModel> getAllSafetyConstraints() {
    List<ITableModel> result = new ArrayList<>();
    for (SafetyConstraint safetyConstraint : this.getSafetyConstraints()) {
      result.add(safetyConstraint);
    }
    return result;
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.sds.ISDSController#moveEntry(boolean, java.util.UUID,
   * xstampp.model.ObserverValue)
   */
  @Override
  public boolean moveEntry(boolean moveUp, UUID id, ObserverValue value) {
    if (value.equals(ObserverValue.SYSTEM_GOAL)) {
      return ATableModel.move(moveUp, id, getSystemGoals());
    } else if (value.equals(ObserverValue.SAFETY_CONSTRAINT)) {
      return ATableModel.move(moveUp, id, getSafetyConstraints());
    } else if (value.equals(ObserverValue.DESIGN_REQUIREMENT)) {
      return ATableModel.move(moveUp, id, getDesignRequirements());
    } else if (value.equals(ObserverValue.DESIGN_REQUIREMENT_STEP1)) {
      return ATableModel.move(moveUp, id, getDesignRequirementsStep1());
    } else if (value.equals(ObserverValue.DESIGN_REQUIREMENT_STEP2)) {
      return ATableModel.move(moveUp, id, getDesignRequirementsStep2());
    }
    return true;
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.sds.ISDSController#getSafetyConstraint(java.util.UUID)
   */
  @Override
  public ITableModel getSafetyConstraint(UUID safetyConstraintId) {
    for (ITableModel s : this.getSafetyConstraints()) {
      if (s.getId().equals(safetyConstraintId)) {
        return s;
      }
    }
    return null;
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.sds.ISDSController#removeSafetyConstraint(java.util.UUID)
   */
  @Override
  public boolean removeSafetyConstraint(UUID safetyConstraintId) {
    ITableModel safetyConstraint = this.getSafetyConstraint(safetyConstraintId);
    int index = this.getSafetyConstraints().indexOf(safetyConstraint);
    this.getSafetyConstraints().remove(index);
    setChanged();
    notifyObservers(ObserverValue.SAFETY_CONSTRAINT);
    return true;
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.sds.ISDSController#addSystemGoal(java.lang.String, java.lang.String)
   */
  @Override
  public UUID addSystemGoal(String title, String description) {
    SystemGoal systemGoal = new SystemGoal(title, description);
    this.getSystemGoals().add(systemGoal);
    return systemGoal.getId();
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.sds.ISDSController#getAllSystemGoals()
   */
  @Override
  public List<ITableModel> getAllSystemGoals() {
    List<ITableModel> result = new ArrayList<>();
    for (SystemGoal systemGoal : this.getSystemGoals()) {
      result.add(systemGoal);
    }
    return result;
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.sds.ISDSController#getSystemGoal(java.util.UUID)
   */
  @Override
  public ITableModel getSystemGoal(UUID systemGoalId) {
    for (ITableModel s : this.getSystemGoals()) {
      if (s.getId().equals(systemGoalId)) {
        return s;
      }
    }
    return null;
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.sds.ISDSController#removeSystemGoal(java.util.UUID)
   */
  @Override
  public boolean removeSystemGoal(UUID systemGoalId) {
    ITableModel systemGoal = this.getSystemGoal(systemGoalId);
    int index = this.getSystemGoals().indexOf(systemGoal);
    this.getSystemGoals().remove(index);
    return true;
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.sds.ISDSController#addDesignRequirement(java.lang.String,
   * java.lang.String)
   */
  @Override
  public UUID addDesignRequirement(String title, String description) {
    return addDesignRequirement(title, description, ObserverValue.DESIGN_REQUIREMENT);
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.sds.ISDSController#addDesignRequirement(java.lang.String,
   * java.lang.String, xstampp.model.ObserverValue)
   */
  @Override
  public UUID addDesignRequirement(String title, String description, ObserverValue type) {
    switch (type) {
    case DESIGN_REQUIREMENT: {
      return addDesignRequirementStep0(title, description);
    }
    case DESIGN_REQUIREMENT_STEP1: {
      return addDesignRequirementStep1(title, description);
    }
    case DESIGN_REQUIREMENT_STEP2: {
      return addDesignRequirementStep2(title, description);
    }
    default:
      return null;
    }
  }

  private UUID addDesignRequirementStep0(String title, String description) {
    DesignRequirement designRequirement = new DesignRequirement(title, description);
    getDesignRequirements().add(designRequirement);
    setChanged();
    notifyObservers(ObserverValue.DESIGN_REQUIREMENT);
    return designRequirement.getId();
  }

  private UUID addDesignRequirementStep1(String title, String description) {
    DesignRequirementStep1 designRequirement = new DesignRequirementStep1(title, description);
    getDesignRequirementsStep1().add(designRequirement);
    setChanged();
    notifyObservers(ObserverValue.DESIGN_REQUIREMENT_STEP1);
    return designRequirement.getId();
  }

  private UUID addDesignRequirementStep2(String title, String description) {
    DesignRequirementStep2 designRequirement = new DesignRequirementStep2(title, description);
    getDesignRequirementsStep2().add(designRequirement);
    setChanged();
    notifyObservers(ObserverValue.DESIGN_REQUIREMENT_STEP2);
    return designRequirement.getId();
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.sds.ISDSController#getAllDesignRequirements()
   */
  @Override
  public List<ITableModel> getAllDesignRequirements() {
    List<ITableModel> result = new ArrayList<>();
    if (this.designRequirements != null) {
      for (DesignRequirement designRequirement : this.designRequirements) {
        result.add(designRequirement);
      }
    }
    return result;
  }

  public List<ITableModel> getAllDesignRequirements(ObserverValue type) {
    List<ITableModel> result = new ArrayList<>();
    switch (type) {
    case DESIGN_REQUIREMENT: {
      result.addAll(getDesignRequirements());
      break;
    }
    case DESIGN_REQUIREMENT_STEP1: {
      result.addAll(getDesignRequirementsStep1());
      break;
    }
    case DESIGN_REQUIREMENT_STEP2: {
      result.addAll(getDesignRequirementsStep2());
      break;
    }
    default:
      break;
    }
    return result;
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.sds.ISDSController#getDesignRequirement(java.util.UUID)
   */
  @Override
  public ITableModel getDesignRequirement(UUID designRequirementId) {
    return getDesignRequirement(designRequirementId, ObserverValue.DESIGN_REQUIREMENT);
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.sds.ISDSController#getDesignRequirement(java.util.UUID,
   * xstampp.model.ObserverValue)
   */
  @Override
  public ITableModel getDesignRequirement(UUID designRequirementId, ObserverValue type) {
    List<? extends ITableModel> list;
    switch (type) {
    case DESIGN_REQUIREMENT: {
      list = getDesignRequirements();
      break;
    }
    case DESIGN_REQUIREMENT_STEP1: {
      list = getDesignRequirementsStep1();
      break;
    }
    case DESIGN_REQUIREMENT_STEP2: {
      list = getDesignRequirementsStep2();
      break;
    }
    default:
      return null;
    }
    for (ITableModel d : list) {
      if (d.getId().equals(designRequirementId)) {
        return d;
      }
    }

    return null;
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.sds.ISDSController#removeDesignRequirement(java.util.UUID)
   */
  @Override
  public boolean removeDesignRequirement(UUID designRequirementId) {
    return removeDesignRequirement(designRequirementId, ObserverValue.DESIGN_REQUIREMENT);
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.sds.ISDSController#removeDesignRequirement(java.util.UUID,
   * xstampp.model.ObserverValue)
   */
  @Override
  public boolean removeDesignRequirement(UUID designRequirementId, ObserverValue type) {

    ITableModel designRequirement = this.getDesignRequirement(designRequirementId, type);
    if (this.designRequirements.remove(designRequirement)) {
      setChanged();
      notifyObservers(type);
      return true;
    }
    return false;
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.sds.ISDSController#setDesignRequirementDescription(xstampp.model.
   * ObserverValue, java.util.UUID, java.lang.String)
   */
  @Override
  public boolean setDesignRequirementDescription(ObserverValue type, UUID designRequirementId,
      String description) {
    if ((description == null) || (designRequirementId == null)) {
      return false;
    }
    ITableModel requirement = getDesignRequirement(designRequirementId, type);

    String oldDescription = ((ATableModel) requirement).setDescription(description);
    if (oldDescription != null) {
      UndoDesignReqChangeCallback changeCallback = new UndoDesignReqChangeCallback(this, type,
          requirement);
      changeCallback.setDescriptionChange(oldDescription, description);
      setChanged();
      notifyObservers(changeCallback);
      return true;
    }
    return false;
  }

  /*
   * (non-Javadoc)
   * @see
   * xstampp.astpa.model.sds.ISDSController#setDesignRequirementTitle(xstampp.model.ObserverValue,
   * java.util.UUID, java.lang.String)
   */
  @Override
  public boolean setDesignRequirementTitle(ObserverValue type, UUID designRequirementId,
      String title) {
    if ((title == null) || (designRequirementId == null)) {
      return false;
    }
    ITableModel requirement = getDesignRequirement(designRequirementId, type);

    String oldTitle = ((ATableModel) requirement).setTitle(title);
    if (oldTitle != null) {
      UndoDesignReqChangeCallback changeCallback = new UndoDesignReqChangeCallback(this, type,
          requirement);
      changeCallback.setTitleChange(oldTitle, title);
      setChanged();
      notifyObservers(changeCallback);
      return true;
    }
    return false;
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.sds.ISDSController#prepareForSave()
   */
  @Override
  public boolean prepareForSave() {
    boolean isUsed = false;
    for (SafetyConstraint safetyConstraint : this.getSafetyConstraints()) {
      safetyConstraint.prepareForSave();
    }
    for (DesignRequirement designRequirement : this.getDesignRequirements()) {
      designRequirement.prepareForSave();
    }
    for (DesignRequirementStep1 designRequirement1 : this.getDesignRequirementsStep1()) {
      designRequirement1.prepareForSave();
    }
    for (DesignRequirementStep2 designRequirement2 : this.getDesignRequirementsStep2()) {
      designRequirement2.prepareForSave();
    }
    for (SystemGoal systemGoal : getSystemGoals()) {
      systemGoal.prepareForSave();
    }
    if (designRequirements != null && designRequirements.isEmpty()) {
      designRequirements = null;
    }
    isUsed |= designRequirements != null;
    if (designRequirementsStep1 != null && designRequirementsStep1.isEmpty()) {
      designRequirementsStep1 = null;
    }
    isUsed |= designRequirementsStep1 != null;
    if (designRequirementsStep2 != null && designRequirementsStep2.isEmpty()) {
      designRequirementsStep2 = null;
    }
    isUsed |= designRequirementsStep2 != null;
    if (safetyConstraints != null && safetyConstraints.isEmpty()) {
      safetyConstraints = null;
    }
    isUsed |= safetyConstraints != null;
    if (systemGoals != null && systemGoals.isEmpty()) {
      systemGoals = null;
    }
    isUsed |= systemGoals != null;
    return isUsed;
  }

  @Override
  public void prepareForExport(LinkController linkController, IHazAccController hazacc,
      IControlActionController caController, ICausalController causalController) {
    for (SafetyConstraint safetyConstraint : this.getSafetyConstraints()) {
      safetyConstraint.prepareForExport();
      String linkString = ""; //$NON-NLS-1$
      for (UUID id : linkController.getLinksFor(ObserverValue.ACC_S0_LINK,
          safetyConstraint.getId())) {
        linkString += hazacc.getAccident(id).getIdString() + ", "; //$NON-NLS-1$
      }
      for (UUID id : linkController.getLinksFor(ObserverValue.DESIGN_REQUIREMENT,
          safetyConstraint.getId())) {
        linkString += getDesignRequirement(id).getIdString() + ", "; //$NON-NLS-1$
      }
      if (linkString.length() > 2) {
        safetyConstraint.setLinks(linkString.substring(0, linkString.length() - 2));
      }
    }
    for (DesignRequirement designRequirement : this.getDesignRequirements()) {
      designRequirement.prepareForExport();
      String linkString = ""; //$NON-NLS-1$
      for (UUID id : linkController.getLinksFor(ObserverValue.DESIGN_REQUIREMENT,
          designRequirement.getId())) {
        linkString += getSafetyConstraint(id).getIdString() + ", "; //$NON-NLS-1$
      }
      if (linkString.length() > 2) {
        designRequirement.setLinks(linkString.substring(0, linkString.length() - 2));
      }
    }
    for (DesignRequirementStep1 designRequirement1 : this.getDesignRequirementsStep1()) {
      designRequirement1.prepareForExport();
      String linkString = ""; //$NON-NLS-1$
      for (UUID id : linkController.getLinksFor(ObserverValue.DESIGN_REQUIREMENT_STEP1,
          designRequirement1.getId())) {
        linkString += caController.getCorrespondingSafetyConstraint(id).getIdString() + ", "; //$NON-NLS-1$
      }
      if (linkString.length() > 2) {
        designRequirement1.setLinks(linkString.substring(0, linkString.length() - 2));
      }
    }
    for (DesignRequirementStep2 designRequirement2 : this.getDesignRequirementsStep2()) {
      designRequirement2.prepareForExport();
      String linkString = ""; //$NON-NLS-1$
      for (UUID id : linkController.getLinksFor(ObserverValue.DESIGN_REQUIREMENT_STEP2,
          designRequirement2.getId())) {
        linkString += causalController.getSafetyConstraint(id).getIdString() + ", "; //$NON-NLS-1$
      }
      if (linkString.length() > 2) {
        designRequirement2.setLinks(linkString.substring(0, linkString.length() - 2));
      }
    }
    for (SystemGoal systemGoal : getSystemGoals()) {
      systemGoal.prepareForExport();
    }
  }

  private List<SafetyConstraint> getSafetyConstraints() {
    if (this.safetyConstraints == null) {
      this.safetyConstraints = new NumberedArrayList<>();
    }
    return safetyConstraints;
  }

  private List<SystemGoal> getSystemGoals() {
    if (this.systemGoals == null) {
      this.systemGoals = new NumberedArrayList<>();
    }
    return systemGoals;
  }

  private List<DesignRequirement> getDesignRequirements() {
    if (this.designRequirements == null) {
      this.designRequirements = new NumberedArrayList<>();
    }
    return designRequirements;
  }

  private List<DesignRequirementStep1> getDesignRequirementsStep1() {
    if (this.designRequirementsStep1 == null) {
      this.designRequirementsStep1 = new NumberedArrayList<>();
    }
    return designRequirementsStep1;
  }

  private List<DesignRequirementStep2> getDesignRequirementsStep2() {
    if (this.designRequirementsStep2 == null) {
      this.designRequirementsStep2 = new NumberedArrayList<>();
    }
    return designRequirementsStep2;
  }
}
