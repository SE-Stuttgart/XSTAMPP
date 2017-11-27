package xstampp.astpa.model.hazacc;

import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.model.linking.UndoAddLinkedComponent;
import xstampp.model.ObserverValue;

public class UndoAddAccident extends UndoAddLinkedComponent {

  private ITableModel model;
  private HazAccController controller;

  public UndoAddAccident(HazAccController controller, ITableModel factor,
      LinkController linkController) {
    super(linkController, factor.getId(), 2);
    this.controller = controller;
    this.model = factor;
  }

  @Override
  public void undo() {
    this.controller.removeAccident(this.model.getId());
  }

  @Override
  public void redo() {
    super.redo();
    this.controller.addAccident(this.model);
  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.ACCIDENT;
  }

  @Override
  public String getChangeMessage() {
    return "Add a Accident";
  }
}