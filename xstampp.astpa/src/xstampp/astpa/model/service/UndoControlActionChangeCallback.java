package xstampp.astpa.model.service;

import java.util.UUID;

import xstampp.astpa.model.interfaces.IControlActionViewDataModel;

public class UndoControlActionChangeCallback extends UndoTableModelChangeCallback<IControlActionViewDataModel> {

  public UndoControlActionChangeCallback(IControlActionViewDataModel dataModel, UUID entryId) {
    super(dataModel, entryId);
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
