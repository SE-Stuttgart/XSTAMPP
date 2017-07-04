package xstampp.astpa.ui.linkingSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import xstampp.astpa.model.interfaces.IHazardModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.ObserverValue;

public class HazardLinkSupport<M extends IHazardModel> extends LinkSupport<M> {

  public HazardLinkSupport(M dataInterface) {
    super(dataInterface);
  }

  @Override
  List<UUID> getAvailable() {
    List<ITableModel> hazards = getDataInterface().getAllHazards();
    List<UUID> result = new ArrayList<>();
    for (ITableModel haz : hazards) {
      result.add(haz.getId());
    }
    result.removeAll(fetch());
    return result;
  }

  @Override
  public String getText(UUID id) {
    ITableModel hazard = getDataInterface().getHazard(id);
    return "H-" + hazard.getNumber(); //$NON-NLS-1$
  }

  @Override
  public String getDescription(UUID id) {
    ITableModel hazard = getDataInterface().getHazard(id);
    return hazard.getDescription();
  }
  
  @Override
  public String getTitle() {
    return "Hazard Linking";
  }

  @Override
  public ObserverValue getLinkType() {
    return ObserverValue.HAZ_ACC_LINK;
  }

}
