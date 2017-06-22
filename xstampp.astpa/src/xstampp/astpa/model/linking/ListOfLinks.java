package xstampp.astpa.model.linking;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ListOfLinks {
  @XmlElement
  private List<Entry> linkList;

  public ListOfLinks() {
    this.linkList = new ArrayList<>();
  }

  public List<Entry> getList() {
    return linkList;
  }
}