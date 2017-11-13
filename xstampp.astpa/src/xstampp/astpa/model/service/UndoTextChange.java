package xstampp.astpa.model.service;

import xstampp.model.ObserverValue;
import xstampp.util.IUndoCallback;

public class UndoTextChange implements IUndoCallback {

  private String oldText;
  private String newText;
  private IApplicator applicator;
  private ObserverValue changeValue;

  @FunctionalInterface
  public interface IApplicator {
    void applyText(String text);
  }

  public UndoTextChange() {
    setConsumer((text) -> {});
  }
  
  public UndoTextChange(String oldText, String newText, ObserverValue changeValue) {
    this();
    this.oldText = oldText;
    this.newText = newText;
    this.changeValue = changeValue;
  }

  public void setConsumer(IApplicator applicator) {
    this.applicator = applicator;
  }

  @Override
  public void undo() {
    this.applicator.applyText(this.oldText);
  }

  @Override
  public void redo() {
    this.applicator.applyText(this.newText);
  }

  @Override
  public ObserverValue getChangeConstant() {
    return this.changeValue;
  }

}
