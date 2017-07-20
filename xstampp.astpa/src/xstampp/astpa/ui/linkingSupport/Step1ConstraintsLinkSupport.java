package xstampp.astpa.ui.linkingSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.ObserverValue;

public class Step1ConstraintsLinkSupport extends LinkSupport<DataModelController> {

  public Step1ConstraintsLinkSupport(DataModelController dataInterface, ObserverValue type) {
    super(dataInterface, type);
  }

  @Override
  List<UUID> getAvailable() {
		setModelList(getDataInterface().getCorrespondingSafetyConstraints());
    List<UUID> result = new ArrayList<>();
		for (ITableModel constraint : getModelList()) {
      result.add(constraint.getId());
    }
    result.removeAll(fetch());
    return result;
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
