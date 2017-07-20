package xstampp.astpa.ui.linkingSupport;

import java.util.List;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.ObserverValue;

public class DesignReq0LinkSupport extends LinkSupport<DataModelController> {

  public DesignReq0LinkSupport(DataModelController dataInterface, ObserverValue type) {
    super(dataInterface, type);
  }

  @Override
  protected List<ITableModel> getModels() {
    return getDataInterface().getAllDesignRequirements();
  }

  @Override
  protected String getLiteral() {
    return "DR0.";
  }

  @Override
  public String getTitle() {
    return "Design Requirement Links";
  }

}
