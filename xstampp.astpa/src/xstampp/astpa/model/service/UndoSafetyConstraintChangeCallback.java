package xstampp.astpa.model.service;

import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.model.interfaces.ISafetyConstraintViewDataModel;

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


}
