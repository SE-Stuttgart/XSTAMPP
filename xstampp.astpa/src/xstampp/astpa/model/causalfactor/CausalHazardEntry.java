package xstampp.astpa.model.causalfactor;

import javax.xml.bind.annotation.XmlElement;

import xstampp.astpa.model.hazacc.Hazard;
import xstampp.astpa.model.interfaces.ITableModel;

public class CausalHazardEntry {

  @XmlElement
  private String text;

  @XmlElement
  private String constraint;

  @XmlElement
  private String cscReference;

  public CausalHazardEntry(Hazard hazard, String constraintText, ITableModel sc1Model) {
    constraint = constraintText;
    text = hazard.getIdString();
    cscReference = sc1Model == null ? "" : sc1Model.getIdString();
  }
}
