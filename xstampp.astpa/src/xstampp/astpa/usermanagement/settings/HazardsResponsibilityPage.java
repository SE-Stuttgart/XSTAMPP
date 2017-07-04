package xstampp.astpa.usermanagement.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import xstampp.astpa.model.hazacc.ATableModel;
import xstampp.astpa.model.interfaces.IHazardViewDataModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.ui.common.ProjectManager;

public class HazardsResponsibilityPage extends EntryResponsibilitiesPage<ATableModel> {

  @Override
  protected List<ATableModel> getEntryList(UUID modelId) {
    List<ATableModel> list = new ArrayList<>();
    List<ITableModel> allAccidents = ((IHazardViewDataModel) ProjectManager.getContainerInstance()
        .getDataModel(modelId)).getAllHazards();
    for (ITableModel model : allAccidents) {
      list.add((ATableModel) model);
    }
    return list;
  }

}
