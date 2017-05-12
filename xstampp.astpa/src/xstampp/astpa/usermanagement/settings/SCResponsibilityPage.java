package xstampp.astpa.usermanagement.settings;

import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.model.hazacc.ATableModel;
import xstampp.astpa.model.interfaces.ISafetyConstraintViewDataModel;
import xstampp.ui.common.ProjectManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
