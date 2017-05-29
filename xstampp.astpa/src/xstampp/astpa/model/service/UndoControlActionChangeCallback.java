package xstampp.astpa.model.service;

import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.model.interfaces.IControlActionViewDataModel;

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


}
