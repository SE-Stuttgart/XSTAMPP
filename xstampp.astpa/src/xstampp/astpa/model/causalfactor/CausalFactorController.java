/*******************************************************************************
 * Copyright (c) 2013-2016 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package xstampp.astpa.model.causalfactor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import xstampp.astpa.haz.causalfactor.CausalFactorHazardLink;
import xstampp.astpa.model.causalfactor.interfaces.CausalFactorEntryData;
import xstampp.astpa.model.causalfactor.interfaces.ICausalComponent;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorController;
import xstampp.astpa.model.causalfactor.linkEntries.CausalFactorEntry;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.hazacc.HazAccController;
import xstampp.astpa.model.sds.interfaces.ISafetyConstraint;
import xstampp.model.AbstractLtlProvider;

/**
 * Manager class for the causal factors
 * 
 * @author Fabian Toth, Benedikt Markt
 * 
 */
public class CausalFactorController implements ICausalFactorController {

  @XmlElementWrapper(name = "causalFactorHazardLinks")
  @XmlElement(name = "causalFactorHazardLink")
  private List<CausalFactorHazardLink> links;

  @XmlElementWrapper(name = "causalComponents")
  @XmlElement(name = "causalComponent")
  private Map<UUID,CausalCSComponent> causalComponents;

  private List<CausalSafetyConstraint> constraints;
	/**
	 * Constructor of the causal factor controller
	 * 
	 * @author Fabian Toth
	 * 
	 */
	public CausalFactorController() {
		this.links = new ArrayList<>();
		this.causalComponents = new HashMap<>();
	}

	@Override
  public UUID addCausalFactor(IRectangleComponent csComp) {
	  UUID factorId = null;
    if(validateCausalComponent(csComp.getComponentType())){
      if (causalComponents== null) {
        this.causalComponents = new HashMap<>();
      }
      if(!this.causalComponents.containsKey(csComp.getId())){
        this.causalComponents.put(csComp.getId(), new CausalCSComponent());
      }
      factorId = this.causalComponents.get(csComp.getId()).addCausalFactor();
    }
    return factorId;
		
	}
	
	@Override
  public boolean setCausalFactorText(UUID componentId,UUID causalFactorId, String causalFactorText) {
	  CausalFactor factor = internal_getCausalFactor(componentId, causalFactorId);
	  if(factor != null){
	    factor.setText(causalFactorText);
      return true;
	  }
		return false;
	}

  @Override
  public UUID addCausalUCAEntry(UUID componentId, UUID causalFactorId, UUID ucaID) {
    CausalFactor factor = internal_getCausalFactor(componentId, causalFactorId);
    if(factor != null){
      return factor.addUCAEntry(ucaID);
    }
    return null;
  }

  @Override
  public UUID addCausalHazardEntry(UUID componentId, UUID causalFactorId) {
    CausalFactor factor = internal_getCausalFactor(componentId, causalFactorId);
    if(factor != null){
      return factor.addHazardEntry();
    }
    return null;
  }

  @Override
  public boolean changeCausalEntry(UUID componentId, UUID causalFactorId, CausalFactorEntryData entryData) {
    CausalFactor factor = internal_getCausalFactor(componentId, causalFactorId);
    
    if(factor != null){
      boolean result = false;
      CausalFactorEntry entry = (CausalFactorEntry) factor.getEntry(entryData.getId());
      result |= entry.changeCausalEntry(entryData);
      
      //get/create the constraint registered for the entry and set its text
      CausalSafetyConstraint constraint = (CausalSafetyConstraint) factor.getSafetyConstraint();
      if(entryData.constraintChanged())
      {
        if(constraint == null){
          constraint = new CausalSafetyConstraint(entryData.getSafetyConstraint());
          this.constraints.add(constraint);
          result |= entry.setSafetyConstraint(constraint.getId());
        }else{
          result |= constraint.setText(entryData.getSafetyConstraint());
        }
      }
      
      return result;
    }
    return false;
  }

  @Override
  public boolean removeCausalFactor(UUID component, UUID causalFactor) {
    if(causalComponents != null && causalComponents.containsKey(component)){
      this.causalComponents.get(component).removeCausalFactor(causalFactor);
    }
    return false;
  }

  @Override
  public boolean removeCausalEntry(UUID componentId, UUID causalFactorId, UUID entryId) {
    CausalFactor factor = internal_getCausalFactor(componentId, causalFactorId);
    
    if(factor != null){
      return factor.removeEntry(entryId);
    }
    return false;
  }
  
  @Override
  public ICausalComponent getCausalComponent(IRectangleComponent csComp) {
    CausalCSComponent component = null;
    if(csComp != null && validateCausalComponent(csComp.getComponentType())){
      if(causalComponents != null && causalComponents.containsKey(csComp.getId())){
        component = causalComponents.get(csComp.getId());
      }else{
        component = new CausalCSComponent();
      }
      component.setText(csComp.getText());
    }
    return component;
  }

  @Override
  public ISafetyConstraint getCausalSafetyConstraint(UUID id){
    for (CausalSafetyConstraint constraint : constraints) {
      if(constraint.getId().equals(id)){
        return constraint;
      }
    }
    return null;
  }
  
  @Override
  public List<ISafetyConstraint> getAllCausalSafetyConstraints(){
    List<ISafetyConstraint> constraints = new ArrayList<>();
      constraints.addAll(this.constraints);
    return constraints;
  }
  private CausalFactor internal_getCausalFactor(UUID componentId,UUID causalFactorId){
    if(this.causalComponents.containsKey(componentId)){
      return causalComponents.get(componentId).getCausalFactor(causalFactorId);
    }
    return null;
  }

  private boolean validateCausalComponent(ComponentType type){
    switch(type){
    case ACTUATOR:
    case CONTROLLED_PROCESS:
    case CONTROLLER:
    case SENSOR:
      return true;
    default:
      return false;
    
    }
  }

  public void prepareForExport(HazAccController hazAccController, List<IRectangleComponent> children,
      List<AbstractLtlProvider> allRefinedRules, List<ICorrespondingUnsafeControlAction> allUnsafeControlActions) {
    for (IRectangleComponent child : children) {
      if(this.causalComponents.containsKey(child.getId())){
        this.causalComponents.get(child.getId()).prepareForExport(hazAccController, child,allRefinedRules, allUnsafeControlActions, constraints);
      }
    }      
    
  }

  public void prepareForSave(HazAccController hazAccController, List<IRectangleComponent> children,
      List<AbstractLtlProvider> allRefinedRules, List<ICorrespondingUnsafeControlAction> allUnsafeControlActions) {
    Map<UUID,List<UUID>> hazardLinksMap = new HashMap<>();
    if(links != null){
      for(CausalFactorHazardLink link : links){
        UUID factorId = link.getCausalFactorId();
        if(!hazardLinksMap.containsKey(factorId)){
          hazardLinksMap.put(factorId, new ArrayList<UUID>());
        }
        hazardLinksMap.get(factorId).add(link.getHazardId());
      }
      links.clear();
      links = null;
    }
    Set<UUID> removeList = this.causalComponents.keySet();
    for (IRectangleComponent child : children) {
      if(this.causalComponents.containsKey(child.getId())){
        
        removeList.remove(child.getId());
        this.causalComponents.get(child.getId()).prepareForSave(hazardLinksMap,hazAccController, child,allRefinedRules, allUnsafeControlActions, constraints);
      }
    }
    for(UUID id: removeList){
      this.causalComponents.remove(id);
    }
  }
	
}
