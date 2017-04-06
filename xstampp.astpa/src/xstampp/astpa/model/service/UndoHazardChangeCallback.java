package xstampp.astpa.model.service;

import java.util.UUID;

import xstampp.astpa.model.interfaces.IHazardViewDataModel;

public class UndoHazardChangeCallback extends UndoTableModelChangeCallback<IHazardViewDataModel> {

  public UndoHazardChangeCallback(IHazardViewDataModel dataModel, UUID entryId) {
    super(dataModel, entryId);
  }

  @Override
  protected void undoDescription(String description) {
    getDataModel().setHazardDescription(getEntryId(), description);
  }

  @Override
  protected void undoTitle(String title) {
    getDataModel().setHazardTitle(getEntryId(), title);
  }


}
