package xstampp.astpa.ui.linkingSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;

import messages.Messages;
import xstampp.astpa.model.interfaces.ILinkModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.ui.CommonTableView;
import xstampp.model.ObserverValue;
import xstampp.ui.common.contentassist.AutoCompleteField;
import xstampp.ui.common.contentassist.LinkProposal;

public abstract class LinkSupport<M extends ILinkModel> extends SelectionAdapter
    implements IContentProposalListener {
  private TableViewer displayedList;
  private UUID[] currentContent;
  private UUID currentLinkedId;
  private M dataInterface;
  private java.util.List<UUID> available;
  private List<Listener> changeListeners;
  private ObserverValue type;
  private List<ITableModel> modelList;
  private Composite linkComp;

  public LinkSupport(M dataInterface, ObserverValue type) {
    this.dataInterface = dataInterface;
    this.type = type;
    this.changeListeners = new ArrayList<>();
  }

  /**
   * Sets the id of the entry which's links are displayed in the {@link TableViewer} set in
   * {@link #setDisplayedList(TableViewer)}. This method updates the array obtained by
   * {@link #getCurrentContent()} as well as the {@link List} returned by {@link #getAvailable()}.
   * 
   * @param id
   *          an id that should serve to create linkings with.
   */
  public void update(UUID id) {
    currentLinkedId = id;
    currentContent = null;
    if (!this.displayedList.getTable().isDisposed()) {

      for (Control control : linkComp.getChildren()) {
        control.setEnabled(true);
      }
      currentContent = fetch().toArray(new UUID[0]);
      this.displayedList.getTable().setEnabled(true);
      this.displayedList.setInput(currentContent);
    }
  }

  /**
   * 
   * @param label
   *          the literal returned by {@link #getDescription(UUID)} of the abstract classes
   *          implementation.
   * @return the index of the linked item with the given description, or <b>null</b> if the label
   *         didn't match any current linked item
   */
  public final int indexOf(String label) {
    if (label != null && available != null) {
      int index = 0;
      for (UUID model : available) {
        if (label.equals(getText(model))) {
          return index;
        }
        index++;
      }
    }
    return -1;
  }

  public void setDisplayedList(TableViewer displayedList) {
    this.displayedList = displayedList;
  }

  UUID[] getCurrentContent() {
    return currentContent;
  }

  public M getDataInterface() {
    return dataInterface;
  }

  public UUID getCurrentId() {
    return currentLinkedId;
  }

  public String getDescription(int index) {
    if (getCurrentContent().length > index && index >= 0) {
      return getDescription(getCurrentContent()[index]);
    }
    return null;
  }

  @Override
  public void proposalAccepted(IContentProposal proposal) {

    int indexOf = indexOf(proposal.getLabel());
    if (indexOf >= 0) {
      UUID uuid = available.get(indexOf);
      getDataInterface().getLinkController().addLink(getLinkType(), getCurrentId(), uuid);
      notifyChangeListeners();
    }
  }

  /**
   * fetches the ID's of all items linked to the currently selected one.
   * 
   * @return a {@link List} of {@link UUID}'s
   */
  List<UUID> fetch() {
    return getDataInterface().getLinkController().getLinksFor(getLinkType(), getCurrentId());
  }

  /**
   * Deletes the link at given index of the widget from the data model.
   * 
   * @return whether the link was successfully removed or not.
   */
  public boolean unlink(int index) {
    UUID uuid = getCurrentContent()[index];
    boolean deleteLink = getDataInterface().getLinkController().deleteLink(getLinkType(),
        getCurrentId(), uuid);
    if (deleteLink) {
      notifyChangeListeners();
    }
    return deleteLink;
  }

  /**
   * Adds a {@link Listener} to the list of listeners that get notified whenever the list is
   * Modified meaning a link is added or removed.
   * 
   * @param listener
   *          A listener that should be called whenever the list of links is changed
   */
  public void addChangeListener(Listener listener) {
    this.changeListeners.add(listener);
  }

  private void notifyChangeListeners() {
    for (Listener listener : this.changeListeners) {
      Event event = new Event();
      event.widget = displayedList.getTable();
      listener.handleEvent(event);
    }
  }

  List<UUID> getAvailable() {
    setModelList(getModels());
    List<UUID> result = new ArrayList<>();
    for (ITableModel constraint : getModelList()) {
      result.add(constraint.getId());
    }
    result.removeAll(fetch());
    return result;
  }

  public String getText(UUID id) {
    for (ITableModel model : getModels()) {
      if (model.getId().equals(id)) {
        return getLiteral() + model.getNumber();
      }
    }
    return null;
  }

  public String getDescription(UUID id) {
    for (ITableModel model : getModels()) {
      if (model.getId().equals(id)) {
        return model.getDescription(); // $NON-NLS-1$
      }
    }
    return null;
  }

  /**
   * This returns a list of all ITAbleModels that can be linked by this {@link LinkSupport}.<br>
   * e.g. all Hazards from the data model in the {@link HazardLinkSupport}
   * 
   * @return a {@link List} with all {@link ITableModel}'s that can be linked
   */
  protected abstract List<ITableModel> getModels();

  protected abstract String getLiteral();

  protected abstract String getTitle();

  public ObserverValue getLinkType() {
    return type;
  }

  @Override
  public void widgetSelected(SelectionEvent e) {
    available = getAvailable();

    LinkProposal[] proposals = new LinkProposal[available.size()];
    for (int i = 0; i < proposals.length; i++) {
      LinkProposal proposal = new LinkProposal();
      proposal.setLabel(getText(available.get(i)));
      proposal.setDescription(getDescription(available.get(i)));
      proposals[i] = proposal;
    }

    AutoCompleteField scLinking = new AutoCompleteField(proposals,
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
    scLinking.setProposalListener(this);
    Point location = Display.getDefault().getCursorLocation();
    Point point = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
        .toControl(location);
    scLinking.setPopupPosition(point);
    scLinking.openPopup();
  }

  public List<ITableModel> getModelList() {
    if (modelList == null) {
      return new ArrayList<>();
    }
    return modelList;
  }

  public void setModelList(List<ITableModel> modelList) {
    this.modelList = modelList;
  }

  public void createLinkWidget(Composite parent) {
    linkComp = new Composite(parent, SWT.BORDER);
    linkComp.setLayout(new GridLayout(3, false));
    Label linkTitle = new Label(linkComp, SWT.WRAP);
    linkTitle.setText(getTitle());
    linkTitle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    Button linkButton = new Button(linkComp, SWT.PUSH);

    linkButton.setImage(CommonTableView.ADD_SMALL);
    linkButton.addSelectionListener(this);
    linkButton.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false));
    linkButton.setEnabled(false);

    Button unlinkButton = new Button(linkComp, SWT.PUSH);
    unlinkButton.setImage(CommonTableView.DELETE_SMALL);
    unlinkButton.setEnabled(false);
    unlinkButton.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false));

    Composite linkTable = new Composite(linkComp, SWT.NONE);
    linkTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
    TableColumnLayout tableColumnLayout = new TableColumnLayout();
    linkTable.setLayout(tableColumnLayout);

    // the table viewer
    final TableViewer tableViewer = new TableViewer(linkTable,
        SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.MULTI);
    setDisplayedList(tableViewer);
    // Listener for showing the description of the selected accident
    // tableViewer.addSelectionChangedListener(new ..);
    tableViewer.getTable().setEnabled(false);
    tableViewer.setContentProvider(new ArrayContentProvider());
    tableViewer.getTable().setLinesVisible(false);
    tableViewer.getTable().setHeaderVisible(false);
    ColumnViewerToolTipSupport.enableFor(tableViewer);
    TableViewerColumn linkColumn = new TableViewerColumn(tableViewer, SWT.NONE);
    linkColumn.getColumn().setText(Messages.ID);
    final int idWeight = 5;
    final int idMinWidth = 39;
    tableViewer.setInput(currentContent);
    tableColumnLayout.setColumnData(linkColumn.getColumn(),
        new ColumnWeightData(idWeight, idMinWidth, true));
    linkColumn.setLabelProvider(new ColumnLabelProvider() {
      @Override
      public String getToolTipText(Object element) {
        return getDescription((UUID) element);
      }

      @Override
      public String getText(Object element) {
        return LinkSupport.this.getText((UUID) element);
      }
    });

    unlinkButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        int[] indices = tableViewer.getTable().getSelectionIndices();
        for (int i = indices.length; i > 0; --i) {
          UUID uuid = currentContent[i - 1];
          getDataInterface().getLinkController().deleteLink(getLinkType(), getCurrentId(), uuid);
          notifyChangeListeners();
        }
      }
    });
  }
}