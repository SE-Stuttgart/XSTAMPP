package xstampp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractNumberedEntry implements NumberedEntry {


  @XmlAttribute
  private int number;
  
  public AbstractNumberedEntry() {
    this.number = -1;
  }

  @Override
  public boolean setNumber(int i) {
    if(this.number != i) {
      this.number = i;
      return true;
    }
    return false;
  }

  @Override
  public int getNumber() {
    return this.number;
  }
}
