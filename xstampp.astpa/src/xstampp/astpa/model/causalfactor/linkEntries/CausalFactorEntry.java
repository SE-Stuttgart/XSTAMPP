/*******************************************************************************
 * Copyright (c) 2013, 2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.model.causalfactor.linkEntries;

import java.util.List;
import java.util.UUID;

import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.model.causalfactor.CausalSafetyConstraint;
import xstampp.astpa.model.causalfactor.interfaces.CausalFactorEntryData;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorEntry;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.hazacc.HazAccController;
import xstampp.astpa.model.sds.interfaces.ISafetyConstraint;
import xstampp.model.AbstractLtlProvider;

public class CausalFactorEntry  implements ICausalFactorEntry{

  private String hazardLinks;
  private String constraintText;
  private UUID constraintId;
  private List<UUID> hazardIds;
  private String note;
  private UUID id;

  public CausalFactorEntry() {
    id = UUID.randomUUID();
  }

  public CausalFactorEntry(CausalFactorEntryData data){
    id = data.getId();
  }
  /**
   * @return the hazardLinks
   */
  public String getHazardLinks() {
    return hazardLinks;
  }

  /**
   * @param hazardLinks the hazardLinks to set
   */
  public void setHazardLinks(String hazardLinks) {
    this.hazardLinks = hazardLinks;
  }

  /**
   * @return the correspondingSafetyConstraint
   */
  public UUID getSafetyConstraintId() {
    return constraintId;
  }

  public boolean setSafetyConstraint(UUID uuid) {
    if(!uuid.equals(constraintId)){
      constraintId = uuid;
      return true;
    }return false;
  }
  public String getConstraintText() {
    return constraintText;
  }

  public void setConstraintText(String constraintText) {
    this.constraintText = constraintText;
  }

  /**
   * @return the hazardIds
   */
  public List<UUID> getHazardIds() {
    return hazardIds;
  }

  /**
   * @param hazardIds the hazardIds to set
   */
  public boolean setHazardIds(List<UUID> hazardIds) {
    this.hazardIds = hazardIds;
    return true;
  }

  /**
   * @return the note
   */
  public String getNote() {
    return note;
  }

  /**
   * @param note the note to set
   */
  public boolean setNote(String note) {
    this.note = note;
    return true;
  }
  
  public UUID getId() {
    return id;
  }

  public boolean changeCausalEntry(CausalFactorEntryData entryData) {
    boolean result = false;
    if(entryData.noteChanged()){
      result |= setNote(entryData.getNote());
    }
   
    if(entryData.hazardsChanged()){
      result |= setHazardIds(entryData.getHazardIds());
    }
    
    return result;
  }

  @Override
  public List<UUID> getScenarioLinks() {
    // The hazard based entry can't be linked to a scenario
    return null;
  }

  @Override
  public UUID getUcaLink() {
    // hazard based entries have no relation to a uca
    return null;
  }
  
  public void prepareForExport(HazAccController hazAccController,
      List<AbstractLtlProvider> allRefinedRules,
      List<ICorrespondingUnsafeControlAction> allUnsafeControlActions,
      List<CausalSafetyConstraint> constraints){
    //fetch the constraint Text form the constraint list of the causalFactorcontroller
    for (CausalSafetyConstraint constraint : constraints) {
      if(constraint.getId().equals(constraintId)){
        setConstraintText(constraint.getText());
        break;
      }
    }
    
    //create the hazard Link String by adding a label for each hazard
    hazardLinks = new String();
    for (ITableModel hazard : hazAccController.getAllHazards()) {
      if(this.hazardIds.contains(hazard.getId())){
        if(!hazardLinks.isEmpty()){
          hazardLinks += ",";//$NON-NLS-1$
        }
        hazardLinks += "H-" + hazard.getNumber();//$NON-NLS-1$
      }
    }
    
  }

  
  public boolean prepareForSave(HazAccController hazAccController,
                                List<ICorrespondingUnsafeControlAction> allUnsafeControlActions) {
    setConstraintText(null);
    setHazardLinks(null);
    UUID[] hazIds= (UUID[]) this.hazardIds.toArray();
    for (int i = 0; i < hazIds.length; i++) {
      if(hazAccController.getHazard(hazIds[i]) == null){
        this.hazardIds.remove(hazIds[i]);
      }
    }
    return true;
  }
}