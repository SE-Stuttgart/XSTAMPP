package xstampp.ui;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISourceProviderListener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.services.ISourceProviderService;

import xstampp.util.IUndoCallback;
import xstampp.util.STPAPluginUtils;
import xstampp.util.service.UndoRedoService;

public class HistoryView extends ViewPart implements ISourceProviderListener {

  private TableViewer listViewer;
  private UndoRedoService sourceProvider;
  private TableViewer redoListViewer;
  private Composite content;

  public HistoryView() {
    // TODO Auto-generated constructor stub
  }

  @Override
  public void createPartControl(Composite parent) {
    content = new Composite(parent, SWT.None);
    content.setLayout(new GridLayout(3, false));
    Composite redoComposite = new Composite(content, SWT.NONE);
    redoComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 3, 1));
    TableColumnLayout tableColumnLayout = new TableColumnLayout();
    redoComposite.setLayout(tableColumnLayout);

    redoListViewer = new TableViewer(redoComposite, SWT.V_SCROLL);
    redoListViewer.setContentProvider(new ArrayContentProvider());

    redoListViewer.getTable().setLinesVisible(false);
    redoListViewer.getTable().setHeaderVisible(false);

    TableViewerColumn labelColumn = new TableViewerColumn(redoListViewer, SWT.None);
    tableColumnLayout.setColumnData(labelColumn.getColumn(), new ColumnWeightData(10, 30, true));
    labelColumn.setLabelProvider(new ColumnLabelProvider() {
      @Override
      public String getText(Object element) {
        return ((IUndoCallback) element).getChangeMessage();
      }
    });
    new Label(content, SWT.SEPARATOR | SWT.HORIZONTAL);
    Button up = new Button(content, SWT.ARROW | SWT.UP);
    up.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        STPAPluginUtils.executeCommand("xstampp.command.undo");
      }
    });
    Button down = new Button(content, SWT.ARROW | SWT.DOWN);
    down.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        STPAPluginUtils.executeCommand("xstampp.command.redo");
      }
    });
    Composite tableComposite = new Composite(content, SWT.NONE);
    tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
    tableColumnLayout = new TableColumnLayout();
    tableComposite.setLayout(tableColumnLayout);

    listViewer = new TableViewer(tableComposite, SWT.V_SCROLL);
    listViewer.setContentProvider(new ArrayContentProvider());

    listViewer.getTable().setLinesVisible(false);
    listViewer.getTable().setHeaderVisible(false);

    labelColumn = new TableViewerColumn(listViewer, SWT.None);
    tableColumnLayout.setColumnData(labelColumn.getColumn(), new ColumnWeightData(10, 30, true));
    labelColumn.setLabelProvider(new ColumnLabelProvider() {
      @Override
      public String getText(Object element) {
        return ((IUndoCallback) element).getChangeMessage();
      }
    });
    listViewer.setInput(new IUndoCallback[0]);

    ISourceProviderService service = (ISourceProviderService) PlatformUI.getWorkbench()
        .getService(ISourceProviderService.class);
    sourceProvider = (UndoRedoService) service.getSourceProvider(UndoRedoService.CAN_REDO);
    sourceProvider.addSourceProviderListener(this);
    service.getSourceProvider(UndoRedoService.CAN_UNDO).addSourceProviderListener(this);
    listViewer.setInput(sourceProvider.getUndoStack());
    redoListViewer.setInput(sourceProvider.getRedoStack());
  }

  @Override
  public void setFocus() {
  }

  @Override
  public void sourceChanged(int sourcePriority, String sourceName, Object sourceValue) {
    redoListViewer.setInput(sourceProvider.getRedoStack());
    List<IUndoCallback> asList = Arrays.asList(sourceProvider.getUndoStack());
    Collections.reverse(asList);
    listViewer.setInput(asList.toArray(new IUndoCallback[0]));
    content.layout();
  }

  @Override
  public void sourceChanged(int sourcePriority, Map sourceValuesByName) {
    listViewer.setInput(sourceProvider.getUndoStack());
  }

  @Override
  public void dispose() {
    ISourceProviderService service = (ISourceProviderService) PlatformUI.getWorkbench()
        .getService(ISourceProviderService.class);
    service.getSourceProvider(UndoRedoService.CAN_REDO).removeSourceProviderListener(this);
    service.getSourceProvider(UndoRedoService.CAN_UNDO).removeSourceProviderListener(this);
  }
}
