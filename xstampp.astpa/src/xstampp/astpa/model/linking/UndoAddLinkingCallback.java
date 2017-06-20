package xstampp.astpa.model.linking;

import java.util.ArrayList;
import java.util.List;

import xstampp.model.ObserverValue;
import xstampp.util.IUndoCallback;

public class UndoAddLinkingCallback implements IUndoCallback {

  private List<Link> links;
  private ObserverValue linkType;
  private LinkController linkController;

  public UndoAddLinkingCallback(LinkController linkController, ObserverValue linkType, List<Link> links) {
    this.linkController = linkController;
    this.linkType = linkType;
    this.links = links;
  }

  public UndoAddLinkingCallback(LinkController linkController,ObserverValue linkType, Link link) {
    this(linkController, linkType, new ArrayList<Link>());
    this.links.add(link);
  }

  
  @Override
  public void undo() {
    this.linkController.deleteLinks(linkType, links);
  }

  @Override
  public void redo() {
    this.linkController.addLinks(linkType, links);
  }

  @Override
  public ObserverValue getChangeConstant() {
    return linkType;
  }

}
