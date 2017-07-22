package xstampp.astpa.model.controlaction.interfaces;

import java.util.List;
import java.util.UUID;

import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.AbstractLTLProvider;

public interface IControlAction extends ITableModel{
  
  List<IUnsafeControlAction> getUnsafeControlActions();
  
  List<IUnsafeControlAction> getUnsafeControlActions(UnsafeControlActionType arg0);
  /**
   * @return the componentLink
   */
  public UUID getComponentLink();
  public List<AbstractLTLProvider> getAllRefinedRules();
  public IUnsafeControlAction getUnsafeControlAction(UUID ucaId);

  /**
   * @return a copie of the provided variables list
   */
  List<UUID> getProvidedVariables();

  /**
   * @return a copie of the the notProvidedVariables List
   */
  List<UUID> getNotProvidedVariables();
}
