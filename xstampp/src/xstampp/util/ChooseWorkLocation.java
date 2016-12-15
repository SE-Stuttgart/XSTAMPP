package xstampp.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import messages.Messages;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import xstampp.preferences.IPreferenceConstants;
import xstampp.ui.common.ProjectManager;

/**
 * 
 * @author Lukas Balzer
 * @since version 2.0.0
 * 
 */
public class ChooseWorkLocation extends TitleAreaDialog {
  private List<String> recentLocs;
  private Combo workspacePathCombo;
  private Button rememeberWS;
  /*
   * Flag set when the workbench need to be restarted to create a new workspace
   * to store the remember workspace preferences
   */
  private static final String INIT_WORKSPACE = "initialize Workspace"; //$NON-NLS-1$
  private static final int STACK = 8;
  private static final String SEPERATOR = ","; //$NON-NLS-1$
  private static final String WS_IDENTIFIER = "stamp"; //$NON-NLS-1$
  private static final Preferences LOCAL_PREFERENCES = Preferences.userNodeForPackage(ChooseWorkLocation.class);

  /**
   * creates a new instance of the Workspace choose Dialog and creates a
   * resentWs Array by splitting the WS_RECENT String in the Preference store by
   * the SEPERATOR
   * 
   * @author Lukas Balzer
   * 
   * @param parentShell
   *          the parent Shell
   */
  public ChooseWorkLocation(Shell parentShell) {
    super(parentShell);
    this.recentLocs = new ArrayList<>();
    String recentLocations = ChooseWorkLocation.LOCAL_PREFERENCES.get(IPreferenceConstants.WS_RECENT, null);
    if (recentLocations != null) {
      String[] pathArray = recentLocations.split(ChooseWorkLocation.SEPERATOR);
      for (int i = 0; i < ChooseWorkLocation.STACK; i++) {
        if (pathArray.length > i) {
          this.recentLocs.add(pathArray[i]);
        }
      }
    }

  }

  @Override
  protected Control createDialogArea(Composite parent) {
    this.setTitle(Messages.WorkspaceSet);

    Composite inner = new Composite(parent, SWT.NONE);

    inner.setLayout(new FormLayout());
    inner.setLayoutData(new GridData(GridData.FILL_BOTH));

    FormData data = new FormData();
    data.left = new FormAttachment(2);
    data.top = new FormAttachment(5);
    // label on left
    CLabel label = new CLabel(inner, SWT.NONE);
    label.setText(Messages.WorkspaceRootPath);
    label.setLayoutData(data);

    data = new FormData();
    data.right = new FormAttachment(98);
    data.top = new FormAttachment(5);
    // browse button on right
    Button browse = new Button(inner, SWT.PUSH);
    browse.setText("Browse..."); //$NON-NLS-1$
    browse.setLayoutData(data);
    browse.addListener(SWT.Selection, new Listener() {

      @Override
      public void handleEvent(Event event) {
        DirectoryDialog dd = new DirectoryDialog(ChooseWorkLocation.this.getParentShell());
        dd.setText("Select the Workspace Root"); //$NON-NLS-1$
        dd.setMessage(Messages.WorkspaceSetDesc);
        dd.setFilterPath(ChooseWorkLocation.this.workspacePathCombo.getText());
        String pick = dd.open();
        if ((pick == null) && (ChooseWorkLocation.this.workspacePathCombo.getText().length() == 0)) {
          ChooseWorkLocation.this.setMessage(Messages.IlegalPath, IMessageProvider.ERROR);
        } else if (pick != null) {
          ChooseWorkLocation.this.setMessage(""); //$NON-NLS-1$
          ChooseWorkLocation.this.workspacePathCombo.setText(pick);
        }
      }
    });

    data = new FormData();
    data.left = new FormAttachment(label, 2);
    data.right = new FormAttachment(browse, -2);
    data.top = new FormAttachment(5);
    // combo in middle
    this.workspacePathCombo = new Combo(inner, SWT.BORDER);
    this.workspacePathCombo.setLayoutData(data);
    if (!this.recentLocs.isEmpty()) {
      this.workspacePathCombo.setItems(this.recentLocs.toArray(new String[0]));
      this.workspacePathCombo.select(0);
    }

    data = new FormData();
    data.left = new FormAttachment(label, 2);
    data.top = new FormAttachment(this.workspacePathCombo, 5);
    // checkbox below
    this.rememeberWS = new Button(inner, SWT.CHECK);
    this.rememeberWS.setText(Messages.RememberWorkspace);
    this.rememeberWS.setLayoutData(data);
    this.rememeberWS
        .setSelection(ChooseWorkLocation.LOCAL_PREFERENCES.getBoolean(IPreferenceConstants.WS_REMEMBER, false));
    this.rememeberWS.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent arg0) {
        ChooseWorkLocation.LOCAL_PREFERENCES.putBoolean(IPreferenceConstants.WS_REMEMBER,
            ChooseWorkLocation.this.rememeberWS.getSelection());

      }

    });
    return inner;

  }

  /**
   * @author Lukas Balzer
   * 
   * @return the last workspace location or null
   */
  public static String getLastWSLocation() {
    return ChooseWorkLocation.LOCAL_PREFERENCES.get(IPreferenceConstants.WS_LAST_LOCATION, null);
  }

  @Override
  protected void okPressed() {
    ChooseWorkLocation.LOCAL_PREFERENCES.putBoolean(IPreferenceConstants.WS_REMEMBER, this.rememeberWS.getSelection());
    if (!this.addRecentWS(this.workspacePathCombo.getText())) {
      return;
    }
    super.okPressed();
  }

  private boolean addRecentWS(String path) {
    String error = ChooseWorkLocation.checkWorkspaceDirectory(this.getShell(), path, true);
    if (error != null) {
      this.setErrorMessage(error);
      return false;
    }
    this.recentLocs.add(0, path);
    for (int i = 1; i < ChooseWorkLocation.STACK; i++) {
      if ((this.recentLocs.size() > i) && path.equals(this.recentLocs.get(i))) {
        // if the value of the current memory and the new path are equal
        // the memory is is deleted from the list
        this.recentLocs.remove(i);
      }
    }
    ChooseWorkLocation.LOCAL_PREFERENCES.put(IPreferenceConstants.WS_RECENT, this.recentWsString());
    return true;
  }

  private String recentWsString() {
    StringBuffer recentString = new StringBuffer();
    for (int i = 0; i < ChooseWorkLocation.STACK; i++) {
      if (i != 0) {
        recentString.append(ChooseWorkLocation.SEPERATOR);
      }
      if (this.recentLocs.size() > i) {
        recentString.append(this.recentLocs.get(i));
      } else {
        break;
      }
    }
    return recentString.toString();

  }

  /**
   * Ensures a workspace directory is OK in regards of reading/writing, etc.
   * This method will get called externally as well.
   * 
   * @param parentShell
   *          Shell parent shell
   * @param workspaceLocation
   *          Directory the user wants to use
   * @param askCreate
   *          Whether to ask if to create the workspace in this location or not
   *          if it does not exist already
   * @return null if everything is ok, or an error message if not
   */
  public static String checkWorkspaceDirectory(Shell parentShell, String workspaceLocation, boolean askCreate) {
    File wsFile = new File(workspaceLocation);
    if (!wsFile.exists()) {
      if (askCreate && MessageDialog.openConfirm(parentShell, Messages.NewDir,
          Messages.DirDoesNotExist + ", " + Messages.DoYouWantToCreateIt)) { //$NON-NLS-1$
        ChooseWorkLocation.createWorkspace(wsFile);

      } else {
        return Messages.PathIsNoDir;
      }
    }

    if (!wsFile.canRead()) {
      return Messages.DirIsNotReadable;
    }

    if (!wsFile.isDirectory()) {
      return Messages.PathIsNoDir;
    }

    // since an identifier is added to each newly created workspace, we can
    // easily check
    // whether the selected path is a workspace or not
    File wsMarkerTest = new File(workspaceLocation + File.separator + ChooseWorkLocation.WS_IDENTIFIER);

    if (!wsMarkerTest.exists()) {
      if (askCreate && MessageDialog.openConfirm(parentShell, Messages.WorkspaceNew,
          String.format(Messages.WorkspaceIsNoWorkspaceYet, workspaceLocation))) {

        ChooseWorkLocation.createWorkspace(wsFile);
      } else {
        return Messages.WorkspaceSetDesc;
      }
    }

    return null;
  }

  private static String createWorkspace(File workspaceFile) {
    try {
      workspaceFile.mkdirs();
      // if a workspace is created he gets a file identifier (which is an
      // empty file)
      // to recognize a workspace related to this platform
      File wsMarker = new File(workspaceFile.getAbsolutePath() + File.separator + ChooseWorkLocation.WS_IDENTIFIER);
      wsMarker.createNewFile();
      // if remember workspace is set false, initWorkspce stores to reset it
      // once the workspace is initialized
      ChooseWorkLocation.LOCAL_PREFERENCES.putBoolean(INIT_WORKSPACE, !shouldRememberWS());
      ChooseWorkLocation.LOCAL_PREFERENCES.putBoolean(IPreferenceConstants.WS_REMEMBER, true);

    } catch (Exception err) {
      return Messages.CannotCreateDir;
    }
    if (!workspaceFile.exists()) {
      return Messages.DirDoesNotExist;
    }

    return null;
  }

  /**
   * method to reset the 'remember workspace' preferences after initializing a
   * workspace.
   * 
   * @author Lukas Balzer
   *
   */
  public static void initializeWs() {
    if (ChooseWorkLocation.LOCAL_PREFERENCES.getBoolean(INIT_WORKSPACE, false)) {
      ChooseWorkLocation.LOCAL_PREFERENCES.putBoolean(INIT_WORKSPACE, false);
      ChooseWorkLocation.LOCAL_PREFERENCES.putBoolean(IPreferenceConstants.WS_REMEMBER, false);

    }
  }

  /**
   * if true the workspace has to be initialized, for that purpose it is
   * neccesary to restart the Platform.
   * 
   * @author Lukas Balzer
   *
   */
  public static void initiateWorkspace() {
    if (ChooseWorkLocation.LOCAL_PREFERENCES.getBoolean(INIT_WORKSPACE, false)) {
      PlatformUI.getWorkbench().restart();
      ProjectManager.getLOGGER().debug("restarted Workbench");
    }
  }

  /**
   * 
   * @author Lukas Balzer
   * 
   * @return the Last used Workspace Location or <code>null</code> no recent
   *         workspace is available
   */
  public static String getLastUsedWorkspace() {
    String lastWorkspace = ChooseWorkLocation.LOCAL_PREFERENCES.get(IPreferenceConstants.WS_RECENT, null);
    if (lastWorkspace == null) {
      return null;
    }
    int lastIndex = lastWorkspace.indexOf(ChooseWorkLocation.SEPERATOR);
    if (lastIndex > 0) {
      lastWorkspace = lastWorkspace.substring(0, lastIndex);
    }
    File f = new File(lastWorkspace);
    lastWorkspace = f.getAbsolutePath();
    return lastWorkspace;
  }

  /**
   * 
   * @author Lukas Balzer
   * 
   * @return whether application should remember the ws or not, false if no
   *         choice is recognised
   */
  public static boolean shouldRememberWS() {

    return ChooseWorkLocation.LOCAL_PREFERENCES.getBoolean(IPreferenceConstants.WS_REMEMBER, false);

  }

  /**
   * 
   * @author Lukas Balzer
   * @param check
   *          if the function should check the workspace instead of just
   *          returning the preference
   * 
   * @return whether application should remember the ws or not, false if no
   *         choice is recognised
   */
  public static boolean shouldRememberWS(boolean check) {
    if (check && shouldRememberWS()) {
      if (checkWorkspaceDirectory(null, getLastUsedWorkspace(), false) != null) {
        ProjectManager.getLOGGER().debug(Messages.PreferedWSDoNotExist);
        return false;
      }
    }
    return shouldRememberWS();

  }
}
