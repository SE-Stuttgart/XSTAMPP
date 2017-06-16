package xstampp.astpa.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import xstampp.astpa.model.interfaces.ISeverityEntry;
import xstampp.astpa.model.interfaces.Severity;

@XmlAccessorType(XmlAccessType.NONE)
public abstract class EntryWithSeverity implements ISeverityEntry {

  @XmlAttribute
  private Severity severityType;

  public EntryWithSeverity() {
    this.severityType = null;
  }

  /* (non-Javadoc)
   * @see xstampp.astpa.model.ISeverityEntry#getSeverity()
   */
  @Override
  public Severity getSeverity() {
    return severityType;
  }

  /**
   * overwrites the current severity with a new one if the given newSeverity doesn't equal the
   * currently assigned
   * 
   * @param newSeverity
   *          the new severity value
   * @return null if the new severity equals the current severity, otherwise this method returns the
   *         current value
   */
  public Severity setSeverity(Severity newSeverity) {
    Severity oldSeverity = getSeverity();
    if (newSeverity.equals(oldSeverity)) {
      return null;
    } else {
      this.severityType = newSeverity;
      return oldSeverity;
    }
  }
}
