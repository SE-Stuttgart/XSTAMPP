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
    e.setNumber(this.getNextNumber());
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

  @Override
  public boolean isEmpty() {
    return super.isEmpty() && nextNumber == null;
  }
}
