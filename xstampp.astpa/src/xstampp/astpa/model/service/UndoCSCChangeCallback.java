package xstampp.astpa.model.service;

import java.util.UUID;

import xstampp.astpa.model.interfaces.ICorrespondingSafetyConstraintDataModel;

public class UndoCSCChangeCallback extends UndoTableModelChangeCallback<ICorrespondingSafetyConstraintDataModel> {

  public UndoCSCChangeCallback(ICorrespondingSafetyConstraintDataModel dataModel, UUID entryId) {
    super(dataModel, entryId);
  }

  @Override
  protected void undoDescription(String description) {
    getDataModel().setCorrespondingSafetyConstraint(getEntryId(), description);
  }

  @Override
  protected void undoTitle(String title) {
  }


}
