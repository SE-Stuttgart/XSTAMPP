package xstampp.astpa.model.linking;

import java.util.List;
import java.util.UUID;

import xstampp.model.ObserverValue;
import xstampp.util.IUndoCallback;

public abstract class UndoRemoveLinkedComponent implements IUndoCallback {

  private LinkController linkController;
  private List<Link> deleteLinksFor;
  private UUID componentId;
  private int linkDepth;

  public UndoRemoveLinkedComponent(LinkController linkController, UUID componentId, int linkDepth) {
    this.componentId = componentId;
    this.linkDepth = linkDepth;
    this.deleteLinksFor = linkController.deleteLinksFor(this.componentId, this.linkDepth);
    this.linkController = linkController;
  }

  @Override
  public void undo() {
    this.linkController.addLinks(this.deleteLinksFor);
  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.CAUSAL_FACTOR;
  }

}