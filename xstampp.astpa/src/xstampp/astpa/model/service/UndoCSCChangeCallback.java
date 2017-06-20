package xstampp.astpa.model.service;

import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.model.interfaces.ICorrespondingSafetyConstraintDataModel;
import xstampp.model.ObserverValue;

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

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.UNSAFE_CONTROL_ACTION;
  }


}
