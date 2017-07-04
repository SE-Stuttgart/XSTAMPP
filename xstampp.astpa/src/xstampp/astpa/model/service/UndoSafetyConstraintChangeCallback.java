package xstampp.astpa.model.service;

import xstampp.astpa.model.interfaces.ISafetyConstraintViewDataModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.ObserverValue;

public class UndoSafetyConstraintChangeCallback extends UndoTableModelChangeCallback<ISafetyConstraintViewDataModel> {

  public UndoSafetyConstraintChangeCallback(ISafetyConstraintViewDataModel dataModel, ITableModel model) {
    super(dataModel, model);
  }

  @Override
  protected void undoDescription(String description) {
    getDataModel().setSafetyConstraintDescription(getEntryId(), description);
  }

  @Override
  protected void undoTitle(String title) {
    getDataModel().setSafetyConstraintTitle(getEntryId(), title);
  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.SAFETY_CONSTRAINT;
  }


}
