package xstampp.stpapriv.ui.linkSuppport;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.ui.linkingSupport.AccidentLinkSupport;
import xstampp.model.ObserverValue;

public class LossesLinkSupport extends AccidentLinkSupport {

  public LossesLinkSupport(DataModelController dataInterface, ObserverValue type) {
    super(dataInterface, type);
  }

  @Override
  public String getTitle() {
    return "Links to Losses";
  }
}
