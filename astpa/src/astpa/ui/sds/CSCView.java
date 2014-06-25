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

package astpa.ui.sds;

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

import astpa.model.ObserverValue;
import astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import astpa.model.interfaces.ICorrespondingSafetyConstraintDataModel;
import astpa.model.interfaces.IDataModel;
import astpa.ui.acchaz.ATableFilter;
import astpa.ui.common.IViewBase;

/**
 * 
 * @author Jarkko Heidenwag
 * 
 */
public class CSCView implements IViewBase {
	
	/**
	 * @author Jarkko Heidenwag
	 * 
	 */
	public static final String ID = "astpa.ui.sds.cscview"; //$NON-NLS-1$
	
	private ICorrespondingSafetyConstraintDataModel dataInterface;
	private TableViewer tableViewer;
	private ATableFilter filter;
	private Text filterTextField;
	TableViewerColumn unsafeControlActionsColumn;
	TableViewerColumn safetyConstraintsColumn;
	
	
	/**
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 */
	public CSCView() {
	}
	
	/**
	 * Create contents of the view part.
	 * 
	 * @author Jarkko Heidenwag
	 * @param parent The parent composite
	 */
	@Override
	public void createPartControl(Composite parent) {
		// setting up the outer composite
		Composite cscComposite = new Composite(parent, SWT.NONE);
		cscComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		cscComposite.setLayout(new GridLayout(1, true));
		
		// setting up the composite for the filter
		Composite filterComposite = new Composite(cscComposite, SWT.NONE);
		filterComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		final int filterColumns = 5;
		filterComposite.setLayout(new GridLayout(filterColumns, false));
		Label filterLabel = new Label(filterComposite, SWT.TRAIL);
		filterLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
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
		ucaButton.setText(Messages.UnsafeControlActions);
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
		rscButton.setText(Messages.CorrespondingSafetyConstraints);
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
		
		this.filterTextField = new Text(filterComposite, SWT.SINGLE | SWT.BORDER);
		this.filterTextField.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		// KeyListener for the filter
		this.filterTextField.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent ke) {
				CSCView.this.filter.setSearchText(CSCView.this.filterTextField.getText());
				CSCView.this.tableViewer.refresh(true, true);
				CSCView.this.tableViewer.setInput(CSCView.this.dataInterface.getAllUnsafeControlActions());
			}
		});
		
		// setting up the composite for the table
		Composite tableComposite = new Composite(cscComposite, SWT.NONE);
		tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tableComposite.setLayout(new GridLayout(1, true));
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);
		
		// setting up the table viewer
		this.tableViewer = new TableViewer(tableComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.MULTI);
		this.tableViewer.setContentProvider(new ArrayContentProvider());
		this.tableViewer.getTable().setLinesVisible(true);
		this.tableViewer.getTable().setHeaderVisible(true);
		
		this.filter = new ATableFilter();
		CSCView.this.tableViewer.addFilter(this.filter);
		
		// since both columns should behave the same both will use these
		// constants as their ColumnWeightData
		final int weight = 50;
		final int minWidth = 100;
		
		// the left column is for the unsafe control actions
		this.unsafeControlActionsColumn = new TableViewerColumn(this.tableViewer, SWT.NONE);
		this.unsafeControlActionsColumn.getColumn().setText(Messages.UnsafeControlActions);
		tableColumnLayout.setColumnData(this.unsafeControlActionsColumn.getColumn(), new ColumnWeightData(weight,
			minWidth, false));
		
		this.unsafeControlActionsColumn.setLabelProvider(new ColumnLabelProvider() {
			
			@Override
			public String getText(Object element) {
				if (element instanceof ICorrespondingUnsafeControlAction) {
					return ((ICorrespondingUnsafeControlAction) element).getDescription();
				}
				return null;
			}
		});
		
		// the right column is for the resulting safety constraints
		this.safetyConstraintsColumn = new TableViewerColumn(this.tableViewer, SWT.NONE);
		this.safetyConstraintsColumn.getColumn().setText(Messages.CorrespondingSafetyConstraints);
		tableColumnLayout.setColumnData(this.safetyConstraintsColumn.getColumn(), new ColumnWeightData(weight,
			minWidth, false));
		
		this.safetyConstraintsColumn.setLabelProvider(new ColumnLabelProvider() {
			
			@Override
			public String getText(Object element) {
				if (element instanceof ICorrespondingUnsafeControlAction) {
					return ((ICorrespondingUnsafeControlAction) element).getCorrespondingSafetyConstraint().getText();
				}
				return null;
			}
		});
		
		// detecting a double click
		ColumnViewerEditorActivationStrategy activationSupport =
			new ColumnViewerEditorActivationStrategy(this.tableViewer) {
				
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
		
		// Listener for editing a title by pressing return
		Listener returnListener = new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				if ((event.type == SWT.KeyDown) && (event.keyCode == SWT.CR)
					&& (!CSCView.this.tableViewer.getSelection().isEmpty())) {
					int selected = CSCView.this.tableViewer.getTable().getSelectionIndex();
					CSCView.this.safetyConstraintsColumn.getViewer().editElement(
						CSCView.this.tableViewer.getElementAt(selected), 1);
				}
			}
		};
		
		this.tableViewer.getTable().addListener(SWT.KeyDown, returnListener);
		
		TableViewerEditor.create(this.tableViewer, null, activationSupport, ColumnViewerEditor.TABBING_HORIZONTAL
			| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.TABBING_VERTICAL
			| ColumnViewerEditor.KEYBOARD_ACTIVATION);
		
		// the editing support for the right column
		final EditingSupport correspondingSafetyConstraintEditingSupport = new CSCEditingSupport(this.tableViewer);
		this.safetyConstraintsColumn.setEditingSupport(correspondingSafetyConstraintEditingSupport);
		this.tableViewer.setInput(this.dataInterface.getAllUnsafeControlActions());
		
	}
	
	@Override
	public String getId() {
		return CSCView.ID;
	}
	
	@Override
	public String getTitle() {
		return Messages.CorrespondingSafetyConstraints;
	}
	
	@Override
	public void onActivateView() {
		// intentionally empty
	}
	
	@Override
	public void setDataModelInterface(IDataModel dataInterface) {
		this.dataInterface = (ICorrespondingSafetyConstraintDataModel) dataInterface;
		this.dataInterface.addObserver(this);
	}
	
	@Override
	public void update(Observable dataModelController, Object updatedValue) {
		ObserverValue type = (ObserverValue) updatedValue;
		switch (type) {
		case UNSAFE_CONTROL_ACTION:
			this.tableViewer.refresh(true, true);
			this.tableViewer.setInput(this.dataInterface.getAllUnsafeControlActions());
			break;
		default:
			break;
		}
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
		this.tableViewer.setInput(CSCView.this.dataInterface.getAllUnsafeControlActions());
	}
	
	
	private class CSCEditingSupport extends EditingSupport {
		
		/**
		 * 
		 * @author Jarkko Heidenwag
		 * 
		 * @param viewer the ColumnViewer
		 */
		public CSCEditingSupport(ColumnViewer viewer) {
			super(viewer);
		}
		
		@Override
		protected boolean canEdit(Object element) {
			return true;
		}
		
		@Override
		protected CellEditor getCellEditor(Object element) {
			return new TextCellEditor(CSCView.this.tableViewer.getTable());
		}
		
		@Override
		protected Object getValue(Object element) {
			if (element instanceof ICorrespondingUnsafeControlAction) {
				return ((ICorrespondingUnsafeControlAction) element).getCorrespondingSafetyConstraint().getText();
			}
			return null;
		}
		
		@Override
		protected void setValue(Object element, Object value) {
			if (element instanceof ICorrespondingUnsafeControlAction) {
				CSCView.this.dataInterface.setCorrespondingSafetyConstraint(
					((ICorrespondingUnsafeControlAction) element).getId(), String.valueOf(value));
			}
			CSCView.this.tableViewer.refresh(true, true);
			CSCView.this.tableViewer.setInput(CSCView.this.dataInterface.getAllUnsafeControlActions());
		}
	}


	@Override
	public boolean triggerExport(String path) {
		// TODO Auto-generated method stub
		return false;
	}
}
