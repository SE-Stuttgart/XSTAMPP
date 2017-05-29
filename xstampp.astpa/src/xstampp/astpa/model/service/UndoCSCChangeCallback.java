package xstampp.astpa.model.service;

import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.model.interfaces.ICorrespondingSafetyConstraintDataModel;

public class UndoCSCChangeCallback extends UndoTableModelChangeCallback<ICorrespondingSafetyConstraintDataModel> {

  public UndoCSCChangeCallback(ICorrespondingSafetyConstraintDataModel dataModel, ITableModel model) {
    super(dataModel, model);
  }

  @Override
  protected void undoDescription(String description) {
    getDataModel().setCorrespondingSafetyConstraint(getEntryId(), description);
  }

  @Override
  protected void undoTitle(String title) {
  }


}
