package xstampp.astpa.model.service;

import java.util.UUID;

import xstampp.astpa.model.interfaces.IUnsafeControlActionDataModel;
import xstampp.model.ObserverValue;
import xstampp.util.IUndoCallback;

public class UndoUCAChangesCallback implements IUndoCallback {

  private IUnsafeControlActionDataModel dataModel;
  private String newDescription;
  private String oldDescription;
  private boolean descriptionDirty;
  private UUID ucaID;

  public UndoUCAChangesCallback(IUnsafeControlActionDataModel dataModel, UUID ucaID) {
    this.dataModel = dataModel;
    this.ucaID = ucaID;
    this.descriptionDirty = false;
  }

  public void setDescriptionChange(String oldDescription, String newDescription) {
    this.oldDescription = oldDescription;
    this.newDescription = newDescription;
    descriptionDirty = true;
  }

  @Override
  public void undo() {
    if(descriptionDirty) {
      this.dataModel.setUcaDescription(ucaID, oldDescription);
    }
  }

  @Override
  public void redo() {
    if(descriptionDirty) {
      this.dataModel.setUcaDescription(ucaID, newDescription);
    }
  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.UNSAFE_CONTROL_ACTION;
  }

}
