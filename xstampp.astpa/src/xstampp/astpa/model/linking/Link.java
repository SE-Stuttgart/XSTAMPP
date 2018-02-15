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

import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "idLink")
public final class Link {

  @XmlAttribute
  private UUID linkA;

  @XmlAttribute
  private UUID linkB;

  @XmlAttribute
  private UUID id;

  @XmlAttribute
  private String note;

  @XmlElement
  private String linkNote;

  private LinkingType linkType;

  public Link(UUID a, UUID b, LinkingType linkType) {
    this(a, b, linkType, UUID.randomUUID());
  }

  public Link(UUID a, UUID b, LinkingType linkType, UUID linkId) {
    linkA = a;
    linkB = b;
    this.linkType = linkType;
    id = linkId;
    this.linkNote = "";
    this.note = null;
  }

  Link() {
    this(null, null, null, UUID.randomUUID());
  }

  public UUID getId() {
    return id;
  }

  public UUID getLinkA() {
    return linkA;
  }

  public UUID getLinkB() {
    return linkB;
  }

  public String getNote() {
    if (note != null) {
      this.linkNote = this.note;
      // this.note = null;
    }
    return this.linkNote;
  }

  boolean setLinkA(UUID linkA) {
    boolean different = this.linkA == null || !this.linkA.equals(linkA);
    if (different) {
      this.linkA = linkA;
    }
    return different;
  }

  boolean setLinkB(UUID linkB) {
    boolean different = this.linkB == null || !this.linkB.equals(linkB);
    if (different) {
      this.linkB = linkB;
    }
    return different;
  }

  boolean setNote(String note) {
    boolean different = !this.linkNote.equals(note);
    if (different) {
      this.linkNote = note == null ? "" : note.replaceAll("\r", "");
      this.note = linkNote;
    }
    return different;
  }

  public boolean isLinkAPresent() {
    return this.linkA != null;
  }

  public boolean isLinkBPresent() {
    return this.linkB != null;
  }

  public boolean links(UUID part) {
    return part != null && (part.equals(this.linkA) || part.equals(this.linkB));
  }

  UUID getLinkFor(UUID part) {
    if (part.equals(this.linkA)) {
      return this.linkB;
    }
    if (part.equals(this.linkB)) {
      return this.linkA;
    }
    return null;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof UUID) {
      return this.links((UUID) obj);
    }
    if (obj instanceof Link) {
      if (((Link) obj).getLinkA() == null && getLinkA() == null && ((Link) obj).getLinkB().equals(linkB)) {
        return true;
      }
      if (((Link) obj).getLinkB() == null && getLinkB() == null && ((Link) obj).getLinkA().equals(linkA)) {
        return true;
      }
      return ((Link) obj).links(this.linkA) && ((Link) obj).links(this.linkB);
    }
    return super.equals(obj);
  }

  public LinkingType getLinkType() {
    return linkType;
  }

  void setLinkType(LinkingType linkType) {
    this.linkType = linkType;
  }
}
