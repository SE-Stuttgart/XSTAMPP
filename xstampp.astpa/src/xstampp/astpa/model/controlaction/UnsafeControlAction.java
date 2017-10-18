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
import xstampp.astpa.model.interfaces.Severity;

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
    super("", description);
    this.type = type;
    this.correspondingSafetyConstraint = null; // $NON-NLS-1$
    setSeverity(Severity.S0);
  }

  /**
   * Empty constructor for JAXB. Do not use it!
   * 
   * @author Fabian Toth
   */
  public UnsafeControlAction() {
    super();
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
    if (this.correspondingSafetyConstraint == null) {
      this.correspondingSafetyConstraint = new CorrespondingSafetyConstraint(new String());
      correspondingSafetyConstraint.setNumber(getNumber());

    }
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
      if (this.correspondingSafetyConstraint != null) {
        this.correspondingSafetyConstraint.setNumber(number);
      }
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
    if(correspondingSafetyConstraint !=null) {
    	correspondingSafetyConstraint.prepareForSave();
    }
  }

  @Override
  public void prepareForExport() {
    super.prepareForExport();
    correspondingSafetyConstraint.prepareForExport();
  }
}
