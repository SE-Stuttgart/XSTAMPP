package xstampp.astpa.model.causalfactor;

import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.model.linking.UndoRemoveLinkedComponent;
import xstampp.model.ObserverValue;

public class UndoRemoveCausalFactor extends UndoRemoveLinkedComponent {

  private CausalFactor factor;
  private CausalFactorController causalController;

  public UndoRemoveCausalFactor(CausalFactorController causalController, CausalFactor factor,
      LinkController linkController) {
    super(linkController, factor.getId(), 4);
    this.causalController = causalController;
    this.factor = factor;
  }

  @Override
  public void undo() {
    super.undo();
    this.causalController.addCausalFactor(this.factor);
  }

  @Override
  public void redo() {
    this.causalController.removeCausalFactor(this.factor.getId());
  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.CAUSAL_FACTOR;
  }

  @Override
  public String getChangeMessage() {
    return "Remove Causal Factor";
  }

}