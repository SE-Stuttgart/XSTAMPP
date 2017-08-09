/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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
package xstampp.astpa.model.hazacc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import xstampp.astpa.model.ATableModel;
import xstampp.astpa.model.NumberedArrayList;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.preferences.ASTPADefaultConfig;
import xstampp.model.ObserverValue;

/**
 * Controller-class for working with accidents and hazards and links between
 * them.
 *
 * @author Fabian Toth
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
public class HazAccController extends Observable implements IHazAccController{

  @XmlElementWrapper(name = "accidents")
  @XmlElement(name = "accident")
  private NumberedArrayList<Accident> accidents;

  @XmlElementWrapper(name = "hazards")
  @XmlElement(name = "hazard")
  private NumberedArrayList<Hazard> hazards;

  @XmlElementWrapper(name = "links")
  @XmlElement(name = "link")
  private List<HazAccLink> links;
  
  @XmlAttribute(name = "useSeverity")
  private Boolean useSeverity;

  /**
   * Constructor for the controller
   *
   * @author Fabian Toth
   *
   */
  public HazAccController() {
  }

  /* (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#addAccident(java.lang.String, java.lang.String)
   */
  @Override
  public UUID addAccident(String title, String description) {
    Accident newAccident = new Accident(title, description, 0);
    this.getAccidents().add(newAccident);
    return newAccident.getId();
  }

  /* (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#removeAccident(java.util.UUID)
   */
  @Override
  public boolean removeAccident(UUID id) {
    ITableModel accident = this.getAccident(id);
    int index = this.getAccidents().indexOf(accident);
    this.getAccidents().remove(index);
    for (; index < this.getAccidents().size(); index++) {
      this.getAccidents().get(index).setNumber(index + 1);
    }
    return true;
  }

  /* (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#moveEntry(boolean, java.util.UUID, xstampp.model.ObserverValue)
   */
  @Override
  public boolean moveEntry(boolean moveUp, UUID id, ObserverValue value) {
    if (value.equals(ObserverValue.ACCIDENT)) {
      return ATableModel.move(moveUp, id, getAccidents());
    } else if (value.equals(ObserverValue.HAZARD)) {
      return ATableModel.move(moveUp, id, getHazards());
    }
    return true;
  }

  /* (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#getAccident(java.util.UUID)
   */
  @Override
  public ITableModel getAccident(UUID accidentID) {
    for (ITableModel accident : this.getAccidents()) {
      if (accident.getId().equals(accidentID)) {
        return accident;
      }
    }
    return null;
  }

  /* (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#getAllAccidents()
   */
  @Override
  public List<ITableModel> getAllAccidents() {
    List<ITableModel> result = new ArrayList<>();
    for (Accident accident : this.getAccidents()) {
      result.add(accident);
    }
    return result;
  }

  /* (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#getLinkedHazards(java.util.UUID)
   */
  @Override
  public List<ITableModel> getLinkedHazards(UUID accidentId) {
    List<ITableModel> result = new ArrayList<>();
    for (HazAccLink link : this.links) {
      if (link.containsId(accidentId)) {
        result.add(this.getHazard(link.getHazardId()));
      }
    }
    Collections.sort(result);
    return result;
  }

  /* (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#addHazard(java.lang.String, java.lang.String)
   */
  @Override
  public UUID addHazard(String title, String description) {
    Hazard newHazard = new Hazard(title, description, 0);
    this.getHazards().add(newHazard);
    return newHazard.getId();
  }

  /* (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#removeHazard(java.util.UUID)
   */
  @Override
  public boolean removeHazard(UUID id) {
    ITableModel hazard = this.getHazard(id);
    int index = this.getHazards().indexOf(hazard);
    this.getHazards().remove(index);
    for (; index < this.getHazards().size(); index++) {
      this.getHazards().get(index).setNumber(index + 1);
    }
    return true;
  }

  /* (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#addLink(java.util.UUID, java.util.UUID)
   */
  @Override
  public boolean addLink(UUID accidentId, UUID hazardId) {
    return this.links.add(new HazAccLink(accidentId, hazardId));
  }

  /* (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#deleteLink(java.util.UUID, java.util.UUID)
   */
  @Override
  public boolean deleteLink(UUID accidentId, UUID hazardId) {
    return this.links.remove(new HazAccLink(accidentId, hazardId));
  }

  /* (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#getAllHazards()
   */
  @Override
  public List<ITableModel> getAllHazards() {
    List<ITableModel> result = new ArrayList<>();
    for (Hazard hazard : this.getHazards()) {
      result.add(hazard);
    }
    return result;
  }

  /* (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#getLinkedAccidents(java.util.UUID)
   */
  @Override
  public List<ITableModel> getLinkedAccidents(UUID hazardId) {
    List<ITableModel> result = new ArrayList<>();
    for (HazAccLink link : this.links) {
      if (link.containsId(hazardId)) {
        result.add(this.getAccident(link.getAccidentId()));
      }
    }
    Collections.sort(result);
    return result;
  }

  /* (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#getHazard(java.util.UUID)
   */
  @Override
  public Hazard getHazard(UUID hazardId) {
    for (Hazard hazard : this.getHazards()) {
      if (hazard.getId().equals(hazardId)) {
        return hazard;
      }
    }
    return null;
  }

  /* (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#prepareForExport(xstampp.astpa.model.linking.LinkController)
   */
  @Override
  public void prepareForExport(LinkController linkController) {
    for (Accident accident : this.getAccidents()) {
      String linkString = ""; //$NON-NLS-1$
      for (UUID id : linkController.getLinksFor(ObserverValue.HAZ_ACC_LINK, accident.getId())) {
        linkString += getHazard(id).getNumber() + ", "; //$NON-NLS-1$
      }
      if(linkString.length() >  2) {
        accident.setLinks(linkString.substring(0, linkString.length() - 2));
      }
    }
    for (Hazard hazard : this.getHazards()) {
      String linkString = ""; //$NON-NLS-1$
      for (UUID id : linkController.getLinksFor(ObserverValue.HAZ_ACC_LINK, hazard.getId())) {
        linkString += getAccident(id).getNumber() + ", "; //$NON-NLS-1$
      }
      if(linkString.length() >  2) {
        hazard.setLinks(linkString.substring(0, linkString.length() - 2));
      }
    }
  }

  /* (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#prepareForSave(xstampp.astpa.model.linking.LinkController)
   */
  @Override
  public boolean prepareForSave(LinkController linkController) {
    boolean isUsed = false;
    for (Accident accident : this.getAccidents()) {
      accident.setLinks(null);
    }
    if(accidents != null && accidents.isEmpty()) {
      accidents = null;
    }
    isUsed |= accidents != null;
    for (Hazard hazard : this.getHazards()) {
      hazard.setLinks(null);
    }
    if(hazards != null && hazards.isEmpty()) {
      hazards = null;
    }
    isUsed |= hazards != null;
    if(this.links != null) {
      for(HazAccLink link: this.links) {
        linkController.addLink(ObserverValue.HAZ_ACC_LINK, link.getAccidentId(), link.getHazardId());
      }
      links = null;
    }
    return isUsed;
  }

  /**
   * Deletes all links that are associated to this id
   *
   * @param id
   *          the id of the hazard or accident
   *
   * @author Fabian Toth
   */
  private void deleteAllLinks(UUID id) {
    List<HazAccLink> toDelete = new ArrayList<>();
    if(this.links != null) {
      for (HazAccLink link : this.links) {
        if (link.containsId(id)) {
          toDelete.add(link);
        }
      }
    }
    this.links.removeAll(toDelete);
  }

  /* (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#getAllHazAccLinks()
   */
  @Override
  public List<HazAccLink> getAllHazAccLinks() {
    return new ArrayList<>(this.links);
  }

  /* (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#isUseSeverity()
   */
  @Override
  public boolean isUseSeverity() {
    if(useSeverity != null) {
      return useSeverity;
    }
    return ASTPADefaultConfig.getInstance().USE_SEVERITY_ANALYSIS;
  }

  /* (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#setUseSeverity(boolean)
   */
  @Override
  public boolean setUseSeverity(boolean useSeverity) {
    if(this.useSeverity == null || useSeverity != this.useSeverity) {
      this.useSeverity = useSeverity;
      return true;
    }
    return false;
  }
  
  private List<Accident> getAccidents() {
    if(this.accidents == null) {
      this.accidents = new NumberedArrayList<>();
    }
    return accidents;
  }
  
  private List<Hazard> getHazards() {
    if(this.hazards == null) {
      this.hazards = new NumberedArrayList<>();
    }
    return hazards;
  }
}
