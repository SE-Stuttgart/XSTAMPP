/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
 * Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian Sieber, Fabian Toth, Patrick Wickenhäuser,
 * Aliaksei Babkovich, Aleksander Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.ui;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Observable;
import java.util.UUID;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.window.DefaultToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;

import messages.Messages;
import xstampp.astpa.Activator;
import xstampp.astpa.model.hazacc.ATableModel;
import xstampp.astpa.model.interfaces.ILinkModel;
import xstampp.astpa.model.interfaces.ISeverityDataModel;
import xstampp.astpa.model.interfaces.ISeverityEntry;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.interfaces.Severity;
import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.ui.linkingSupport.LinkSupport;
import xstampp.astpa.ui.unsafecontrolaction.SeverityButton;
import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.preferences.IPreferenceConstants;
import xstampp.ui.editors.StandartEditorPart;
import xstampp.ui.editors.interfaces.IEditorBase;
import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.IUser;
import xstampp.usermanagement.api.IUserProject;

/**
 * @author Jarkko Heidenwag
 * @author Lukas Balzer
 * 
 * @since 1.0.0
 * @version 2.0.2
 * 
 */
public abstract class CommonTableView<T extends IDataModel> extends StandartEditorPart {

  public static final String COMMON_TABLE_VIEW_SHOW_NUMBER_COLUMN = "commonTableView.showNumberColumn";

  private static String id;

  private List<LinkSupport<?>> linkFields;
  private TableViewer tableViewer;

  private boolean internalUpdate;

  private EnumSet<ObserverValue> updateValues;
  private Label itemsLabel, filterLabel, descriptionLabel;

  private Composite tableContainer;

  private Composite buttonComposite;
  private Text descriptionWidget;
  private TableColumnLayout tableColumnLayout;
  private TableViewerColumn idColumn;
  private TableViewerColumn titleColumn;
  private Button addNewItemButton;
  private ATableFilter filter;
  private Text filterTextField;
  private T dataInterface;
  private EnumSet<TableStyle> style;

  protected enum TableStyle {
    RESTRICTED, WITH_SEVERITY
  }

  private static final Image DELETE = Activator
      .getImageDescriptor("/icons/buttons/commontables/remove.png") //$NON-NLS-1$
      .createImage();
  private static final Image DELETE_ALL = Activator
      .getImageDescriptor("/icons/buttons/commontables/removeAll.png") //$NON-NLS-1$
      .createImage();
  private static final Image ADD = Activator
      .getImageDescriptor("/icons/buttons/commontables/add.png") //$NON-NLS-1$
      .createImage();
  private static final Image ADD_SMALL = Activator.getImageDescriptor("/icons/buttons/plus.png") //$NON-NLS-1$
      .createImage();
  private static final Image DELETE_SMALL = Activator.getImageDescriptor("/icons/buttons/minus.png") //$NON-NLS-1$
      .createImage();
  private static final Image MOVE_UP = Activator
      .getImageDescriptor("/icons/buttons/commontables/up.png") //$NON-NLS-1$
      .createImage();
  private static final Image MOVE_DOWN = Activator
      .getImageDescriptor("/icons/buttons/commontables/down.png") //$NON-NLS-1$
      .createImage();

  private ATableModel selectedEntry;

  private Button deleteItemsButton;

  private DefaultToolTip toolTipSupport;

  private class CommonSelectionChangedListener implements ISelectionChangedListener {

    @Override
    public void selectionChanged(SelectionChangedEvent event) {
      // if the selection is empty clear the label
      if (event.getSelection().isEmpty()) {
        selectedEntry = null;
        getDescriptionWidget().setText(""); //$NON-NLS-1$
        getDescriptionWidget().setEnabled(false);
        return;
      }
      if (event.getSelection() instanceof IStructuredSelection) {
        IStructuredSelection selection = (IStructuredSelection) event.getSelection();
        if (selection.getFirstElement() instanceof ATableModel) {
          selectedEntry = ((ATableModel) selection.getFirstElement());
          getDescriptionWidget()
              .setText(((ATableModel) selection.getFirstElement()).getDescription());
          deleteItemsButton.setEnabled(canEdit(selectedEntry, AccessRights.CREATE));
          getDescriptionWidget().setEnabled(canEdit(selectedEntry, AccessRights.WRITE));
          for (LinkSupport<?> linkSupport : linkFields) {
            linkSupport.update(getCurrentSelection());
          }
        }
      }
    }
  }

  /**
   * 
   * @author Jarkko Heidenwag
   * 
   * @return the table viewer
   */
  public TableViewer getTableViewer() {
    return this.tableViewer;
  }

  /**
   * 
   * @author Jarkko Heidenwag
   * 
   * @param tableViewer
   *          the table viewer
   */
  public void setTableViewer(TableViewer tableViewer) {
    this.tableViewer = tableViewer;
  }

  /**
   * 
   * @author Jarkko Heidenwag
   * 
   * @return the table container
   */
  public Composite getTableContainer() {
    return this.tableContainer;
  }

  /**
   * 
   * @author Jarkko Heidenwag
   * 
   * @param tableContainer
   *          the table container
   */
  public void setTableContainer(Composite tableContainer) {
    this.tableContainer = tableContainer;
  }

  /**
   * 
   * @author Jarkko Heidenwag
   * 
   * @return the description widget
   */
  public Text getDescriptionWidget() {
    return this.descriptionWidget;
  }

  /**
   * 
   * @author Jarkko Heidenwag
   * 
   * @param descriptionWidget
   *          the description widget
   */
  public void setDescriptionWidget(Text descriptionWidget) {
    this.descriptionWidget = descriptionWidget;
  }

  /**
   * 
   * @author Jarkko Heidenwag
   * 
   * @return the table column layout
   */
  public TableColumnLayout getTableColumnLayout() {
    return this.tableColumnLayout;
  }

  /**
   * 
   * @author Jarkko Heidenwag
   * 
   * @param tableColumnLayout
   *          the table column layout
   */
  public void setTableColumnLayout(TableColumnLayout tableColumnLayout) {
    this.tableColumnLayout = tableColumnLayout;
  }

  /**
   * 
   * @author Jarkko Heidenwag
   * 
   * @return the id column
   */
  public TableViewerColumn getIdColumn() {
    return this.idColumn;
  }

  /**
   * 
   * @author Jarkko Heidenwag
   * 
   * @param idColumn
   *          the id column
   */
  public void setIdColumn(TableViewerColumn idColumn) {
    this.idColumn = idColumn;
  }

  /**
   * 
   * @author Jarkko Heidenwag
   * 
   * @return the title column
   */
  public TableViewerColumn getTitleColumn() {
    return this.titleColumn;
  }

  /**
   * 
   * @author Jarkko Heidenwag
   * 
   * @param titleColumn
   *          the title column
   */
  public void setTitleColumn(TableViewerColumn titleColumn) {
    this.titleColumn = titleColumn;
  }

  /**
   * 
   * @author Jarkko Heidenwag
   * 
   * @return the add button
   */
  public Button getAddNewItemButton() {
    return this.addNewItemButton;
  }

  /**
   * 
   * @author Jarkko Heidenwag
   * 
   * @param addNewItemButton
   *          the add button
   */
  public void setAddNewItemButton(Button addNewItemButton) {
    this.addNewItemButton = addNewItemButton;
  }

  /**
   * 
   * @author Jarkko Heidenwag
   * 
   * @return the filter
   */
  public ATableFilter getFilter() {
    return this.filter;
  }

  /**
   * 
   * @author Jarkko Heidenwag
   * 
   * @param filter
   *          the filter to set
   */
  public void setFilter(ATableFilter filter) {
    this.filter = filter;
  }

  /**
   * 
   * @author Jarkko Heidenwag
   * 
   * @return the filter TextField
   */
  public Text getFilterTextField() {
    return this.filterTextField;
  }

  /**
   * 
   * @author Jarkko Heidenwag
   * 
   * @param filterTextField
   *          the filter TextField
   */
  public void setFilterTextField(Text filterTextField) {
    this.filterTextField = filterTextField;
  }

  /**
   * Constructs a commonTableView that can be manipulated at will<br> calling
   * {@link #CommonTableView(boolean)} with <b>true</b>
   * 
   * @author Jarkko Heidenwag
   * @author Lukas Balzer
   * 
   */
  public CommonTableView() {
    this(false);
  }

  /**
   * 
   * @param restrict
   *          a boolean that indicates whether this table can only be edited by a user possesing the
   *          access rights in a user system.
   */
  public CommonTableView(boolean restrict) {
    this.style = EnumSet.noneOf(TableStyle.class);
    if (restrict) {
      this.style.add(TableStyle.RESTRICTED);
    }
    this.internalUpdate = false;
    updateValues = EnumSet.noneOf(ObserverValue.class);
    this.linkFields = new ArrayList<>();
  }

  public CommonTableView(EnumSet<TableStyle> style) {
    this();
    this.style = style;
  }

  public void setUpdateValues(EnumSet<ObserverValue> updateValues) {
    this.updateValues = updateValues;
  }

  /**
   * Create contents of the view part.
   * 
   * @author Jarkko Heidenwag
   * @param parent
   *          The parent composite
   */
  @Override
  public void createPartControl(Composite parent) {
    // to be implemented by the sub class
  }

  /**
   * Create the standard contents of the CommonTableView.
   * 
   * @author Jarkko Heidenwag
   * @param parent
   *          The parent composite
   * @param tableHeader
   *          Type of item, e.g. "Accidents"
   */
  public void createCommonTableView(Composite parent, String tableHeader) {
    SashForm sashForm = new SashForm(parent, SWT.HORIZONTAL);

    // composite for the table and the "New item" button
    this.tableContainer = new Composite(sashForm, SWT.NONE);
    this.tableContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    this.tableContainer.setLayout(new GridLayout(1, true));

    Composite leftHeadComposite = new Composite(this.tableContainer, SWT.NONE);
    leftHeadComposite.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
    final int leftHeadColumns = 5;
    leftHeadComposite.setLayout(new GridLayout(leftHeadColumns, true));
    this.itemsLabel = new Label(leftHeadComposite, SWT.LEAD);
    this.itemsLabel.setFont(new Font(Display.getCurrent(),
        PreferenceConverter.getFontData(IEditorBase.STORE, IPreferenceConstants.DEFAULT_FONT)));
    this.itemsLabel.setText(tableHeader);
    this.itemsLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

    this.filterLabel = new Label(leftHeadComposite, SWT.TRAIL);
    this.filterLabel.setFont(new Font(Display.getCurrent(),
        PreferenceConverter.getFontData(IEditorBase.STORE, IPreferenceConstants.DEFAULT_FONT)));
    this.filterLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    this.filterLabel.setText(Messages.Filter);
    this.filterTextField = new Text(leftHeadComposite, SWT.SINGLE | SWT.BORDER);
    this.filterTextField.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

    // the composite for the table viewer
    Composite tableComposite = new Composite(this.tableContainer, SWT.NONE);
    tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    this.tableColumnLayout = new TableColumnLayout();
    tableComposite.setLayout(this.tableColumnLayout);

    createButtonBar();

    // START of the creation of the right side of the view

    // composite for the description and its label
    final Composite textContainer = new Composite(sashForm, SWT.NONE);
    textContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    textContainer.setLayout(new GridLayout(1, false));

    this.descriptionLabel = new Label(textContainer, SWT.LEAD);
    this.descriptionLabel.setFont(new Font(Display.getCurrent(),
        PreferenceConverter.getFontData(IEditorBase.STORE, IPreferenceConstants.DEFAULT_FONT)));
    this.descriptionLabel.setText(Messages.DescriptionNotes);

    SashForm leftSash = new SashForm(textContainer, SWT.VERTICAL);
    leftSash.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    // the textfield for the description of the selected item
    this.descriptionWidget = new Text(leftSash,
        SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP | SWT.SEARCH);

    this.descriptionWidget.setMessage(Messages.DoubleClickToEditTitle);
    this.descriptionWidget.setEnabled(false);
    this.descriptionWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    // KeyListener for ctrl + a (SelectAll) in the description
    this.descriptionWidget.addKeyListener(new KeyAdapter() {

      @Override
      public void keyReleased(final KeyEvent e) {
        if (((e.stateMask == SWT.CTRL) || (e.stateMask == SWT.COMMAND)) && (e.keyCode == 'a')) {
          CommonTableView.this.getDescriptionWidget().selectAll();
        }
      }

    });

    this.descriptionWidget.addModifyListener(new ModifyListener() {

      @Override
      public void modifyText(ModifyEvent e) {
        internalUpdate = true;
      }
    });
    if (linkFields.size() > 0) {
      createFooter(leftSash);
    }
    tableSetUp(tableComposite);
    // tab order: if tableComposite is active and tab is pressed,
    // you will leave the tableContainer and enter the description
    Control[] controls = { leftHeadComposite, this.buttonComposite, tableComposite };
    this.tableContainer.setTabList(controls);
    refreshView();
  }

  private void createFooter(SashForm leftSash) {
    Composite footer = new Composite(leftSash, SWT.None);
    footer.setLayoutData(new GridData(SWT.BOTTOM, SWT.CENTER, true, false));
    footer.setLayout(new FillLayout(SWT.HORIZONTAL));
    leftSash.setWeights(new int[] { 5, 1 });

    for (final LinkSupport<?> linkSupport : linkFields) {
      Composite linkComp = new Composite(footer, SWT.BORDER);
      linkComp.setLayout(new GridLayout(3, false));
      Label linkTitle = new Label(linkComp, SWT.WRAP);
      linkTitle.setText(linkSupport.getTitle());
      linkTitle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
      Button linkButton = new Button(linkComp, SWT.PUSH);
      linkButton.setImage(ADD_SMALL);
      linkButton.addSelectionListener(linkSupport);
      linkButton.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false));
      Button unlinkButton = new Button(linkComp, SWT.PUSH);
      unlinkButton.setImage(DELETE_SMALL);
      unlinkButton.addSelectionListener(linkSupport);
      unlinkButton.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false));

      Composite linkTable = new Composite(linkComp, SWT.NONE);
      linkTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
      tableColumnLayout = new TableColumnLayout();
      linkTable.setLayout(tableColumnLayout);

      // the table viewer
      TableViewer tableViewer = new TableViewer(linkTable,
          SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.SINGLE);
      linkSupport.setDisplayedList(tableViewer);
      // Listener for showing the description of the selected accident
      // tableViewer.addSelectionChangedListener(new ..);

      tableViewer.setContentProvider(new ArrayContentProvider());
      tableViewer.getTable().setLinesVisible(false);
      tableViewer.getTable().setHeaderVisible(false);
      ColumnViewerToolTipSupport.enableFor(tableViewer);
      TableViewerColumn linkColumn = new TableViewerColumn(tableViewer, SWT.NONE);
      linkColumn.getColumn().setText(Messages.ID);
      final int idWeight = 5;
      final int idMinWidth = 39;
      tableColumnLayout.setColumnData(linkColumn.getColumn(),
          new ColumnWeightData(idWeight, idMinWidth, true));
      linkColumn.setLabelProvider(new ColumnLabelProvider() {
        @Override
        public String getToolTipText(Object element) {
          return linkSupport.getDescription((UUID) element);
        }

        @Override
        public String getText(Object element) {
          return linkSupport.getText((UUID) element);
        }
      });

    }
  }

  private void tableSetUp(Composite tableComposite) {
    // the table viewer
    this.setTableViewer(new TableViewer(tableComposite,
        SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.MULTI | SWT.WRAP));
    tableComposite.addControlListener(new ControlListener() {
      
      @Override
      public void controlResized(ControlEvent e) {
        getTableViewer().getTable().redraw();
      }
      
      @Override
      public void controlMoved(ControlEvent e) {
        // TODO Auto-generated method stub
        
      }
    });
    // Listener for showing the description of the selected accident
    this.getTableViewer().addSelectionChangedListener(new CommonSelectionChangedListener());
    this.getTableViewer().setContentProvider(new ArrayContentProvider());
    this.getTableViewer().getTable().setLinesVisible(true);
    this.getTableViewer().getTable().setHeaderVisible(true);
    if (Activator.getDefault().getPreferenceStore()
        .getBoolean(COMMON_TABLE_VIEW_SHOW_NUMBER_COLUMN)) {
      final TableViewerColumn nrColumn = new TableViewerColumn(getTableViewer(), SWT.None);
      nrColumn.getColumn().setText("Nr.");
      this.tableColumnLayout.setColumnData(nrColumn.getColumn(), new ColumnWeightData(0, 10, true));
      nrColumn.getColumn().pack();
      // the columns of the table
      nrColumn.setLabelProvider(new ColumnLabelProvider() {
        @Override
        public String getText(Object element) {
          Object input = getTableViewer().getInput();
          String content = new String();
          if (input instanceof List<?>) {
            content = Integer.toString(((List<?>) input).indexOf(element) + 1);
          }
          return content;
        }
      });
      nrColumn.getColumn().setResizable(true);
    }
    this.idColumn = new TableViewerColumn(this.getTableViewer(), SWT.NONE);
    this.idColumn.getColumn().setText(Messages.ID);
    final int idWeight = 5;
    final int idMinWidth = 39;
    this.tableColumnLayout.setColumnData(this.idColumn.getColumn(),
        new ColumnWeightData(idWeight, idMinWidth, true));
    this.idColumn.setLabelProvider(new ColumnLabelProvider() {
      @Override
      public Color getForeground(Object element) {
        if (!canEdit((ATableModel) element, AccessRights.WRITE)) {
          return getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY);
        }
        return null;
      }

      @Override
      public String getText(Object element) {
        if (element instanceof ATableModel) {
          return ((ATableModel) element).getIdString();
        }
        return null;
      }
    });
    this.titleColumn = new TableViewerColumn(this.getTableViewer(), SWT.None);
    this.titleColumn.getColumn().setText(Messages.Title);
    final int titleWeight = 50;
    final int titleMinWidth = 50;
    this.tableColumnLayout.setColumnData(this.titleColumn.getColumn(),
        new ColumnWeightData(titleWeight, titleMinWidth, true));
    titleColumn.getColumn().setResizable(true);
    this.titleColumn.setLabelProvider(new ColumnLabelProvider() {

      @Override
      public Color getForeground(Object element) {
        if (!canEdit((ATableModel) element, AccessRights.WRITE)) {
          return getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY);
        }
        if (((ATableModel) element).getTitle().equals("")) {
          return getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND);
        }
        return null;
      }

      @Override
      public String getText(Object element) {
        if (((ATableModel) element).getTitle().equals("")) {
          return Messages.DoubleClickToEditTitle;
        }
        return ((ATableModel) element).getTitle();
      }
    });

    if (style.contains(TableStyle.WITH_SEVERITY)) {
      addSeverityColumn();
    }
    if (!linkFields.isEmpty()) {
      addLinkColumn();
    }
    // detecting a double click
    ColumnViewerEditorActivationStrategy activationSupport = new ColumnViewerEditorActivationStrategy(
        this.getTableViewer()) {

      @Override
      protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
        final int leftClick = 1;
        // Enable editor only with mouse double click
        if (((event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION)
            && (((MouseEvent) event.sourceEvent).button == leftClick))
            || (event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC)) {
          return true;
        }
        return false;
      }
    };
    TableViewerEditor.create(this.getTableViewer(), null, activationSupport,
        ColumnViewerEditor.DEFAULT);
    // ctrl + a selects all items with this KeyListener
    this.getTableViewer().getTable().addKeyListener(new KeyListener() {

      @Override
      public void keyPressed(KeyEvent e) {
        if (((e.stateMask == SWT.CTRL) || (e.stateMask == SWT.COMMAND)) && (e.keyCode == 'a')
            && (CommonTableView.this.getTableViewer().getTable().getItemCount() > 0)) {
          CommonTableView.this.tableViewer.setSelection(
              new StructuredSelection(CommonTableView.this.getTableViewer().getElementAt(0)), true);
          CommonTableView.this.tableViewer.getTable().selectAll();
        }
      }

      @Override
      public void keyReleased(KeyEvent e) {
        // nothing happens
      }
    });
  }

  private void createButtonBar() {
    // the add and delete buttons are arranged in a composite with a 2x1
    // GridLayout
    this.buttonComposite = new Composite(this.tableContainer, SWT.NONE);
    this.buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    this.buttonComposite.setLayout(new GridLayout(6, true));
    GridData gridData = new GridData(SWT.NONE, SWT.NONE, false, false);
    final int buttonSize = 46;
    gridData.widthHint = buttonSize;
    gridData.heightHint = buttonSize;

    // the Button for adding new items
    this.addNewItemButton = new Button(this.buttonComposite, SWT.PUSH);
    this.addNewItemButton.setLayoutData(gridData);
    this.addNewItemButton.setImage(ADD);
    // the Button for deleting selected items
    deleteItemsButton = new Button(this.buttonComposite, SWT.PUSH);
    deleteItemsButton.setLayoutData(gridData);
    deleteItemsButton.setImage(DELETE);

    deleteItemsButton.addListener(SWT.Selection, new Listener() {

      @Override
      public void handleEvent(Event event) {
        CommonTableView.this.deleteItems();
      }
    });

    // the Button for deleting all items
    Button deleteAllButton = new Button(this.buttonComposite, SWT.PUSH);
    deleteAllButton.setLayoutData(gridData);

    deleteAllButton.addListener(SWT.Selection, new Listener() {

      @Override
      public void handleEvent(Event event) {
        CommonTableView.this.deleteAllItems();
      }
    });
    deleteAllButton.setImage(DELETE_ALL);
    new Label(buttonComposite, SWT.NONE);
    // the Button for deleting all items
    Button moveUp = new Button(this.buttonComposite, SWT.PUSH);
    moveUp.setToolTipText("Decreases the index of the selected entry\n(Does not change the ID)");
    moveUp.setLayoutData(gridData);

    moveUp.addListener(SWT.Selection, new Listener() {

      @Override
      public void handleEvent(Event event) {
        if (tableViewer.getTable().getSelectionCount() == 1
            && tableViewer.getTable().getSelection()[0].getData() instanceof ITableModel) {
          ITableModel model = (ITableModel) tableViewer.getTable().getSelection()[0].getData();
          moveEntry(model.getId(), true);
        }
      }
    });
    moveUp.setImage(MOVE_UP);
    // the Button for deleting all items
    Button moveDown = new Button(this.buttonComposite, SWT.PUSH);
    moveDown.setLayoutData(gridData);
    moveDown.setToolTipText("Increases the index of the selected entry\n(Does not change the ID)");

    moveDown.addListener(SWT.Selection, new Listener() {

      @Override
      public void handleEvent(Event event) {
        if (tableViewer.getTable().getSelectionCount() == 1
            && tableViewer.getTable().getSelection()[0].getData() instanceof ITableModel) {
          ITableModel model = (ITableModel) tableViewer.getTable().getSelection()[0].getData();
          moveEntry(model.getId(), false);
        }
      }
    });
    moveDown.setImage(MOVE_DOWN);

    if (style.contains(TableStyle.RESTRICTED) && getDataInterface() instanceof IUserProject
        && !((IUserProject) getDataInterface()).getUserSystem().checkAccess(AccessRights.ADMIN)) {
      this.addNewItemButton.setEnabled(false);
      deleteItemsButton.setEnabled(false);
    }
    if (getDataInterface() instanceof IUserProject
        && !((IUserProject) getDataInterface()).getUserSystem().checkAccess(AccessRights.ADMIN)) {
      moveDown.setEnabled(false);
      moveUp.setEnabled(false);
      deleteAllButton.setEnabled(false);
    }
  }

  protected void addTitleEditor(AbstractEditingSupport support) {
    this.titleColumn.setEditingSupport(support);
  }

  protected void resizeColumns() {
    for (int i = 0; i < getTableViewer().getTable().getColumns().length; i++) {
      TableColumn tableColumn = getTableViewer().getTable().getColumns()[i];
      if (tableColumn.getResizable()) {
        tableColumn.pack();
      }
    }
  }

  protected void deleteAllItems() {
    getTableViewer().getTable().setSelection(0, getTableViewer().getTable().getItemCount());
    deleteItems();
  }

  protected abstract void moveEntry(UUID id, boolean moveUp);

  /**
   * @author Jarkko Heidenwag
   * 
   */
  public void refreshView() {
    this.getTableViewer().refresh(true, true);
    this.updateTable();
  }

  @Override
  public String getId() {
    return CommonTableView.id;
  }

  @Override
  public String getTitle() {
    return Messages.CommonTable;
  }

  @Override
  public void update(Observable dataModelController, Object updatedValue) {
    super.update(dataModelController, updatedValue);
    if (updateValues.contains(updatedValue)) {
      this.refreshView();
      if (!internalUpdate && selectedEntry != null) {
        getDescriptionWidget().setText(selectedEntry.getDescription());
      } else {
        internalUpdate = false;
      }
    }
  }

  /**
   * deleting all selected items
   * 
   * @author Jarkko Heidenwag
   * 
   */
  public final void deleteItems() {
    final int maxNumOfDisplayedEntries = 10;
    IStructuredSelection selection = (IStructuredSelection) getTableViewer().getSelection();
    if (selection.size() > 0 && selection.getFirstElement() instanceof ATableModel) {
      Object[] models = selection.toArray();
      int shownIds = Math.min(models.length, maxNumOfDisplayedEntries);
      String modelIds = new String();
      if (models.length == 1) {
        modelIds += "Do you really want to delete the following entry?\n";
      } else if (models.length == getTableViewer().getTable().getItemCount()) {
        modelIds += "Do you really want to delete all list entries?\n";
      } else {
        modelIds += "Do you really want to delete all of the following entries?\n";
      }
      modelIds += "\n";
      for (int i = 0; i < shownIds; i++) {
        ATableModel model = ((ATableModel) models[i]);
        modelIds += model.getIdString() + " - " + model.getTitle().trim() + "\n";
      }
      if (models.length > maxNumOfDisplayedEntries) {
        modelIds += "..." + (models.length - maxNumOfDisplayedEntries) + " more";
      }
      if (MessageDialog.openQuestion(this.getTableContainer().getShell(), Messages.Confirm,
          modelIds)) {
        getDataInterface().lockUpdate();
        for (Object model : models) {
          deleteEntry(((ATableModel) model));
        }
        this.getDescriptionWidget().setText(""); //$NON-NLS-1$
        getDataInterface().releaseLockAndUpdate(new ObserverValue[0]);
        updateTable();
        this.refreshView();
      }
    } else {
      MessageDialog.openInformation(this.getTableContainer().getShell(), Messages.Information,
          Messages.NoAccidentSelected);
    }
  }

  protected abstract void deleteEntry(ATableModel model);

  /**
   * @author Jarkko Heidenwag
   * 
   */
  public abstract void updateTable();

  public UUID getCurrentSelection() {
    if (this.selectedEntry != null) {
      return this.selectedEntry.getId();
    }
    return null;
  }

  protected void resetCurrentSelection() {
    this.selectedEntry = null;
  }

  @SuppressWarnings("unchecked")
  public void setDataModelInterface(IDataModel dataInterface) {
    this.dataInterface = (T) dataInterface;
    dataInterface.addObserver(this);

  }

  public T getDataInterface() {

    return dataInterface;
  }

  @Override
  public void partActivated(IWorkbenchPart arg0) {
    if (arg0 == this) {
      CommonTableView.this.itemsLabel.setFont(new Font(Display.getCurrent(),
          PreferenceConverter.getFontData(IEditorBase.STORE, IPreferenceConstants.DEFAULT_FONT)));
      CommonTableView.this.filterLabel.setFont(new Font(Display.getCurrent(),
          PreferenceConverter.getFontData(IEditorBase.STORE, IPreferenceConstants.DEFAULT_FONT)));
      CommonTableView.this.descriptionLabel.setFont(new Font(Display.getCurrent(),
          PreferenceConverter.getFontData(IEditorBase.STORE, IPreferenceConstants.DEFAULT_FONT)));
    }
    super.partActivated(arg0);
  }

  protected abstract class AbstractEditingSupport extends EditingSupport {

    /**
     * EditingSupport for the title column
     */
    public AbstractEditingSupport(ColumnViewer viewer) {
      super(viewer);
    }

    @Override
    protected boolean canEdit(Object element) {
      return CommonTableView.this.canEdit((ATableModel) element, AccessRights.WRITE);
    }

    protected Object getValue(String string) {
      return string;
    }
  }

  protected boolean canEdit(ATableModel entryId) {
    return canEdit(entryId, AccessRights.WRITE);
  }

  private void addSeverityColumn() {
    if (getDataInterface() instanceof ISeverityDataModel) {
      final ISeverityDataModel model = ((ISeverityDataModel) getDataInterface());
      if (model.isUseSeverity()) {
        TableViewerColumn severityColumn = new TableViewerColumn(getTableViewer(), SWT.NONE);
        severityColumn.getColumn()
            .setToolTipText(xstampp.astpa.messages.Messages.ProjectSpecifics_UseSeverityTip);
        severityColumn.getColumn().setText("Severity*");
        severityColumn.setLabelProvider(new ColumnLabelProvider() {

          @Override
          public String getText(Object element) {
            try {
              return ((ISeverityEntry) element).getSeverity().toString();
            } catch (Exception exc) {
              return "";
            }
          }
        });
        severityColumn.setEditingSupport(new EditingSupport(getTableViewer()) {

          @Override
          protected void setValue(Object element, Object value) {
            model.setSeverity(element, Severity.values()[(int) value]);
          }

          @Override
          protected Object getValue(Object element) {
            try {
              return ((ISeverityEntry) element).getSeverity().ordinal();
            } catch (Exception exc) {
              return 0;
            }
          }

          @Override
          protected CellEditor getCellEditor(Object element) {

            return null;
          }

          @Override
          protected boolean canEdit(Object element) {
            SeverityButton button = new SeverityButton(((ISeverityEntry) element),
                getDataInterface(), getViewer().getControl());
            Point location = getTableViewer().getTable().getDisplay().getCursorLocation();
            getTableViewer().getTable().toDisplay(location);
            Point point = new Point(0, 0);
            point.x = idColumn.getColumn().getWidth() + titleColumn.getColumn().getWidth();
            button.onButtonDown(point, new Rectangle(0, 0, 0, 0));
            return true;
          }
        });
        this.getTableColumnLayout().setColumnData(severityColumn.getColumn(),
            new ColumnWeightData(10, 50, false));
      }
    }
  }

  private void addLinkColumn() {
    TableViewerColumn linksColumn;
    linksColumn = new TableViewerColumn(getTableViewer(), SWT.NONE);
    linksColumn.getColumn().setText(Messages.Links);
    linksColumn.setLabelProvider(new ColumnLabelProvider() {

      @Override
      public String getText(Object element) {
        String linkString = ""; //$NON-NLS-1$
        LinkController linkController = ((ILinkModel) getDataInterface()).getLinkController();
        UUID partId = ((ITableModel) element).getId();
        for (LinkSupport<?> support : linkFields) {
          for (UUID uuid : linkController.getLinksFor(support.getLinkType(), partId)) {
            linkString += support.getText(uuid) + ", "; //$NON-NLS-1$
          }
        }

        return linkString.substring(0, Math.max(0, linkString.length() - 2));
      }
    });

    final int linksWeight = 10;
    final int linksMinWidth = 50;
    this.getTableColumnLayout().setColumnData(linksColumn.getColumn(),
        new ColumnWeightData(linksWeight, linksMinWidth, false));

  }

  public void addLinkSupport(LinkSupport<?> support) {
    this.linkFields.add(support);
  }

  void setToolTip(String tip) {
    this.toolTipSupport.setText(tip);
  }

  protected boolean canEdit(ATableModel entryId, AccessRights level) {
    if (getDataInterface() instanceof IUserProject) {
      IUser user = ((IUserProject) getDataInterface()).getUserSystem().getCurrentUser();
      if (user != null && user.getUserId().equals(entryId.getCreatedBy())) {
        return true;
      } else {
        return ((IUserProject) getDataInterface()).getUserSystem().checkAccess(entryId.getId(),
            level);
      }
    }
    return true;
  }
}
