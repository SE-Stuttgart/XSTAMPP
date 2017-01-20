package xstampp.astpa.ui.causalfactors;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import xstampp.astpa.model.causalfactor.interfaces.CausalFactorUCAEntryData;
import xstampp.astpa.model.causalfactor.interfaces.ICausalComponent;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactor;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorEntry;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.interfaces.IExtendedDataModel.ScenarioType;
import xstampp.model.AbstractLtlProviderData;
import xstampp.model.ObserverValue;
import xstampp.ui.common.grid.GridCellTextEditor;
import xstampp.ui.common.grid.GridWrapper;

public class CellEditorCausalScenario extends GridCellTextEditor {

  private UUID ruleId;
  private ICausalFactorDataModel dataInterface;
  private ScenarioType type;
  private ICausalFactorEntry entry;
  private UUID componentId;
  private UUID factorId;
  
  public CellEditorCausalScenario(GridWrapper gridWrapper,ICausalFactorDataModel dataInterface,
                      ICausalFactorEntry entry, ICausalComponent component, ICausalFactor factor, UUID ruleId, ScenarioType type) {
    super(gridWrapper, dataInterface.getRefinedScenario(ruleId).getSafetyRule(),ruleId);
    this.entry = entry;
    this.type = type;
    this.componentId = component.getId();
    this.factorId = factor.getId();
    if(type != ScenarioType.CAUSAL_SCENARIO){
      setReadOnly(true);
    }
    setShowDelete(true);
    this.dataInterface = dataInterface;
    this.ruleId = ruleId;
  }

  @Override
  public void updateDataModel(String newText) {
    AbstractLtlProviderData data = new AbstractLtlProviderData();
    data.setRule(newText);
    dataInterface.updateRefinedRule(ruleId, data, null);
    
  }
  
  @Override
  public void delete() {
    if(type == ScenarioType.CAUSAL_SCENARIO){
      dataInterface.removeRefinedSafetyRule(type , false, ruleId);
    }
    CausalFactorUCAEntryData data = new CausalFactorUCAEntryData(entry.getId());
    List<UUID> ids = new ArrayList<>();
    if(entry.getScenarioLinks() != null){
      ids.addAll(entry.getScenarioLinks());
    }
    ids.remove(ruleId);
    data.setScenarioLinks(ids);
    this.dataInterface.changeCausalEntry(componentId,factorId, data);
  }

  @Override
  protected void editorOpening() {
    dataInterface.lockUpdate();
  }
  
  @Override
  protected void editorClosing() {
    dataInterface.releaseLockAndUpdate(new ObserverValue[]{ObserverValue.CAUSAL_FACTOR,ObserverValue.Extended_DATA});
  }
}
