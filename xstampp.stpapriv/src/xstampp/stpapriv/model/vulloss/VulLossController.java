/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
 * Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian Sieber, Fabian Toth, Patrick Wickenhäuser,
 * Aliaksei Babkovich, Aleksander Zotov).
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package xstampp.stpapriv.model.vulloss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import xstampp.model.ObserverValue;
import xstampp.astpa.model.ATableModel;
import xstampp.astpa.model.NumberedArrayList;
import xstampp.astpa.model.hazacc.HazAccController;
import xstampp.astpa.model.hazacc.HazAccLink;
import xstampp.astpa.model.interfaces.ITableModel;

/**
 * Controller-class for working with losses and vulnerabilities and links between them. Scince the
 * struture of this analysis mirrors this of the STPA analysis the Controller inherits from the
 * {@link HazAccController} and supstitutes:
 * <ul>
 * <li><b>losses</b> with <b>losses</b>
 * <li><b>vulnerabilities</b> with <b>vulnerabilities</b> 
 * </ul>
 *
 */
public class VulLossController extends HazAccController {

  @XmlElementWrapper(name = "losses")
  @XmlElement(name = "loss")
  private NumberedArrayList<Loss> losses;

  @XmlElementWrapper(name = "vulnerabilities")
  @XmlElement(name = "vulnerability")
  private NumberedArrayList<Vulnerability> vulnerabilities;

  @XmlElementWrapper(name = "seclinks")
  @XmlElement(name = "seclink")
  private List<HazAccLink> links;

  @XmlAttribute(name = "nextAccidentNumber")
  private Integer accidentIndex;

  @XmlAttribute(name = "nextHazardNumber")
  private Integer hazardIndex;

  /**
   * Constructor for the controller
   *
   * @author Fabian Toth
   *
   */
  public VulLossController() {
    this.losses = new NumberedArrayList<>();
    this.vulnerabilities = new NumberedArrayList<>();
    this.links = new ArrayList<>();
  }

  /**
   * Creates a new accident and adds it to the list of losses.
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
    Loss newAccident = new Loss(title, description);
    this.losses.add(newAccident);
    return newAccident.getId();
  }

  /**
   * Removes the accident from the list of losses and removes all links associated with this
   * accident.
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
    int index = this.losses.indexOf(accident);
    this.losses.remove(index);
    for (; index < this.losses.size(); index++) {
      this.losses.get(index).setNumber(index + 1);
    }
    return true;
  }

  public boolean moveEntry(boolean moveUp, UUID id, ObserverValue value) {
    if (value.equals(ObserverValue.ACCIDENT)) {
      return ATableModel.move(moveUp, id, losses);
    } else if (value.equals(ObserverValue.HAZARD)) {
      return ATableModel.move(moveUp, id, vulnerabilities);
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
    for (ITableModel accident : this.losses) {
      if (accident.getId().equals(accidentID)) {
        return accident;
      }
    }
    return null;
  }

  /**
   * Gets all losses
   *
   * @return all losses
   *
   * @author Fabian Toth
   */
  public List<ITableModel> getAllAccidents() {
    List<ITableModel> result = new ArrayList<>();
    for (Loss accident : this.losses) {
      result.add(accident);
    }
    return result;
  }

  /**
   * Searches for all the vulnerabilities linked with given accident.
   *
   * @param accidentId
   *          the id of the accident
   * @return list of vulnerabilities linked to the given accident
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
   * Creates a new hazard and adds it to the list of vulnerabilities.
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
    Vulnerability newHazard = new Vulnerability(title, description);
    this.vulnerabilities.add(newHazard);
    return newHazard.getId();
  }

  /**
   * Removes the hazard from the list of vulnerabilities and remove all links associated with this hazard.
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
    int index = this.vulnerabilities.indexOf(hazard);
    this.vulnerabilities.remove(index);
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
   * Gets all vulnerabilities
   *
   * @return all vulnerabilities
   *
   * @author Fabian Toth
   */
  public List<ITableModel> getAllHazards() {
    List<ITableModel> result = new ArrayList<>();
    for (Vulnerability hazard : this.vulnerabilities) {
      result.add(hazard);
    }
    return result;
  }

  /**
   * Searches for all the losses linked with given hazard.
   *
   * @param hazardId
   *          the ID of the hazard
   * @return list of losses linked with given hazard
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
  public Vulnerability getHazard(UUID hazardId) {
    for (Vulnerability hazard : this.vulnerabilities) {
      if (hazard.getId().equals(hazardId)) {
        return hazard;
      }
    }
    return null;
  }

  /**
   * Prepares the losses and vulnerabilities for the export
   *
   * @author Fabian Toth
   *
   */
  public void prepareForExport() {
    for (Loss accident : this.losses) {
      List<ITableModel> tempLinks = this.getLinkedHazards(accident.getId());
      String linkString = ""; //$NON-NLS-1$
      for (int i = 0; i < tempLinks.size(); i++) {
        linkString += tempLinks.get(i).getNumber();
        if (i < (tempLinks.size() - 1)) {
          linkString += ", "; //$NON-NLS-1$
        }
      }
      accident.setLinks(linkString);
    }
    for (Vulnerability hazard : this.vulnerabilities) {
      List<ITableModel> tempLinks = this.getLinkedAccidents(hazard.getId());
      String linkString = ""; //$NON-NLS-1$
      for (int i = 0; i < tempLinks.size(); i++) {
        linkString += tempLinks.get(i).getNumber();
        if (i < (tempLinks.size() - 1)) {
          linkString += ", "; //$NON-NLS-1$
        }
      }
      hazard.setLinks(linkString);
    }
  }

  /**
   * Removes the preparations that were made for the export
   *
   * @author Fabian Toth
   *
   */
  public void prepareForSave() {
    for (Loss accident : this.losses) {
      accident.setLinks(null);
    }
    for (Vulnerability hazard : this.vulnerabilities) {
      hazard.setLinks(null);
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

}
