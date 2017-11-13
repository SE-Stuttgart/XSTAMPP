/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.model.causalfactor;

import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import xstampp.astpa.model.causalfactor.interfaces.CausalFactorEntryData;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorEntry;
import xstampp.astpa.model.linking.LinkController;
import xstampp.model.ObserverValue;

@XmlAccessorType(XmlAccessType.NONE)
public class CausalFactorEntry implements ICausalFactorEntry {

  @XmlAttribute(name = "id", required = true)
  private UUID id;

  @XmlElement(name = "hazardLinks")
  private String hazardLinks;

  @XmlElement(name = "constraintText")
  private String constraintText;

  @XmlElement
  private UUID constraintId;

  @XmlElementWrapper(name = "hazardIds")
  @XmlElement(name = "id")
  private List<UUID> hazardIds;

  @XmlElement(name = "note")
  private String note;

  @XmlElement(name = "ucaDescription")
  private String ucaDescription;

  @XmlElement(name = "ucaLink")
  private UUID ucaLink;

  @XmlElementWrapper(name = "scenarioEntries")
  @XmlElement(name = "scenarioEntry")
  private List<CausalScenarioEntry> scenarioEntries;

  @XmlElementWrapper(name = "scenarioLinks")
  @XmlElement(name = "id")
  private List<UUID> scenarioLinks;

  public CausalFactorEntry() {
    id = UUID.randomUUID();

  }

  public CausalFactorEntry(UUID ucaLink, UUID id) {
    this.id = id;
    setUcaLink(ucaLink);
  }

  public CausalFactorEntry(UUID ucaLink) {
    this();
    setUcaLink(ucaLink);
  }

  public CausalFactorEntry(CausalFactorEntryData data) {
    id = data.getId();
  }

  /**
   * @return the hazardLinks
   */
  public String getHazardLinks() {
    return hazardLinks;
  }

  /**
   * @param hazardLinks
   *          the hazardLinks to set
   */
  public void setHazardLinks(String hazardLinks) {
    this.hazardLinks = hazardLinks;
  }

  public String getConstraintText() {
    return constraintText;
  }

  /**
   * @return the hazardIds
   */
  public List<UUID> getHazardIds() {
    return hazardIds;
  }

  /**
   * @param hazardIds
   *          the hazardIds to set
   */
  public boolean setHazardIds(List<UUID> hazardIds) {
    if (this.hazardIds == null || !this.hazardIds.equals(hazardIds)) {
      this.hazardIds = hazardIds;
      return true;
    }
    return false;
  }

  /**
   * @return the note
   */
  public String getNote() {
    if (note == null) {
      return new String();
    }
    return note;
  }

  /**
   * @param note
   *          the note to set
   */
  public boolean setNote(String note) {
    if (note == null || !note.equals(getNote())) {
      this.note = note;
      return true;
    }
    return false;
  }

  public UUID getId() {
    return id;
  }

  /**
   * @param ucaLink
   *          the ucaLink to set
   */
  public void setUcaLink(UUID ucaLink) {
    this.ucaLink = ucaLink;
  }

  public String getUcaDescription() {
    return ucaDescription;
  }

  public void setUcaDescription(String ucaDescription) {
    this.ucaDescription = ucaDescription;
  }

  @Override
  public List<UUID> getScenarioLinks() {
    return scenarioLinks;
  }

  void setConstraintId(UUID constraintId) {
    this.constraintId = constraintId;
  }

  public UUID getConstraintId() {
    return constraintId;
  }

  boolean prepareForSave(UUID componentId, CausalFactor causalFactor,
      LinkController linkController, List<CausalSafetyConstraint> list) {
    // get rid of redundant entries which should not be stored
    if (constraintText != null) {
      CausalSafetyConstraint safetyConstraint = new CausalSafetyConstraint(constraintText);
      this.constraintText = null;
      this.constraintId = safetyConstraint.getId();
      list.add(safetyConstraint);
    }
    if (ucaLink != null) {
      UUID ucaCfLink = linkController.addLink(ObserverValue.UCA_CausalFactor_LINK, ucaLink,
          causalFactor.getId());
      UUID UcaCfCompLink = linkController.addLink(ObserverValue.UcaCfLink_Component_LINK, ucaCfLink,
          componentId);
      ucaLink = null;
      // if a constraint is already linked to this entry than the link is re-added as
      // UcaHazLink_SC2_LINK to the LinkController
      linkController.getRawLinksFor(ObserverValue.UCA_HAZ_LINK, ucaLink)
          .forEach(link -> {
            UUID hazEntryId = linkController.addLink(ObserverValue.UCAEntryLink_HAZ_LINK,
                UcaCfCompLink, link.getLinkB());
            if (this.constraintId != null) {
              linkController.addLink(ObserverValue.CausalHazLink_SC2_LINK, hazEntryId,
                  this.constraintId);
            }
          });
    }
    constraintText = null;
    scenarioEntries = null;
    hazardIds = null;
    return true;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof CausalFactorEntry) {
      return ((CausalFactorEntry) obj).getId().equals(getId());
    }
    return super.equals(obj);
  }
}