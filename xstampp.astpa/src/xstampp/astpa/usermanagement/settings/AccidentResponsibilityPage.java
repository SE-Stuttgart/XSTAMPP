package xstampp.astpa.usermanagement.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import xstampp.astpa.model.ATableModel;
import xstampp.astpa.model.interfaces.IAccidentViewDataModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.ui.common.ProjectManager;

public class AccidentResponsibilityPage extends EntryResponsibilitiesPage<ATableModel> {

  @Override
  protected List<ATableModel> getEntryList(UUID modelId) {
    List<ATableModel> list = new ArrayList<>();
    List<ITableModel> allAccidents = ((IAccidentViewDataModel) ProjectManager.getContainerInstance().getDataModel(modelId))
        .getAllAccidents();
    for(ITableModel model : allAccidents) {
      list.add((ATableModel) model);
    }
    // TODO Auto-generated method stub
    return list;
  }

}
