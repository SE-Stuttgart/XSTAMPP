package xstampp.astpa.ui.linkingSupport;

import java.util.List;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.ObserverValue;

public class Step1ConstraintsLinkSupport extends LinkSupport<DataModelController> {

  public Step1ConstraintsLinkSupport(DataModelController dataInterface, ObserverValue type) {
    super(dataInterface, type);
  }

  @Override
  protected List<ITableModel> getModels() {
    return getDataInterface().getCorrespondingSafetyConstraints();
  }

  @Override
	protected String getLiteral() {
		return "S1.";
  }

  @Override
	public String getTitle() {
		return "Corresponding Safety Constraint Links";
  }

}
