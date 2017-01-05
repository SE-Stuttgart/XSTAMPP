package xstampp.astpa.ui.causalfactors;

import java.util.UUID;

import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.ui.common.grid.GridCellTextEditor;
import xstampp.ui.common.grid.GridWrapper;

public class CausalFactorEditorCell extends GridCellTextEditor {

  private UUID componentId;
  private UUID factorId;
  private ICausalFactorDataModel dataInterface;
  
  public CausalFactorEditorCell(GridWrapper gridWrapper,ICausalFactorDataModel dataInterface,
      String initialText, UUID componentId,UUID factorId) {
    super(gridWrapper, initialText,true, false,factorId);
    this.dataInterface = dataInterface;
    this.componentId = componentId;
    this.factorId = factorId;
  }

  @Override
  public void updateDataModel(String newText) {
    dataInterface.setCausalFactorText(componentId, factorId, newText);
    
  }
  
  @Override
  public void delete() {
    dataInterface.removeCausalFactor(componentId, factorId);
  }

  @Override
  public void onTextChange(String newValue) {
    updateDataModel(newValue);
  }
}
