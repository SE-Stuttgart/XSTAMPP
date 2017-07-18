package xstampp.astpa.model.service;

import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.sds.ISDSController;
import xstampp.astpa.model.sds.SDSController;
import xstampp.model.ObserverValue;

public class UndoDesignReqChangeCallback extends UndoTableModelChangeCallback<ISDSController> {

  private ObserverValue type;

  public UndoDesignReqChangeCallback(ISDSController dataModel, ObserverValue type,
      ITableModel model) {
    super(dataModel, model);
    this.type = type;
  }

  @Override
  protected void undoDescription(String description) {
    getDataModel().setDesignRequirementDescription(type, getEntryId(), description);
  }

  @Override
  protected void undoTitle(String title) {
    getDataModel().setDesignRequirementTitle(type, getEntryId(), title);
  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.DESIGN_REQUIREMENT;
  }

}
