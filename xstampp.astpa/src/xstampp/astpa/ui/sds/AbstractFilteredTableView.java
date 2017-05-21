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

package xstampp.astpa.ui.sds;

import java.util.Arrays;
import java.util.List;

import messages.Messages;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.StandartEditorPart;
import xstampp.util.ColorManager;

/**
 * 
 * @author Jarkko Heidenwag, Lukas Balzer
 * 
 */
public abstract class AbstractFilteredTableView extends StandartEditorPart{
	
	/**
	 * an object of type EditSupportProvider is used to construct an EditingSupport using the
	 * methods {@link EditSupportProvider#getEditingValue(Object)} and {@link EditSupportProvider#setEditValue(Object, Object)} 
	 * 
	 * @author Lukas Balzer
	 *
	 */
	protected abstract class EditSupportProvider{
	  protected boolean canEdit(Object element) {
	    return true;
	  }
		/**
		 * Returns the value that is available for editing the given element<br> this function is called 
		 * by the EditingSupport of the displayed table, if this returns null the edit is disabled<p>
		 * <b>the default value is null, implementors should overwrite this function to enable the editing support</b> 
		 * @param element the element of the table column in focus
		 * 
		 * @return the value that is available for editing the given element, or null if the given element cannot be edited
		 * 
		 */
		abstract protected Object getEditingValue(Object element);
		
		/**
		 * Called in the setValue function of the Editing support,
		 * the default implementation does nothing 
		 * @param element the model 
		 * @param value the new value
		 */
		abstract protected void setEditValue(Object element, Object value);
	}
	
	private class CSCEditingSupport extends EditingSupport {

		private EditSupportProvider supportProvider;
		/**
		 * 
		 * @author Jarkko Heidenwag
		 * 
		 * @param viewer
		 *            the ColumnViewer
		 */
		public CSCEditingSupport(ColumnViewer viewer, EditSupportProvider provider) {
			super(viewer);
			this.supportProvider = provider;
		}

		@Override
		protected boolean canEdit(Object element) {
			return supportProvider.canEdit(element);
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return new TextCellEditor(tableViewer.getTable(),SWT.NONE);
		}

		@Override
		protected Object getValue(Object element) {
			return supportProvider.getEditingValue(element);
		}

		@Override
		protected void setValue(Object element, Object value) {
			supportProvider.setEditValue(element, value);
			tableViewer.refresh(true, true);
			tableViewer.setInput(getInput());
		}
	}
	
	protected class CSCLabelProvider extends ColumnLabelProvider{

    @Override
    public Color getForeground(Object element) {
      if (!canEdit(element)) {
        return getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY);
      }
      return null;
    }
    
		@Override
		public Color getBackground(Object element) {
			int index = ((List<?>)tableViewer.getInput()).indexOf(element);
			if(index % 2 == 0){
				return ColorManager.registerColor("EVEN_TABLE_ENTRY", new RGB(230,230,230));
			}
			return ColorManager.registerColor("UNEVEN_TABLE_ENTRY", new RGB(255,255,255));
		}
	}
	
	
	private IDataModel dataInterface;
	private TableViewer tableViewer;
	protected ModeFilter filter;
	private Text filterTextField;
	private TableViewerColumn safetyConstraintsColumn;
	private String[] headers;
	private EditSupportProvider[] editingSupports;
	private int[] columnWeights;
	private boolean[] refreshVector;
	/**
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 */
	public AbstractFilteredTableView(ModeFilter filter, String[] cols) {
		this.filter = filter;
		this.headers = cols;
		columnWeights = new int[this.headers.length];
		editingSupports = new EditSupportProvider[this.headers.length];
		refreshVector = new boolean[this.headers.length];
		Arrays.fill(columnWeights, 1);
		Arrays.fill(editingSupports, null);
		Arrays.fill(refreshVector, false);
		
	}

	/**
	 * this method assigns new weights to the columns defined by the header names in the constructor
	 *
	 * @param columnWeights an array containing new weights for each column in the table the length of the given array must match the amounts of columns
	 */
	public void setColumnWeights(int[] columnWeights) {
		Assert.isTrue(this.columnWeights.length == columnWeights.length);
		this.columnWeights = columnWeights;
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
		this.setDataModelInterface(ProjectManager.getContainerInstance()
				.getDataModel(this.getProjectID()));
		 // setting up the outer composite
		Composite cscComposite = new Composite(parent, SWT.NONE);
		cscComposite
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		cscComposite.setLayout(new GridLayout(1, true));

		// setting up the composite for the filter
		Composite filterComposite = new Composite(cscComposite, SWT.NONE);
		filterComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));
		filterComposite.setLayout(new GridLayout(filter.getCategorys().size() +2, false));
		Label filterLabel = new Label(filterComposite, SWT.TRAIL);
		filterLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		filterLabel.setText(Messages.Filter);

		for(String category: filter.getCategorys()){
			Button bothButton = new Button(filterComposite, SWT.RADIO);
			bothButton.setText(category);
			bothButton.setSelection(true);
			final String fin_category = category;
			bothButton.addSelectionListener(new SelectionListener() {
	
				@Override
				public void widgetSelected(SelectionEvent e) {
					AbstractFilteredTableView.this.filter.setCSCFilterMode(fin_category);
					AbstractFilteredTableView.this.emptyFilter();
				}
	
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// intentionally empty
				}
			});
		}
	

		this.filterTextField = new Text(filterComposite, SWT.SINGLE
				| SWT.BORDER);
		this.filterTextField.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, false));

		// KeyListener for the filter
		this.filterTextField.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent ke) {
				AbstractFilteredTableView.this.filter.setSearchText(AbstractFilteredTableView.this.filterTextField
						.getText());
				AbstractFilteredTableView.this.tableViewer.refresh(true, true);
				tableViewer.setInput(getInput());
			}
		});

		// setting up the composite for the table
		Composite tableComposite = new Composite(cscComposite, SWT.NONE);
		tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));
		tableComposite.setLayout(new GridLayout(1, true));
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);
		// setting up the table viewer
		this.tableViewer = new TableViewer(tableComposite, SWT.BORDER
				| SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.MULTI);
		this.tableViewer.setContentProvider(new ArrayContentProvider());
		this.tableViewer.getTable().setLinesVisible(true);
		this.tableViewer.getTable().setHeaderVisible(true);
		if(this.filter != null){
			AbstractFilteredTableView.this.tableViewer.addFilter(this.filter);
		}

		for (int i = 0; i < headers.length; i++) {
			TableViewerColumn column = new TableViewerColumn(
					this.tableViewer, SWT.NONE);
			column.setLabelProvider(getColumnProvider(i));
			column.getColumn().setText(this.headers[i]);
			if(columnWeights[i]<0){
				refreshVector[i] = true;
				tableColumnLayout.setColumnData(
						column.getColumn(),
						new ColumnWeightData(1,30, true));
			}else{
				tableColumnLayout.setColumnData(
					column.getColumn(),
					new ColumnWeightData(columnWeights[i],30, true));
			}
			
			if(editingSupports[i] != null){
				column.setEditingSupport(new CSCEditingSupport(tableViewer, editingSupports[i]));
			}
		}
		
		

		// detecting a double click
		ColumnViewerEditorActivationStrategy activationSupport = new ColumnViewerEditorActivationStrategy(
				this.tableViewer) {

			@Override
			protected boolean isEditorActivationEvent(
					ColumnViewerEditorActivationEvent event) {
				final int leftClick = 1;
				// Enable editor only with mouse double click
				if (((event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION) && (((MouseEvent) event.sourceEvent).button == leftClick))
						|| (event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC)) {
					return true;
				}
				return false;
			}
		};

		// Listener for editing a title by pressing return
		Listener returnListener = new Listener() {

			@Override
			public void handleEvent(Event event) {
				if ((event.type == SWT.KeyDown) && (event.keyCode == SWT.CR)
						&& (!AbstractFilteredTableView.this.tableViewer.getSelection().isEmpty())) {
					int selected = AbstractFilteredTableView.this.tableViewer.getTable()
							.getSelectionIndex();
					AbstractFilteredTableView.this.safetyConstraintsColumn
							.getViewer()
							.editElement(
									AbstractFilteredTableView.this.tableViewer
											.getElementAt(selected),
									1);
				}
			}
		};

		this.tableViewer.getTable().addListener(SWT.KeyDown, returnListener);

		if(hasEditSupport()){
			TableViewerEditor.create(this.tableViewer, null, activationSupport,
					ColumnViewerEditor.TABBING_HORIZONTAL
							| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
							| ColumnViewerEditor.TABBING_VERTICAL
							| ColumnViewerEditor.KEYBOARD_ACTIVATION);
		}
		packColumns();
		

	}

	protected final void packColumns() {
		this.tableViewer.refresh(true, true);
		this.tableViewer.setInput(getInput());
		for(int i=0; i< refreshVector.length;i++){
			TableColumn col = this.tableViewer.getTable().getColumn(i);
			if(refreshVector[i]){
				col.pack();
			}
		}
	}

	
	public void setDataModelInterface(IDataModel dataInterface) {
		this.dataInterface = dataInterface;
		this.dataInterface.addObserver(this);
	}
	
	protected IDataModel getDataInterface(){
		return dataInterface;
	}
	
	protected void refresh(){
		if(!this.tableViewer.getTable().isDisposed()){
			this.tableViewer.refresh(true, true);
			this.tableViewer.setInput(getInput());
		}
	}
	
	/**
	 * deletes any String contained in the filter text field
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 */
	private void emptyFilter() {
		this.filter.setSearchText(""); //$NON-NLS-1$
		this.filterTextField.setText(""); //$NON-NLS-1$
		this.tableViewer.refresh(true, true);
		this.tableViewer.setInput(getInput());
	}
	protected void addEditingSupport(int index, EditSupportProvider support) {
		this.editingSupports[index] = support;
	}
	
	@Override
	public void dispose() {
		this.dataInterface.deleteObserver(this);
		super.dispose();
	}

	abstract protected CSCLabelProvider getColumnProvider(int columnIndex);
	
	abstract protected List<?> getInput();
	abstract protected boolean hasEditSupport();
	abstract protected boolean canEdit(Object element);

}
