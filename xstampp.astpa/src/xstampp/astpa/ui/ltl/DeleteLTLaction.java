package xstampp.astpa.ui.ltl;

import java.util.UUID;

import xstampp.astpa.model.interfaces.IExtendedDataModel;
import xstampp.astpa.model.interfaces.IExtendedDataModel.ScenarioType;
import xstampp.ui.common.grid.DeleteGridEntryAction;
import xstampp.ui.common.grid.GridWrapper;

public class DeleteLTLaction extends DeleteGridEntryAction<IExtendedDataModel> {

  private IExtendedDataModel dataModel2;

  public DeleteLTLaction(GridWrapper grid, IExtendedDataModel dataModel, String entryType, String prefix) {
    super(grid, dataModel, entryType, prefix);
  }

  @Override
  protected String getIdString(UUID id) {
    return getPrefix()+getDataModel().getRefinedScenario(id).getNumber();
  }

  @Override
  protected void removeEntry(UUID id) {
    getDataModel().removeRefinedSafetyRule(ScenarioType.CUSTOM_LTL, false, id);
  }

}
