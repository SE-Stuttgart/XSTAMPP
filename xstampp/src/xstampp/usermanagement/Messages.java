package xstampp.usermanagement;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
  private static final String BUNDLE_NAME = "xstampp.usermanagement.messages"; //$NON-NLS-1$
  public static String UserManagementPage_0;
  public static String UserManagementPage_3;
  public static String UserManagementPage_5;
  public static String UserManagementPage_6;
  static {
    // initialize resource bundle
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages() {
  }
}
