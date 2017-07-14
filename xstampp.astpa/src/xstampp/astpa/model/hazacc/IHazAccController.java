package xstampp.astpa.model.hazacc;

import java.util.List;
import java.util.Observer;
import java.util.UUID;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkController;
import xstampp.model.ObserverValue;

public interface IHazAccController {

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
  UUID addAccident(String title, String description);

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
  boolean removeAccident(UUID id);

  boolean moveEntry(boolean moveUp, UUID id, ObserverValue value);

  /**
   * Searches for an Accident with given ID
   *
   * @param accidentID
   *          the id of the accident
   * @return found accident
   *
   * @author Fabian Toth
   */
  ITableModel getAccident(UUID accidentID);

  /**
   * Gets all accidents
   *
   * @return all accidents
   *
   * @author Fabian Toth
   */
  List<ITableModel> getAllAccidents();

  /**
   * Searches for all the hazards linked with given accident.
   *
   * @param accidentId
   *          the id of the accident
   * @return list of hazards linked to the given accident
   *
   * @author Fabian Toth
   */
  List<ITableModel> getLinkedHazards(UUID accidentId);

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
  UUID addHazard(String title, String description);

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
  boolean removeHazard(UUID id);

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
  boolean addLink(UUID accidentId, UUID hazardId);

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
  boolean deleteLink(UUID accidentId, UUID hazardId);

  /**
   * Gets all hazards
   *
   * @return all hazards
   *
   * @author Fabian Toth
   */
  List<ITableModel> getAllHazards();

  /**
   * Searches for all the accidents linked with given hazard.
   *
   * @param hazardId
   *          the ID of the hazard
   * @return list of accidents linked with given hazard
   *
   * @author Fabian Toth
   */
  List<ITableModel> getLinkedAccidents(UUID hazardId);

  /**
   * Searches for a Hazard with given ID
   *
   * @param hazardId
   *          the id of the hazard
   * @return found hazard
   *
   * @author Fabian Toth
   */
  Hazard getHazard(UUID hazardId);

  /**
   * Prepares the accidents and hazards for the export
   *
   * @author Fabian Toth
   * @author Lukas Balzer
   * @param linkController 
   *
   */
  void prepareForExport(LinkController linkController);

  /**
   * Removes the preparations that were made for the export
   *
   * @author Fabian Toth
   * @return TODO
   *
   */
  boolean prepareForSave(LinkController linkController);

  List<HazAccLink> getAllHazAccLinks();

  /**
   * @return the useSeverity
   */
  boolean isUseSeverity();

  /**
   * @param useSeverity the useSeverity to set
   */
  boolean setUseSeverity(boolean useSeverity);

  void addObserver(Observer obs);

}