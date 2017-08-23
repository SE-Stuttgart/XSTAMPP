/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.model.causalfactor.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * an object of this type is used to transport dataChanges of a causalFactorsEntry
 * this object can be used to commit changes to a causalFactorEntry or to retrieve
 * informations about an entry from the dataModel
 * 
 * @author Lukas Balzer
 *
 */
public class CausalFactorEntryData {

  final private UUID entryId;
  private String note;
  private boolean noteChanged;
  private String constraint;
  private UUID constraintId;
  private boolean constraintChanged;
  private List<UUID> hazardIds;
  private boolean hazardsChanged;

  /**
   * 
   * @param entryId
   *          {@link #getId()}
   */
  public CausalFactorEntryData(UUID entryId) {
    this.entryId = entryId;
    note = null;
    constraint = null;
    constraintId = null;
    hazardIds = null;
    this.constraintChanged = false;
    this.noteChanged = false;
    this.hazardsChanged = false;
  }

  /**
   * @return the id of the causalFactorEntry which is to be changed
   */
  public UUID getId() {
    return entryId;
  }

  /**
   * @return the note text of this entry
   */
  public String getNote() {
    return note;
  }

  /***
   * setter for the note text {@link #getNote()}
   * 
   * @param note
   *          the new text which is added as an additional note for the entry
   */
  public void setNote(String note) {
    this.note = note;
    this.noteChanged = true;
  }

  public boolean noteChanged() {
    return noteChanged;
  }

  /**
   * @return the text of the safety constraint linked to the corresponding entry
   */
  public String getSafetyConstraint() {
    return constraint;
  }

  public void setConstraint(String constraint) {
    this.constraint = constraint;
    this.constraintChanged = true;
  }

  public UUID getConstraintId() {
    return constraintId;
  }

  public void setConstraintId(UUID constraintId) {
    this.constraintId = constraintId;
  }

  public boolean constraintChanged() {
    return constraintChanged;
  }

  public List<UUID> getHazardIds() {
    return hazardIds;
  }

  public void addHazardId(UUID hazardId) {
    if (this.hazardIds == null) {
      this.hazardIds = new ArrayList<>();
    }
    this.hazardsChanged = this.hazardIds.add(hazardId);
  }

  public void setHazardIds(List<UUID> hazardIds) {
    this.hazardIds = hazardIds;
    this.hazardsChanged = true;
  }

  public boolean hazardsChanged() {
    return hazardsChanged;
  }

  public List<UUID> getScenarioLinks() {
    return null;
  }

  public boolean scenariosChanged() {
    return false;
  }

}
