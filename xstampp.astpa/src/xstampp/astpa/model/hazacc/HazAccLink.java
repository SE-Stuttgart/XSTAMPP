package xstampp.astpa.model.hazacc;

import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "link")
@XmlType(propOrder = { "accidentId", "hazardId" })
public class HazAccLink {

  private UUID accidentId;

  private UUID hazardId;

  public HazAccLink(UUID accidentId2, UUID hazardId2) {
    // TODO Auto-generated constructor stub
  }

  public HazAccLink() {
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
