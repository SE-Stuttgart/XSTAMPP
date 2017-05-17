package xstampp.usermanagement;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
  private static final String BUNDLE_NAME = "xstampp.usermanagement.messages"; //$NON-NLS-1$
  public static String AbstractUserShell_Create;
  public static String CollaborationSettings_CreateWorkingCopy;
  public static String CollaborationSettings_PullChanges;
  public static String CollaborationSettings_WorkingCopyToolTip;
  public static String CreateAdminShell_CreateAdmin;
  public static String CreateUserShell_CreateReadOnlyUser;
  public static String CreateUserShell_CreateUser;
  public static String CreateUserShell_UsernameExists;
  public static String ProjectSettingsShell_Collaboration;
  public static String ProjectSettingsShell_Title;
  public static String ProjectSettingsShell_UserManagementTitle;
  public static String UserContribution_LoggedAsAdministrator;
  public static String UserContribution_LoggedAsUser;
  public static String UserContribution_LoginLabel;
  public static String UserContribution_OpenUserSettingsTip;
  public static String UserContribution_ReadOnlyAccsessToolTip;
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
