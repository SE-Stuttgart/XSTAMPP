/*******************************************************************************
 * Copyright (C) 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Lukas Balzer - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.model.linking;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import xstampp.astpa.model.service.UndoTextChange;
import xstampp.model.ObserverValue;
import xstampp.util.IUndoCallback;

/**
 * see <i>xstampp.astpa/docs/architecture/causalFactors.png</i> for more details
 *
 */
public class LinkController extends Observable {

  @XmlElement
  @XmlJavaTypeAdapter(Adapter.class)
  private Map<LinkingType, List<Link>> linkMap;

  public LinkController() {
    this.linkMap = new TreeMap<>();
  }

  /**
   * Constructor for testing
   * 
   * @param asList
   */
  LinkController(List<Link> asList) {
    this();
    for (Link link : asList) {
      if (!this.linkMap.containsKey(link.getLinkType())) {
        assert (link.getLinkType() != null);
        this.linkMap.put(link.getLinkType(), new ArrayList<>());
      }
      this.linkMap.get(link.getLinkType()).add(link);
    }
  }

  /**
   * Adds a new {@link Link} to the List of {@link Link}'s mapped to the given linkType
   * <p>
   * if a Link for the given linkType and <b>null</b> either <code>linkA</code> or
   * <code>linkB</code> already exists it is updated with the given <code>linkB</code> or
   * <code>linkA</code>
   * <p>
   * if a Link with the same information for both <code>linkA</code> <b>and</b> <code>linkB</code>
   * exists, than its' id is returned and nothing is added.
   * 
   * @param linkType
   *          an {@link LinkingType} for which links have been created in the {@link LinkController}
   * @param linkA
   *          the part whose {@link UUID} is the first part of the {@link Link}
   * @param linkB
   *          the part whose {@link UUID} is the second part of the {@link Link}
   * @return
   *         The id of the Link which was either added, updated or which already existed
   */
  public UUID addLink(LinkingType linkType, UUID linkA, UUID linkB) {
    return addLink(linkType, linkA, linkB, true);
  }

  /**
   * calls {@link LinkController#addLink(LinkingType, UUID, UUID, boolean, UUID, String)} with
   * canUndo set to <b>true</b> and with id as {@link UUID#randomUUID()}
   * 
   */
  public UUID addLink(LinkingType linkType, UUID linkA, UUID linkB, String note) {
    return addLink(linkType, linkA, linkB, true, UUID.randomUUID(), note);
  }

  /**
   * Adds a new {@link Link} to the List of {@link Link}'s mapped to the given linkType
   * <p>
   * if a Link for the given linkType and <b>null</b> either <code>linkA</code> or
   * <code>linkB</code> already exists it is updated with the given <code>linkB</code> or
   * <code>linkA</code>
   * <p>
   * if a Link with the same information for both <code>linkA</code> <b>and</b> <code>linkB</code>
   * exists, than its' id is returned and nothing is added.
   * 
   * @param linkType
   *          an {@link LinkingType} for which links have been created in the
   *          {@link LinkController}
   * @param linkA
   *          the part whose {@link UUID} is the first part of the {@link Link}
   * @param linkB
   *          the part whose {@link UUID} is the second part of the {@link Link}
   * @param canUndo
   *          if this call should push a {@link IUndoCallback} to the Undo Stack or not
   * @return
   *         The id of the Link which was either added, updated or which already existed
   */
  public UUID addLink(LinkingType linkType, UUID linkA, UUID linkB, boolean canUndo) {
    return this.addLink(linkType, linkA, linkB, canUndo, UUID.randomUUID(), "");
  }

  /**
   * Adds a new {@link Link} to the List of {@link Link}'s mapped to the given linkType
   * <p>
   * if a Link for the given linkType and <b>null</b> either <code>linkA</code> or
   * <code>linkB</code> already exists it is updated with the given <code>linkB</code> or
   * <code>linkA</code>
   * <p>
   * if a Link with the same information for both <code>linkA</code> <b>and</b> <code>linkB</code>
   * exists, than its' id is returned and nothing is added.
   * 
   * @param linkType
   *          an {@link LinkingType} for which links have been created in the
   *          {@link LinkController}
   * @param linkA
   *          the part whose {@link UUID} is the first part of the {@link Link}
   * @param linkB
   *          the part whose {@link UUID} is the second part of the {@link Link}
   * @param canUndo
   *          if this call should push a {@link IUndoCallback} to the Undo Stack or not
   * @param id
   *          the {@link UUID} that should be assigned to a new link, if one is created
   * @return
   *         The id of the Link which was either added, updated or which already existed
   */
  public UUID addLink(LinkingType linkType, UUID linkA, UUID linkB, boolean canUndo, UUID id) {
    return addLink(linkType, linkA, linkB, canUndo, id, "");
  }

  /**
   * Adds a new {@link Link} to the List of {@link Link}'s mapped to the given linkType
   * <p>
   * if a Link for the given linkType and <b>null</b> either <code>linkA</code> or
   * <code>linkB</code> already exists it is updated with the given <code>linkB</code> or
   * <code>linkA</code>
   * <p>
   * if a Link with the same information for both <code>linkA</code> <b>and</b> <code>linkB</code>
   * exists, than its' id is returned and nothing is added.
   * 
   * @param linkType
   *          an {@link LinkingType} for which links have been created in the
   *          {@link LinkController}
   * @param linkA
   *          the part whose {@link UUID} is the first part of the {@link Link}
   * @param linkB
   *          the part whose {@link UUID} is the second part of the {@link Link}
   * @param canUndo
   *          if this call should push a {@link IUndoCallback} to the Undo Stack or not
   * @param id
   *          the {@link UUID} that should be assigned to a new link, if one is created
   * @param note
   *          n optional note that can be added to the link, can be a design hint or a rational for
   *          the relationship this link represents
   *          <p>
   *          see <i>xstampp.astpa/docs/architecture/causalFactors.png</i> for more details
   * @return
   *         The id of the Link which was either added, updated or which already existed
   */
  public UUID addLink(LinkingType linkType, UUID linkA, UUID linkB, boolean canUndo, UUID id, String note) {
    if (!this.linkMap.containsKey(linkType)) {
      assert (linkType != null);
      this.linkMap.put(linkType, new ArrayList<Link>());
    }
    Link newLink = new Link(linkA, linkB, linkType, id);
    newLink.setNote(note);
    // if the link already exists it is just returned
    int index = this.linkMap.get(linkType).indexOf(newLink);
    if (index != -1) {
      return this.linkMap.get(linkType).get(index).getId();
    }
    // if a link exists that links one of A and B to null than null is just replaced with the B or A
    // and the existing link id is returned
    index = this.linkMap.get(linkType).indexOf(new Link(null, linkB, linkType, id));
    if (index != -1 && changeLink(this.linkMap.get(linkType).get(index), linkA, linkB)) {
      return this.linkMap.get(linkType).get(index).getId();
    }
    index = this.linkMap.get(linkType).indexOf(new Link(linkA, null, linkType, id));
    if (index != -1 && changeLink(this.linkMap.get(linkType).get(index), linkA, linkB)) {
      return this.linkMap.get(linkType).get(index).getId();
    }
    if (this.linkMap.get(linkType).add(newLink)) {
      setChanged();
      if (canUndo) {
        notifyObservers(new UndoAddLinkingCallback(this, linkType, newLink));
      } else {
        notifyObservers(ObserverValue.LINKING);
      }

      return newLink.getId();
    }
    return null;
  }

  void addLinks(List<Link> links) {
    for (Link link : links) {
      addLink(link);
    }
  }

  void addLink(Link link) {
    this.linkMap.putIfAbsent(link.getLinkType(), new ArrayList<>());
    addLink(link.getLinkType(), link.getLinkA(), link.getLinkB(), true, link.getId(), "");
  }

  /**
   * this method returns a list of all UUID links stored under the given {@link LinkingType}. If
   * <b>null</b> is given as linkType than the returned list is filled with all linked ids.
   * 
   * @param linkType
   *          an {@link LinkingType} for which links have been created in the
   *          {@link LinkController}
   * @param part
   *          the ID of the part for which all available links are returned
   * @return a {@link List} of {@link UUID}'s of all linked items, or an empty {@link List} if part
   *         is given as <b>null</b>
   */
  public List<UUID> getLinksFor(LinkingType linkType, UUID part) {
    List<UUID> links = new ArrayList<>();
    for (Link link : getRawLinksFor(linkType, part)) {
      UUID linkFor = link.getLinkFor(part);
      if (linkFor != null) {
        links.add(linkFor);
      }
    }
    return links;
  }

  /**
   * Returns all matching {@link Link} stored under the given linkType that contain the given partId
   * as link
   * 
   * @param linkType
   *          one of the LINK constants in {@link LinkingType} <b>must not be <i>null</i></b>
   * @param partId
   *          the {@link UUID} of a {@link Link}
   * @return a {@link List} of {@link Link}'s stored under the given linkType with the given linkId
   */
  public List<Link> getRawLinksFor(LinkingType linkType, UUID partId) {
    List<Link> links = new ArrayList<>();
    for (Link link : getLinkObjectsFor(linkType)) {
      if (link.links(partId)) {
        links.add(link);
      }
    }
    return links;
  }

  /**
   * Returns and removes all matching {@link Link} that contain the given partId as link component.
   * <p>
   * <b>This method does not trigger an update and thus is not part of the API</b>
   * 
   * @param partId
   *          the {@link UUID} of a {@link Link}
   * @param depth
   *          the amount of recursions that are used to find {@link Link} Objects,<br>
   *          e.g. if depth
   *          is 2 than also the links are included that contain a {@link UUID} of a Link found in
   *          the first recursion
   * @return a {@link List} of {@link Link}'s that contain the given partId as link component.
   */
  List<Link> deleteLinksFor(UUID partId, int depth) {
    List<Link> links = new ArrayList<>();
    List<LinkingType> keySet = new ArrayList<>();
    keySet.addAll(this.linkMap.keySet());
    for (LinkingType linkType : keySet) {
      List<Link> list = getRawLinksFor(linkType, partId);
      deleteLinks(linkType, list);
      links.addAll(list);
    }
    if (depth > 1) {
      List<Link> deepLinks = new ArrayList<>();
      for (Link link : links) {
        deepLinks.addAll(deleteLinksFor(link.getId(), depth - 1));
      }
      links.addAll(deepLinks);
    }
    return links;
  }

  /**
   * Changes the two link components of the {@link Link} with the given linkId.
   * 
   * @param linkType
   *          one of the LINK constants in {@link LinkingType}
   * @param linkId
   *          the {@link UUID} of a {@link Link}
   * @param linkA
   *          the part whose {@link UUID} is the first part of the {@link Link}
   * @param linkB
   *          the part whose {@link UUID} is the second part of the {@link Link}
   * 
   * @return the first matching {@link Link} stored under the given linkType with the given linkId
   */
  public boolean changeLink(LinkingType linkType, UUID linkId, UUID linkA, UUID linkB) {
    Link link = getLinkObjectFor(linkType, linkId);
    return changeLink(link, linkA, linkB);
  }

  /**
   * Returns the first matching {@link Link} stored under the given linkType with the given linkId
   * 
   * @param link
   *          a {@link Link}
   * @param linkA
   *          the part whose {@link UUID} is the first part of the {@link Link}
   * @param linkB
   *          the part whose {@link UUID} is the second part of the {@link Link}
   * @return the first matching {@link Link} stored under the given linkType with the given linkId
   */
  public boolean changeLink(Link link, UUID linkA, UUID linkB) {
    UUID oldA = link.getLinkA();
    UUID oldB = link.getLinkB();
    if (link.setLinkA(linkA) || link.setLinkB(linkB)) {
      setChanged();
      notifyObservers(new UndoChangeLinkingCallback(this, link.getLinkType(), link.getId(), oldA,
          oldB, linkA, linkB));
      return true;
    }

    return false;
  }

  /**
   * Returns the first matching {@link Link} stored under the given linkType with the given linkId
   * 
   * @param link
   *          the {@link Link} whose note should be changed
   * @param note
   *          a String containing the new notes for the link
   * @return the first matching {@link Link} stored under the given linkType with the given linkId
   */
  public boolean changeLinkNote(Link link, String note) {
    String oldNote = link.getNote();
    boolean isSet = link.setNote(note);
    if (isSet) {
      UndoTextChange textChange = new UndoTextChange(oldNote, note, ObserverValue.LINKING);
      textChange.setConsumer((text) -> changeLinkNote(link, text));
      setChanged();
      notifyObservers(textChange);
    }
    return isSet;
  }

  public boolean changeLinkNote(UUID linkId, LinkingType type, String note) {
    Link link = getLinkObjectFor(type, linkId);
    return changeLinkNote(link, note);
  }

  /**
   * Returns the first matching {@link Link} stored under the given linkType with the given linkId
   * 
   * @param linkType
   *          one of the LINK constants in {@link LinkingType}
   * @param linkId
   *          the {@link UUID} of a {@link Link}
   * @return the first matching {@link Link} stored under the given linkType with the given linkId
   */
  public Link getLinkObjectFor(LinkingType linkType, UUID linkId) {
    if (this.linkMap.containsKey(linkType)) {
      this.linkMap.get(linkType).removeIf((t) -> {
        return t.getLinkA() == null && t.getLinkB() == null;
      });
      return this.linkMap.get(linkType).stream().filter((link) -> link.getId().equals(linkId))
          .findFirst().orElse(null);
    }
    return null;
  }

  /**
   * Returns the first matching {@link Link} stored under the given linkType which links the given
   * components
   * 
   * @param linkType
   *          one of the LINK constants in {@link LinkingType}
   * @param linkA
   *          the part whose {@link UUID} is the first part of the {@link Link}
   * @param linkB
   *          the part whose {@link UUID} is the second part of the {@link Link}
   * @return the first matching {@link Link} stored under the given linkType with the given linkId
   */
  public Link getLinkObjectFor(LinkingType linkType, UUID linkA, UUID linkB) {
    if (this.linkMap.containsKey(linkType)) {
      return this.linkMap.get(linkType).stream().filter((link) -> {
        return link.links(linkA) && link.links(linkB);
      }).findFirst().orElse(null);
    }
    return null;
  }

  /**
   * Returns all {@link Link}'s stored under the given linkType
   * 
   * @param linkType
   *          one of the LINK constants in {@link LinkingType}
   * @return a {@link List} containing all {@link Link}'s for the given linkType
   */
  public List<Link> getLinksFor(LinkingType linkType) {
    return getLinkObjectsFor(linkType);
  }

  private List<Link> getLinkObjectsFor(LinkingType linkType) {
    if (this.linkMap.containsKey(linkType)) {
      this.linkMap.get(linkType).removeIf((t) -> {
        return t.getLinkA() == null && t.getLinkB() == null;
      });
      return this.linkMap.get(linkType);
    }
    return new ArrayList<>();
  }

  /**
   * 
   * @param linkType
   *          the {@link LinkingType} of the link
   * @param part
   *          the id of the element
   * @return whether the {@link LinkController} contains a link for the given id or not
   */
  public boolean isLinked(LinkingType linkType, UUID part) {
    if (!isLinked(linkType, part, Optional.empty())) {
      // if the part itself is not part of a link stored under that type than maybe it is part of a
      // link that itself is linked
      Optional<Link> optional = this.linkMap.getOrDefault(linkType, new ArrayList<>()).parallelStream()
          .filter((link) -> {
            return link.links(part) && isLinked(linkType, link.getId());
          }).findFirst();
      return optional.isPresent();
    }
    return true;
  }

  public boolean isLinked(LinkingType linkType, UUID part, UUID rightPart) {
    return isLinked(linkType, rightPart, Optional.of(rightPart));
  }

  public boolean isLinked(LinkingType linkType, UUID part, Optional<UUID> rightPart) {
    if (this.linkMap.containsKey(linkType)) {
      for (Link link : this.linkMap.get(linkType)) {
        if (link.links(part) && (!rightPart.isPresent() || link.links(rightPart.get()))) {
          return true;
        }
      }
      if (this.linkMap.get(linkType).isEmpty()) {
        this.linkMap.remove(linkType);
      }
    }
    return false;

  }

  /**
   * Finds and removes the first matching {@link Link} stored under the given linkType with the
   * given linkId
   * 
   * @param linkType
   *          one of the LINK constants in {@link LinkingType}
   * @param linkId
   *          the {@link UUID} of a {@link Link}
   * @return whether something has been deleted or not
   */
  public boolean deleteLink(LinkingType linkType, UUID linkId) {
    if (this.linkMap.containsKey(linkType)) {
      return this.linkMap.get(linkType).removeIf((t) -> {
        return t.getId().equals(linkId);
      });
    }
    return false;
  }

  /**
   * Finds and deletes a {@link Link} based on the two parts of the link
   * 
   * @param linkType
   *          one of the LINK constants in {@link LinkingType}
   * @param linkA
   *          the part whose {@link UUID} is the first part of the {@link Link}
   * @param linkB
   *          the part whose {@link UUID} is the second part of the {@link Link}
   * @return
   */
  public boolean deleteLink(LinkingType linkType, UUID linkA, UUID linkB) {
    if (this.linkMap.containsKey(linkType)) {
      Link o = new Link(linkA, linkB, linkType, null);
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
   *          the {@link LinkingType} of the link
   * @param part
   *          the part that should be included in all links that are to to be deleted,<br>
   *          or
   *          <b><i>null</i></b> if all links for the given <b>type</b> should be deleted
   */
  public void deleteAllFor(LinkingType linkType, UUID part) {
    List<Link> links = new ArrayList<>();
    if (this.linkMap.containsKey(linkType)) {
      for (Link link : this.linkMap.get(linkType)) {
        if (part == null || link.links(part)) {
          links.add(link);
        }
      }
      deleteLinks(linkType, links);
      setChanged();
      notifyObservers(new UndoRemoveLinkingCallback(this, linkType, links));
    }
  }

  void deleteLinks(LinkingType linkType, List<Link> links) {
    if (this.linkMap.containsKey(linkType) &&
        this.linkMap.get(linkType).removeAll(links)) {
      if (this.linkMap.get(linkType).isEmpty()) {
        this.linkMap.remove(linkType);
      }
      setChanged();
      notifyObservers(ObserverValue.LINKING);
    }
  }

  public void prepareForSave() {
    for (Entry<LinkingType, List<Link>> entry : linkMap.entrySet()) {
      ArrayList<Link> links = new ArrayList<>();
      for (Link link : entry.getValue()) {
        if (links.contains(link)) {
          if (entry.getKey().isAcceptingNullLinks()
              && (link.isLinkAPresent() || link.isLinkBPresent())) {
            links.add(link);
          } else if (link.isLinkAPresent() && link.isLinkBPresent()) {
            links.add(link);
          }
        }
      }
      entry.setValue(links);
    }
    linkMap.entrySet().removeIf((entry) -> {
      return entry.getValue().isEmpty();
    });
  }

  public List<LinkingType> getLinkKeys() {
    return new ArrayList<>(this.linkMap.keySet());
  }

  public void syncContent(LinkController controller) {
    List<Link> removeList = new ArrayList<>();
    for (Entry<LinkingType, List<Link>> ownEntry : linkMap.entrySet()) {
      for (Link ownLink : ownEntry.getValue()) {
        if (!controller.isLinked(ownEntry.getKey(), ownLink.getLinkA(), ownLink.getLinkB())) {
          removeList.add(ownLink);
        }
      }
    }
    removeList.forEach((link) -> deleteLink(link.getLinkType(), link.getLinkA(), link.getLinkB()));
    for (Entry<LinkingType, List<Link>> foraignEntry : controller.linkMap.entrySet()) {
      addLinks(foraignEntry.getValue());
    }
  }

  public int getLinkMapSize() {
    return this.linkMap.size();
  }
}
