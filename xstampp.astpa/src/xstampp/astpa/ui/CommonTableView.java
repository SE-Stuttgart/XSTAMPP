/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
 * Grahovac, Jarkko Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian
 * Sieber, Fabian Toth, Patrick Wickenhäuser, Aliaksei Babkovich, Aleksander
 * Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.ui;

import messages.Messages;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
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

import xstampp.astpa.Activator;
import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.model.hazacc.ATableModel;
import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.preferences.IPreferenceConstants;
import xstampp.ui.editors.StandartEditorPart;
import xstampp.ui.editors.interfaces.IEditorBase;

import java.util.List;
import java.util.Observable;
import java.util.UUID;

/**
 * @author Jarkko Heidenwag
 * 
 * @since 1.0.0
 * @version 2.0.2
 * 
 */
public abstract class CommonTableView<T extends IDataModel> extends StandartEditorPart {

	public static final String COMMON_TABLE_VIEW_SHOW_NUMBER_COLUMN = "commonTableView.showNumberColumn";

	private static String id;

	private TableViewer tableViewer;

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

	private Button moveUp;

	private Button moveDown;
	private static final Image DELETE = Activator.getImageDescriptor("/icons/buttons/commontables/remove.png") //$NON-NLS-1$
			.createImage();
	private static final Image DELETE_ALL = Activator.getImageDescriptor("/icons/buttons/commontables/removeAll.png") //$NON-NLS-1$
			.createImage();
	private static final Image ADD = Activator.getImageDescriptor("/icons/buttons/commontables/add.png") //$NON-NLS-1$
			.createImage();
	private static final Image MOVE_UP = Activator.getImageDescriptor("/icons/buttons/commontables/up.png") //$NON-NLS-1$
			.createImage();
	private static final Image MOVE_DOWN = Activator.getImageDescriptor("/icons/buttons/commontables/down.png") //$NON-NLS-1$
			.createImage();

	/**
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 */
	public enum commonTableType {
		/**
		 * 
		 * @author Jarkko Heidenwag
		 */
		UNDEFINED,
		/**
		 * 
		 * @author Jarkko Heidenwag
		 */
		SafetyConstraintsView,
		/**
		 * 
		 * @author Jarkko Heidenwag
		 */
		SystemGoalsView,
		/**
		 * 
		 * @author Jarkko Heidenwag
		 */
		DesignRequirementsView,
		/**
		 * 
		 * @author Jarkko Heidenwag
		 */
		AccidentsView,
		/**
		 * 
		 * @author Jarkko Heidenwag
		 */
		HazardsView,
		/**
		 * 
		 * @author Jarkko Heidenwag
		 */
		ControlActionsView
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
	 *            the table viewer
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
	 *            the table container
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
	 *            the description widget
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
	 *            the table column layout
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
	 *            the id column
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
	 *            the title column
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
	 *            the add button
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
	 *            the filter to set
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
	 *            the filter TextField
	 */
	public void setFilterTextField(Text filterTextField) {
		this.filterTextField = filterTextField;
	}

	/**
	 * @author Jarkko Heidenwag
	 * 
	 */
	public CommonTableView() {
	}

	/**
	 * Create contents of the view part.
	 * 
	 * @author Jarkko Heidenwag
	 * @param parent
	 *            The parent composite
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
	 *            The parent composite
	 * @param tableHeader
	 *            Type of item, e.g. "Accidents"
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

		// composite for the description and its label
		final Composite textContainer = new Composite(sashForm, SWT.NONE);
		textContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		textContainer.setLayout(new GridLayout(1, false));

		// the composite for the table viewer
		Composite tableComposite = new Composite(this.tableContainer, SWT.NONE);
		tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		this.tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(this.tableColumnLayout);

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
		Button deleteItemsButton = new Button(this.buttonComposite, SWT.PUSH);
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
		moveUp = new Button(this.buttonComposite, SWT.PUSH);
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
		moveDown = new Button(this.buttonComposite, SWT.PUSH);
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

		Composite rightHeadComposite = new Composite(textContainer, SWT.NONE);
		rightHeadComposite.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		rightHeadComposite.setLayout(new GridLayout(2, true));

		this.descriptionLabel = new Label(rightHeadComposite, SWT.LEAD);
		this.descriptionLabel.setFont(new Font(Display.getCurrent(),
				PreferenceConverter.getFontData(IEditorBase.STORE, IPreferenceConstants.DEFAULT_FONT)));
		this.descriptionLabel.setText(Messages.DescriptionNotes);

		Text invisibleTextField = new Text(rightHeadComposite, SWT.SINGLE | SWT.BORDER);
		invisibleTextField.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		invisibleTextField.setVisible(false);

		// the textfield for the description of the selected item
		this.descriptionWidget = new Text(textContainer, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);

		this.descriptionWidget.setEnabled(false);
		this.descriptionWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// KeyListener for ctrl + a (SelectAll) in the description
		this.descriptionWidget.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(final KeyEvent e) {
				if (((e.stateMask == SWT.CTRL) || (e.stateMask == SWT.COMMAND)) && (e.keyCode == 'a')) {
					CommonTableView.this.getDescriptionWidget().selectAll();
				}
				switch (e.keyCode) {
				case SWT.ARROW_LEFT: {
					((Text) e.getSource()).setSelection(((Text) e.getSource()).getSelection().x - 1,
							((Text) e.getSource()).getSelection().x - 1);
					break;
				}
				case SWT.ARROW_RIGHT: {
					((Text) e.getSource()).setSelection(((Text) e.getSource()).getSelection().x + 1,
							((Text) e.getSource()).getSelection().x + 1);
				}
				}
			}

		});

		// the table viewer
		this.setTableViewer(
				new TableViewer(tableComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.MULTI | SWT.WRAP));
		this.getTableViewer().setContentProvider(new ArrayContentProvider());
		this.getTableViewer().getTable().setLinesVisible(true);
		this.getTableViewer().getTable().setHeaderVisible(true);
		if (Activator.getDefault().getPreferenceStore().getBoolean(COMMON_TABLE_VIEW_SHOW_NUMBER_COLUMN)) {
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
			public String getText(Object element) {
				if (element instanceof ATableModel) {
					return ((ATableModel) element).getIdString();
				}
				return null;
			}
		});

		this.titleColumn = new TableViewerColumn(this.getTableViewer(), SWT.NONE);
		this.titleColumn.getColumn().setText(Messages.Title);
		final int titleWeight = 50;
		final int titleMinWidth = 50;
		this.tableColumnLayout.setColumnData(this.titleColumn.getColumn(),
				new ColumnWeightData(titleWeight, titleMinWidth, false));
		titleColumn.getColumn().setResizable(false);
		this.titleColumn.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				if (element instanceof ATableModel) {
					return ((ATableModel) element).getTitle();
				}
				return null;
			}
		});
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

		TableViewerEditor.create(this.getTableViewer(), null, activationSupport, ColumnViewerEditor.DEFAULT);
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
		// tab order: if tableComposite is active and tab is pressed,
		// you will leave the tableContainer and enter the description
		Control[] controls = { leftHeadComposite, this.buttonComposite, tableComposite };
		this.tableContainer.setTabList(controls);
		refreshView();
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
		ObserverValue type = (ObserverValue) updatedValue;
		switch (type) {
		case ACCIDENT:
		case HAZARD:
		case HAZ_ACC_LINK:
		case DESIGN_REQUIREMENT:
		case SAFETY_CONSTRAINT:
		case SYSTEM_GOAL:
			this.refreshView();
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 * @return the type of this view
	 */
	public commonTableType getCommonTableType() {
		return commonTableType.UNDEFINED;
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
			if (MessageDialog.openQuestion(this.getTableContainer().getShell(), Messages.Confirm, modelIds)) {
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

}
