package xstampp.astpa.model.sds;

import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.model.linking.UndoRemoveLinkedComponent;
import xstampp.model.ObserverValue;

public class UndoRemoveSC extends UndoRemoveLinkedComponent {

  private ITableModel model;
  private SDSController controller;

  public UndoRemoveSC(SDSController controller, ITableModel model,
      LinkController linkController) {
    super(linkController, model.getId(), 2);
    this.controller = controller;
    this.model = model;
  }

  @Override
  public void undo() {
    super.undo();
    this.controller.addSafetyConstraint(this.model);
  }

  @Override
  public void redo() {
    this.controller.removeSafetyConstraint(this.model.getId());
  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.SAFETY_CONSTRAINT;
  }

  @Override
  public String getChangeMessage() {
    return "Remove Safety Constraint";
  }

}