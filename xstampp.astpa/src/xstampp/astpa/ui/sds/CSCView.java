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

package xstampp.astpa.ui.sds;

import java.util.Observable;

import messages.Messages;

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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.interfaces.ICorrespondingSafetyConstraintDataModel;
import xstampp.astpa.ui.acchaz.ATableFilter;
import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.StandartEditorPart;

/**
 * 
 * @author Jarkko Heidenwag
 * 
 */
public class CSCView extends StandartEditorPart{

	/**
	 * @author Jarkko Heidenwag
	 * 
	 */
	public static final String ID = "astpa.steps.step2_3"; //$NON-NLS-1$

	private ICorrespondingSafetyConstraintDataModel dataInterface;
	private TableViewer tableViewer;
	protected ModeFilter filter;
	private Text filterTextField;
	private TableViewerColumn unsafeControlActionsColumn;
	private TableViewerColumn safetyConstraintsColumn;
	protected final String[] headers = new String[4];
	/**
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 */
	public CSCView() {
		this.filter = new ATableFilter();
		this.headers[0] = Messages.ID;
		this.headers[1] = Messages.UnsafeControlActions;
		this.headers[2] = Messages.ID;
		this.headers[3] = Messages.CorrespondingSafetyConstraints;
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
		final int filterColumns = 5;
		filterComposite.setLayout(new GridLayout(filterColumns, false));
		Label filterLabel = new Label(filterComposite, SWT.TRAIL);
		filterLabel
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		filterLabel.setText(Messages.Filter);

		Button bothButton = new Button(filterComposite, SWT.RADIO);
		bothButton.setText(Messages.All);
		bothButton.setSelection(true);
		bothButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				CSCView.this.filter.setCSCFilterMode(0);
				CSCView.this.emptyFilter();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// intentionally empty
			}
		});

		Button ucaButton = new Button(filterComposite, SWT.RADIO);
		ucaButton.setText(headers[1]);
		ucaButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				CSCView.this.filter.setCSCFilterMode(1);
				CSCView.this.emptyFilter();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// intentionally empty
			}
		});

		Button rscButton = new Button(filterComposite, SWT.RADIO);
		rscButton.setText(headers[3]);
		rscButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				CSCView.this.filter.setCSCFilterMode(2);
				CSCView.this.emptyFilter();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// intentionally empty
			}
		});

		this.filterTextField = new Text(filterComposite, SWT.SINGLE
				| SWT.BORDER);
		this.filterTextField.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, false));

		// KeyListener for the filter
		this.filterTextField.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent ke) {
				CSCView.this.filter.setSearchText(CSCView.this.filterTextField
						.getText());
				CSCView.this.tableViewer.refresh(true, true);
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
			CSCView.this.tableViewer.addFilter(this.filter);
		}

		// since both columns should behave the same both will use these
		// constants as their ColumnWeightData
		final int weight = 40;
		final int minWidth = 100;

		TableViewerColumn ucaIdColumn = new TableViewerColumn(
				this.tableViewer, SWT.NONE);
		ucaIdColumn.setLabelProvider(getColumnProvider(0));
		ucaIdColumn.getColumn().setText(this.headers[0]);
		tableColumnLayout.setColumnData(
				ucaIdColumn.getColumn(),
				new ColumnWeightData(2, false));
		
		// the left column is for the unsafe control actions
		this.unsafeControlActionsColumn = new TableViewerColumn(
				this.tableViewer, SWT.NONE);
		unsafeControlActionsColumn.setLabelProvider(getColumnProvider(1));
		this.unsafeControlActionsColumn.getColumn().setText(
				this.headers[1]);
		tableColumnLayout.setColumnData(
				this.unsafeControlActionsColumn.getColumn(),
				new ColumnWeightData(weight, minWidth, false));

		
		TableViewerColumn srIdColumn = new TableViewerColumn(
				this.tableViewer, SWT.NONE);
		srIdColumn.setLabelProvider(getColumnProvider(2));
		srIdColumn.getColumn().setText(this.headers[2]);
		tableColumnLayout.setColumnData(
				srIdColumn.getColumn(),
				new ColumnWeightData(3, false));
		// the right column is for the resulting safety constraints
		this.safetyConstraintsColumn = new TableViewerColumn(this.tableViewer,
				SWT.NONE);
		safetyConstraintsColumn.setLabelProvider(getColumnProvider(3));
		this.safetyConstraintsColumn.getColumn().setText(this.headers[3]);
		tableColumnLayout.setColumnData(this.safetyConstraintsColumn
				.getColumn(), new ColumnWeightData(weight, minWidth, false));

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
						&& (!CSCView.this.tableViewer.getSelection().isEmpty())) {
					int selected = CSCView.this.tableViewer.getTable()
							.getSelectionIndex();
					CSCView.this.safetyConstraintsColumn
							.getViewer()
							.editElement(
									CSCView.this.tableViewer
											.getElementAt(selected),
									1);
				}
			}
		};

		this.tableViewer.getTable().addListener(SWT.KeyDown, returnListener);

		TableViewerEditor.create(this.tableViewer, null, activationSupport,
				ColumnViewerEditor.TABBING_HORIZONTAL
						| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
						| ColumnViewerEditor.TABBING_VERTICAL
						| ColumnViewerEditor.KEYBOARD_ACTIVATION);

		// the editing support for the right column
		final EditingSupport correspondingSafetyConstraintEditingSupport = new CSCEditingSupport(
				this.tableViewer, SWT.NONE);
		this.safetyConstraintsColumn
				.setEditingSupport(correspondingSafetyConstraintEditingSupport);
		packColumns();
		

	}

	@Override
	public String getId() {
		return CSCView.ID;
	}

	protected void packColumns() {
		this.tableViewer.refresh(true, true);
		this.tableViewer.setInput(getInput());
		this.tableViewer.getTable().getColumn(0).pack();
		this.tableViewer.getTable().getColumn(2).pack();
	}
	@Override
	public String getTitle() {
		return Messages.CorrespondingSafetyConstraints;
	}


	public void setDataModelInterface(IDataModel dataInterface) {
		this.dataInterface = (ICorrespondingSafetyConstraintDataModel) dataInterface;
		this.dataInterface.addObserver(this);
	}

	@Override
	public void update(Observable dataModelController, Object updatedValue) {
		super.update(dataModelController, updatedValue);
		ObserverValue type = (ObserverValue) updatedValue;
		switch (type) {
		case UNSAFE_CONTROL_ACTION:
			packColumns();
			break;
		default:
			break;
		}
	}

	protected void refresh(){
		if(!this.tableViewer.getTable().isDisposed()){
			this.tableViewer.refresh(true, true);
			this.tableViewer.setInput(getInput());
		}
	}
	
	protected ICorrespondingSafetyConstraintDataModel getDataInterface(){
		return dataInterface;
	}
	protected Object getInput() {
		return this.dataInterface.getAllUnsafeControlActions();
	}
	/**
	 * deletes any String contained in the filter text field
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 */
	public void emptyFilter() {
		this.filter.setSearchText(""); //$NON-NLS-1$
		this.filterTextField.setText(""); //$NON-NLS-1$
		this.tableViewer.refresh(true, true);
		this.tableViewer.setInput(getInput());
	}

	private class CSCEditingSupport extends EditingSupport {

		private int style;
		/**
		 * 
		 * @author Jarkko Heidenwag
		 * 
		 * @param viewer
		 *            the ColumnViewer
		 * @param style TODO
		 */
		public CSCEditingSupport(ColumnViewer viewer, int style) {
			super(viewer);
			this.style = style;
		}

		@Override
		protected boolean canEdit(Object element) {
			return true;
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return new TextCellEditor(CSCView.this.tableViewer.getTable(),this.style);
		}

		@Override
		protected Object getValue(Object element) {
			if (element instanceof ICorrespondingUnsafeControlAction) {
				return ((ICorrespondingUnsafeControlAction) element)
						.getCorrespondingSafetyConstraint().getText();
			}
			return null;
		}

		@Override
		protected void setValue(Object element, Object value) {
			if (element instanceof ICorrespondingUnsafeControlAction) {
				CSCView.this.dataInterface.setCorrespondingSafetyConstraint(
						((ICorrespondingUnsafeControlAction) element).getId(),
						String.valueOf(value));
			}
			CSCView.this.tableViewer.refresh(true, true);
			tableViewer.setInput(getInput());
		}
	}


	@Override
	public void dispose() {
		this.dataInterface.deleteObserver(this);
		super.dispose();
	}

	protected ColumnLabelProvider getColumnProvider(int columnIndex){
		switch(columnIndex){
		case 0: 
			return new ColumnLabelProvider(){
				@Override
				public String getText(Object element) {
					return "UCA1."+ CSCView.this.dataInterface.getUCANumber(((ICorrespondingUnsafeControlAction) element)
							.getId());
				}
			};
		case 1:
			return new ColumnLabelProvider(){
				@Override
				public String getText(Object element) {
					return ((ICorrespondingUnsafeControlAction) element)
							.getDescription();
				}
			};
		case 2:
			return new ColumnLabelProvider(){
				@Override
				public String getText(Object element) {
					return "SR1."+ CSCView.this.dataInterface.getUCANumber(((ICorrespondingUnsafeControlAction) element)
							.getId());
				}
			};
		case 3:
			return new ColumnLabelProvider(){
				@Override
				public String getText(Object element) {
					return ((ICorrespondingUnsafeControlAction) element)
							.getCorrespondingSafetyConstraint()
							.getText();
				}
			};
			
			
		}
		return null;
	}
	
}
