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
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import xstampp.astpa.haz.causalfactor.CausalFactorHazardLink;
import xstampp.astpa.model.causalfactor.interfaces.CausalFactorEntryData;
import xstampp.astpa.model.causalfactor.interfaces.ICausalComponent;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorController;
import xstampp.astpa.model.causalfactor.linkEntries.CausalFactorEntry;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.controlstructure.components.Component;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.hazacc.HazAccController;
import xstampp.model.AbstractLTLProvider;

/**
 * Manager class for the causal factors
 * 
 * @author Fabian Toth, Benedikt Markt
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
public class CausalFactorController implements ICausalFactorController {

  @XmlElementWrapper(name = "causalFactorHazardLinks")
  @XmlElement(name = "causalFactorHazardLink")
  private List<CausalFactorHazardLink> links;

  @XmlElementWrapper(name = "causalComponents")
  @XmlElement(name = "causalComponent")
  private Map<UUID,CausalCSComponent> causalComponents;

	/**
	 * Constructor of the causal factor controller
	 * 
	 * @author Fabian Toth
	 * 
	 */
	public CausalFactorController() {
		this.links = new ArrayList<>();
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
	public List<UUID> getLinkedUCAList(){
	  List<UUID> list = new ArrayList<>();
	  if(causalComponents != null){
  	  for(CausalCSComponent comp: causalComponents.values()){
  	    list.addAll(comp.getLinkedUCAList());
  	  }
	  }
	  return list;
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
      CausalFactorEntry entry = (CausalFactorEntry) factor.getEntry(entryData.getId());
      if(entry != null){
        return entry.changeCausalEntry(entryData);
      }
    }
    return false;
  }

  @Override
  public boolean removeCausalFactor(UUID componentId, UUID causalFactor) {
    if(causalComponents != null){
      if(componentId == null){
        for(CausalCSComponent comp: causalComponents.values()){
          if(comp.removeCausalFactor(causalFactor)){
            return true;
          }
        } 
      }else if(causalComponents.containsKey(componentId)){
        CausalCSComponent comp = this.causalComponents.get(componentId);
        return comp.removeCausalFactor(causalFactor);
      }
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
      if(causalComponents == null){
        causalComponents = new HashMap<>();
      }
      if(!causalComponents.containsKey(csComp.getId())){
        causalComponents.put(csComp.getId(), new CausalCSComponent());
      }
      
      component = causalComponents.get(csComp.getId());
      component.setText(csComp.getText());
      component.setId(csComp.getId());
      component.setType(csComp.getComponentType());
    }
    return component;
  }
  
  private CausalFactor internal_getCausalFactor(UUID componentId,UUID causalFactorId){
    if(causalComponents != null && this.causalComponents.containsKey(componentId)){
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
      List<AbstractLTLProvider> allRefinedRules, List<ICorrespondingUnsafeControlAction> allUnsafeControlActions) {

      for (IRectangleComponent child : children) {
        if(getCausalComponent(child) != null){
          this.causalComponents.get(child.getId()).prepareForExport(hazAccController, child,allRefinedRules, allUnsafeControlActions);
        }
      }
  }

  public void prepareForSave(HazAccController hazAccController, List<Component> list,
      List<AbstractLTLProvider> allRefinedRules, List<ICorrespondingUnsafeControlAction> allUnsafeControlActions) {
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
    ArrayList<UUID> removeList = new ArrayList<>();
    if(causalComponents != null){
      removeList.addAll(causalComponents.keySet());
    }
    for (Component child : list) {
      removeList.remove(child.getId());
      if(getCausalComponent(child) != null){
        this.causalComponents.get(child.getId()).prepareForSave(hazardLinksMap,hazAccController, child,allRefinedRules, allUnsafeControlActions);
      }
    }
    if(causalComponents != null){
      for(UUID id: removeList){
        this.causalComponents.remove(id);
      }
      if(this.causalComponents.isEmpty()){
        causalComponents = null;
      }
    }
  }
	
}
