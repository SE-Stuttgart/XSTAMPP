package xstampp.astpa.model.causalfactor;

import java.util.ArrayList;

import xstampp.model.ObserverValue;
import xstampp.util.IUndoCallback;

public class UndoCausalSync implements IUndoCallback {
  
  private CausalFactorController controller;
  private ArrayList<CausalFactor> newFactors;
  private ArrayList<CausalSafetyConstraint> newConstraints;
  private ArrayList<CausalSafetyConstraint> oldConstraints;
  private ArrayList<CausalFactor> oldFactors;

  public UndoCausalSync(CausalFactorController controller) {
    this.controller = controller;
  }

  @Override
  public void undo() {
    controller.setCausalFactors(oldFactors);
    controller.setCausalSafetyConstraints(oldConstraints);
  }

  @Override
  public void redo() {
    // TODO Auto-generated method stub

  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.LINKING;
  }

  public void setOldValues(ArrayList<CausalFactor> oldFactors, ArrayList<CausalSafetyConstraint> oldConstraints) {
    this.oldFactors = oldFactors;
    this.oldConstraints = oldConstraints;
  }

  public void setNewValues(ArrayList<CausalFactor> newFactors, ArrayList<CausalSafetyConstraint> newConstraints) {
    this.newFactors = newFactors;
    this.newConstraints = newConstraints;
  }

}
