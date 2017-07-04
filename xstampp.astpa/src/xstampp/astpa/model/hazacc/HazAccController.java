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
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import xstampp.astpa.model.NumberedArrayList;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.preferences.IASTPADefaults;
import xstampp.model.ObserverValue;

/**
 * Controller-class for working with accidents and hazards and links between
 * them.
 *
 * @author Fabian Toth
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
public class HazAccController {

  @XmlElementWrapper(name = "accidents")
  @XmlElement(name = "accident")
  private List<Accident> accidents;

  @XmlElementWrapper(name = "hazards")
  @XmlElement(name = "hazard")
  private List<Hazard> hazards;

  @XmlElementWrapper(name = "links")
  @XmlElement(name = "link")
  private List<HazAccLink> links;

  @XmlAttribute(name = "nextAccidentNumber")
  private Integer accidentIndex;

  @XmlAttribute(name = "nextHazardNumber")
  private Integer hazardIndex;
  
  @XmlAttribute(name = "useSeverity")
  private boolean useSeverity;

  /**
   * Constructor for the controller
   *
   * @author Fabian Toth
   *
   */
  public HazAccController() {
    this.accidents = new NumberedArrayList<>();
    this.hazards = new NumberedArrayList<>();
    this.links = new ArrayList<>();
    this.useSeverity = IASTPADefaults.USE_SEVERITY_ANALYSIS;
  }

  /**
   * Creates a new accident and adds it to the list of accidents.
   *
   * @param title
   *          the title of the new accident
   * @param description
   *          the description of the new accident
   * @return the ID of the new accident
   *
   * @author Fabian Toth
   */
  public UUID addAccident(String title, String description) {
    Accident newAccident = new Accident(title, description, 0);
    this.accidents.add(newAccident);
    return newAccident.getId();
  }

  /**
   * Removes the accident from the list of accidents and removes all links
   * associated with this accident.
   *
   * @param id
   *          Accident's ID
   * @return true if the accident has been removed
   *
   * @author Fabian Toth
   *
   */
  public boolean removeAccident(UUID id) {
    ITableModel accident = this.getAccident(id);
    this.deleteAllLinks(id);
    int index = this.accidents.indexOf(accident);
    this.accidents.remove(index);
    for (; index < this.accidents.size(); index++) {
      this.accidents.get(index).setNumber(index + 1);
    }
    return true;
  }

  public boolean moveEntry(boolean moveUp, UUID id, ObserverValue value) {
    if (value.equals(ObserverValue.ACCIDENT)) {
      return ATableModel.move(moveUp, id, accidents);
    } else if (value.equals(ObserverValue.HAZARD)) {
      return ATableModel.move(moveUp, id, hazards);
    }
    return true;
  }

  /**
   * Searches for an Accident with given ID
   *
   * @param accidentID
   *          the id of the accident
   * @return found accident
   *
   * @author Fabian Toth
   */
  public ITableModel getAccident(UUID accidentID) {
    for (ITableModel accident : this.accidents) {
      if (accident.getId().equals(accidentID)) {
        return accident;
      }
    }
    return null;
  }

  /**
   * Gets all accidents
   *
   * @return all accidents
   *
   * @author Fabian Toth
   */
  public List<ITableModel> getAllAccidents() {
    List<ITableModel> result = new ArrayList<>();
    for (Accident accident : this.accidents) {
      result.add(accident);
    }
    return result;
  }

  /**
   * Searches for all the hazards linked with given accident.
   *
   * @param accidentId
   *          the id of the accident
   * @return list of hazards linked to the given accident
   *
   * @author Fabian Toth
   */
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

  /**
   * Creates a new hazard and adds it to the list of hazards.
   *
   * @param title
   *          the title of the new hazard
   * @param description
   *          the description of the new hazard
   * @return the ID of the new hazard
   *
   * @author Fabian Toth
   */
  public UUID addHazard(String title, String description) {
    Hazard newHazard = new Hazard(title, description, 0);
    this.hazards.add(newHazard);
    return newHazard.getId();
  }

  /**
   * Removes the hazard from the list of hazards and remove all links associated
   * with this hazard.
   *
   * @param id
   *          the hazard's ID
   * @return true if the hazard has been removed
   *
   * @author Fabian Toth
   *
   */
  public boolean removeHazard(UUID id) {
    ITableModel hazard = this.getHazard(id);
    this.deleteAllLinks(hazard.getId());
    int index = this.hazards.indexOf(hazard);
    this.hazards.remove(index);
    for (; index < this.hazards.size(); index++) {
      this.hazards.get(index).setNumber(index + 1);
    }
    return true;
  }

  /**
   * Creates a link between an accident and a hazard.
   *
   * @param accidentId
   *          the id of the accident
   * @param hazardId
   *          the id of the hazard
   *
   * @author Fabian Toth
   * @return
   */
  public boolean addLink(UUID accidentId, UUID hazardId) {
    return this.links.add(new HazAccLink(accidentId, hazardId));
  }

  /**
   * Removes a link between an accident and a hazard.
   *
   * @param accidentId
   *          the id of the accident
   * @param hazardId
   *          the id of the hazard
   * @return true if the link has been removed
   *
   * @author Fabian Toth
   */
  public boolean deleteLink(UUID accidentId, UUID hazardId) {
    return this.links.remove(new HazAccLink(accidentId, hazardId));
  }

  /**
   * Gets all hazards
   *
   * @return all hazards
   *
   * @author Fabian Toth
   */
  public List<ITableModel> getAllHazards() {
    List<ITableModel> result = new ArrayList<>();
    for (Hazard hazard : this.hazards) {
      result.add(hazard);
    }
    return result;
  }

  /**
   * Searches for all the accidents linked with given hazard.
   *
   * @param hazardId
   *          the ID of the hazard
   * @return list of accidents linked with given hazard
   *
   * @author Fabian Toth
   */
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

  /**
   * Searches for a Hazard with given ID
   *
   * @param hazardId
   *          the id of the hazard
   * @return found hazard
   *
   * @author Fabian Toth
   */
  public Hazard getHazard(UUID hazardId) {
    for (Hazard hazard : this.hazards) {
      if (hazard.getId().equals(hazardId)) {
        return hazard;
      }
    }
    return null;
  }

  /**
   * Prepares the accidents and hazards for the export
   *
   * @author Fabian Toth
   * @author Lukas Balzer
   * @param linkController 
   *
   */
  public void prepareForExport(LinkController linkController) {
    for (Accident accident : this.accidents) {
      String linkString = ""; //$NON-NLS-1$
      for (UUID id : linkController.getLinksFor(ObserverValue.HAZ_ACC_LINK, accident.getId())) {
        linkString += getHazard(id).getNumber() + ", "; //$NON-NLS-1$
      }
      accident.setLinks(linkString.substring(0, linkString.length() - 2));
    }
    for (Hazard hazard : this.hazards) {
      String linkString = ""; //$NON-NLS-1$
      for (UUID id : linkController.getLinksFor(ObserverValue.HAZ_ACC_LINK, hazard.getId())) {
        linkString += getAccident(id).getNumber() + ", "; //$NON-NLS-1$
      }
      hazard.setLinks(linkString.substring(0, linkString.length() - 2));
    }
  }

  /**
   * Removes the preparations that were made for the export
   *
   * @author Fabian Toth
   *
   */
  public void prepareForSave(LinkController linkController) {
    for (Accident accident : this.accidents) {
      accident.setLinks(null);
    }
    for (Hazard hazard : this.hazards) {
      hazard.setLinks(null);
    }
    if(this.links != null) {
      for(HazAccLink link: this.links) {
        linkController.addLink(ObserverValue.HAZ_ACC_LINK, link.getAccidentId(), link.getHazardId());
      }
      links = null;
    }
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
    for (HazAccLink link : this.links) {
      if (link.containsId(id)) {
        toDelete.add(link);
      }
    }
    this.links.removeAll(toDelete);
  }

  public List<HazAccLink> getAllHazAccLinks() {
    return new ArrayList<>(this.links);
  }

  /**
   * @return the useSeverity
   */
  public boolean isUseSeverity() {
    return useSeverity;
  }

  /**
   * @param useSeverity the useSeverity to set
   */
  public boolean setUseSeverity(boolean useSeverity) {
    if(useSeverity != this.useSeverity) {
      this.useSeverity = useSeverity;
      return true;
    }
    return false;
  }
}
