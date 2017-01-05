package xstampp.astpa.model.causalfactor.linkEntries;

import java.util.List;
import java.util.UUID;

import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorEntry;

public class CausalFactorEntryContainer implements ICausalFactorEntry {
  final ICausalFactorEntry entry;
  
  public CausalFactorEntryContainer(ICausalFactorEntry entry) {
    this.entry = entry;
  }

  @Override
  public List<UUID> getHazardIds() {
    return entry.getHazardIds();
  }

  @Override
  public String getNote() {
    return entry.getNote();
  }

  @Override
  public UUID getId() {
    return entry.getId();
  }

  @Override
  public UUID getUcaLink() {
    return entry.getUcaLink();
  }

  @Override
  public List<UUID> getScenarioLinks() {
    return entry.getScenarioLinks();
  }
  @Override
  public String getConstraintText() {
    return entry.getConstraintText();
  }

}
