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
package xstampp.astpa.model.causalfactor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import xstampp.astpa.model.causalfactor.interfaces.CausalFactorEntryData;
import xstampp.astpa.model.causalfactor.interfaces.ICausalComponent;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactor;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.hazacc.HazAccController;
import xstampp.model.AbstractLTLProvider;

@XmlRootElement(name="causalComponent")
@XmlAccessorType(XmlAccessType.NONE)
public class CausalCSComponent implements ICausalComponent{
  
  @XmlElement(name="title")
  private String text;
  
  @XmlElementWrapper(name="causalFactors")
  @XmlElement(name="factor")
  private List<CausalFactor> factors;
  
  private UUID id;
  
  @XmlElement(name="type")
  private ComponentType type;
  
  
  @Override
  public String getText() {
    return text;
  }
  
  public void setText(String text) {
    this.text = text;
  }
  
  public void setId(UUID id){
    this.id = id;
  }
  @Override
  public UUID getId() {
    return id;
  }
  
  public List<UUID> getLinkedUCAList(UUID factorId){
    List<UUID> list = new ArrayList<>();
    for(CausalFactor factor : internal_getFactors()){
      if(factor.getId().equals(factorId))
      list.addAll(factor.getLinkedUCAList());
    }
    return list;
  }
  
  /**
   * @param factors the factors to set
   */
  public UUID addCausalFactor() {
    CausalFactor factor = new CausalFactor(new String());    
    return intern_addCausalFactor(factor);
  }
  
  private  UUID intern_addCausalFactor(CausalFactor factor) {
    if(this.factors == null){
      this.factors = new ArrayList<>();
    }
    if(this.factors.add(factor)){
      return factor.getId();
    }
    
    return null;
  }
  
  public boolean removeCausalFactor(UUID id){
    for (CausalFactor factor : internal_getFactors()) {
      if(factor.getId().equals(id)){
        return factors.remove(factor);
      }
    }
    return false;
  }
  public CausalFactor getCausalFactor(UUID factorId){
    for (CausalFactor factor : internal_getFactors()) {
      if(factor.getId().equals(factorId)){
        return factor;
      }
    }
    return null;
  }
  
  @Override
  public List<ICausalFactor> getCausalFactors() {
    List<ICausalFactor> factors = new ArrayList<>();
    for (CausalFactor causalFactor : internal_getFactors()) {
      factors.add(causalFactor);
    }
    return factors;
  }
  
  private List<CausalFactor>  internal_getFactors(){
    if(this.factors == null){
      return new ArrayList<>();
    }
    return new ArrayList<>(factors);
  }
  
  @Override
  public ComponentType getComponentType() {
    return type;
  }

  public void setType(ComponentType type) {
    this.type = type;
  }

  public boolean changeCausalEntry(UUID causalFactorId, CausalFactorEntryData entryData) {
    CausalFactor factor = getCausalFactor(causalFactorId);
    
    if(factor != null){
      return factor.changeCausalEntry(entryData) != null;
    }
    return false;
  }
  
  public void prepareForExport(HazAccController hazAccController,
                               IRectangleComponent child, 
                               List<AbstractLTLProvider> allRefinedRules,
                               List<ICorrespondingUnsafeControlAction> allUnsafeControlActions) {
    this.text = child.getText();
    for (CausalFactor causalFactor : internal_getFactors()) {
      causalFactor.prepareForExport(hazAccController, allRefinedRules, allUnsafeControlActions);
    }
  }
  
  public void prepareForSave(Map<UUID, List<UUID>> hazardLinksMap,
                             HazAccController hazAccController,
                             ICausalComponent child,
                             List<AbstractLTLProvider> allRefinedRules,
                             List<ICorrespondingUnsafeControlAction> allUnsafeControlActions) {
    List<ICausalFactor> causalFactors = child.getCausalFactors();
    if(causalFactors != null){
      for (ICausalFactor legacyEntry : causalFactors) {
        intern_addCausalFactor((CausalFactor) legacyEntry);
        
      }
    }
    for (CausalFactor causalFactor : internal_getFactors()) {
      causalFactor.prepareForSave(hazardLinksMap,hazAccController, allRefinedRules,allUnsafeControlActions);
    }
  }

  void moveSafetyConstraints(List<CausalSafetyConstraint> list) {
    for (CausalFactor factor : factors) {
      factor.moveSafetyConstraints(list);
    }
  }
}
