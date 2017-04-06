package xstampp.astpa.model.service;

import java.util.UUID;

import xstampp.astpa.model.causalfactor.interfaces.CausalFactorEntryData;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.util.IUndoCallback;

public class CausalDataUndoCallback implements IUndoCallback {

  private UUID componentId;
  private UUID causalFactorId;
  private CausalFactorEntryData oldData;
  private CausalFactorEntryData newData;
  private ICausalFactorDataModel dataModel;

  public CausalDataUndoCallback(ICausalFactorDataModel dataModel,
      UUID componentId,
      UUID causalFactorId,
      CausalFactorEntryData oldData,
      CausalFactorEntryData newData) {
        this.dataModel = dataModel;
        this.componentId = componentId;
        this.causalFactorId = causalFactorId;
        this.oldData = oldData;
        this.newData = newData;
  }
  
  @Override
  public void undo() {
    dataModel.changeCausalEntry(componentId, causalFactorId, oldData);
  }
  
  @Override
  public void redo() {
    dataModel.changeCausalEntry(componentId, causalFactorId, newData);
  }

}
