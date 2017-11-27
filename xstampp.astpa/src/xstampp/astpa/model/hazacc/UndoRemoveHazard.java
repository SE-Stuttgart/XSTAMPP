package xstampp.astpa.model.hazacc;

import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.model.linking.UndoRemoveLinkedComponent;
import xstampp.model.ObserverValue;

public class UndoRemoveHazard extends UndoRemoveLinkedComponent {

  private ITableModel model;
  private HazAccController controller;

  public UndoRemoveHazard(HazAccController controller, ITableModel factor,
      LinkController linkController) {
    super(linkController, factor.getId(), 2);
    this.controller = controller;
    this.model = factor;
  }

  @Override
  public void undo() {
    super.undo();
    this.controller.addAccident(this.model);
  }

  @Override
  public void redo() {
    this.controller.removeAccident(this.model.getId());
  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.ACCIDENT;
  }

  @Override
  public String getChangeMessage() {
    return "Remove Accident";
  }

}