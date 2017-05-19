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

import java.util.UUID;

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
import xstampp.astpa.model.interfaces.IDesignRequirementViewDataModel;
import xstampp.astpa.model.sds.DesignRequirement;
import xstampp.astpa.ui.ATableFilter;
import xstampp.astpa.ui.CommonTableView;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;

/**
 * @author Jarkko Heidenwag
 * 
 */
public class DesignRequirementView extends CommonTableView<IDesignRequirementViewDataModel> {

	/**
	 * @author Jarkko Heidenwag
	 * 
	 */
	public static final String ID = "astpa.steps.step1_7"; //$NON-NLS-1$

	/**
	 * @author Jarkko Heidenwag
	 * 
	 */
	public DesignRequirementView() {
    super(true);
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

		this.createCommonTableView(parent, Messages.DesignRequirements);

		this.getFilterTextField().addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent ke) {
				DesignRequirementView.this.getFilter().setSearchText(
						DesignRequirementView.this.getFilterTextField()
								.getText());
				DesignRequirementView.this.refreshView();
			}
		});

		this.setFilter(new ATableFilter());
		this.getTableViewer().addFilter(this.getFilter());

		Listener addDesignRequirementListener = new Listener() {

			@Override
			public void handleEvent(Event event) {
				if ((event.type == SWT.KeyDown) && (event.keyCode != 'n')) {
					return;
				}
				DesignRequirementView.this.getFilter().setSearchText(""); //$NON-NLS-1$
				DesignRequirementView.this.getFilterTextField().setText(""); //$NON-NLS-1$
				DesignRequirementView.this.refreshView();
				DesignRequirementView.this.getDataInterface().addDesignRequirement(
						"", Messages.DescriptionOfThisDesignReq); //$NON-NLS-1$
				int newID = DesignRequirementView.this.getDataInterface()
						.getAllDesignRequirements().size() - 1;
				DesignRequirementView.this.updateTable();
				DesignRequirementView.this.refreshView();
				DesignRequirementView.this.getTableViewer().setSelection(
						new StructuredSelection(DesignRequirementView.this
								.getTableViewer().getElementAt(newID)), true);
				DesignRequirementView.this
						.getTitleColumn()
						.getViewer()
						.editElement(
								DesignRequirementView.this.getTableViewer()
										.getElementAt(newID), 1);
			}
		};

		this.getAddNewItemButton().addListener(SWT.Selection,
				addDesignRequirementListener);

		this.getTableViewer().getTable()
				.addListener(SWT.KeyDown, addDesignRequirementListener);

		// Listener for editing a title by pressing return
		Listener returnListener = new Listener() {

			@Override
			public void handleEvent(Event event) {
				if ((event.type == SWT.KeyDown)
						&& (event.keyCode == SWT.CR)
						&& (!DesignRequirementView.this.getTableViewer()
								.getSelection().isEmpty())) {
					int indexFirstSelected = DesignRequirementView.this
							.getTableViewer().getTable().getSelectionIndices()[0];
					DesignRequirementView.this
							.getTitleColumn()
							.getViewer()
							.editElement(
									DesignRequirementView.this.getTableViewer()
											.getElementAt(indexFirstSelected),
									1);
				}
			}
		};

		this.getTableViewer().getTable()
				.addListener(SWT.KeyDown, returnListener);

		// check if the description is default and delete it in that case
		this.getDescriptionWidget().addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				Text text = (Text) e.widget;
				String description = text.getText();
				if (description.compareTo(Messages.DescriptionOfThisDesignReq) == 0) {
					DesignRequirementView.this.getDataInterface()
							.setDesignRequirementDescription(getCurrentSelection(), ""); //$NON-NLS-1$
					text.setText(""); //$NON-NLS-1$
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				// intentionally empty

			}
		});

		// Listener for the Description
		this.getDescriptionWidget().addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
					Text text = (Text) e.widget;
					String description = text.getText();
					DesignRequirementView.this.getDataInterface()
							.setDesignRequirementDescription(getCurrentSelection(), description);
			}
		});

		final EditingSupport titleEditingSupport = new DREditingSupport(
				DesignRequirementView.this.getTableViewer());
		this.getTitleColumn().setEditingSupport(titleEditingSupport);

		// KeyListener for deleting design requirements by selecting them and
		// pressing the delete key
		DesignRequirementView.this.getTableViewer().getControl()
				.addKeyListener(new KeyAdapter() {

					@Override
					public void keyReleased(final KeyEvent e) {
						if ((e.keyCode == SWT.DEL)
								|| ((e.stateMask == SWT.COMMAND) && (e.keyCode == SWT.BS))) {
							IStructuredSelection selection = (IStructuredSelection) DesignRequirementView.this
									.getTableViewer().getSelection();
							if (selection.isEmpty()) {
								return;
							}
							DesignRequirementView.this.deleteItems();
						}
					}
				});

		// Adding a right click context menu and the option to delete an entry
		// this way
		MenuManager menuMgr = new MenuManager();
		Menu menu = menuMgr.createContextMenu(DesignRequirementView.this
				.getTableViewer().getControl());
		menuMgr.addMenuListener(new IMenuListener() {

			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if (DesignRequirementView.this.getTableViewer().getSelection()
						.isEmpty()) {
					return;
				}
				if (DesignRequirementView.this.getTableViewer().getSelection() instanceof IStructuredSelection) {
					Action deleteDesignRequirement = new Action(
							Messages.DeleteDesignRequirements) {

						@Override
						public void run() {
							DesignRequirementView.this.deleteItems();
						}
					};
					manager.add(deleteDesignRequirement);
				}
			}
		});

		menuMgr.setRemoveAllWhenShown(true);
		DesignRequirementView.this.getTableViewer().getControl().setMenu(menu);

		this.updateTable();

	}

	@Override
	protected void deleteEntry(ATableModel model) {
    resetCurrentSelection();
    this.getDataInterface().removeDesignRequirement(model.getId());
	}
	
	private class DREditingSupport extends EditingSupport {

		/**
		 * 
		 * @author Jarkko Heidenwag
		 * 
		 * @param viewer
		 *            the ColumnViewer
		 */
		public DREditingSupport(ColumnViewer viewer) {
			super(viewer);
		}

		@Override
		protected boolean canEdit(Object element) {
			return true;
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return new TextCellEditor(DesignRequirementView.this
					.getTableViewer().getTable());
		}

		@Override
		protected Object getValue(Object element) {
			if (element instanceof DesignRequirement) {
				// deleting the default title
				if ((((DesignRequirement) element).getTitle()
						.compareTo(Messages.DoubleClickToEditTitle)) == 0) {
					((DesignRequirement) element).setTitle(""); //$NON-NLS-1$
				}
				return ((DesignRequirement) element).getTitle();
			}
			return null;
		}

		@Override
		protected void setValue(Object element, Object value) {
			if (element instanceof DesignRequirement) {
				((DesignRequirement) element).setTitle(String.valueOf(value));
				// Fill in the default title if the user left it blank
				if (((DesignRequirement) element).getTitle().length() == 0) {
					((DesignRequirement) element)
							.setTitle(Messages.DoubleClickToEditTitle);
				}
			}
			DesignRequirementView.this.refreshView();
		}
	}

	/**
	 * @author Jarkko Heidenwag
	 * 
	 */
	@Override
	public void updateTable() {
		DesignRequirementView.this.getTableViewer().setInput(
				this.getDataInterface().getAllDesignRequirements());
	}

	@Override
	public String getId() {
		return DesignRequirementView.ID;
	}

	@Override
	public String getTitle() {
		return Messages.DesignRequirements;
	}

	/**
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 * @return the type of this view
	 */
	@Override
	public commonTableType getCommonTableType() {
		return commonTableType.DesignRequirementsView;
	}

	@Override
	public void dispose() {
		this.getDataInterface().deleteObserver(this);
		super.dispose();
	}

	@Override
	protected void moveEntry(UUID id, boolean moveUp) {
		getDataInterface().moveEntry(moveUp, id, ObserverValue.DESIGN_REQUIREMENT);
	}
}
