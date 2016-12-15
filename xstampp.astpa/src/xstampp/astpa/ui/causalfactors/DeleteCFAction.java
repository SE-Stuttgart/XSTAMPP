package xstampp.astpa.ui.causalfactors;

import java.util.UUID;

import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.ui.common.grid.DeleteGridEntryAction;
import xstampp.ui.common.grid.GridWrapper;

public class DeleteCFAction extends DeleteGridEntryAction<ICausalFactorDataModel> {

  public DeleteCFAction(GridWrapper grid, ICausalFactorDataModel dataModel, String entryType, String prefix) {
    super(grid, dataModel, entryType, prefix);
  }

  @Override
  protected String getIdString(UUID id) {
    return new String();
  }

  @Override
  protected void removeEntry(UUID id) {
    getDataModel().removeCausalFactor(id);
  }

}
