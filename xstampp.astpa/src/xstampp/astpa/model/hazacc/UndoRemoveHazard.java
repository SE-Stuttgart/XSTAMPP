package xstampp.astpa.model.hazacc;

import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.model.linking.UndoRemoveLinkedComponent;
import xstampp.model.ObserverValue;

public class UndoRemoveHazard extends UndoRemoveLinkedComponent {

  private ITableModel model;
  private HazAccController controller;

  public UndoRemoveHazard(HazAccController controller, ITableModel model,
      LinkController linkController) {
    super(linkController, model.getId(), 2);
    this.controller = controller;
    this.model = model;
  }

  @Override
  public void undo() {
    super.undo();
    this.controller.addHazard(this.model);
  }

  @Override
  public void redo() {
    this.controller.removeHazard(this.model.getId());
  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.HAZARD;
  }

  @Override
  public String getChangeMessage() {
    return "Remove Hazard";
  }

}