package xstampp.astpa.ui.linkingSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.ObserverValue;

public class Step2ConstraintsLinkSupport extends LinkSupport<DataModelController> {

  private List<ITableModel> safetyConstraints;

  public Step2ConstraintsLinkSupport(DataModelController dataInterface, ObserverValue type) {
    super(dataInterface, type);
  }

  @Override
  List<UUID> getAvailable() {
		setModelList(getDataInterface().getCausalFactorController().getSafetyConstraints());
    List<UUID> result = new ArrayList<>();
		for (ITableModel constraint : getModelList()) {
      result.add(constraint.getId());
    }
    result.removeAll(fetch());
    return result;
  }

  @Override
	protected String getLiteral() {
		return "S2.";
    }

  @Override
  public String getTitle() {
    return "Causal Safety Constraint Links";
  }

}
