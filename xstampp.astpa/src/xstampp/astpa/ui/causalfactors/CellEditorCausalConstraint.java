package xstampp.astpa.ui.causalfactors;

import java.util.UUID;

import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.interfaces.IExtendedDataModel.ScenarioType;
import xstampp.model.AbstractLtlProviderData;
import xstampp.model.ObserverValue;
import xstampp.ui.common.grid.GridCellTextEditor;
import xstampp.ui.common.grid.GridWrapper;

public class CellEditorCausalConstraint extends GridCellTextEditor {

  private UUID ruleId;
  private ICausalFactorDataModel dataInterface;
  
  public CellEditorCausalConstraint(GridWrapper gridWrapper,ICausalFactorDataModel dataInterface,
                      UUID ruleId, ScenarioType type) {
    super(gridWrapper, dataInterface.getRefinedScenario(ruleId).getRefinedSafetyConstraint(),ruleId);
    if(type != ScenarioType.CAUSAL_SCENARIO){
      setReadOnly(true);
    }
    this.dataInterface = dataInterface;
    this.ruleId = ruleId;
  }

  @Override
  public void updateDataModel(String newText) {
    AbstractLtlProviderData data = new AbstractLtlProviderData();
    data.setRefinedConstraint(newText);
    dataInterface.updateRefinedRule(ruleId, data, null);
    
  }
  
  @Override
  public void delete() {
    //not used
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
