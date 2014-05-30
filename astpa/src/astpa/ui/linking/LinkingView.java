/*******************************************************************************
 * Copyright (c) 2013 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package astpa.ui.linking;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.UUID;

import messages.Messages;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import astpa.Activator;
import astpa.model.ITableModel;
import astpa.model.ObserverValue;
import astpa.model.interfaces.IDataModel;
import astpa.model.interfaces.ILinkingViewDataModel;
import astpa.preferences.IPreferenceConstants;
import astpa.ui.common.IViewBase;

/**
 * Editor to create and delete links between hazards and accidents. There are
 * two table modes: accident mode and hazard mode, which identifies whether
 * accidents are linked to hazards or hazards to accidents respectively.
 * 
 * @author Patrick Wickenhaeuser
 * @author Jarkko Heidenwag
 */
public class LinkingView implements IViewBase {
	
	/**
	 * Identifier of the view
	 */
	public static final String ID = Messages.LinkingView;
	
	/**
	 * available table viewer
	 */
	private TableViewer availableTableViewer;
	
	/**
	 * currently linked hazards/accidents viewer
	 */
	private TableViewer currentTableViewer;
	
	/**
	 * The id of the selected hazard/accident
	 */
	private UUID selectedId;
	
	/**
	 * Current mode in which the tables are.
	 */
	private boolean accidentMode;
	
	/**
	 * The interface to the data model.
	 */
	private ILinkingViewDataModel dataInterface = null;
	
	private static final int BUTTON_AREA_COLUMNS = 5;
	
	private static final int COLUMN_ID_WEIGHT_X = 5;
	private static final int COLUMN_ID_WEIGHT1_Y = 40;
	
	private static final int COLUMN_TITLE_WEIGHT_X = 50;
	private static final int COLUMN_TITLE_WEIGHT_Y = 50;
	
	private final static int HMINUS = 9;
	private final static int VMINUS = 40;
	
	
	/**
	 * The current table mode.
	 * 
	 * @return the current mode
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	public boolean isAccidentMode() {
		return this.accidentMode;
	}
	
	/**
	 * Sets the table mode.
	 * 
	 * @param accidentMode The new mode.
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	public void setAccidentMode(boolean accidentMode) {
		this.accidentMode = accidentMode;
		
		// clear selection
		this.selectionTableViewer.setSelection(null);
		
		this.updateAll();
	}
	
	
	/**
	 * displays the current mode
	 */
	private Label lblSelected;
	
	/**
	 * table viewer for the selection table
	 */
	private TableViewer selectionTableViewer;
	
	/**
	 * Toggle button for the mode
	 */
	private Button modeButton;
	private Label lblAvailableForLinking;
	private Label lblCurrentlyLinked;
	
	
	/**
	 * Ctor
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	public LinkingView() {
		this.accidentMode = true;
	}
	
	/**
	 * Updates the content of all tables and forces them to redraw.
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	private void updateSelectionTable() {
		if (this.accidentMode) {
			this.selectionTableViewer.setInput(this.dataInterface.getAllAccidents());
		} else {
			this.selectionTableViewer.setInput(this.dataInterface.getAllHazards());
		}
	}
	
	/**
	 * Updates the content of the available table.
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	private void updateAvailableTable() {
		if (this.selectedId != null) {
			if (this.accidentMode) {
				java.util.List<ITableModel> linked = this.dataInterface.getLinkedHazards(this.selectedId);
				java.util.List<ITableModel> available =
					new java.util.ArrayList<ITableModel>(this.dataInterface.getAllHazards());
				if (!(linked == null)) {
					// remove all linked hazards from the available
					Iterator<ITableModel> iter = linked.iterator();
					while (iter.hasNext()) {
						available.remove(iter.next());
					}
				}
				this.availableTableViewer.setInput(available);
			} else {
				java.util.List<ITableModel> available =
					new java.util.ArrayList<ITableModel>(this.dataInterface.getAllAccidents());
				java.util.List<ITableModel> linked = this.dataInterface.getLinkedAccidents(this.selectedId);
				
				// remove all linked hazards from the available
				if (!(linked == null)) {
					Iterator<ITableModel> iter = linked.iterator();
					while (iter.hasNext()) {
						available.remove(iter.next());
					}
				}
				this.availableTableViewer.setInput(available);
			}
		} else {
			this.availableTableViewer.setInput(null);
		}
	}
	
	/**
	 * Updates the content of the current table.
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	private void updateCurrentTable() {
		if (this.selectedId != null) {
			if (this.accidentMode) {
				this.currentTableViewer.setInput(this.dataInterface.getLinkedHazards(this.selectedId));
			} else {
				this.currentTableViewer.setInput(this.dataInterface.getLinkedAccidents(this.selectedId));
			}
		} else {
			this.currentTableViewer.setInput(null);
		}
	}
	
	/**
	 * Updates the selected item. Updates all tables accordingly.
	 * 
	 * @param selectedItem the selected object
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	private void updateSelection() {
		IStructuredSelection selection = (IStructuredSelection) this.selectionTableViewer.getSelection();
		
		if (selection.size() > 0) {
			this.selectedId = ((ITableModel) selection.getFirstElement()).getId();
		} else {
			List<ITableModel> selectionList;
			if (this.accidentMode) {
				selectionList = this.dataInterface.getAllAccidents();
			} else {
				selectionList = this.dataInterface.getAllHazards();
			}
			
			if (selectionList.size() > 0) {
				this.selectedId = selectionList.get(0).getId();
				
				this.selectionTableViewer.getTable().setSelection(0);
			} else {
				this.selectedId = null;
				this.availableTableViewer.setInput(null);
				this.currentTableViewer.setInput(null);
			}
		}
		
		this.updateAvailableTable();
		this.updateCurrentTable();
	}
	
	/**
	 * Updates the selection and content of all tables.
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	public void updateAll() {
		this.updateSelection();
		this.updateSelectionTable();
		this.updateMode();
	}
	
	/**
	 * Returns the selected item in the available table. Returns null if nothing
	 * is selected.
	 * 
	 * @return the selected item
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	private Object[] getSelectedAvailable() {
		if (!LinkingView.this.availableTableViewer.getSelection().isEmpty()) {
			return ((IStructuredSelection) LinkingView.this.availableTableViewer.getSelection()).toArray();
		}
		
		return null;
	}
	
	/**
	 * Returns the selected item in the current table. Returns null if nothing
	 * is selected.
	 * 
	 * @return the selected item
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	private Object[] getSelectedCurrent() {
		if (!LinkingView.this.currentTableViewer.getSelection().isEmpty()) {
			return ((StructuredSelection) LinkingView.this.currentTableViewer.getSelection()).toArray();
		}
		
		return null;
	}
	
	/**
	 * Removes all accidents/hazards, currently linked to the selected
	 * accidents/hazards.
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	private void removelAllCurrentLinks() {
		UUID elementId = this.selectedId;
		if (LinkingView.this.accidentMode) {
			java.util.List<ITableModel> current =
				(java.util.List<ITableModel>) LinkingView.this.currentTableViewer.getInput();
			
			if ((elementId != null) && (current != null)) {
				Iterator<ITableModel> iter = current.iterator();
				while (iter.hasNext()) {
					this.dataInterface.deleteLink(elementId, iter.next().getId());
				}
			}
			
		} else {
			java.util.List<ITableModel> current =
				(java.util.List<ITableModel>) LinkingView.this.currentTableViewer.getInput();
			
			if ((elementId != null) && (current != null)) {
				Iterator<ITableModel> iter = current.iterator();
				while (iter.hasNext()) {
					this.dataInterface.deleteLink(iter.next().getId(), elementId);
				}
			}
		}
	}
	
	/**
	 * Adds all hazards/accidents from the available table to the as links.
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	private void addAllAvailableLinks() {
		UUID elementId = this.selectedId;
		if (LinkingView.this.accidentMode) {
			
			List<ITableModel> available = (List<ITableModel>) LinkingView.this.availableTableViewer.getInput();
			
			if ((elementId != null) && (available != null)) {
				Iterator<ITableModel> iter = available.iterator();
				while (iter.hasNext()) {
					this.dataInterface.addLink(elementId, iter.next().getId());
				}
			}
			
		} else {
			List<ITableModel> available = (List<ITableModel>) LinkingView.this.availableTableViewer.getInput();
			
			if ((elementId != null) && (available != null)) {
				Iterator<ITableModel> iter = available.iterator();
				while (iter.hasNext()) {
					this.dataInterface.addLink(iter.next().getId(), elementId);
				}
			}
		}
	}
	
	/**
	 * Removes the currently selected hazards/accidents in the current table, if
	 * any, from the linked hazards/accident. Does not do anything if no
	 * hazard/accident is selected.
	 * 
	 * @author Patrick Wickenhaeuser
	 * @author Jarkko Heidenwag
	 */
	private void removeSelectedLinks() {
		List<UUID> accidentIDs = new ArrayList<UUID>();
		List<UUID> hazardIDs = new ArrayList<UUID>();
		
		Object[] selected = LinkingView.this.getSelectedCurrent();
		
		if (selected != null) {
			if (LinkingView.this.accidentMode) {
				accidentIDs.add(this.selectedId);
				for (int i = 0; i < selected.length; i++) {
					hazardIDs.add(((ITableModel) selected[i]).getId());
				}
			} else {
				for (int i = 0; i < selected.length; i++) {
					accidentIDs.add(((ITableModel) selected[i]).getId());
				}
				hazardIDs.add(this.selectedId);
			}
			for (int i = 0; i < accidentIDs.size(); i++) {
				for (int j = 0; j < hazardIDs.size(); j++) {
					this.dataInterface.deleteLink(accidentIDs.get(i), hazardIDs.get(j));
				}
			}
			
		}
	}
	
	/**
	 * Adds the currently selected hazards/accidents in the available table, if
	 * any, to the linked hazards/accident. Does not do anything if no
	 * hazard/accident is selected.
	 * 
	 * @author Patrick Wickenhaeuser
	 * @author Jarkko Heidenwag
	 */
	private void addSelectedLinks() {
		List<UUID> accidentIDs = new ArrayList<UUID>();
		List<UUID> hazardIDs = new ArrayList<UUID>();
		
		Object[] selected = LinkingView.this.getSelectedAvailable();
		
		if (selected != null) {
			if (LinkingView.this.accidentMode) {
				accidentIDs.add(this.selectedId);
				for (int i = 0; i < selected.length; i++) {
					hazardIDs.add(((ITableModel) selected[i]).getId());
				}
			} else {
				for (int i = 0; i < selected.length; i++) {
					accidentIDs.add(((ITableModel) selected[i]).getId());
				}
				hazardIDs.add(this.selectedId);
			}
			for (int i = 0; i < accidentIDs.size(); i++) {
				for (int j = 0; j < hazardIDs.size(); j++) {
					this.dataInterface.addLink(accidentIDs.get(i), hazardIDs.get(j));
				}
			}
		}
	}
	
	@Override
	public String getId() {
		return LinkingView.ID;
	}
	
	@Override
	public void onActivateView() {
		this.updateAll();
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void createPartControl(Composite parent) {
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				LinkingView.this.lblSelected.setFont(new Font(Display.getCurrent(), PreferenceConverter.getFontData(
					IViewBase.STORE, IPreferenceConstants.DEFAULT_FONT)));
				LinkingView.this.lblAvailableForLinking.setFont(new Font(Display.getCurrent(), PreferenceConverter
					.getFontData(IViewBase.STORE, IPreferenceConstants.DEFAULT_FONT)));
				
				LinkingView.this.lblCurrentlyLinked.setFont(new Font(Display.getCurrent(), PreferenceConverter
					.getFontData(IViewBase.STORE, IPreferenceConstants.DEFAULT_FONT)));
			}
		});
		
		// font used for the labels over the tables
		Font labelFont =
			new Font(Display.getCurrent(), PreferenceConverter.getFontData(IViewBase.STORE,
				IPreferenceConstants.DEFAULT_FONT));
		
		ColumnLabelProvider idLabelProvider = new ColumnLabelProvider() {
			
			@Override
			public Image getImage(Object element) {
				return null;
			}
			
			@Override
			public String getText(Object element) {
				if (element == null) {
					return "error: element == null"; //$NON-NLS-1$
				}
				
				return Integer.toString(((ITableModel) element).getNumber());
			}
		};
		
		ColumnLabelProvider titleLabelProvider = new ColumnLabelProvider() {
			
			@Override
			public Image getImage(Object element) {
				return null;
			}
			
			@Override
			public String getText(Object element) {
				if (element == null) {
					return "error: element == null"; //$NON-NLS-1$
				}
				
				return ((ITableModel) element).getTitle();
			}
		};
		
		Composite selectionForm = new Composite(parent, SWT.NONE);
		GridLayout selectionFormGridLayout = new GridLayout(2, true);
		selectionForm.setLayout(selectionFormGridLayout);
		
		Composite selectionArea = new Composite(selectionForm, SWT.NONE);
		selectionArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		selectionArea.setLayout(new GridLayout(1, false));
		
		this.lblSelected = new Label(selectionArea, SWT.NONE);
		this.lblSelected.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		this.lblSelected.setFont(labelFont);
		
		// layout selection table columns
		TableColumnLayout tableLayout = new TableColumnLayout();
		Composite tableLayoutComposite = new Composite(selectionArea, SWT.NONE);
		tableLayoutComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tableLayoutComposite.setLayout(tableLayout);
		
		this.selectionTableViewer =
			new TableViewer(tableLayoutComposite, SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.BORDER);
		Table selectionTable = this.selectionTableViewer.getTable();
		selectionTable.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				LinkingView.this.updateSelection();
			}
		});
		selectionTable.setLinesVisible(true);
		selectionTable.setHeaderVisible(true);
		
		TableViewerColumn selectionTableIdColumn = new TableViewerColumn(this.selectionTableViewer, SWT.NONE);
		selectionTableIdColumn.setLabelProvider(idLabelProvider);
		TableColumn selectionIdColumn = selectionTableIdColumn.getColumn();
		selectionIdColumn.setText(Messages.ID);
		
		TableViewerColumn selectionTableTitleColumn = new TableViewerColumn(this.selectionTableViewer, SWT.NONE);
		selectionTableTitleColumn.setLabelProvider(titleLabelProvider);
		TableColumn selectionShortDescriptionColumn = selectionTableTitleColumn.getColumn();
		selectionShortDescriptionColumn.setText(Messages.Title);
		
		Composite modeButtonArea = new Composite(selectionArea, SWT.NONE);
		modeButtonArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		modeButtonArea.setLayout(new GridLayout(1, true));
		
		this.modeButton = new Button(modeButtonArea, SWT.NONE);
		this.modeButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				LinkingView.this.setAccidentMode(!LinkingView.this.accidentMode);
			}
		});
		this.modeButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		this.selectionTableViewer.setContentProvider(new ArrayContentProvider());
		
		final Composite editingForm = new Composite(selectionForm, SWT.NONE);
		editingForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		editingForm.setLayout(new GridLayout(1, true));
		
		this.lblAvailableForLinking = new Label(editingForm, SWT.NONE);
		this.lblAvailableForLinking.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		this.lblAvailableForLinking.setFont(labelFont);
		final Composite availableTableLayoutComposite = new Composite(editingForm, SWT.BORDER);
		availableTableLayoutComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));
		availableTableLayoutComposite.setLayout(tableLayout);
		
		this.availableTableViewer =
			new TableViewer(availableTableLayoutComposite, SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL);
		this.availableTableViewer.getTable().setHeaderVisible(true);
		this.availableTableViewer.getTable().setLinesVisible(true);
		
		this.availableTableViewer.addDoubleClickListener(new IDoubleClickListener() {
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				LinkingView.this.addSelectedLinks();
			}
		});
		
		TableViewerColumn availableTableIdColumn = new TableViewerColumn(this.availableTableViewer, SWT.NONE);
		availableTableIdColumn.setLabelProvider(idLabelProvider);
		
		TableViewerColumn availableTableTitleColumn = new TableViewerColumn(this.availableTableViewer, SWT.NONE);
		availableTableTitleColumn.setLabelProvider(titleLabelProvider);
		this.availableTableViewer.setContentProvider(new ArrayContentProvider());
		
		final Composite buttonArea = new Composite(editingForm, SWT.NONE);
		buttonArea.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 1));
		buttonArea.setLayout(new GridLayout(LinkingView.BUTTON_AREA_COLUMNS, true));
		
		Button addButton = new Button(buttonArea, SWT.NONE);
		addButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		addButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				LinkingView.this.addSelectedLinks();
			}
		});
		addButton.setText(Messages.Add);
		
		Button addAllButton = new Button(buttonArea, SWT.NONE);
		addAllButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		addAllButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				LinkingView.this.addAllAvailableLinks();
			}
		});
		addAllButton.setText(Messages.AddAll);
		
		Button removeButton = new Button(buttonArea, SWT.NONE);
		removeButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		removeButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				LinkingView.this.removeSelectedLinks();
			}
		});
		removeButton.setText(Messages.Remove);
		
		Button removeAllButton = new Button(buttonArea, SWT.NONE);
		removeAllButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		removeAllButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				LinkingView.this.removelAllCurrentLinks();
			}
		});
		removeAllButton.setText(Messages.RemoveAll);
		
		this.lblCurrentlyLinked = new Label(editingForm, SWT.NONE);
		this.lblCurrentlyLinked.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		this.lblCurrentlyLinked.setFont(labelFont);
		final Composite currentTableLayoutComposite = new Composite(editingForm, SWT.BORDER);
		currentTableLayoutComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));
		currentTableLayoutComposite.setLayout(tableLayout);
		
		this.currentTableViewer =
			new TableViewer(currentTableLayoutComposite, SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL);
		this.currentTableViewer.getTable().setHeaderVisible(true);
		this.currentTableViewer.getTable().setLinesVisible(true);
		
		this.currentTableViewer.addDoubleClickListener(new IDoubleClickListener() {
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				LinkingView.this.removeSelectedLinks();
			}
		});
		
		TableViewerColumn currentViewerColumnId = new TableViewerColumn(this.currentTableViewer, SWT.NONE);
		currentViewerColumnId.setLabelProvider(idLabelProvider);
		
		TableColumn currentColumnId = currentViewerColumnId.getColumn();
		currentColumnId.setText(Messages.ID);
		
		TableViewerColumn currentViewerColumnShortDescription =
			new TableViewerColumn(this.currentTableViewer, SWT.NONE);
		currentViewerColumnShortDescription.setLabelProvider(titleLabelProvider);
		
		TableColumn currentColumnTitle = currentViewerColumnShortDescription.getColumn();
		currentColumnTitle.setText(Messages.Title);
		this.currentTableViewer.setContentProvider(new ArrayContentProvider());
		
		TableColumn availableColumnId = availableTableIdColumn.getColumn();
		availableColumnId.setText(Messages.ID);
		TableColumn availableColumnTitle = availableTableTitleColumn.getColumn();
		availableColumnTitle.setText(Messages.Title);
		
		// the controlListener which resizes the tables on the right and makes
		// sure they have the same height
		editingForm.addControlListener(new ControlListener() {
			
			@Override
			public void controlMoved(ControlEvent e) {
				// not needed
			}
			
			@Override
			public void controlResized(ControlEvent e) {
				GridData gridData = new GridData();
				gridData.grabExcessVerticalSpace = true;
				gridData.widthHint = (editingForm.getSize().x - LinkingView.HMINUS);
				int space = editingForm.getSize().y - buttonArea.getSize().y;
				gridData.heightHint = (space / 2) - LinkingView.VMINUS;
				availableTableLayoutComposite.setLayoutData(gridData);
				currentTableLayoutComposite.setLayoutData(gridData);
			}
		});
		
		// setting up the column behavior and layout
		tableLayout.setColumnData(selectionIdColumn, new ColumnWeightData(LinkingView.COLUMN_ID_WEIGHT_X,
			LinkingView.COLUMN_ID_WEIGHT1_Y, false));
		tableLayout.setColumnData(selectionShortDescriptionColumn, new ColumnWeightData(
			LinkingView.COLUMN_TITLE_WEIGHT_X, LinkingView.COLUMN_TITLE_WEIGHT_Y, false));
		tableLayout.setColumnData(currentColumnId, new ColumnWeightData(LinkingView.COLUMN_ID_WEIGHT_X,
			LinkingView.COLUMN_ID_WEIGHT1_Y, false));
		tableLayout.setColumnData(currentColumnTitle, new ColumnWeightData(LinkingView.COLUMN_TITLE_WEIGHT_X,
			LinkingView.COLUMN_TITLE_WEIGHT_Y, false));
		tableLayout.setColumnData(availableColumnId, new ColumnWeightData(LinkingView.COLUMN_ID_WEIGHT_X,
			LinkingView.COLUMN_ID_WEIGHT1_Y, false));
		tableLayout.setColumnData(availableColumnTitle, new ColumnWeightData(LinkingView.COLUMN_TITLE_WEIGHT_X,
			LinkingView.COLUMN_TITLE_WEIGHT_Y, false));
		
		this.setAccidentMode(true);
		
		this.updateAll();
	}
	
	/**
	 * Updates the text of the mode button.
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	private void updateMode() {
		if (this.accidentMode) {
			this.lblSelected.setText(Messages.Accidents);
			this.lblAvailableForLinking.setText(Messages.HazardsAvailableForLinking);
			this.lblCurrentlyLinked.setText(Messages.CurrentlyLinkedHazards);
			this.modeButton.setText(Messages.SwitchToHazards);
		} else {
			this.lblSelected.setText(Messages.Hazards);
			this.lblAvailableForLinking.setText(Messages.AccidentsAvailableForLinking);
			this.lblCurrentlyLinked.setText(Messages.CurrentlyLinkedAccidents);
			this.modeButton.setText(Messages.SwitchToAccidents);
		}
		
		this.updateSelection();
	}
	
	@Override
	public String getTitle() {
		return Messages.LinkingOfAccidentsAndHazards;
	}
	
	@Override
	public void setDataModelInterface(IDataModel dataInterface) {
		this.dataInterface = (ILinkingViewDataModel) dataInterface;
		this.dataInterface.addObserver(this);
	}
	
	@Override
	public void update(Observable dataModelController, Object updatedValue) {
		ObserverValue type = (ObserverValue) updatedValue;
		
		switch (type) {
		case ACCIDENT:
			this.updateAll();
			break;
		
		case HAZARD:
			this.updateAll();
			break;
		
		case HAZ_ACC_LINK:
			this.updateAll();
			break;
		
		default:
			break;
		}
	}

	@Override
	public boolean triggerExport() {
		// TODO Auto-generated method stub
		return false;
	}
}
