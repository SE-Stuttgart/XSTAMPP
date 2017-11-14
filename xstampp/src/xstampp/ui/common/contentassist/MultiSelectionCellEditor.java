package xstampp.ui.common.contentassist;

import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * 
 * 
 * @author Lukas Balzer
 *
 */
public class MultiSelectionCellEditor extends CellEditor {

  @FunctionalInterface
  public interface LinkProposalProvider {
    void setInput(CheckboxTableViewer viewer);
  }

  private Listener listener;
  private Listener focusListener;

  private List<LinkProposal> proposals;

  private Shell popup;

  private Composite parent;
  private CheckboxTableViewer listViewer;

  public MultiSelectionCellEditor(Composite parent, List<LinkProposal> proposals, int style) {
    super(parent, style);
    this.proposals = proposals;
  }

  @Override
  protected Control createControl(Composite parent) {

    this.parent = parent;
    createPopup();
    return new Composite(parent, 0);
  }

  private void createPopup() {
    popup = new Shell(parent.getShell(), SWT.NO_TRIM | SWT.ON_TOP);
    TableItem[] selection = ((Table) parent).getSelection();
    Rectangle bounds = selection[0].getBounds();
    TableColumn[] columns = ((Table) parent).getColumns();
    int x = columns[0].getWidth();
    x += columns[1].getWidth();
    int width = columns[2].getWidth();
    Point pos = parent.toDisplay(x, bounds.y);
    popup.setBounds(pos.x, pos.y, width, 150);
    GridLayout layout = new GridLayout(4, false);
    popup.setLayout(layout);

    createTable(popup);

    Button selectAllButton = new Button(popup, SWT.PUSH);
    selectAllButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
    selectAllButton.setText("Select All");
    selectAllButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        proposals.forEach((prop) -> prop.setSelected(true));
        listViewer.setAllChecked(true);
      }
    });
    
    Button deselectAllButton = new Button(popup, SWT.PUSH);
    deselectAllButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
    deselectAllButton.setText("deselect All");
    deselectAllButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        proposals.forEach((prop) -> prop.setSelected(false));
        listViewer.setAllChecked(false);
      }
    });
    
    Button okButton = new Button(popup, SWT.PUSH);
    okButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
    okButton.setText("Ok");
    okButton.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        fireApplyEditorValue();
        deactivate();
        markDirty();
        setValueValid(true);
        popup.setVisible(false);
      }
    });

    Button cancelButton = new Button(popup, SWT.PUSH);
    cancelButton.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false));
    cancelButton.setText("Cancel");
    cancelButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        fireCancelEditor();
        deactivate();
        popup.setVisible(false);
      }
    });

    popup.addFocusListener(new FocusAdapter() {

      @Override
      public void focusLost(FocusEvent e) {
        popup.close();
        deactivate();
      }
    });
    focusListener = (event) -> popup.setFocus();
    popup.setVisible(true);
  }

  private void createTable(Composite backComp) {
    Composite tableComposite = new Composite(backComp, SWT.NONE);
    tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
    TableColumnLayout tableColumnLayout = new TableColumnLayout();
    tableComposite.setLayout(tableColumnLayout);

    listViewer = CheckboxTableViewer.newCheckList(tableComposite, SWT.V_SCROLL | SWT.CHECK);
    listViewer.setContentProvider(new ArrayContentProvider());
    listViewer.setCheckStateProvider(new ICheckStateProvider() {

      @Override
      public boolean isGrayed(Object element) {
        return false;
      }

      @Override
      public boolean isChecked(Object element) {
        return ((LinkProposal) element).isSelected();
      }
    });
    
    listViewer.addCheckStateListener((event) -> {
      ((LinkProposal) event.getElement()).setSelected(event.getChecked()); 
    });
    listViewer.getTable().setLinesVisible(false);
    listViewer.getTable().setHeaderVisible(false);

    TableViewerColumn labelColumn = new TableViewerColumn(listViewer, SWT.CHECK);
    tableColumnLayout.setColumnData(labelColumn.getColumn(), new ColumnWeightData(10, 30, true));
    labelColumn.setLabelProvider(new ColumnLabelProvider() {
      @Override
      public String getText(Object element) {
        return ((LinkProposal) element).getLabel();
      }
    });
    listener = (event) -> ((LinkProposalProvider) event.data).setInput(listViewer);
  }

  @Override
  protected Object doGetValue() {
    return proposals;
  }

  @Override
  protected void doSetFocus() {
    this.focusListener.handleEvent(null);
  }

  @Override
  protected void doSetValue(Object value) {
    Event event = new Event();
    @SuppressWarnings("unchecked")
    LinkProposalProvider data = (viewer) -> {
      this.proposals = (List<LinkProposal>) value;
      viewer.setInput(value);
    };
    event.data = data;
    listener.handleEvent(event);
  }

  @Override
  public void activate() {
    super.activate();
    popup.setVisible(true);
  }

  @Override
  protected void focusLost() {
    popup.close();
    super.focusLost();
  }

  @Override
  protected void keyReleaseOccured(KeyEvent keyEvent) {
    if (keyEvent.character == '\u001b') { // Escape character
      fireCancelEditor();
    }
  }

  @Override
  public LayoutData getLayoutData() {
    LayoutData result = new LayoutData();
    Control control = getControl();
    if (control != null) {
      result.minimumWidth = 70;
    }
    return result;
  }
}
