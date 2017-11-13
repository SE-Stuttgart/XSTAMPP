package xstampp.astpa.model.linking;

import java.util.List;
import java.util.UUID;

import xstampp.model.ObserverValue;
import xstampp.util.IUndoCallback;

public class UndoAddLinkedComponent implements IUndoCallback {

  private LinkController linkController;
  private UUID componentId;
  private List<Link> deleteLinksFor;
  private int linkDepth;

  public UndoAddLinkedComponent(LinkController linkController, UUID componentId, int linkDepth) {
    this.componentId = componentId;
    this.linkDepth = linkDepth;
    deleteLinksFor = linkController.deleteLinksFor(this.componentId, this.linkDepth);
    this.linkController = linkController;
  }

  @Override
  public void undo() {
    this.deleteLinksFor = this.linkController.deleteLinksFor(this.componentId, this.linkDepth);
  }

  @Override
  public void redo() {
    this.linkController.addLinks(this.deleteLinksFor);
  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.CAUSAL_FACTOR;
  }

}