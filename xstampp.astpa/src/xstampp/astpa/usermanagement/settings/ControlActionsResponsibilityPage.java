package xstampp.astpa.usermanagement.settings;

import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.haz.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.hazacc.ATableModel;
import xstampp.astpa.model.interfaces.IControlActionViewDataModel;
import xstampp.ui.common.ProjectManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ControlActionsResponsibilityPage extends EntryResponsibilitiesPage<ATableModel> {

  @Override
  protected List<ATableModel> getEntryList(UUID modelId) {
    List<ATableModel> list = new ArrayList<>();
    List<IControlAction> allSafetyConstraints = ((IControlActionViewDataModel) ProjectManager.getContainerInstance().getDataModel(modelId))
        .getAllControlActions();
    for(ITableModel model : allSafetyConstraints) {
      list.add((ATableModel) model);
    }
    return list;
  }

}
