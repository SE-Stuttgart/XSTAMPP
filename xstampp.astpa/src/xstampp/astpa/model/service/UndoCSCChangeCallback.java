package xstampp.astpa.model.service;

import java.util.UUID;

import xstampp.astpa.model.interfaces.ICorrespondingSafetyConstraintDataModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.ObserverValue;

public class UndoCSCChangeCallback extends UndoTableModelChangeCallback<ICorrespondingSafetyConstraintDataModel> {

  public UndoCSCChangeCallback(ICorrespondingSafetyConstraintDataModel dataModel, ITableModel model) {
    super(dataModel, model);
  }
  
  public UndoCSCChangeCallback(ICorrespondingSafetyConstraintDataModel dataModel, UUID model) {
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
