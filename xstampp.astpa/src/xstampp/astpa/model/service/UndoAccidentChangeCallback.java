package xstampp.astpa.model.service;

import java.util.UUID;

import xstampp.astpa.model.interfaces.IAccidentViewDataModel;

public class UndoAccidentChangeCallback extends UndoTableModelChangeCallback<IAccidentViewDataModel> {

  public UndoAccidentChangeCallback(IAccidentViewDataModel dataModel, UUID entryId) {
    super(dataModel, entryId);
  }

  @Override
  protected void undoDescription(String description) {
    getDataModel().setAccidentDescription(getEntryId(), description);
  }

  @Override
  protected void undoTitle(String title) {
    getDataModel().setAccidentTitle(getEntryId(), title);
  }


}
