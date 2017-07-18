package xstampp.astpa.ui.linkingSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.ObserverValue;

public class Step2ConstraintsLinkSupport extends LinkSupport<DataModelController> {

  private List<ITableModel> safetyConstraints;

  public Step2ConstraintsLinkSupport(DataModelController dataInterface) {
    super(dataInterface);
  }

  @Override
  List<UUID> getAvailable() {
    safetyConstraints = getDataInterface().getCausalFactorController().getSafetyConstraints();
    List<UUID> result = new ArrayList<>();
    for (ITableModel constraint : safetyConstraints) {
      result.add(constraint.getId());
    }
    result.removeAll(fetch());
    return result;
  }

  @Override
  public String getText(UUID id) {
    for (ITableModel constraint : safetyConstraints) {
      if (constraint.getId().equals(id)) {
        return "S2." + constraint.getNumber(); //$NON-NLS-1$
      }
    }
    return null;
  }

  @Override
  public String getDescription(UUID id) {
    ITableModel hazard = getDataInterface().getSafetyConstraint(id);
    return hazard.getDescription();
  }

  @Override
  public String getTitle() {
    return "Causal Safety Constraint Links";
  }

  @Override
  public ObserverValue getLinkType() {
    return ObserverValue.DESIGN_REQUIREMENT_STEP1;
  }

}
