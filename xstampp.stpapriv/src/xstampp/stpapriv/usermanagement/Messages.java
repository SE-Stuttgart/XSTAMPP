package xstampp.stpapriv.usermanagement;

import org.eclipse.osgi.util.NLS;

import xstampp.stpapriv.usermanagement.Messages;

public class Messages extends NLS {
  private static final String BUNDLE_NAME = "xstampp.stpapriv.usermanagement.messages"; //$NON-NLS-1$
  public static String ControlStructure_NameMustBeUnique;
  public static String ControlStructure_New;
  public static String NoAccessRights_AdminNeeded;
  public static String NoAccessRights_title;
  public static String ResponsibilityEditingSupport_0;
  public static String ResponsibilitySettings_0;
  static {
    // initialize resource bundle
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages() {
  }
}
