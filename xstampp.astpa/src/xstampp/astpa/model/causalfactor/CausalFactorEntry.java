/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.model.causalfactor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import xstampp.astpa.model.NumberedArrayList;
import xstampp.astpa.model.causalfactor.interfaces.CausalFactorEntryData;
import xstampp.astpa.model.causalfactor.interfaces.CausalFactorUCAEntryData;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorEntry;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.hazacc.IHazAccController;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.Link;
import xstampp.astpa.model.linking.LinkController;
import xstampp.model.AbstractLTLProvider;
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

  void moveSafetyConstraints(List<CausalSafetyConstraint> list, LinkController linkController) {
    if (constraintText != null) {
      CausalSafetyConstraint safetyConstraint = new CausalSafetyConstraint(constraintText);
      this.constraintText = null;
      this.constraintId = safetyConstraint.getId();
      list.add(safetyConstraint);
    }
    if (this.constraintId != null) {
      Link ucaHazLink = linkController.getRawLinksFor(ObserverValue.UCA_HAZ_LINK , ucaLink).stream().findFirst().orElse(null);
      if (ucaHazLink != null) {
        linkController.addLink(ObserverValue.UcaHazLink_SC2_LINK, ucaHazLink.getId(), this.constraintId);
      }
      this.constraintId = null;
    }
    
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

  void setConstraintId(UUID constraintId) {
    this.constraintId = constraintId;
  }

  public UUID getConstraintId() {
    return constraintId;
  }

  public CausalFactorEntryData changeCausalEntry(CausalFactorEntryData entryData,
      NumberedArrayList<CausalSafetyConstraint> causalSafetyConstraints) {
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
      CausalSafetyConstraint safetyConstraint = causalSafetyConstraints.get(getConstraintId());

      if (safetyConstraint != null) {
        String title = safetyConstraint.setTitle(entryData.getSafetyConstraint());
        resultData.setConstraint(title);
        res |= true;
      } else {
        resultData.setConstraint("");
        safetyConstraint = new CausalSafetyConstraint(
            entryData.getSafetyConstraint());
        setConstraintId(safetyConstraint.getId());
        res |= causalSafetyConstraints.add(safetyConstraint);
      }
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
  public void prepareForExport(IHazAccController hazAccController,
      List<AbstractLTLProvider> allRefinedRules,
      List<ICorrespondingUnsafeControlAction> allUnsafeControlActions,
      List<CausalSafetyConstraint> safetyConstraints) {

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

    constraintText = "";
    for (CausalSafetyConstraint constraint : safetyConstraints) {
      if (constraint.getId().equals(constraintId)) {
        constraintText = constraint.getTitle();
      }
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

  public boolean prepareForSave(IHazAccController hazAccController,
      List<ICorrespondingUnsafeControlAction> allUnsafeControlActions,
      LinkController linkController, List<CausalSafetyConstraint> list) {
    // get rid of redundant entries which should not be stored
    moveSafetyConstraints(list, linkController);
    if(ucaLink != null) {
      linkController.addLink(ObserverValue.UCA_CausalFactor_LINK, ucaLink, id);
      ucaLink = null;
    }
    setHazardLinks(null);
    setUcaDescription(null);
    if (getNote().equals("")) {
      setNote(null);
    }
    constraintText = null;
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