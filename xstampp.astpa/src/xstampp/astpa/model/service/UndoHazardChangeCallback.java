package xstampp.astpa.model.service;

import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.model.interfaces.IHazardViewDataModel;

public class UndoHazardChangeCallback extends UndoTableModelChangeCallback<IHazardViewDataModel> {

  public UndoHazardChangeCallback(IHazardViewDataModel dataModel, ITableModel model) {
    super(dataModel, model);
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
