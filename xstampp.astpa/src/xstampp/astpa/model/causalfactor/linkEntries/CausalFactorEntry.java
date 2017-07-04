/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.model.causalfactor.linkEntries;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import xstampp.astpa.model.causalfactor.interfaces.CausalFactorEntryData;
import xstampp.astpa.model.causalfactor.interfaces.CausalFactorUCAEntryData;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorEntry;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.hazacc.HazAccController;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.AbstractLTLProvider;

@XmlAccessorType(XmlAccessType.NONE)
public class CausalFactorEntry implements ICausalFactorEntry {

  @XmlAttribute(name = "id", required = true)
  private UUID id;

  @XmlElement(name = "hazardLinks")
  private String hazardLinks;

  @XmlElement(name = "constraintText")
  private String constraintText;

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

  public boolean setConstraintText(String constraintText) {
    if (this.constraintText == null || !this.constraintText.equals(constraintText)) {
      this.constraintText = constraintText;
      return true;
    }
    return false;
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

  /**
   * @param scenarioLinks
   *          the scenarioLinks to set
   */
  public boolean setScenarioLinks(List<UUID> scenarioLinks) {
    if (this.scenarioLinks == null || !this.scenarioLinks.equals(scenarioLinks)) {
      this.scenarioLinks = scenarioLinks;
      return true;
    }
    return false;
  }

  @Override
  public List<UUID> getScenarioLinks() {
    return scenarioLinks;
  }

  @Override
  public UUID getUcaLink() {
    return ucaLink;
  }

  public CausalFactorEntryData changeCausalEntry(CausalFactorEntryData entryData) {
    CausalFactorUCAEntryData resultData = new CausalFactorUCAEntryData(id);
    boolean res = false;
    if (entryData.noteChanged() && !entryData.getNote().equals(getNote())) {
      resultData.setNote(note);
      res |= setNote(entryData.getNote());
    }

    if (entryData.hazardsChanged()) {
      resultData.setHazardIds(hazardIds);
      res |= setHazardIds(entryData.getHazardIds());
    }

    if (entryData.constraintChanged()) {
      resultData.setConstraint(constraintText);
      res |= setConstraintText(entryData.getSafetyConstraint());
    }

    if (entryData instanceof CausalFactorUCAEntryData
        && ((CausalFactorUCAEntryData) entryData).scenariosChanged()) {
      resultData.setScenarioLinks(scenarioLinks);
      res |= setScenarioLinks(((CausalFactorUCAEntryData) entryData).getScenarioLinks());
    }
    if (res) {
      return resultData;
    }
    return null;
  }

  /**
   * this must be executed after the controlActionController has been prepared for export so that
   * the given uca list already has all needed entries
   * 
   * @param hazAccController
   * @param allRefinedRules
   * @param allUnsafeControlActions
   */
  public void prepareForExport(HazAccController hazAccController,
      List<AbstractLTLProvider> allRefinedRules,
      List<ICorrespondingUnsafeControlAction> allUnsafeControlActions) {

    // if there is a link to a uca then sync the respective description and hazard links
    if (getUcaLink() != null) {
      for (ICorrespondingUnsafeControlAction uca : allUnsafeControlActions) {
        if (uca.getId().equals(getUcaLink())) {
          setUcaDescription("UCA1." + uca.getNumber() + "\n" + uca.getDescription());
          setHazardLinks(uca.getLinks());
        }
      }
    } else if (getHazardIds() != null) {
      // create the hazard Link String by adding a label for each hazard
      hazardLinks = new String();
      for (ITableModel hazard : hazAccController.getAllHazards()) {
        if (this.hazardIds.contains(hazard.getId())) {
          if (!hazardLinks.isEmpty()) {
            hazardLinks += ", ";//$NON-NLS-1$
          }
          hazardLinks += "H-" + hazard.getNumber();//$NON-NLS-1$
        }
      }
    }

    scenarioEntries = new ArrayList<>();
    if (getScenarioLinks() != null) {
      for (AbstractLTLProvider ltlProvider : allRefinedRules) {
        if (getScenarioLinks().contains(ltlProvider.getRuleId())) {
          scenarioEntries.add(new CausalScenarioEntry(ltlProvider.getSafetyRule(),
              ltlProvider.getRefinedSafetyConstraint()));
        }
      }
    }
    // prevent the string entries to be null to ensure a smooth export
    if (getConstraintText() == null) {
      setConstraintText("");
    }
    if (getHazardLinks() == null) {
      setHazardLinks("");
    }
    if (getNote() == null) {
      setNote("");
    }
    if (getUcaDescription() == null) {
      setUcaDescription("");
    }
  }

  public boolean prepareForSave(HazAccController hazAccController,
      List<ICorrespondingUnsafeControlAction> allUnsafeControlActions) {
    // get rid of redundant entries which should not be stored
    setHazardLinks(null);
    setUcaDescription(null);
    if (getNote().equals("")) {
      setNote(null);
    }
    if (getConstraintText() != null && getConstraintText().equals("")) {
      setConstraintText(null);
    }
    scenarioEntries = null;
    if (hazardIds != null) {
      UUID[] hazIds = (UUID[]) this.hazardIds.toArray(new UUID[0]);
      for (int i = 0; i < hazIds.length; i++) {
        if (hazAccController.getHazard(hazIds[i]) == null) {
          this.hazardIds.remove(hazIds[i]);
        }
      }
    }
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