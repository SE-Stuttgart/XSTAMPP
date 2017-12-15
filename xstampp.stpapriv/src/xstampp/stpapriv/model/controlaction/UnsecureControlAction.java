/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
 * Grahovac, Jarkko Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian
 * Sieber, Fabian Toth, Patrick Wickenhäuser, Aliaksei Babkovich, Aleksander
 * Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.stpapriv.model.controlaction;

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

/**
 * Class for unsafe control action objects.
 * 
 * @author Fabian Toth
 * 
 */
@XmlRootElement(name = "unsecurecontrolaction")
@XmlAccessorType(XmlAccessType.NONE)
public class UnsecureControlAction
    extends EntryWithSeverity
    implements IUnsafeControlAction, ICorrespondingUnsafeControlAction, IEntryWithNameId {

  @XmlElement(name = "description")
  private String description;

  @XmlElement(name = "id")
  private UUID id;

  @XmlElement(name = "number")
  private int number;

  @XmlElement(name = "type")
  private UnsafeControlActionType type;

  @XmlElement(name = "correspondingSecurityConstraint")
  private CorrespondingSafetyConstraint correspondingSafetyConstraint;

  @XmlElement(name = "links")
  private String links;

  @XmlElement(name = "identifier")
  public String identifier;

  @XmlElement(name = "safetyCritical")
  public boolean isSafetyCritical;

  @XmlElement(name = "securityCritical")
  public boolean isSecurityCritical;

  @XmlElement(name = "privacyCritical")
  public boolean isPrivacyCritical;

  public boolean isPrivacyCritical() {
    return isPrivacyCritical;
  }

  public void setPrivacyCritical(boolean isPrivacyCritical) {
    this.isPrivacyCritical = isPrivacyCritical;
  }

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
  public UnsecureControlAction(String description, UnsafeControlActionType type) {
    this.description = description;
    this.type = type;
    this.isSafetyCritical = false;
    this.isSecurityCritical = false;
    this.correspondingSafetyConstraint = new CorrespondingSafetyConstraint(""); //$NON-NLS-1$
    this.id = UUID.randomUUID();
    this.number = 0;
    setSeverity(Severity.S0);
  }

  public boolean isSafetyCritical() {
    return isSafetyCritical;
  }

  public void setSafetyCritical(boolean isSafetyCritical) {
    this.isSafetyCritical = isSafetyCritical;
  }

  public boolean isSecurityCritical() {
    return isSecurityCritical;
  }

  public void setSecurityCritical(boolean isSecurityCritical) {
    this.isSecurityCritical = isSecurityCritical;
  }

  /**
   * Empty constructor for JAXB. Do not use it!
   * 
   * @author Fabian Toth
   */
  public UnsecureControlAction() {
    setSeverity(Severity.S0);
    // empty constructor for JAXB
  }

  @Override
  public String getDescription() {
    return this.description;
  }

  /**
   * @param description
   *          the description to set
   */
  public boolean setDescription(String description) {
    boolean result = false;
    if (!description.equals(this.description)) {
      result = true;
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

    }
    return this.correspondingSafetyConstraint;
  }

  /**
   * @param correspondingSafetyConstraint
   *          the correspondingSafetyConstraint to set
   */
  public void setCorrespondingSafetyConstraint(CorrespondingSafetyConstraint correspondingSafetyConstraint) {
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
    return true;
  }

  @Override
  public int getNumber() {
    return number;
  }

  @Override
  public String getTitle() {
    return "PCA1." + getNumber();
  }

  @Override
  public String getText() {
    return getTitle() + ":" + getDescription();
  }

  @Override
  public String getIdString() {
    return getTitle();
  }

}
