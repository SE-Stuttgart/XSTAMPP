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

import xstampp.astpa.model.EntryWithSeverity;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.controlaction.interfaces.UnsafeControlActionType;
import xstampp.astpa.model.controlaction.safetyconstraint.CorrespondingSafetyConstraint;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.interfaces.IEntryWithNameId;
import xstampp.astpa.model.interfaces.Severity;
import xstampp.model.ITableEntry;

/**
 * Class for unsafe control action objects.
 * 
 * @author Fabian Toth
 * 
 */
@XmlRootElement(name = "unsafecontrolaction")
@XmlAccessorType(XmlAccessType.NONE)
public class UnsafeControlAction extends EntryWithSeverity
    implements IUnsafeControlAction, ICorrespondingUnsafeControlAction, IEntryWithNameId {

  @XmlElement(name = "description")
  private String description;

  @XmlElement(name = "id")
  private UUID id;

  @XmlElement(name = "number")
  private int number;

  @XmlElement(name = "type")
  private UnsafeControlActionType type;

  @XmlElement(name = "correspondingSafetyConstraint")
  private CorrespondingSafetyConstraint correspondingSafetyConstraint;

  @XmlElement(name = "links")
  private String links;

  @XmlElement(name = "identifier")
  public String identifier;

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
    this.description = description;
    this.type = type;
    this.correspondingSafetyConstraint = null; //$NON-NLS-1$
    this.id = UUID.randomUUID();
    this.number = 0;
    setSeverity(Severity.S0);
  }
  

  /**
   * Empty constructor for JAXB. Do not use it!
   * 
   * @author Fabian Toth
   */
  public UnsafeControlAction() {
    setSeverity(Severity.S0);
  }

  @Override
  public String getDescription() {
    return this.description;
  }

  /**
   * @param description
   *          the description to set
   */
  public String setDescription(String description) {
    String result = null;
    if(!description.equals(this.description) ){
      result = this.description;
      this.description = description;
    }
    return result;
  }

  @Override
  public UUID getId() {
    return this.id;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(UUID id) {
    this.id = id;
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
  public String getLinks() {
    return this.links;
  }

  /**
   * @param links
   *          the links to set
   */
  public void setLinks(String links) {
    this.links = links;
  }

  public boolean setNumber(int number) {
    this.number = number;
    if(this.correspondingSafetyConstraint != null) {
      this.correspondingSafetyConstraint.setNumber(number);
    }
    return true;
  }

  @Override
  public int compareTo(ITableEntry o) {
    try {
      if (o.getNumber() < this.getNumber()) {
        return 1;
      } else if (o.getNumber() > this.getNumber()) {
        return -1;
      }
      return 0;
    } catch (NullPointerException exc) {
      return 0;
    }
  }

  @Override
  public int getNumber() {
    return number;
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

  public void prepareForSave() {
    identifier = null;
    setLinks(null);
    correspondingSafetyConstraint.prepareForSave();
  }
}
