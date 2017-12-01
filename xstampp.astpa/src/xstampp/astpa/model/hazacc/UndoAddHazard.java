package xstampp.astpa.model.hazacc;

import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.model.linking.UndoAddLinkedComponent;
import xstampp.model.ObserverValue;

public class UndoAddHazard extends UndoAddLinkedComponent {

  private ITableModel model;
  private HazAccController controller;

  public UndoAddHazard(HazAccController controller, ITableModel model,
      LinkController linkController) {
    super(linkController, model.getId(), 2);
    this.controller = controller;
    this.model = model;
  }

  @Override
  public void undo() {
    this.controller.removeHazard(this.model.getId());
  }

  @Override
  public void redo() {
    super.redo();
    this.controller.addHazard(this.model);
  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.HAZARD;
  }

  @Override
  public String getChangeMessage() {
    return "Add a Hazard";
  }
}