package xstampp.astpa.model.causalfactor.linkEntries;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
public class CausalScenarioEntry {

  @XmlElement(name="description")
  private String description;
  
  @XmlElement(name="constraint")
  private String constraint;
  
  public CausalScenarioEntry(String description, String constraint) {
    if(description == null){
      this.description = new String();
    }else{
      this.description = description;
    }
    if(constraint == null){
      this.constraint = new String();
    }else{
      this.constraint = constraint;
    }
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @param constraint the constraint to set
   */
  public void setConstraint(String constraint) {
    this.constraint = constraint;
  }
}
