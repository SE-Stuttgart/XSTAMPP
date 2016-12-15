package xstpa.ui.tables;

import java.util.UUID;

import xstampp.astpa.model.interfaces.IExtendedDataModel;
import xstampp.ui.common.grid.DeleteGridEntryAction;
import xstampp.ui.common.grid.GridWrapper;

public class deleteLTLaction extends DeleteGridEntryAction<IExtendedDataModel> {

  public deleteLTLaction(GridWrapper grid, IExtendedDataModel dataModel, String entryType, String prefix) {
    super(grid, dataModel, entryType, prefix);
    // TODO Auto-generated constructor stub
  }

  @Override
  protected String getIdString(UUID id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected void removeEntry(UUID id) {
    // TODO Auto-generated method stub
    
  }

}
