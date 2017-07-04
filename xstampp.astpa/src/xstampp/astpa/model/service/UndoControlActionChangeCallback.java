package xstampp.astpa.model.service;

import xstampp.astpa.model.interfaces.IControlActionViewDataModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.ObserverValue;

public class UndoControlActionChangeCallback extends UndoTableModelChangeCallback<IControlActionViewDataModel> {

  public UndoControlActionChangeCallback(IControlActionViewDataModel dataModel, ITableModel model) {
    super(dataModel, model);
  }

  @Override
  protected void undoDescription(String description) {
    getDataModel().setControlActionDescription(getEntryId(), description);
  }

  @Override
  protected void undoTitle(String title) {
    getDataModel().setControlActionTitle(getEntryId(), title);
  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.CONTROL_ACTION;
  }


}
