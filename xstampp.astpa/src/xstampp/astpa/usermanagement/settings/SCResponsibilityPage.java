package xstampp.astpa.usermanagement.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import xstampp.astpa.model.hazacc.ATableModel;
import xstampp.astpa.model.interfaces.ISafetyConstraintViewDataModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.ui.common.ProjectManager;

public class SCResponsibilityPage extends EntryResponsibilitiesPage<ATableModel> {

  @Override
  protected List<ATableModel> getEntryList(UUID modelId) {
    List<ATableModel> list = new ArrayList<>();
    List<ITableModel> allSafetyConstraints = ((ISafetyConstraintViewDataModel) ProjectManager.getContainerInstance().getDataModel(modelId))
        .getAllSafetyConstraints();
    for(ITableModel model : allSafetyConstraints) {
      list.add((ATableModel) model);
    }
    return list;
  }

}
