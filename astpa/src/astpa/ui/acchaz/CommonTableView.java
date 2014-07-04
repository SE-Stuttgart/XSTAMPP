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

package astpa.ui.acchaz;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import messages.Messages;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ColumnWeightData;
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import astpa.Activator;
import astpa.model.ITableModel;
import astpa.model.ObserverValue;
import astpa.model.interfaces.IDataModel;
import astpa.preferences.IPreferenceConstants;
import astpa.ui.common.IViewBase;

/**
 * @author Jarkko Heidenwag
 * 
 */
public abstract class CommonTableView implements IViewBase {
	
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
	private Button deleteItemsButton;
	private ATableFilter filter;
	private Text filterTextField;
	
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
	 * @param tableViewer the table viewer
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
	 * @param tableContainer the table container
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
	 * @param descriptionWidget the description widget
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
	 * @param tableColumnLayout the table column layout
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
	 * @param idColumn the id column
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
	 * @param titleColumn the title column
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
	 * @param addNewItemButton the add button
	 */
	public void setAddNewItemButton(Button addNewItemButton) {
		this.addNewItemButton = addNewItemButton;
	}
	
	/**
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 * @return the delete button
	 */
	public Button getDeleteItemsButton() {
		return this.deleteItemsButton;
	}
	
	/**
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 * @param deleteItemsButton the delete button
	 */
	public void setDeleteItemsButton(Button deleteItemsButton) {
		this.deleteItemsButton = deleteItemsButton;
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
	 * @param filter the filter to set
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
	 * @param filterTextField the filter TextField
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
	 * @param parent The parent composite
	 */
	@Override
	public void createPartControl(Composite parent) {
		// to be implemented by the sub class
	}
	
	/**
	 * Create the standard contents of the CommonTableView.
	 * 
	 * @author Jarkko Heidenwag
	 * @param parent The parent composite
	 * @param tableHeader Type of item, e.g. "Accidents"
	 */
	public void createCommonTableView(Composite parent, String tableHeader) {
		
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				CommonTableView.this.itemsLabel.setFont(new Font(Display.getCurrent(), PreferenceConverter.getFontData(
					IViewBase.STORE, IPreferenceConstants.DEFAULT_FONT)));
				CommonTableView.this.filterLabel.setFont(new Font(Display.getCurrent(), PreferenceConverter
					.getFontData(IViewBase.STORE, IPreferenceConstants.DEFAULT_FONT)));
				CommonTableView.this.descriptionLabel.setFont(new Font(Display.getCurrent(), PreferenceConverter
					.getFontData(IViewBase.STORE, IPreferenceConstants.DEFAULT_FONT)));
			}
		});
		
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
		this.itemsLabel.setFont(new Font(Display.getCurrent(), PreferenceConverter.getFontData(IViewBase.STORE,
			IPreferenceConstants.DEFAULT_FONT)));
		this.itemsLabel.setText(tableHeader);
		this.itemsLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		
		this.filterLabel = new Label(leftHeadComposite, SWT.TRAIL);
		this.filterLabel.setFont(new Font(Display.getCurrent(), PreferenceConverter.getFontData(IViewBase.STORE,
			IPreferenceConstants.DEFAULT_FONT)));
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
		
		//the add and delete buttons are arranged in a composite with a 2x1 GridLayout
		this.buttonComposite = new Composite(this.tableContainer, SWT.NONE);
		this.buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		this.buttonComposite.setLayout(new GridLayout(2, true));
		
		// the Button for adding new items
		this.addNewItemButton = new Button(this.buttonComposite, SWT.PUSH);
		
		GridData gridData = new GridData(SWT.NONE, SWT.NONE, false, false);
		final int buttonSize = 46;
		gridData.widthHint = buttonSize;
		gridData.heightHint = buttonSize;
		this.addNewItemButton.setLayoutData(gridData);
		
		// the Button for deleting selected items
		this.deleteItemsButton = new Button(this.buttonComposite, SWT.PUSH);
		
		this.deleteItemsButton.setLayoutData(gridData);
		
		Listener deleteItemListener = new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				CommonTableView.this.deleteItems();
			}
		};
		
		this.deleteItemsButton.addListener(SWT.Selection, deleteItemListener);
		
		Composite rightHeadComposite = new Composite(textContainer, SWT.NONE);
		rightHeadComposite.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		rightHeadComposite.setLayout(new GridLayout(2, true));
		
		this.descriptionLabel = new Label(rightHeadComposite, SWT.LEAD);
		this.descriptionLabel.setFont(new Font(Display.getCurrent(), PreferenceConverter.getFontData(IViewBase.STORE,
			IPreferenceConstants.DEFAULT_FONT)));
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
			}
			
		});
		
		// the table viewer
		this.setTableViewer(new TableViewer(tableComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.MULTI));
		this.getTableViewer().setContentProvider(new ArrayContentProvider());
		this.getTableViewer().getTable().setLinesVisible(true);
		this.getTableViewer().getTable().setHeaderVisible(true);
		
		// the columns of the table
		this.idColumn = new TableViewerColumn(this.getTableViewer(), SWT.NONE);
		this.idColumn.getColumn().setText(Messages.ID);
		final int idWeight = 5;
		final int idMinWidth = 39;
		this.tableColumnLayout.setColumnData(this.idColumn.getColumn(), new ColumnWeightData(idWeight, idMinWidth,
			false));
		
		this.titleColumn = new TableViewerColumn(this.getTableViewer(), SWT.NONE);
		this.titleColumn.getColumn().setText(Messages.Title);
		final int titleWeight = 50;
		final int titleMinWidth = 50;
		this.tableColumnLayout.setColumnData(this.titleColumn.getColumn(), new ColumnWeightData(titleWeight,
			titleMinWidth, false));
		
		// detecting a double click
		ColumnViewerEditorActivationStrategy activationSupport =
			new ColumnViewerEditorActivationStrategy(this.getTableViewer()) {
				
				@Override
				protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
					final int leftClick = 1;
					// Enable editor only with mouse double click
					if (((event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION) && (((MouseEvent) event.sourceEvent).button == leftClick))
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
					CommonTableView.this.tableViewer.setSelection(new StructuredSelection(CommonTableView.this
						.getTableViewer().getElementAt(0)), true);
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
		Control[] controls = {leftHeadComposite, this.buttonComposite, tableComposite};
		this.tableContainer.setTabList(controls);
	}

	
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
	public void onActivateView() {
		// intentionally empty
	}
	
	@Override
	public String getTitle() {
		return Messages.CommonTable;
	}
	
	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @param models
	 * 			the data which shall be exported as CSV
	 * @param seperator TODO
	 * @throws IOException 
	 */
	protected void exportAsCSV(List<ITableModel> models,BufferedWriter csvWriter, char seperator) throws IOException{
			csvWriter.newLine();
			csvWriter.write(this.getTitle());
			csvWriter.newLine();
			csvWriter.write("ID ;");
			csvWriter.write("Name ;");
			csvWriter.write("Description");
			csvWriter.newLine();
			for(ITableModel data: models){
				csvWriter.write(data.getNumber() + seperator);
				csvWriter.write(data.getTitle() + seperator);
				csvWriter.write(data.getDescription());
				csvWriter.newLine();
			}
	}
	
	
	
	@Override
	public void update(Observable dataModelController, Object updatedValue) {
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
	public abstract void deleteItems();
	
	/**
	 * @author Jarkko Heidenwag
	 * 
	 */
	public abstract void updateTable();
	
	@Override
	public abstract void setDataModelInterface(IDataModel dataInterface);
	
	@Override
	public boolean triggerExport(String path) {
		// TODO Auto-generated method stub
		return false;
	}
}
