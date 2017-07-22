package xstampp.stpapriv.model.vulloss;

import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import xstampp.astpa.model.hazacc.HazAccLink;

@XmlRootElement(name = "seclink")
public class VulLossLink extends HazAccLink{

  private UUID accidentId;

  private UUID hazardId;

  public VulLossLink(UUID accidentId2, UUID hazardId2) {
    // TODO Auto-generated constructor stub
  }

  public VulLossLink() {
    // TODO Auto-generated constructor stub
  }

  public UUID getHazardId() {
    return hazardId;
  }

  public void setHazardId(UUID hazardId) {
    this.hazardId = hazardId;
  }

  public UUID getAccidentId() {
    return accidentId;
  }

  public void setAccidentId(UUID accidentId) {
    this.accidentId = accidentId;
  }

  public boolean containsId(UUID id) {
    return accidentId.equals(id) || hazardId.equals(id);
  }
}
