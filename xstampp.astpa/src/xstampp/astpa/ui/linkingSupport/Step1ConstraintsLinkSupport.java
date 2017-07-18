package xstampp.astpa.ui.linkingSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.ObserverValue;

public class Step1ConstraintsLinkSupport extends LinkSupport<DataModelController> {

  public Step1ConstraintsLinkSupport(DataModelController dataInterface) {
    super(dataInterface);
  }

  @Override
  List<UUID> getAvailable() {
    List<ITableModel> safetyConstraints = getDataInterface().getCorrespondingSafetyConstraints();
    List<UUID> result = new ArrayList<>();
    for (ITableModel constraint : safetyConstraints) {
      result.add(constraint.getId());
    }
    result.removeAll(fetch());
    return result;
  }

  @Override
  public String getText(UUID id) {
    ITableModel constraint = getDataInterface().getSafetyConstraint(id);
    return "S1." + constraint.getNumber(); //$NON-NLS-1$
  }

  @Override
  public String getDescription(UUID id) {
    ITableModel hazard = getDataInterface().getSafetyConstraint(id);
    return hazard.getDescription();
  }
  
  @Override
  public String getTitle() {
    return "Corresponding Safety Constraint Links";
  }

  @Override
  public ObserverValue getLinkType() {
    return ObserverValue.DESIGN_REQUIREMENT_STEP1;
  }

}
