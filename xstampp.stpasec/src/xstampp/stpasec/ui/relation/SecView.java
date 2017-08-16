package xstampp.stpasec.ui.relation;

import xstampp.model.IDataModel;
import xstampp.stpapriv.ui.relation.PrivacyRelationsView;

public class SecView extends PrivacyRelationsView {
  public static final String ID = "stpasec.view.context";


  public SecView() {
    super();
  }

  public SecView(IDataModel model) {
    super(model);
  }
  @Override
  protected String[] getCA_PROPS_COLUMNS() {
    return new String[] { CONTROL_ACTIONS, UNSECURE_CONTROL_ACTIONS, SECURITY_CRITICAL,
        SAFETY_CRITICAL, COMMENTS };
  }
}
