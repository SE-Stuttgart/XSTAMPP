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

import messages.Messages;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;

import xstampp.astpa.model.hazacc.ATableModel;
import xstampp.astpa.model.interfaces.ISafetyConstraintViewDataModel;
import xstampp.astpa.model.sds.SafetyConstraint;
import xstampp.astpa.ui.ATableFilter;
import xstampp.astpa.ui.CommonTableView;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;

import java.util.UUID;

/**
 * @author Jarkko Heidenwag
 * 
 */
public class SafetyConstraintView extends CommonTableView<ISafetyConstraintViewDataModel> {

	/**
	 * @author Jarkko Heidenwag
	 * 
	 */
	public static final String ID = "astpa.steps.step1_5"; //$NON-NLS-1$

	/**
	 * @author Jarkko Heidenwag
	 * 
	 */
	public SafetyConstraintView() {

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

		this.createCommonTableView(parent, Messages.SafetyConstraints);

		this.getFilterTextField().addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent ke) {
				SafetyConstraintView.this.getFilter().setSearchText(
						SafetyConstraintView.this.getFilterTextField()
								.getText());
				SafetyConstraintView.this.refreshView();
			}
		});

		this.setFilter(new ATableFilter());
		this.getTableViewer().addFilter(this.getFilter());

		Listener addSafetyConstraintListener = new Listener() {

			@Override
			public void handleEvent(Event event) {
				if ((event.type == SWT.KeyDown) && (event.keyCode != 'n')) {
					return;
				}
				SafetyConstraintView.this.getFilter().setSearchText(""); //$NON-NLS-1$
				SafetyConstraintView.this.getFilterTextField().setText(""); //$NON-NLS-1$
				SafetyConstraintView.this.refreshView();
				SafetyConstraintView.this.getDataInterface().addSafetyConstraint(
						"", ""); //$NON-NLS-1$ //$NON-NLS-2$
				int newID = SafetyConstraintView.this.getDataInterface()
						.getAllSafetyConstraints().size() - 1;
				SafetyConstraintView.this.updateTable();
				SafetyConstraintView.this.refreshView();
				SafetyConstraintView.this.getTableViewer().setSelection(
						new StructuredSelection(SafetyConstraintView.this
								.getTableViewer().getElementAt(newID)), true);
				SafetyConstraintView.this
						.getTitleColumn()
						.getViewer()
						.editElement(
								SafetyConstraintView.this.getTableViewer()
										.getElementAt(newID), 1);
			}
		};

		this.getAddNewItemButton().addListener(SWT.Selection,
				addSafetyConstraintListener);

		this.getTableViewer().getTable()
				.addListener(SWT.KeyDown, addSafetyConstraintListener);

		// Listener for editing a title by pressing return
		Listener returnListener = new Listener() {

			@Override
			public void handleEvent(Event event) {
				if ((event.type == SWT.KeyDown)
						&& (event.keyCode == SWT.CR)
						&& (!SafetyConstraintView.this.getTableViewer()
								.getSelection().isEmpty())) {
					int indexFirstSelected = SafetyConstraintView.this
							.getTableViewer().getTable().getSelectionIndices()[0];
					SafetyConstraintView.this
							.getTitleColumn()
							.getViewer()
							.editElement(
									SafetyConstraintView.this.getTableViewer()
											.getElementAt(indexFirstSelected),
									1);
				}
			}
		};

		this.getTableViewer().getTable()
				.addListener(SWT.KeyDown, returnListener);

		// Listener for the Description
		this.getDescriptionWidget().addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
					Text text = (Text) e.widget;
					String description = text.getText();
					SafetyConstraintView.this.getDataInterface()
							.setSafetyConstraintDescription(getCurrentSelection(), description);
			}
		});
	
		final EditingSupport titleEditingSupport = new SCEditingSupport(
				SafetyConstraintView.this.getTableViewer());
		this.getTitleColumn().setEditingSupport(titleEditingSupport);

		// KeyListener for deleting safety constraints by selecting them and
		// pressing the delete key
		SafetyConstraintView.this.getTableViewer().getControl()
				.addKeyListener(new KeyAdapter() {

					@Override
					public void keyReleased(final KeyEvent e) {
						if ((e.keyCode == SWT.DEL)
								|| ((e.stateMask == SWT.COMMAND) && (e.keyCode == SWT.BS))) {
							IStructuredSelection selection = (IStructuredSelection) SafetyConstraintView.this
									.getTableViewer().getSelection();
							if (selection.isEmpty()) {
								return;
							}
							SafetyConstraintView.this.deleteItems();
						}
					}
				});

		// Adding a right click context menu and the option to delete an entry
		// this way
		MenuManager menuMgr = new MenuManager();
		Menu menu = menuMgr.createContextMenu(SafetyConstraintView.this
				.getTableViewer().getControl());
		menuMgr.addMenuListener(new IMenuListener() {

			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if (SafetyConstraintView.this.getTableViewer().getSelection()
						.isEmpty()) {
					return;
				}
				if (SafetyConstraintView.this.getTableViewer().getSelection() instanceof IStructuredSelection) {
					Action deleteSafetyConstraint = new Action(
							Messages.DeleteSafetyConstraints) {

						@Override
						public void run() {
							SafetyConstraintView.this.deleteItems();
						}
					};
					manager.add(deleteSafetyConstraint);
				}
			}
		});

		menuMgr.setRemoveAllWhenShown(true);
		SafetyConstraintView.this.getTableViewer().getControl().setMenu(menu);

		this.updateTable();

	}

	@Override
	protected void deleteEntry(ATableModel model) {
    resetCurrentSelection();
    this.getDataInterface().removeSafetyConstraint(model.getId());
	}
	private class SCEditingSupport extends AbstractEditingSupport {

		/**
		 * 
		 * @author Jarkko Heidenwag
		 * 
		 * @param viewer
		 *            the ColumnViewer
		 */
		public SCEditingSupport(ColumnViewer viewer) {
			super(viewer);
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return new TextCellEditor(SafetyConstraintView.this
					.getTableViewer().getTable());
		}

		@Override
		protected Object getValue(Object element) {
			return getValue(((SafetyConstraint) element).getTitle());
		}

		@Override
		protected void setValue(Object element, Object value) {
			((SafetyConstraint) element).setTitle(String.valueOf(value));
		}
	}

	/**
	 * @author Jarkko Heidenwag
	 * 
	 */
	@Override
	public void updateTable() {
		SafetyConstraintView.this.getTableViewer().setInput(
				this.getDataInterface().getAllSafetyConstraints());
	}

	@Override
	public String getId() {
		return SafetyConstraintView.ID;
	}

	@Override
	public String getTitle() {
		return Messages.SafetyConstraints;
	}


	/**
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 * @return the type of this view
	 */
	@Override
	public commonTableType getCommonTableType() {
		return commonTableType.SafetyConstraintsView;
	}

	@Override
	public void dispose() {
		this.getDataInterface().deleteObserver(this);
		super.dispose();
	}

	@Override
	protected void moveEntry(UUID id, boolean moveUp) {
		getDataInterface().moveEntry(moveUp, id, ObserverValue.SAFETY_CONSTRAINT);
	}
}
