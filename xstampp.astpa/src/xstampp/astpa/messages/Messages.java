package xstampp.astpa.messages;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
  private static final String BUNDLE_NAME = "xstampp.astpa.messages.messages"; //$NON-NLS-1$
  public static String ControlStructure_NameMustBeUnique;
  public static String ControlStructure_New;
  public static String CSContextMenuProvider_MoveDOWN;
  public static String CSContextMenuProvider_MoveUP;
  public static String CSContextMenuProvider_MoveUP_DOWNAllWay;
  public static String CSContextMenuProvider_MoveUP_DOWNOneStep;
  public static String DeleteControlStructureQuestion;
  public static String DeleteControlStructureTitle;
  public static String ListOfCAEditPart_ToolTip0;
  public static String ProjectSpecifics_GeneralSettings;
  public static String ProjectSpecifics_ReopenHazardView;
  public static String ProjectSpecifics_UseCausalScenarios;
  public static String ProjectSpecifics_UseCausalScenariosTip;
  public static String ProjectSpecifics_UseSeverity;
  public static String ProjectSpecifics_UseSeverityTip;
  static {
    // initialize resource bundle
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages() {
  }
}
