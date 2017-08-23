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
package xstampp.astpa.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;

public class NumberedArrayList<E extends NumberedEntry> extends ArrayList<E> {

  /**
   * 
   */
  private static final long serialVersionUID = 1066462696186297605L;

  @XmlAttribute
  private Integer nextNumber;

  private Map<UUID, Integer> trash;

  public NumberedArrayList() {
    trash = new HashMap<>();
  }

  @Override
  public boolean add(E e) {
    if (trash.containsKey(e.getId())) {
      add(trash.remove(e.getId()), e);
      return true;
    }
    if (e.getNumber() < 0) {
      e.setNumber(this.getNextNumber());
    }
    return super.add(e);
  }

  @Override
  public boolean remove(Object o) {
    if (o instanceof NumberedEntry) {
      trash.put(((NumberedEntry) o).getId(), ((NumberedEntry) o).getNumber());
    }
    return super.remove(o);
  }

  private Integer getNextNumber() {
    if (this.nextNumber == null) {
      Iterator<E> iterator = iterator();
      int i = 0;
      while (iterator.hasNext()) {
        iterator.next().setNumber(++i);
      }
      this.nextNumber = size() + 1;
    }
    return nextNumber++;
  }

  public E get(UUID id) {
    if (id == null) {
      return null;
    }
    for (E entry : this) {
      if (entry.getId().equals(id)) {
        return entry;
      }
    }
    return null;

  }

  @Override
  public boolean isEmpty() {
    return super.isEmpty() && nextNumber == null;
  }
}
