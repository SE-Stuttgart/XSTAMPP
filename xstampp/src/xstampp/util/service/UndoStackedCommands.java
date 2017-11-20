package xstampp.util.service;

import java.util.ArrayList;
import java.util.List;

import xstampp.model.ObserverValue;
import xstampp.util.IUndoCallback;

public class UndoStackedCommands implements IUndoCallback {

  private List<IUndoCallback> recordList;
  private ObserverValue updateValue;
  private UndoRedoService undoRedoService;

  public UndoStackedCommands(UndoRedoService undoRedoService) {
    this.undoRedoService = undoRedoService;
    this.recordList = new ArrayList<>();
    this.updateValue = ObserverValue.UNSAVED_CHANGES;
  }

  public boolean add(IUndoCallback callback) {
    this.updateValue = callback.getChangeConstant();
    return this.recordList.add(callback);
  }

  public List<IUndoCallback> getRecordList() {
    return new ArrayList<>(this.recordList);
  }

  @Override
  public void undo() {
    
    this.recordList.forEach((callback) -> callback.undo());
  }

  @Override
  public void redo() {
    this.undoRedoService.startRecord();
    this.recordList.forEach((callback) -> callback.redo());
    this.undoRedoService.pushRecord();
  }

  @Override
  public ObserverValue getChangeConstant() {
    return this.updateValue;
  }

}
