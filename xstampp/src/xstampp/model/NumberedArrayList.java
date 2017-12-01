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
package xstampp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class NumberedArrayList<E extends NumberedEntry> extends ArrayList<E> {

  /**
   * 
   */
  private static final long serialVersionUID = 1066462696186297605L;

  private Integer nextNumber;
  private Map<UUID, Integer> trash;
  private boolean useNextNumber;

  public NumberedArrayList() {
    this.useNextNumber = false;
    trash = new HashMap<>();
  }

  /**
   * Setter for the boolean value that decides whether the nextNumber attribute to determine the
   * number for the next added element. If useNextNumber is set to true than every element gets a
   * unique number which will never be given to an other element in this list even if the element is
   * removed.
   * 
   * @param useNextNumber
   *          if <b style="color:blue">true</b> than each new element gets the number that is
   *          currently the nextNumber and
   *          nextNumber is increased. Since nextNumber is never decreased one number can never be
   *          reassigned.
   *          <br>
   *          If <b style="color:blue">false</b> each new element gets the number of the last
   *          element in the list plus one.
   */
  public void setUseNextNumber(boolean useNextNumber) {
    this.useNextNumber = useNextNumber;
  }

  @Override
  public boolean add(E e) {
    if (trash.containsKey(e.getId())) {
      e.setNumber(trash.remove(e.getId()));
    }
    if (e.getNumber() < 0) {
      e.setNumber(this.getNextNumber());
    } else if (useNextNumber) {
      this.nextNumber = this.nextNumber == null ? e.getNumber() + 1
          : Math.max(this.nextNumber, e.getNumber() + 1);
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
    if (useNextNumber) {
      if (this.nextNumber == null) {
        Iterator<E> iterator = iterator();
        int i = 0;
        while (iterator.hasNext()) {
          iterator.next().setNumber(++i);
        }
        this.nextNumber = size() + 1;
      }
      return nextNumber++;
    } else if (size() > 0) {
      return get(size() - 1).getNumber() + 1;
    } else {
      return 1;
    }
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
