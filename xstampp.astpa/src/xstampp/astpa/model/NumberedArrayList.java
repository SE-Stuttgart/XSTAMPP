package xstampp.astpa.model;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlAttribute;

public class NumberedArrayList<E extends NumberedEntry> extends ArrayList<E> {

  /**
   * 
   */
  private static final long serialVersionUID = 1066462696186297605L;

  @XmlAttribute
  private Integer nextNumber;

  @Override
  public boolean add(E e) {
    e.setNumber(this.getNextNumber());
    return super.add(e);
  }

  public Integer getNextNumber() {
    if (this.nextNumber == null) {
      Iterator<E> iterator = iterator();
      int i = 0;
      while(iterator.hasNext()) {
        iterator.next().setNumber(++i);
      }
      this.nextNumber = size() + 1;
    }
    return nextNumber++;
  }
}
