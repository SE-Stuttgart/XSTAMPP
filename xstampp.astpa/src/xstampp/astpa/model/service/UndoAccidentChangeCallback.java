package xstampp.astpa.model.service;

import xstampp.astpa.model.interfaces.IAccidentViewDataModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.ObserverValue;

public class UndoAccidentChangeCallback extends UndoTableModelChangeCallback<IAccidentViewDataModel> {

  public UndoAccidentChangeCallback(IAccidentViewDataModel dataModel, ITableModel model) {
    super(dataModel, model);
  }

  @Override
  protected void undoDescription(String description) {
    getDataModel().setAccidentDescription(getEntryId(), description);
  }

  @Override
  protected void undoTitle(String title) {
    getDataModel().setAccidentTitle(getEntryId(), title);
  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.ACCIDENT;
  }


}
