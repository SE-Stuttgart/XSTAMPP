package xstampp.astpa.ui.linkingSupport;

import java.util.List;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.ObserverValue;

public class Step0ConstraintsLinkSupport extends LinkSupport<DataModelController> {

  public Step0ConstraintsLinkSupport(DataModelController dataInterface, ObserverValue type) {
    super(dataInterface, type);
  }

  @Override
  protected List<ITableModel> getModels() {
    return getDataInterface().getAllSafetyConstraints();
  }

  @Override
  protected String getLiteral() {
    return "S0.";
  }

  @Override
  public String getTitle() {
    return "Safety Constraint Links";
  }

}
