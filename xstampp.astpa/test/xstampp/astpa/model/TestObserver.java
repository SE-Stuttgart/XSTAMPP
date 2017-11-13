package xstampp.astpa.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import xstampp.model.ObserverValue;
import xstampp.util.IUndoCallback;

public class TestObserver implements Observer {

  private List<ObserverValue> updatedValues;

  public TestObserver() {
    updatedValues = new ArrayList<>();
  }

  public Observer getCleanObserver() {
    this.updatedValues.clear();
    return this;
  }

  public boolean hasUpdates(List<ObserverValue> updates) {
    return this.updatedValues.containsAll(updates);
  }

  @Override
  public void update(Observable o, Object arg) {
    if(arg instanceof ObserverValue) {
      this.updatedValues.add((ObserverValue) arg);
    } else if (arg instanceof IUndoCallback) {
      this.updatedValues.add(((IUndoCallback) arg).getChangeConstant());
    }
  }

}
