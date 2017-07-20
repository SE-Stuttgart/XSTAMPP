package xstampp.astpa.ui.linkingSupport;

import java.util.List;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.interfaces.IAccidentModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.ObserverValue;

public class AccidentLinkSupport extends LinkSupport<DataModelController> {

  public AccidentLinkSupport(DataModelController dataInterface, ObserverValue type) {
    super(dataInterface, type);
  }

  @Override
  protected List<ITableModel> getModels() {
    return getDataInterface().getAllAccidents();
  }

  @Override
  protected String getLiteral() {
    return "A-";
  }

  @Override
  public String getTitle() {
    return "Accident Links";
  }

}
