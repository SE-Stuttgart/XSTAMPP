package xstampp.astpa.model.interfaces;

import java.util.List;
import java.util.UUID;

import xstampp.astpa.haz.ITableModel;

public interface IAccidentModel extends ILinkModel{

  /**
   * Getter for all existing accidents
   * 
   * @author Jarkko Heidenwag
   * 
   * @return All accidents
   */
  List<ITableModel> getAllAccidents();

  /**
   * Get an accident by it's ID.
   * 
   * @author Jarkko Heidenwag, Patrick Wickenhaeuser
   * @param accidentId
   *            the ID of the accident.
   * 
   * @return the accident.
   */
  ITableModel getAccident(UUID accidentId);
}
