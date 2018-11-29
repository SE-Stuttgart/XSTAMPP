package xstampp.astpa.model.interfaces;

import xstampp.model.ObserverValue;

public interface ITableModelController {

  public boolean setModelTitle(ITableModel model, String newText, ObserverValue value);

  /**
   * Universal Method for changing the description of a table entry while triggering a
   * {@link ObserverValue} which cannot be <b>null</b> to update the right parts of the data model
   * 
   * @param model
   *          a table entry which implements the common {@link ITableModel} interface
   * @param newText
   *          the new description, if <b>null</b> or the current text is given the method returns
   *          with <b>null</b>
   * @param value
   *          an {@link ObserverValue}, if <b>null</b> is given than an exception is thrown
   * @return The old description if the description has been changed null otherwise
   */
  public boolean setModelDescription(ITableModel model, String newText, ObserverValue value);
}
