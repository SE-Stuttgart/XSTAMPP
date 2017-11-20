package xstampp.ui;

import java.util.Map;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISourceProviderListener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.services.ISourceProviderService;

import xstampp.util.ColorManager;
import xstampp.util.IUndoCallback;
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
    content.setLayout(new GridLayout());
    content.setBackground(ColorManager.COLOR_GREEN);
    Composite redoComposite = new Composite(content, SWT.NONE);
    redoComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
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
    Label seperator = new Label(content, SWT.SEPARATOR | SWT.HORIZONTAL);
    Composite tableComposite = new Composite(content, SWT.NONE);
    tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
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
    // TODO Auto-generated method stub

  }

  @Override
  public void sourceChanged(int sourcePriority, String sourceName, Object sourceValue) {
    redoListViewer.setInput(sourceProvider.getRedoStack());
    redoListViewer.getControl().pack();
    listViewer.setInput(sourceProvider.getUndoStack());
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
