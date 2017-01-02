package xstampp.astpa.model.causalfactor.interfaces;

import java.util.List;
import java.util.UUID;

public class CausalFactorUCAEntryData extends CausalFactorEntryData{
  
  public CausalFactorUCAEntryData(UUID id) {
    super(id);
    this.scenariosChanged = false;
  }
  
  private List<UUID> scenarioLinks;
  private boolean scenariosChanged;
  
  /**
   * @return the scenarioLinks
   */
  public List<UUID> getScenarioLinks() {
    return scenarioLinks;
  }
  /**
   * @param scenarioLinks the scenarioLinks to set
   */
  public void setScenarioLinks(List<UUID> scenarioLinks) {
    this.scenarioLinks = scenarioLinks;
    this.scenariosChanged = true;
  }
  public boolean scenariosChanged() {
    return scenariosChanged;
  }
  
}