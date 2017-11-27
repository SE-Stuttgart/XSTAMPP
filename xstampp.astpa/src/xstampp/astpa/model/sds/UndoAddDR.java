package xstampp.astpa.model.sds;

import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.model.linking.UndoAddLinkedComponent;
import xstampp.model.ObserverValue;

public class UndoAddDR extends UndoAddLinkedComponent {

  private ITableModel model;
  private SDSController controller;
  private ObserverValue type;

  public UndoAddDR(SDSController controller, ITableModel model,
      LinkController linkController, ObserverValue type) {
    super(linkController, model.getId(), 2);
    this.controller = controller;
    this.model = model;
    this.type = type;
  }

  @Override
  public void undo() {
    this.controller.removeDesignRequirement(this.model.getId(), this.type);
  }

  @Override
  public void redo() {
    super.redo();
    this.controller.addDesignRequirement(this.model, this.type);
  }

  @Override
  public ObserverValue getChangeConstant() {
    return this.type;
  }

  @Override
  public String getChangeMessage() {
    return "Add a " + this.type;
  }
}