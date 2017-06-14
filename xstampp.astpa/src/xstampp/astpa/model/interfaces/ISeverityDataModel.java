package xstampp.astpa.model.interfaces;

public interface ISeverityDataModel {

  /**
   * overwrites the current severity with a new one if the given newSeverity doesn't equal the
   * currently assigned
   * 
   * @param entry the entry in which the given severity should be stored, if entry is no instance of {@link ISeverityEntry} 
   *              than <i>null</i> is returned
   * @param severity
   *          the new severity value
   * @return null if the new severity equals the current severity, otherwise this method returns the
   *         current value
   */
  public boolean setSeverity(Object entry, Severity severity);
  

  /**
   * @return the useSeverity
   */
  public boolean isUseSeverity();

  /**
   * @param useSeverity the useSeverity to set
   * @return if the value was really changed 
   */
  public boolean setUseSeverity(boolean useSeverity);
}
