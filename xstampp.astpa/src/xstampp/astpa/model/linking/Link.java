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
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "idLink")
public final class Link {

  @XmlAttribute
  private UUID linkA;

  @XmlAttribute
  private UUID linkB;

  @XmlAttribute
  private UUID id;

  public Link(UUID a, UUID b) {
    linkA = a;
    linkB = b;
    this.id = UUID.randomUUID();
  }

  Link() {
    this(null, null);
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
      if (((Link) obj).getLinkA() == null && getLinkA() == null) {
        return true;
      }
      if (((Link) obj).getLinkB() == null && getLinkB() == null) {
        return true;
      }
      return ((Link) obj).links(this.linkA) && ((Link) obj).links(this.linkB);
    }
    return super.equals(obj);
  }
}
