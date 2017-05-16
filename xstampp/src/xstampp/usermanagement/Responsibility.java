package xstampp.usermanagement;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.NONE)
public class Responsibility {
  @XmlAttribute
  private UUID userId;

  @XmlAttribute
  private UUID entryId;

  public Responsibility() {
  }

  public Responsibility(UUID userId, UUID entryId) {
    this.userId = userId;
    this.entryId = entryId;
  }

  /**
   * @return the userId
   */
  public UUID getUserId() {
    return userId;
  }

  /**
   * @return the entryId
   */
  public UUID getEntryId() {
    return entryId;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Responsibility) {
      Responsibility resp = (Responsibility) obj;
      boolean equalUser = false;
      boolean userNull = true;
      boolean equalEntry = false;
      if (resp.userId != null && userId != null) {
        userNull = false;
        equalUser = resp.userId != null && userId.equals(resp.userId);
      }
      if (resp.entryId != null && entryId != null) {
        equalEntry = resp.entryId != null && entryId.equals(resp.entryId);
      }
      if (equalUser && equalEntry) {
        return true;
      }
      if (userNull && equalEntry) {
        return true;
      }
    }
    return super.equals(obj);
  }
}