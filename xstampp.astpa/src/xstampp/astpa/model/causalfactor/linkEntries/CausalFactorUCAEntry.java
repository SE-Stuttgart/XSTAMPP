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

import xstampp.astpa.model.causalfactor.CausalSafetyConstraint;
import xstampp.astpa.model.causalfactor.interfaces.CausalFactorEntryData;
import xstampp.astpa.model.causalfactor.interfaces.CausalFactorUCAEntryData;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorEntry;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.hazacc.HazAccController;
import xstampp.model.AbstractLtlProvider;

public class CausalFactorUCAEntry extends CausalFactorEntry implements ICausalFactorEntry{
  private String ucaDescription;
  private UUID ucaLink;
  private String scenarioString;
  private List<UUID> scenarioLinks;
  
  public CausalFactorUCAEntry(UUID ucaLink) {
    super();
    this.ucaLink = ucaLink;
  }
  /**
   * @return the ucaLink
   */
  public UUID getUcaLink() {
    return ucaLink;
  }
  /**
   * @param ucaLink the ucaLink to set
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
   * @return the scenarioLinks
   */
  public List<UUID> getScenarioLinks() {
    return scenarioLinks;
  }
  /**
   * @param scenarioLinks the scenarioLinks to set
   */
  public boolean setScenarioLinks(List<UUID> scenarioLinks) {
    this.scenarioLinks = scenarioLinks;
    return true;
  }
  
  public String getScenarioString() {
    return scenarioString;
  }
  public void setScenarioString(String scenarioString) {
    this.scenarioString = scenarioString;
  }
  @Override
  public boolean setHazardIds(List<UUID> hazardIds) {
    return false;
  }
  
  @Override
  public boolean changeCausalEntry(CausalFactorEntryData entryData) {
    boolean result = false;
    if(entryData.noteChanged()){
      result |= setNote(entryData.getNote());
    }
    if(entryData instanceof CausalFactorUCAEntryData){
      if(((CausalFactorUCAEntryData) entryData).scenariosChanged()){
        result |= setScenarioLinks(((CausalFactorUCAEntryData) entryData).getScenarioLinks());
      }
    }
    return result;
  }
  
  @Override
  public void prepareForExport(HazAccController hazAccController, List<AbstractLtlProvider> allRefinedRules,
      List<ICorrespondingUnsafeControlAction> allUnsafeControlActions, List<CausalSafetyConstraint> constraints) {
     //fetch the constraint Text form the constraint list of the causalFactorcontroller
    for (CausalSafetyConstraint constraint : constraints) {
      if(constraint.getId().equals(getSafetyConstraintId())){
        setConstraintText(constraint.getText());
        break;
      }
    }
    
    for (ICorrespondingUnsafeControlAction uca : allUnsafeControlActions) {
      if(uca.getId().equals(ucaLink)){
        ucaDescription = uca.getDescription();
        setHazardLinks(uca.getLinks());
      }
    }

    scenarioString = new String();
    for (AbstractLtlProvider ltlProvider : allRefinedRules) {
      if(getScenarioLinks().contains(ltlProvider.getRuleId())){
        if(!scenarioString.isEmpty()){
          scenarioString += ",";//$NON-NLS-1$
        }
        scenarioString += "S-" + ltlProvider.getNumber();//$NON-NLS-1$
      }
    }
  }
  
  @Override
  public boolean prepareForSave(HazAccController hazAccController, List<ICorrespondingUnsafeControlAction> allUca) {
    
    for (ICorrespondingUnsafeControlAction uca : allUca) {
      if(uca.getId().equals(ucaLink)){
        setConstraintText(null);
        setHazardLinks(null);
        ucaDescription = null;
        scenarioString = null;
        return true;
      }
    }
    return false;
  }
}
