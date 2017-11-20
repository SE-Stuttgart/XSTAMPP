package xstampp.stpapriv.ui.linkSuppport;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.astpa.ui.linkingSupport.AccidentLinkSupport;

public class LossesLinkSupport extends AccidentLinkSupport {

  public LossesLinkSupport(DataModelController dataInterface, LinkingType type) {
    super(dataInterface, type);
  }

  @Override
  public String getTitle() {
    return "Links to Losses";
  }
}
