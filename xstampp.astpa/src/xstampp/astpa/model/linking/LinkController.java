package xstampp.astpa.model.linking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.UUID;

import xstampp.model.ObserverValue;

public class LinkController extends Observable{
  private Map<ObserverValue, List<Link>> linkMap;

  public LinkController() {
    this.linkMap = new HashMap<>();
  }

  public boolean addLink(ObserverValue linkType, UUID a, UUID b) {
    if (linkType != null && !linkType.equals("")) {
      if (!this.linkMap.containsKey(linkType)) {
        this.linkMap.put(linkType, new ArrayList<Link>());
      }
      Link o = new Link(a,b);
      if(this.linkMap.get(linkType).add(o)) {
        setChanged();
        notifyObservers(new UndoAddLinkingCallback(this, linkType, o));
        
        return true;
      }
    }
    return false;
  }

  public List<UUID> getLinksFor(ObserverValue linkType, UUID part) {
    List<UUID> links = new ArrayList<>();
    if (this.linkMap.containsKey(linkType)) {
      for (Link link : this.linkMap.get(linkType)) {
        if (link.links(part)) {
          links.add(link.getLinkFor(part));
        }
      }
    }
    return links;
  }

  public Map<UUID,UUID> getLinksFor(ObserverValue linkType) {
    Map<UUID,UUID> links = new HashMap<>();
    if (this.linkMap.containsKey(linkType)) {
      for (Link link : this.linkMap.get(linkType)) {
        links.put(link.getLinkA(), link.getLinkB());
      }
    }
    return links;
  }

  public boolean isLinked(ObserverValue linkType, UUID part) {
    if (this.linkMap.containsKey(linkType)) {
      for (Link link : this.linkMap.get(linkType)) {
        if (link.links(part)) {
          return true;
        }
      }
    }
    return false;
  }
  
  public boolean deleteLink(ObserverValue linkType, UUID a, UUID b) {
    if (this.linkMap.containsKey(linkType)) {
      Link o = new Link(a,b);
      if(this.linkMap.get(linkType).remove(o)) {
        setChanged();
        notifyObservers(new UndoRemoveLinkingCallback(this, linkType, o));
        return true;
      }
    }
    return false;
  }
  
  public void deleteAllFor(ObserverValue linkType, UUID part) {
    List<Link> links = new ArrayList<>();
    if (this.linkMap.containsKey(linkType)) {
      for (Link link : this.linkMap.get(linkType)) {
        if (link.links(part)) {
          links.add(link);
        }
      }
      deleteLinks(linkType, links);
    }
  }
  
  void deleteLinks(ObserverValue linkType, List<Link> links) {
    if (this.linkMap.containsKey(linkType)) {
      this.linkMap.get(linkType).removeAll(links);
    }
    setChanged();
    notifyObservers(new UndoRemoveLinkingCallback(this, linkType, links));
  }
  
  void addLinks(ObserverValue linkType, List<Link> links) {
    if (this.linkMap.containsKey(linkType)) {
      this.linkMap.get(linkType).addAll(links);
    }
    setChanged();
    notifyObservers(new UndoAddLinkingCallback(this, linkType, links));
  }
}
