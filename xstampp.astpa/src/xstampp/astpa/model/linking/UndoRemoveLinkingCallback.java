package xstampp.astpa.model.linking;

import java.util.List;

import xstampp.model.ObserverValue;

public class UndoRemoveLinkingCallback extends UndoAddLinkingCallback {
  
  public UndoRemoveLinkingCallback(LinkController linkController, ObserverValue linkType,
      List<Link> links) {
    super(linkController, linkType, links);
  }

  public UndoRemoveLinkingCallback(LinkController linkController, ObserverValue linkType,
      Link link) {
    super(linkController, linkType, link);
  }

  @Override
  public void undo() {
    super.redo();
  }

  @Override
  public void redo() {
    super.undo();
  }

}
