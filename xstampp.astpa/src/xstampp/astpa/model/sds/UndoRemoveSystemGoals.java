package xstampp.astpa.model.sds;

import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.model.linking.UndoRemoveLinkedComponent;
import xstampp.model.ObserverValue;

public class UndoRemoveSystemGoals extends UndoRemoveLinkedComponent {

  private ITableModel model;
  private SDSController controller;

  public UndoRemoveSystemGoals(SDSController controller, ITableModel factor,
      LinkController linkController) {
    super(linkController, factor.getId(), 2);
    this.controller = controller;
    this.model = factor;
  }

  @Override
  public void undo() {
    super.undo();
    this.controller.addSystemGoal(this.model);
  }

  @Override
  public void redo() {
    this.controller.removeSystemGoal(this.model.getId());
  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.SYSTEM_GOAL;
  }

  @Override
  public String getChangeMessage() {
    return "Remove System Goal";
  }

}