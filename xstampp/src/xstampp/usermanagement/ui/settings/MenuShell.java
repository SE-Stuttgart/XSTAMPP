package xstampp.usermanagement.ui.settings;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import xstampp.ui.navigation.IProjectSelection;
import xstampp.usermanagement.Messages;
import xstampp.usermanagement.UserSystem;
import xstampp.util.STPAPluginUtils;

public class MenuShell extends MouseTrackAdapter {

  private static final String _COMMAND = "xstampp.command.openProjectSettings"; //$NON-NLS-1$
  private static final String _PARAM = "xstampp.commandParameter.project.settings"; //$NON-NLS-1$
  private static final String _PAGE = "xstampp.settings.users"; //$NON-NLS-1$
  private Shell shell;

  public MenuShell(final IProjectSelection selection, final UserSystem system, Point location) {
    shell = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.None);
    shell.setLocation(location);
    shell.addShellListener(new ShellAdapter() {
      @Override
      public void shellDeactivated(ShellEvent e) {
        shell.close();
      }
    });
    
    GridLayout layout = new GridLayout();
    shell.setLayout(layout);
    Label settings = new Label(shell, SWT.None);
    settings.setText(Messages.MenuShell_openUserSettings);
    settings.setToolTipText(Messages.MenuShell_UserSettingsToolTip);
    settings.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
    settings.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseUp(MouseEvent e) {
        shell.close();
        Map<String, String> values = new HashMap<>();
        values.put(_PARAM, _PAGE);
        STPAPluginUtils.executeParaCommand(_COMMAND, values);
      }
    });
    settings.setBackground(
        PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
    settings.addMouseTrackListener(this);
    Label logout = new Label(shell, SWT.PUSH);
    logout.setText(Messages.MenuShell_Logout);
    logout.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
    logout.addMouseTrackListener(this);
    logout.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseUp(MouseEvent e) {
        shell.close();
        system.logout();
        selection.deaktivate();
      }
    });
    logout.setBackground(
        PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
    shell.pack();
    shell.open();
  }

  @Override
  public void mouseEnter(MouseEvent e) {
    ((Label) e.getSource()).setBackground(
        PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_GRAY));
  }

  @Override
  public void mouseExit(MouseEvent e) {
    ((Label) e.getSource()).setBackground(
        PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
  }
}
