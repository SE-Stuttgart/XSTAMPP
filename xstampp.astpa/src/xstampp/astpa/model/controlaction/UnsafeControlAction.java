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

package xstampp.astpa.model.controlaction;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import xstampp.astpa.model.ATableModel;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.controlaction.interfaces.UnsafeControlActionType;
import xstampp.astpa.model.controlaction.safetyconstraint.CorrespondingSafetyConstraint;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.interfaces.IEntryWithNameId;
import xstampp.astpa.model.interfaces.ISTPADataModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.interfaces.Severity;
import xstampp.astpa.model.linking.LinkingType;

/**
 * Class for unsafe control action objects.
 * 
 * @author Fabian Toth
 * 
 */
@XmlRootElement(name = "unsafecontrolaction")
@XmlAccessorType(XmlAccessType.NONE)
public class UnsafeControlAction extends ATableModel
    implements IUnsafeControlAction, ICorrespondingUnsafeControlAction, IEntryWithNameId {

  @XmlElement(name = "type")
  private UnsafeControlActionType type;

  @XmlElement(name = "correspondingSafetyConstraint")
  private CorrespondingSafetyConstraint correspondingSafetyConstraint;

  /**
   * Constructs a new unsafe control action with the given values
   * 
   * @param description
   *          the description of the new unsafe control action
   * @param type
   *          the type of the new unsafe control action
   * 
   * @author Fabian Toth
   */
  public UnsafeControlAction(String description, UnsafeControlActionType type) {
    this();
    setDescription(description);
    setTitle("");
    this.type = type;
  }

  /**
   * Empty constructor for JAXB. Do not use it!
   * 
   * @author Fabian Toth
   */
  public UnsafeControlAction() {
    super();
    this.correspondingSafetyConstraint = new CorrespondingSafetyConstraint();
    setSeverity(Severity.S0);
  }

  /**
   * Constructs a new unsafe control action with the same values as the given one
   * 
   * @param description
   *          the description of the new unsafe control action
   * @param type
   *          the type of the new unsafe control action
   */
  public UnsafeControlAction(IUnsafeControlAction otherUca) {
    super(otherUca, otherUca.getNumber());
    this.type = otherUca.getType();
    if (otherUca instanceof UnsafeControlAction) {
      this.correspondingSafetyConstraint = new CorrespondingSafetyConstraint(
          ((UnsafeControlAction) otherUca).getCorrespondingSafetyConstraint());
    } else {
      this.correspondingSafetyConstraint = new CorrespondingSafetyConstraint();
    }
    setSeverity(Severity.S0);
  }

  @Override
  public UnsafeControlActionType getType() {
    return this.type;
  }

  /**
   * @param type
   *          the type to set
   */
  public void setType(UnsafeControlActionType type) {
    this.type = type;
  }

  @Override
  public CorrespondingSafetyConstraint getCorrespondingSafetyConstraint() {
    correspondingSafetyConstraint.setNumber(getNumber());
    return this.correspondingSafetyConstraint;
  }

  /**
   * @param correspondingSafetyConstraint
   *          the correspondingSafetyConstraint to set
   */
  public void setCorrespondingSafetyConstraint(
      CorrespondingSafetyConstraint correspondingSafetyConstraint) {
    this.correspondingSafetyConstraint = correspondingSafetyConstraint;
  }

  @Override
  public boolean setNumber(int number) {
    if (super.setNumber(number)) {
      this.correspondingSafetyConstraint.setNumber(number);
      return true;
    }
    return false;
  }

  @Override
  public String getTitle() {
    return "UCA1." + getNumber();
  }

  @Override
  public String getText() {
    return getTitle() + ":" + getDescription();
  }

  @Override
  public String getIdString() {
    return getTitle();
  }

  @Override
  public void prepareForSave() {
    super.prepareForSave();
    this.correspondingSafetyConstraint.setNumber(getNumber());
    correspondingSafetyConstraint.prepareForSave();
  }

  public void prepareForExport(ISTPADataModel dataModel) {
    super.prepareForExport();
    correspondingSafetyConstraint.prepareForExport();
    String links = "";
    for (UUID uuid : dataModel.getLinkController().getLinksFor(LinkingType.SC2_SC1_LINK,
        correspondingSafetyConstraint.getId())) {
      ITableModel constraint = dataModel.getControlActionController()
          .getCorrespondingSafetyConstraint(uuid);
      if (constraint != null) {
        links += links.isEmpty() ? "" : ", ";
        links += constraint.getIdString();
      }
    }
    correspondingSafetyConstraint.setLinks(links);
  }
}
