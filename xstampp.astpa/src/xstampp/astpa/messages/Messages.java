package xstampp.astpa.messages;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
  private static final String BUNDLE_NAME = "xstampp.astpa.messages.messages"; //$NON-NLS-1$
  public static String ControlStructure_NameMustBeUnique;
  public static String ControlStructure_New;
  public static String DeleteControlStructureQuestion;
  public static String DeleteControlStructureTitle;
  static {
    // initialize resource bundle
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages() {
  }
}
