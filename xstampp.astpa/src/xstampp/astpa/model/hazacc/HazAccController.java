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
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.astpa.model.sds.ISDSController;
import xstampp.astpa.preferences.ASTPADefaultConfig;
import xstampp.model.NumberedArrayList;
import xstampp.model.ObserverValue;

/**
 * Controller-class for working with accidents and hazards and links between
 * them.
 *
 * @author Fabian Toth
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
public class HazAccController extends Observable implements IHazAccController {

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

  private LinkController linkController;

  /**
   * Constructor for the controller
   *
   * @author Fabian Toth
   *
   */
  public HazAccController() {
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#addAccident(java.lang.String,
   * java.lang.String)
   */
  @Override
  public UUID addAccident(String title, String description) {
    Accident newAccident = new Accident(title, description);
    return addAccident(newAccident);
  }

  UUID addAccident(ITableModel model) {
    Accident newAccident = new Accident(model);
    this.getAccidents().add(newAccident);
    setChanged();
    notifyObservers(new UndoAddAccident(this, newAccident, getLinkController()));
    return newAccident.getId();
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#removeAccident(java.util.UUID)
   */
  @Override
  public boolean removeAccident(UUID accidentId) {
    ITableModel accident = this.getAccident(accidentId);
    if (this.getAccidents().remove(accident)) {
      setChanged();
      notifyObservers(new UndoRemoveAccidents(this, accident, getLinkController()));
      return true;
    }
    return false;
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#moveEntry(boolean, java.util.UUID,
   * xstampp.model.ObserverValue)
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

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#getAccident(java.util.UUID)
   */
  @Override
  public ITableModel getAccident(UUID accidentID) {
    return getAccidents().stream().filter(x -> x.getId().equals(accidentID)).findFirst()
        .orElse(null);
  }

  /*
   * (non-Javadoc)
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

  /*
   * (non-Javadoc)
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

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#addHazard(java.lang.String, java.lang.String)
   */
  @Override
  public UUID addHazard(String title, String description) {
    return addHazard(new Hazard(title, description));
  }

  UUID addHazard(ITableModel model) {
    Hazard newHazard = new Hazard(model);
    if(this.getHazards().add(newHazard)) {
      setChanged();
      notifyObservers(new UndoAddHazard(this, newHazard, getLinkController()));
      return newHazard.getId();
    }
    return null;
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#removeHazard(java.util.UUID)
   */
  @Override
  public boolean removeHazard(UUID id) {
    Hazard hazard = this.getHazard(id);
    if(this.getHazards().remove(hazard)) {
      setChanged();
      notifyObservers(new UndoRemoveHazard(this, hazard, getLinkController()));
      return true;
    }
    return false;
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#addLink(java.util.UUID, java.util.UUID)
   */
  @Override
  public boolean addLink(UUID accidentId, UUID hazardId) {
    return this.links.add(new HazAccLink(accidentId, hazardId));
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#deleteLink(java.util.UUID, java.util.UUID)
   */
  @Override
  public boolean deleteLink(UUID accidentId, UUID hazardId) {
    return this.links.remove(new HazAccLink(accidentId, hazardId));
  }

  /*
   * (non-Javadoc)
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

  /*
   * (non-Javadoc)
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

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#getHazard(java.util.UUID)
   */
  @Override
  public Hazard getHazard(UUID hazardId) {
    return getHazards().stream().filter(x -> x.getId().equals(hazardId)).findFirst()
        .orElse(null);
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#prepareForExport(xstampp.astpa.model.linking.
   * LinkController)
   */
  @Override
  public void prepareForExport(LinkController linkController, ISDSController sdsController) {
    for (Accident accident : this.getAccidents()) {
      accident.prepareForExport();
      String linkString = ""; //$NON-NLS-1$
      for (UUID id : linkController.getLinksFor(LinkingType.HAZ_ACC_LINK, accident.getId())) {
        linkString += getHazard(id).getIdString() + ", "; //$NON-NLS-1$
      }
      for (UUID id : linkController.getLinksFor(LinkingType.ACC_S0_LINK, accident.getId())) {
        linkString += sdsController.getSafetyConstraint(id).getIdString() + ", "; //$NON-NLS-1$
      }
      if (linkString.length() > 2) {
        accident.setLinks(linkString.substring(0, linkString.length() - 2));
      }
    }
    for (Hazard hazard : this.getHazards()) {
      hazard.prepareForExport();
      String linkString = ""; //$NON-NLS-1$
      for (UUID id : linkController.getLinksFor(LinkingType.HAZ_ACC_LINK, hazard.getId())) {
        linkString += getAccident(id).getIdString() + ", "; //$NON-NLS-1$
      }
      if (linkString.length() > 2) {
        hazard.setLinks(linkString.substring(0, linkString.length() - 2));
      }
    }
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#prepareForSave(xstampp.astpa.model.linking.
   * LinkController)
   */
  @Override
  public boolean prepareForSave(LinkController linkController) {
    boolean isUsed = false;
    for (Accident accident : this.getAccidents()) {
      accident.prepareForSave();
    }
    if (accidents != null && accidents.isEmpty()) {
      accidents = null;
    }
    isUsed |= accidents != null;
    for (Hazard hazard : this.getHazards()) {
      hazard.prepareForSave();
    }
    if (hazards != null && hazards.isEmpty()) {
      hazards = null;
    }
    isUsed |= hazards != null;
    if (this.links != null) {
      for (HazAccLink link : this.links) {
        linkController.addLink(LinkingType.HAZ_ACC_LINK, link.getAccidentId(),
            link.getHazardId());
      }
      links = null;
    }
    return isUsed;
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#getAllHazAccLinks()
   */
  @Override
  public List<HazAccLink> getAllHazAccLinks() {
    return new ArrayList<>(this.links);
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#isUseSeverity()
   */
  @Override
  public boolean isUseSeverity() {
    if (useSeverity != null) {
      return useSeverity;
    }
    return ASTPADefaultConfig.getInstance().USE_SEVERITY_ANALYSIS;
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.hazacc.IHazAccController#setUseSeverity(boolean)
   */
  @Override
  public boolean setUseSeverity(boolean useSeverity) {
    if (this.useSeverity == null || useSeverity != this.useSeverity) {
      this.useSeverity = useSeverity;
      return true;
    }
    return false;
  }

  private List<Accident> getAccidents() {
    if (this.accidents == null) {
      this.accidents = new NumberedArrayList<>();
    }
    return accidents;
  }

  private List<Hazard> getHazards() {
    if (this.hazards == null) {
      this.hazards = new NumberedArrayList<>();
    }
    return hazards;
  }

  public LinkController getLinkController() {
    return linkController;
  }

  public void setLinkController(LinkController linkController) {
    this.linkController = linkController;
  }
}
