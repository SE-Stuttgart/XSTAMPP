package xstampp.astpa.ui.linkingSupport;

import java.util.List;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.ObserverValue;

public class HazardLinkSupport extends LinkSupport<DataModelController> {

  public HazardLinkSupport(DataModelController dataInterface, ObserverValue type) {
    super(dataInterface, type);
  }

  @Override
  protected List<ITableModel> getModels() {
    return getDataInterface().getAllHazards();
  }

  @Override
  protected String getLiteral() {
    return "H-";
  }

  @Override
  public String getTitle() {
    return "Hazard Linking";
  }
}
