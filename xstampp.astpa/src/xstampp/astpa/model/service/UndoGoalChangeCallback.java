package xstampp.astpa.model.service;

import java.util.UUID;

import xstampp.astpa.model.interfaces.ISystemGoalViewDataModel;

public class UndoGoalChangeCallback extends UndoTableModelChangeCallback<ISystemGoalViewDataModel> {

  public UndoGoalChangeCallback(ISystemGoalViewDataModel dataModel, UUID entryId) {
    super(dataModel, entryId);
  }

  @Override
  protected void undoDescription(String description) {
    getDataModel().setSystemGoalDescription(getEntryId(), description);
  }

  @Override
  protected void undoTitle(String title) {
    getDataModel().setSystemGoalTitle(getEntryId(), title);
  }


}
