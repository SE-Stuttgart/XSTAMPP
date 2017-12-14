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
import xstampp.astpa.model.causalfactor.ICausalController;
import xstampp.astpa.model.controlaction.IControlActionController;
import xstampp.astpa.model.hazacc.IHazAccController;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.astpa.model.service.UndoDesignReqChangeCallback;
import xstampp.astpa.model.service.UndoGoalChangeCallback;
import xstampp.astpa.model.service.UndoSafetyConstraintChangeCallback;
import xstampp.model.NumberedArrayList;
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

  private LinkController linkController;

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

  public void setLinkController(LinkController linkController) {
    this.linkController = linkController;
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
    if (this.getSafetyConstraints().add(safetyConstraint)) {
      setChanged();
      notifyObservers(new UndoAddSC(this, safetyConstraint, linkController));
    }
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
    if (this.getSafetyConstraints().add(safetyConstraint)) {
      setChanged();
      notifyObservers(new UndoAddSC(this, safetyConstraint, linkController));
    }
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
    notifyObservers(new UndoRemoveSC(this, safetyConstraint, linkController));
    return true;
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.sds.ISDSController#addSystemGoal(java.lang.String, java.lang.String)
   */
  @Override
  public UUID addSystemGoal(String title, String description) {
    SystemGoal systemGoal = new SystemGoal(title, description);
    return addSystemGoal(systemGoal);
  }

  UUID addSystemGoal(ITableModel model) {
    SystemGoal systemGoal = new SystemGoal(model);
    if (this.getSystemGoals().add(systemGoal)) {
      setChanged();
      notifyObservers(new UndoAddSystemGoals(this, systemGoal, linkController));
    }
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
    if (systemGoalId == null) {
      return false;
    }
    if (!(getSystemGoal(systemGoalId) instanceof SystemGoal)) {
      return false;
    }

    ITableModel systemGoal = this.getSystemGoal(systemGoalId);
    int index = this.getSystemGoals().indexOf(systemGoal);
    if (this.getSystemGoals().remove(index) != null) {
      setChanged();
      notifyObservers(new UndoRemoveSystemGoals(this, systemGoal, linkController));
      return true;
    }
    return false;
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

  UUID addDesignRequirement(ITableModel model, ObserverValue type) {

    switch (type) {
    case DESIGN_REQUIREMENT: {
      return addDesignRequirementStep0(model);
    }
    case DESIGN_REQUIREMENT_STEP1: {
      return addDesignRequirementStep1(model);
    }
    case DESIGN_REQUIREMENT_STEP2: {
      return addDesignRequirementStep2(model);
    }
    default:
      return null;
    }
  }

  private UUID addDesignRequirementStep0(String title, String description) {
    DesignRequirement designRequirement = new DesignRequirement(title, description);
    return addDesignRequirementStep0(designRequirement);
  }

  private UUID addDesignRequirementStep0(ITableModel model) {
    DesignRequirement designRequirement = new DesignRequirement(model);
    getDesignRequirements().add(designRequirement);
    setChanged();
    notifyObservers(new UndoAddDR(this, designRequirement, linkController, ObserverValue.DESIGN_REQUIREMENT));
    return designRequirement.getId();
  }

  private UUID addDesignRequirementStep1(String title, String description) {
    DesignRequirementStep1 designRequirement = new DesignRequirementStep1(title, description);
    return addDesignRequirementStep1(designRequirement);
  }

  private UUID addDesignRequirementStep1(ITableModel model) {
    DesignRequirementStep1 designRequirement = new DesignRequirementStep1(model);
    getDesignRequirementsStep1().add(designRequirement);
    setChanged();
    notifyObservers(new UndoAddDR(this, designRequirement, linkController, ObserverValue.DESIGN_REQUIREMENT_STEP1));
    return designRequirement.getId();
  }

  private UUID addDesignRequirementStep2(String title, String description) {
    DesignRequirementStep2 designRequirement = new DesignRequirementStep2(title, description);
    return addDesignRequirementStep2(designRequirement);
  }

  private UUID addDesignRequirementStep2(ITableModel model) {
    DesignRequirementStep2 designRequirement = new DesignRequirementStep2(model);
    getDesignRequirementsStep2().add(designRequirement);
    setChanged();
    notifyObservers(new UndoAddDR(this, designRequirement, linkController, ObserverValue.DESIGN_REQUIREMENT_STEP2));
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
    return new ArrayList<>(getDesignReqList(type));
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
    for (ITableModel d : getDesignReqList(type)) {
      if (d.getId().equals(designRequirementId)) {
        return d;
      }
    }

    return null;
  }

  private List<? extends ITableModel> getDesignReqList(ObserverValue type) {
    switch (type) {
    case DESIGN_REQUIREMENT: {
      return getDesignRequirements();
    }
    case DESIGN_REQUIREMENT_STEP1: {
      return getDesignRequirementsStep1();
    }
    case DESIGN_REQUIREMENT_STEP2: {
      return getDesignRequirementsStep2();
    }
    default:
      return new ArrayList<>();
    }
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
    if (getDesignReqList(type).remove(designRequirement)) {
      setChanged();
      notifyObservers(new UndoRemoveDR(this, designRequirement, linkController, type));
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

  @Override
  public boolean setSafetyConstraintTitle(UUID safetyConstraintId, String title) {
    if ((title == null) || (safetyConstraintId == null)) {
      return false;
    }
    ITableModel safetyConstraint = getSafetyConstraint(safetyConstraintId);
    if (!(safetyConstraint instanceof SafetyConstraint)) {
      return false;
    }
    String oldTitle = ((SafetyConstraint) safetyConstraint).setTitle(title);
    if (oldTitle != null) {
      setChanged();
      UndoSafetyConstraintChangeCallback changeCallback = new UndoSafetyConstraintChangeCallback(
          this, safetyConstraint);
      changeCallback.setTitleChange(oldTitle, title);
      notifyObservers(changeCallback);
      return true;
    }
    return false;
  }

  @Override
  public boolean setSafetyConstraintDescription(UUID safetyConstraintId, String description) {
    if ((description == null) || (safetyConstraintId == null)) {
      return false;
    }
    ITableModel safetyConstraint = getSafetyConstraint(safetyConstraintId);
    if (!(safetyConstraint instanceof SafetyConstraint)) {
      return false;
    }

    String oldDescription = ((SafetyConstraint) safetyConstraint).setDescription(description);
    if (oldDescription != null) {
      setChanged();
      UndoSafetyConstraintChangeCallback changeCallback = new UndoSafetyConstraintChangeCallback(
          this, safetyConstraint);
      changeCallback.setDescriptionChange(oldDescription, description);
      notifyObservers(changeCallback);
      return true;
    }
    return false;
  }

  @Override
  public boolean setSystemGoalDescription(UUID systemGoalId, String description) {
    if ((systemGoalId == null) || (description == null)) {
      return false;
    }
    ITableModel systemGoal = getSystemGoal(systemGoalId);
    if (!(systemGoal instanceof SystemGoal)) {
      return false;
    }

    String oldDescription = ((SystemGoal) systemGoal).setDescription(description);
    if (oldDescription != null) {
      setChanged();
      UndoGoalChangeCallback changeCallback = new UndoGoalChangeCallback(this, systemGoal);
      changeCallback.setDescriptionChange(oldDescription, description);
      notifyObservers(changeCallback);
      return true;
    }
    return false;
  }

  @Override
  public boolean setSystemGoalTitle(UUID systemGoalId, String title) {
    if ((systemGoalId == null) || (title == null)) {
      return false;
    }
    ITableModel systemGoal = getSystemGoal(systemGoalId);
    if (!(systemGoal instanceof SystemGoal)) {
      return false;
    }
    String oldTitle = ((SystemGoal) systemGoal).setTitle(title);
    if (oldTitle != null) {
      setChanged();
      UndoGoalChangeCallback changeCallback = new UndoGoalChangeCallback(this, systemGoal);
      changeCallback.setTitleChange(oldTitle, title);
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
      for (UUID id : linkController.getLinksFor(LinkingType.ACC_S0_LINK,
          safetyConstraint.getId())) {
        linkString += hazacc.getAccident(id).getIdString() + ", "; //$NON-NLS-1$
      }
      for (UUID id : linkController.getLinksFor(LinkingType.DR0_SC_LINK,
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
      for (UUID id : linkController.getLinksFor(LinkingType.DR0_SC_LINK,
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
      for (UUID id : linkController.getLinksFor(LinkingType.DR1_CSC_LINK,
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
      for (UUID id : linkController.getLinksFor(LinkingType.DR2_CausalSC_LINK,
          designRequirement2.getId())) {
        ITableModel constraint = causalController.getSafetyConstraint(id);
        linkString += constraint != null ? constraint.getIdString() + ", " : ""; //$NON-NLS-1$
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

  public void syncContent(SDSController controller) {
    for (SystemGoal other : controller.systemGoals) {
      ITableModel own = getSystemGoal(other.getId());
      if (own == null) {
        addSystemGoal(other);
      } else {
        setSystemGoalTitle(other.getId(), other.getTitle());
        setSystemGoalDescription(other.getId(), other.getDescription());
      }
    }
    for (SafetyConstraint other : controller.safetyConstraints) {
      ITableModel own = getSafetyConstraint(other.getId());
      if (own == null) {
        addSafetyConstraint(other);
      } else {
        setSafetyConstraintTitle(other.getId(), other.getTitle());
        setSafetyConstraintDescription(other.getId(), other.getDescription());
      }
    }
    for (DesignRequirement otherReq : controller.getDesignRequirements()) {
      ITableModel ownReq = getDesignRequirement(otherReq.getId(), ObserverValue.DESIGN_REQUIREMENT);
      if (ownReq == null) {
        addDesignRequirement(otherReq, ObserverValue.DESIGN_REQUIREMENT);
      } else {
        setDesignRequirementTitle(ObserverValue.DESIGN_REQUIREMENT, otherReq.getId(), otherReq.getTitle());
        setDesignRequirementDescription(ObserverValue.DESIGN_REQUIREMENT, otherReq.getId(), otherReq.getDescription());
      }
    }
    for (DesignRequirementStep1 otherReq : controller.getDesignRequirementsStep1()) {
      ITableModel ownReq = getDesignRequirement(otherReq.getId(), ObserverValue.DESIGN_REQUIREMENT_STEP1);
      if (ownReq == null) {
        addDesignRequirement(otherReq, ObserverValue.DESIGN_REQUIREMENT_STEP1);
      } else {
        setDesignRequirementTitle(ObserverValue.DESIGN_REQUIREMENT_STEP1, otherReq.getId(), otherReq.getTitle());
        setDesignRequirementDescription(ObserverValue.DESIGN_REQUIREMENT_STEP1, otherReq.getId(),
            otherReq.getDescription());
      }
    }
    for (DesignRequirementStep2 otherReq : controller.getDesignRequirementsStep2()) {
      ITableModel ownReq = getDesignRequirement(otherReq.getId(), ObserverValue.DESIGN_REQUIREMENT_STEP2);
      if (ownReq == null) {
        addDesignRequirement(otherReq, ObserverValue.DESIGN_REQUIREMENT_STEP2);
      } else {
        setDesignRequirementTitle(ObserverValue.DESIGN_REQUIREMENT_STEP2, otherReq.getId(), otherReq.getTitle());
        setDesignRequirementDescription(ObserverValue.DESIGN_REQUIREMENT_STEP2, otherReq.getId(),
            otherReq.getDescription());
      }
    }
  }
}
