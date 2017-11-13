package xstampp.astpa.model.causalfactor;

import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.model.linking.UndoAddLinkedComponent;
import xstampp.model.ObserverValue;

public class UndoAddCausalFactor extends UndoAddLinkedComponent{

  private CausalFactor factor;
  private CausalFactorController causalController;

  public UndoAddCausalFactor(CausalFactorController causalController, CausalFactor factor, LinkController linkController) {
    super(linkController, factor.getId(), 2);
    this.causalController = causalController;
    this.factor = factor;
  }

  @Override
  public void undo() {
    super.undo();
    this.causalController.removeCausalFactor(this.factor.getId());
  }

  @Override
  public void redo() {
    super.redo();
    this.causalController.addCausalFactor(this.factor);
  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.CAUSAL_FACTOR;
  }

}