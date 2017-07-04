package xstampp.astpa.model.service;

import xstampp.astpa.model.interfaces.ISystemGoalViewDataModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.ObserverValue;

public class UndoGoalChangeCallback extends UndoTableModelChangeCallback<ISystemGoalViewDataModel> {

  public UndoGoalChangeCallback(ISystemGoalViewDataModel dataModel, ITableModel model) {
    super(dataModel, model);
  }

  @Override
  protected void undoDescription(String description) {
    getDataModel().setSystemGoalDescription(getEntryId(), description);
  }

  @Override
  protected void undoTitle(String title) {
    getDataModel().setSystemGoalTitle(getEntryId(), title);
  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.SYSTEM_GOAL;
  }


}
