/*******************************************************************************
 * Copyright (c) 2013, 2015 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

import xstampp.astpa.model.causalfactor.interfaces.ICausalFactor;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorEntry;
import xstampp.astpa.model.causalfactor.linkEntries.CausalFactorEntry;
import xstampp.astpa.model.causalfactor.linkEntries.CausalFactorEntryContainer;
import xstampp.astpa.model.causalfactor.linkEntries.CausalFactorUCAEntry;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.hazacc.HazAccController;
import xstampp.astpa.model.sds.interfaces.ISafetyConstraint;
import xstampp.model.AbstractLtlProvider;

/**
 * A causal factor
 * 
 * @author Fabian
 * 
 */
public class CausalFactor implements ICausalFactor {

	private UUID id;
	private String text;
	private CausalSafetyConstraint safetyConstraint;
	private String note;
	private String links;
  private List<CausalFactorEntry> entries;
	/**
	 * Constructor of a causal factor
	 * 
	 * @author Fabian Toth
	 * 
	 * @param text
	 *            the text of the new causal factor
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
	 *            the id to set
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	@Override
	public String getText() {
		return this.text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public ISafetyConstraint getSafetyConstraint() {
		return this.safetyConstraint;
	}

	/**
	 * @param safetyConstraint
	 *            the safetyConstraint to set
	 */
	public void setSafetyConstraint(CausalSafetyConstraint safetyConstraint) {
		this.safetyConstraint = safetyConstraint;
	}

	@Override
	public String getNote() {
		return this.note;
	}

	/**
	 * @param note
	 *            the note to set
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
	 *            the links to set
	 */
	public void setLinks(String links) {
		this.links = links;
	}
  
	public UUID addUCAEntry(UUID ucaId){
    if(this.entries == null){
      this.entries = new ArrayList<>();
    }
    CausalFactorUCAEntry entry = new CausalFactorUCAEntry(ucaId);
    this.entries.add(entry);
    return entry.getId();
  }
	public UUID addHazardEntry(){
	  CausalFactorEntry entry = int_addHazardEntry();
	  if(entry == null){
	    return null;
	  }
    return entry.getId();
  }
	
	private CausalFactorEntry int_addHazardEntry(){
    if(this.entries == null){
      this.entries = new ArrayList<>();
    }
    CausalFactorEntry entry;
    entry = new CausalFactorEntry();
    this.entries.add(entry);
    return entry;
  }
	
	public boolean removeEntry(UUID entryId){
	  for (int i= 0;entries != null && i < entries.size(); i++) {
      if(entries.get(i).getId().equals(entryId)){
        return entries.remove(i) != null;
      }
    }
	  return false;
	}
	
	public ICausalFactorEntry getEntry(UUID entryId){
	  for (int i= 0;entries != null && i < entries.size(); i++) {
      if(entries.get(i).getId().equals(entryId)){
        return entries.get(i);
      }
    }
    return null;
	}

  @Override
  public List<ICausalFactorEntry> getAllEntries() {
    List<ICausalFactorEntry> result = new ArrayList<>();
    for (int i= 0;entries != null && i < entries.size(); i++) {
      result.add(new CausalFactorEntryContainer(entries.get(i)));      
    }
    return result;
  }
  
  public void prepareForExport(HazAccController hazAccController,
      List<AbstractLtlProvider> allRefinedRules,
      List<ICorrespondingUnsafeControlAction> allUnsafeControlActions){
    for (CausalFactorEntry entry : entries) {
      entry.prepareForExport(hazAccController, allRefinedRules, allUnsafeControlActions);
    }
  }

  public void prepareForSave(Map<UUID, List<UUID>> hazardLinksMap,
                             HazAccController hazAccController,
                             List<AbstractLtlProvider> allRefinedRules,
                             List<ICorrespondingUnsafeControlAction> allUnsafeControlActions) {
    if(hazardLinksMap.containsKey(getId())){
      CausalFactorEntry entry = int_addHazardEntry();
      if(entry != null){
        entry.setConstraintText(getSafetyConstraint().getText());
        entry.setHazardIds(hazardLinksMap.get(getId()));
        entry.setNote(getNote());
      }
    }
    for (CausalFactorEntry entry : entries) {
      entry.prepareForSave(hazAccController,allUnsafeControlActions);
    }
  }
  public List<UUID> getLinkedUCAList(){
    List<UUID> list = new ArrayList<>();
    if(entries != null){
      for(CausalFactorEntry entry : entries){
        if(entry.getUcaLink() != null){
          list.add(entry.getUcaLink());
        }
      }
    }
    return list;
  }
}
