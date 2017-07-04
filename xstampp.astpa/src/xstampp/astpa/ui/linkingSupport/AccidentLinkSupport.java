package xstampp.astpa.ui.linkingSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import xstampp.astpa.model.interfaces.IAccidentModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.ObserverValue;

public class AccidentLinkSupport<M extends IAccidentModel> extends LinkSupport<M> {

  public AccidentLinkSupport(M dataInterface) {
    super(dataInterface);
  }

  @Override
  List<UUID> getAvailable() {
    List<ITableModel> hazards = getDataInterface().getAllAccidents();
    List<UUID> result = new ArrayList<>();
    for (ITableModel haz : hazards) {
      result.add(haz.getId());
    }
    result.removeAll(fetch());
    return result;
  }

  @Override
  public String getText(UUID id) {
    ITableModel hazard = getDataInterface().getAccident(id);
    return "A-" + hazard.getNumber(); //$NON-NLS-1$
  }

  @Override
  public String getDescription(UUID id) {
    ITableModel hazard = getDataInterface().getAccident(id);
    return hazard.getDescription();
  }
  
  @Override
  public String getTitle() {
    return "Accident Links";
  }

  @Override
  public ObserverValue getLinkType() {
    return ObserverValue.HAZ_ACC_LINK;
  }

}
