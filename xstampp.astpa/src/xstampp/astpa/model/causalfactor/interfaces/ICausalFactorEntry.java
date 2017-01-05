package xstampp.astpa.model.causalfactor.interfaces;

import java.util.List;
import java.util.UUID;

public interface ICausalFactorEntry {
  
 
  
  /**
   * @return the hazardIds
   */
  public List<UUID> getHazardIds();
  
  /**
   * @return the note
   */
  public String getNote();
  
  String getConstraintText();
  
  public UUID getId();
  
  /**
   * @return the ucaLink
   */
  public UUID getUcaLink();
  
  /**
   * @return the scenarioLinks
   */
  public List<UUID> getScenarioLinks();
}
