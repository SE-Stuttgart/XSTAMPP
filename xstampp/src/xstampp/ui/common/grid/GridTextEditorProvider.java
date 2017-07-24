package xstampp.ui.common.grid;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import xstampp.util.ColorManager;

public class GridTextEditorProvider {
  private String returnString;

  /**
   * 
   * @param text the initial text of the editor
   * @param textSize the size of the text input
   * @param size the overall 
   * @return
   */
  public String open(String text, Rectangle size) {
    final Shell shell = new Shell(Display.getCurrent().getActiveShell(),
        SWT.APPLICATION_MODAL | SWT.RESIZE);
    size.width = Math.max(size.width, 150);
    returnString = null;
    final GridLayout layout = new GridLayout();
    shell.setLayout(layout);
    
    shell.setLocation(size.x - 5,size.y);
    final Text textField = new Text(shell, SWT.WRAP | SWT.V_SCROLL);
    textField.setLocation(new Point(0, 0));
    textField.setText(text);
    GridData layoutData = new GridData(size.width,size.height);
    layoutData.horizontalAlignment = SWT.FILL;
    layoutData.verticalAlignment = SWT.FILL;
    layoutData.grabExcessHorizontalSpace = true;
    layoutData.grabExcessVerticalSpace = true;
    textField.setLayoutData(layoutData);
    Composite buttonComp = new Composite(shell, SWT.None);
    buttonComp.setLayout(new GridLayout(2,true));
    buttonComp.setLayoutData(new GridData(SWT.END, SWT.BOTTOM, false, false));
    GridData data = new GridData(70,20);
    data.horizontalAlignment = SWT.RIGHT;
    data.grabExcessHorizontalSpace = false;
    data.grabExcessVerticalSpace = false;
    data.horizontalAlignment = SWT.RIGHT;
    Button okButton = new Button(buttonComp, SWT.PUSH);
    okButton.setForeground(ColorManager.COLOR_WHITE);
    okButton.setBackground(ColorManager.COLOR_GREEN);
    okButton.setText("Save");
    okButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent ev) {
        returnString = textField.getText();
        shell.close();
      }
    });
    okButton.setLayoutData(data);

    Button cancelButton = new Button(buttonComp, SWT.PUSH);
    cancelButton.setText("Cancel");
    cancelButton.setSize(20, 70);
    cancelButton.setForeground(ColorManager.COLOR_WHITE);
    cancelButton.setBackground(ColorManager.COLOR_RED);
    cancelButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent ev) {
        shell.close();
      }
    });
    cancelButton.setLayoutData(data);
    shell.pack();
    shell.addControlListener(new ControlAdapter() {
      @Override
      public void controlResized(ControlEvent e) {
        shell.layout();
      }
    });
    shell.open();
    while (!shell.isDisposed()) {
      if (!Display.getDefault().readAndDispatch()) {
        Display.getDefault().sleep();
      }
    }
    return returnString;

  }
}
