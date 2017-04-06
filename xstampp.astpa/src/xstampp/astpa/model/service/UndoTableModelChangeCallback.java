package xstampp.astpa.model.service;

import java.util.UUID;

import xstampp.model.IDataModel;
import xstampp.util.IUndoCallback;

public abstract class UndoTableModelChangeCallback<T extends IDataModel> implements IUndoCallback {

  private T dataModel;
  private String oldDescription;
  private String newDescription;
  private boolean descriptionDirty;
  private String oldTitle;
  private String newTitle;
  private boolean titleDirty;
  private UUID entryId;

  public UndoTableModelChangeCallback(T dataModel, UUID entryId) {
    this.dataModel = dataModel;
    this.entryId = entryId;
  }

  public void setDescriptionChange(String oldDescription, String newDescription) {
    this.oldDescription = oldDescription;
    this.newDescription = newDescription;
    this.descriptionDirty = true;
  }

  public void setTitleChange(String oldTitle, String newTitle) {
    this.oldTitle = oldTitle;
    this.newTitle = newTitle;
    this.titleDirty = true;
  }

  @Override
  public void undo() {
    if(descriptionDirty){
      undoDescription(this.oldDescription);
    }
    if(titleDirty){
      undoTitle(this.oldTitle);
    }
  }
  @Override
  public void redo() {
    if(descriptionDirty){
      undoDescription(this.newDescription);
    }
    if(titleDirty){
      undoTitle(this.newTitle);
    }
  }

  public UUID getEntryId() {
    return entryId;
  }
  /**
   * @return the dataModel
   */
  public T getDataModel() {
    return dataModel;
  }

  protected abstract void undoDescription(String description);
  protected abstract void undoTitle(String title);
}
