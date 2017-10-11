/*******************************************************************************
 * Copyright (C) 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lukas Balzer - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.model.linking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.UUID;
import java.util.function.Predicate;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import xstampp.model.ObserverValue;

public class LinkController extends Observable {

  @XmlElement
  @XmlJavaTypeAdapter(Adapter.class)
  private Map<ObserverValue, List<Link>> linkMap;

  public LinkController() {
    this.linkMap = new HashMap<>();
  }

  public boolean addLink(ObserverValue linkType, UUID a, UUID b) {
    if (linkType != null) {
      if (!this.linkMap.containsKey(linkType)) {
        this.linkMap.put(linkType, new ArrayList<Link>());
      }
      Link o = new Link(a, b);
      if (this.linkMap.get(linkType).add(o)) {
        setChanged();
        notifyObservers(new UndoAddLinkingCallback(this, linkType, o));

        return true;
      }
    }
    return false;
  }

  /**
   * this method returns a list of all UUID links stored under the given {@link ObserverValue}. If
   * <b>null</b> is given as linkType than the returned list is filled with all linked ids.
   * 
   * @param linkType
   *          an {@link ObserverValue} for which links have been created in the
   *          {@link LinkController}
   * @param part
   *          the ID of the part for which all available links are returned
   * @return a {@link List} of {@link UUID}'s of all linked items, or an empty {@link List} if part
   *         is given as <b>null</b>
   */
  public List<UUID> getLinksFor(ObserverValue linkType, UUID part) {
    List<UUID> links = new ArrayList<>();
    if (linkType == null) {
      for (ObserverValue value : this.linkMap.keySet()) {
        links.addAll(getLinksFor(value, part));
      }
    } else {
      for (Link link : getLinkObjectsFor(linkType)) {
        if (link.links(part)) {
          links.add(link.getLinkFor(part));
        }
      }
    }
    return links;
  }

  public List<Link> getLinksFor(ObserverValue linkType) {
    List<Link> links = new ArrayList<>();
    if (this.linkMap.containsKey(linkType)) {
      for (Link link : getLinkObjectsFor(linkType)) {
        links.add(new Link(link.getLinkA(), link.getLinkB()));
      }
    }
    return links;
  }

  private List<Link> getLinkObjectsFor(ObserverValue linkType) {
    if (this.linkMap.containsKey(linkType)) {
      this.linkMap.get(linkType).removeIf(new Predicate<Link>() {

        @Override
        public boolean test(Link t) {
          return t.getLinkA() == null || t.getLinkB() == null;
        }

      });
      return this.linkMap.get(linkType);
    }
    return new ArrayList<>();
  }

  /**
   * 
   * @param linkType
   *          the {@link ObserverValue} of the link
   * @param part
   *          the id of the element
   * @return whether the {@link LinkController} contains a link for the given id or not
   */
  public boolean isLinked(ObserverValue linkType, UUID part) {
    if (this.linkMap.containsKey(linkType)) {
      for (Link link : this.linkMap.get(linkType)) {
        if (link.links(part)) {
          return true;
        }
      }
      if (this.linkMap.get(linkType).isEmpty()) {
        this.linkMap.remove(linkType);
      }
    }
    return false;
  }

  public boolean deleteLink(ObserverValue linkType, UUID a, UUID b) {
    if (this.linkMap.containsKey(linkType)) {
      Link o = new Link(a, b);
      if (this.linkMap.get(linkType).remove(o)) {
        setChanged();
        notifyObservers(new UndoRemoveLinkingCallback(this, linkType, o));
        return true;
      }
    }
    return false;
  }

  /**
   * 
   * @param linkType
   *          the {@link ObserverValue} of the link
   * @param part
   *          the part that should be included in all links that are to to be deleted,<br> or
   *          <b><i>null</i></b> if all links for the given <b>type</b> should be deleted
   */
  public void deleteAllFor(ObserverValue linkType, UUID part) {
    List<Link> links = new ArrayList<>();
    if (this.linkMap.containsKey(linkType)) {
      for (Link link : this.linkMap.get(linkType)) {
        if (part == null || link.links(part)) {
          links.add(link);
        }
      }
      deleteLinks(linkType, links);
    }
  }

  void deleteLinks(ObserverValue linkType, List<Link> links) {
    if (this.linkMap.containsKey(linkType)) {
      this.linkMap.get(linkType).removeAll(links);
      this.linkMap.remove(linkType);
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
