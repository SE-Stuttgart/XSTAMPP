package xstampp.astpa.model.service;

import java.util.UUID;

import xstampp.astpa.model.interfaces.ISafetyConstraintViewDataModel;

public class UndoSafetyConstraintChangeCallback extends UndoTableModelChangeCallback<ISafetyConstraintViewDataModel> {

  public UndoSafetyConstraintChangeCallback(ISafetyConstraintViewDataModel dataModel, UUID entryId) {
    super(dataModel, entryId);
  }

  @Override
  protected void undoDescription(String description) {
    getDataModel().setSafetyConstraintDescription(getEntryId(), description);
  }

  @Override
  protected void undoTitle(String title) {
    getDataModel().setSafetyConstraintTitle(getEntryId(), title);
  }


}
