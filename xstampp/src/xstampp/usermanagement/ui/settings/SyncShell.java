package xstampp.usermanagement.ui.settings;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import xstampp.ui.common.shell.ModalShell;
import xstampp.ui.common.shell.ModalShell.Style;
import xstampp.usermanagement.Messages;
import xstampp.usermanagement.api.CollaborationSystem;
import xstampp.usermanagement.api.ICollaborationSystem;
import xstampp.usermanagement.api.IUser;
import xstampp.util.ColorManager;

public class SyncShell extends ModalShell {

  private Listener listener;
  private ICollaborationSystem system;
  private List<IUser> users;

  public SyncShell(IUser user, CollaborationSystem system) {
    this(new ArrayList<IUser>(), system);
    this.users.add(user);
  }

  public SyncShell(List<IUser> users, ICollaborationSystem system) {
    super(Messages.SyncShell_0, Style.PACKED);
    setAcceptLabel(Messages.SyncShell_1);
    this.users = users;
    this.system = system;
  }

  @Override
  protected boolean validate() {
    return true;
  }

  @Override
  protected boolean doAccept() {
    int value = 0;
    for (IUser user : users) {
      value += system.syncDataWithUser(user, listener);
    }
    MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), Messages.SyncShell_2, String.format(Messages.SyncShell_3,value));
    setReturnValue(value);
    return true;
  }

  @Override
  protected void createCenter(Shell parent) {
    final Canvas bar = new Canvas(parent, SWT.None);
    bar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
    listener = new Listener() {

      @Override
      public void handleEvent(Event event) {
        bar.redraw();
      }
    };
    bar.addPaintListener(new PaintListener() {

      @Override
      public void paintControl(PaintEvent e) {
        int percent;
        try {
          percent = (int) e.data;
        } catch(Exception exc){
          percent = 0;
        }
        Rectangle bounds = bar.getBounds();
        e.gc.setBackground(ColorManager.COLOR_WHITE);
        e.gc.fillRectangle(bounds.x, bounds.y, bounds.width, bounds.height);
        e.gc.setBackground(ColorManager.COLOR_GREEN);
        e.gc.fillRectangle(bounds.x, bounds.y, bounds.width * percent, bounds.height);
      }

    });
  }

}
