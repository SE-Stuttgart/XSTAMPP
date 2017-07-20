package xstampp.astpa.ui.linkingSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import xstampp.astpa.model.interfaces.IHazardModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.ObserverValue;

public class HazardLinkSupport<M extends IHazardModel> extends LinkSupport<M> {

  public HazardLinkSupport(M dataInterface, ObserverValue type) {
    super(dataInterface, type);
  }

  @Override
  List<UUID> getAvailable() {
		setModelList(getDataInterface().getAllHazards());
    List<UUID> result = new ArrayList<>();
		for (ITableModel haz : getModelList()) {
      result.add(haz.getId());
    }
    result.removeAll(fetch());
    return result;
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
