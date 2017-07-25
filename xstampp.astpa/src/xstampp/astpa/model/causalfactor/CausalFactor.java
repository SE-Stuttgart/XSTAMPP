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

package xstampp.astpa.model.causalfactor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import xstampp.astpa.model.causalfactor.interfaces.ICausalFactor;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorEntry;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.hazacc.IHazAccController;
import xstampp.astpa.model.interfaces.IEntryWithNameId;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.AbstractLTLProvider;

/**
 * A causal factor
 * 
 * @author Fabian
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
public class CausalFactor implements ICausalFactor, IEntryWithNameId {

  @XmlElement(name = "id")
  private UUID id;

  @XmlElement(name = "text")
  private String text;

  @XmlElement(name = "safetyConstraint")
  private CausalSafetyConstraint safetyConstraint;

  @XmlElement(name = "note")
  private String note;

  @XmlElement(name = "hazardLinks")
  private String links;

  @XmlElementWrapper(name = "causalEntries")
  @XmlElement(name = "causalEntry")
  private List<CausalFactorEntry> entries;

  @XmlElement
  private UUID constraintId;

  /**
   * Constructor of a causal factor
   * 
   * @author Fabian Toth
   * 
   * @param text
   *          the text of the new causal factor
   */
  public CausalFactor(String text) {
    this.id = UUID.randomUUID();
    this.text = text;
  }

  /**
   * Empty constructor used for JAXB. Do not use it!
   * 
   * @author Fabian Toth
   */
  public CausalFactor() {
    // empty constructor for JAXB
  }

  @Override
  public UUID getId() {
    return this.id;
  }

  /**
   * @param id
   *          the id to set
   */
  void setId(UUID id) {
    this.id = id;
  }

  @Override
  public String getText() {
    return this.text;
  }

  /**
   * @param text
   *          the text to set
   */
  void setText(String text) {
    this.text = text;
  }

  @Override
  public ITableModel getSafetyConstraint() {
    return this.safetyConstraint;
  }

  /**
   * @param safetyConstraint
   *          the safetyConstraint to set
   */
  void setSafetyConstraint(CausalSafetyConstraint safetyConstraint) {
    this.safetyConstraint = safetyConstraint;
  }

  @Override
  public String getNote() {
    return this.note;
  }

  /**
   * @param note
   *          the note to set
   */
  public void setNote(String note) {
    this.note = note;
  }

  /**
   * @return the links
   */
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

  public UUID addUCAEntry(UUID ucaId) {
    CausalFactorEntry entry = new CausalFactorEntry(ucaId);
    return addUCAEntry(entry);
  }

  public UUID addUCAEntry(ICausalFactorEntry entry) {
    CausalFactorEntry addEntry;
    if (!(entry instanceof CausalFactorEntry)) {
      addEntry = new CausalFactorEntry(entry.getUcaLink(), entry.getId());

      addEntry.setConstraintId(entry.getConstraintId());
      addEntry.setScenarioLinks(entry.getScenarioLinks());
      addEntry.setNote(entry.getNote());
    } else {
      addEntry = (CausalFactorEntry) entry;
    }
    if (this.entries == null) {
      this.entries = new ArrayList<>();
    }
    if (!entries.contains(addEntry)) {
      this.entries.add(addEntry);
      return entry.getId();
    }
    return null;
  }

  public UUID addHazardEntry() {
    CausalFactorEntry entry = int_addHazardEntry();
    if (entry == null) {
      return null;
    }
    return entry.getId();
  }

  private CausalFactorEntry int_addHazardEntry() {
    if (this.entries == null) {
      this.entries = new ArrayList<>();
    }
    CausalFactorEntry entry;
    entry = new CausalFactorEntry();
    this.entries.add(entry);
    return entry;
  }

  public boolean removeEntry(UUID entryId) {
    for (int i = 0; entries != null && i < entries.size(); i++) {
      if (entries.get(i).getId().equals(entryId)) {
        return entries.remove(i) != null;
      }
    }
    return false;
  }

  public ICausalFactorEntry getEntry(UUID entryId) {
    for (int i = 0; entries != null && i < entries.size(); i++) {
      if (entries.get(i).getId().equals(entryId)) {
        return entries.get(i);
      }
    }
    return null;
  }

  @Override
  public List<ICausalFactorEntry> getAllEntries() {
    List<ICausalFactorEntry> result = new ArrayList<>();
    for (int i = 0; entries != null && i < entries.size(); i++) {
      result.add(entries.get(i));
    }
    return result;
  }

  public void prepareForExport(IHazAccController hazAccController,
      List<AbstractLTLProvider> allRefinedRules,
      List<ICorrespondingUnsafeControlAction> allUnsafeControlActions,
      List<CausalSafetyConstraint> safetyConstraints) {
    for (int i = 0; entries != null && i < entries.size(); i++) {
      entries.get(i).prepareForExport(hazAccController, allRefinedRules, allUnsafeControlActions,
          safetyConstraints);
    }
  }

  public void prepareForSave(Map<UUID, List<UUID>> hazardLinksMap,
      IHazAccController hazAccController, List<AbstractLTLProvider> allRefinedRules,
      List<ICorrespondingUnsafeControlAction> allUnsafeControlActions,
      List<CausalSafetyConstraint> safetyConstraints) {
    if (hazardLinksMap.containsKey(getId()) || note != null || safetyConstraint != null) {
      CausalFactorEntry entry = int_addHazardEntry();
      if (entry != null) {
        safetyConstraints.add(safetyConstraint);
        entry.setConstraintId(getSafetyConstraint().getId());
        entry.setHazardIds(hazardLinksMap.get(getId()));
        entry.setNote(getNote());
      }
      safetyConstraint = null;
      note = null;
    }

    if (entries != null) {
      for (CausalFactorEntry entry : entries) {
        entry.prepareForSave(hazAccController, allUnsafeControlActions);
      }
    }
  }

  public List<UUID> getLinkedUCAList() {
    List<UUID> list = new ArrayList<>();
    if (entries != null) {
      for (CausalFactorEntry entry : entries) {
        if (entry.getUcaLink() != null) {
          list.add(entry.getUcaLink());
        }
      }
    }
    return list;
  }

  void moveSafetyConstraints(List<CausalSafetyConstraint> list) {
    if (safetyConstraint != null) {
      CausalSafetyConstraint newConstraint = new CausalSafetyConstraint(
          safetyConstraint.getDescription());
      safetyConstraint = null;
      constraintId = safetyConstraint.getId();
      list.add(newConstraint);
    }
    if (entries != null) {
      for (CausalFactorEntry factorEntry : entries) {
        factorEntry.moveSafetyConstraints(list);
      }
    }
  }
}
