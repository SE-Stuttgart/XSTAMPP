package xstampp.astpa.usermanagement;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import xstampp.ui.common.shell.ModalShell;
import xstampp.usermanagement.api.IUser;
import xstampp.util.ColorManager;

public class SyncShell extends ModalShell {

  private Listener listener;
  private AstpaCollaborationSystem system;
  private IUser user;
  private int percent;

  public SyncShell(IUser user,AstpaCollaborationSystem system) {
    super("Get changes", PACKED);
    this.user = user;
    setAcceptLabel("Sync");
    this.system = system;
  }

  @Override
  protected boolean validate() {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  protected boolean doAccept() {
    setReturnValue(system.syncDataWithUser(user, listener));
    return true;
  }

  @Override
  protected void createCenter(Shell parent) {
    percent = 0;
    final Canvas bar = new Canvas(parent, SWT.None);
    bar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
    listener = new Listener() {
      
      @Override
      public void handleEvent(Event event) {
        percent = (int) event.data;
        bar.redraw();
      }
    };
    bar.addPaintListener(new PaintListener() {

      @Override
      public void paintControl(PaintEvent e) {
        Rectangle bounds = bar.getBounds();
        e.gc.setBackground(ColorManager.COLOR_WHITE);
        e.gc.fillRectangle(bounds.x, bounds.y, bounds.width, bounds.height);
        e.gc.setBackground(ColorManager.COLOR_GREEN);
        e.gc.fillRectangle(bounds.x, bounds.y, bounds.width * e.count/100, bounds.height);
      }
      
    });
  }

}
