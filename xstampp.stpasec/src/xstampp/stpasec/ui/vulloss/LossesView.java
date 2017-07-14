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

package xstampp.stpasec.ui.vulloss;

import java.util.List;
import java.util.UUID;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;

import messages.Messages;
import xstampp.astpa.model.hazacc.ATableModel;
import xstampp.astpa.model.interfaces.IAccidentViewDataModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.ui.ATableFilter;
import xstampp.astpa.ui.CommonTableView;
import xstampp.model.ObserverValue;
import xstampp.stpapriv.model.vulloss.Loss;
import xstampp.stpapriv.model.vulloss.Vulnerability;
import xstampp.stpasec.messages.SecMessages;
import xstampp.ui.common.ProjectManager;

/**
 * @author Jarkko Heidenwag
 * 
 */
public class LossesView extends CommonTableView<IAccidentViewDataModel> {

	/**
	 * @author Jarkko Heidenwag
	 * 
	 */
	public static final String ID = "stpasec.steps.step1_2"; //$NON-NLS-1$



	/**
	 * @author Jarkko Heidenwag
	 * 
	 */
	public LossesView() {
		 super();

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
		this.createCommonTableView(parent, SecMessages.Losses);

		this.getFilterTextField().addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent ke) {
				LossesView.this.getFilter().setSearchText(
						LossesView.this.getFilterTextField().getText());
				LossesView.this.refreshView();
			}
		});

		this.setFilter(new ATableFilter());
		this.getTableViewer().addFilter(this.getFilter());

		Listener addAccidentListener = new Listener() {

			@Override
			public void handleEvent(Event event) {
				if ((event.type == SWT.KeyDown) && (event.keyCode != 'n')) {
					return;
				}
				LossesView.this.getFilter().setSearchText(""); //$NON-NLS-1$
				LossesView.this.getFilterTextField().setText(""); //$NON-NLS-1$
				LossesView.this.refreshView();
				LossesView.this.getDataInterface().addAccident("",""); //$NON-NLS-1$ //$NON-NLS-2$
				int newID = LossesView.this.getDataInterface().getAllAccidents()
						.size() - 1;
				LossesView.this.updateTable();
				LossesView.this.refreshView();
				LossesView.this.getTableViewer().setSelection(
						new StructuredSelection(LossesView.this
								.getTableViewer().getElementAt(newID)), true);
				LossesView.this
						.getTitleColumn()
						.getViewer()
						.editElement(
								LossesView.this.getTableViewer()
										.getElementAt(newID), 1);
			}
		};

		this.getAddNewItemButton().addListener(SWT.Selection,
				addAccidentListener);

		this.getTableViewer().getTable()
				.addListener(SWT.KeyDown, addAccidentListener);

		// Listener for editing a title by pressing return
		Listener returnListener = new Listener() {

			@Override
			public void handleEvent(Event event) {
				if ((event.type == SWT.KeyDown)
						&& (event.keyCode == SWT.CR)
						&& (!LossesView.this.getTableViewer().getSelection()
								.isEmpty())) {
					int indexFirstSelected = LossesView.this
							.getTableViewer().getTable().getSelectionIndices()[0];
					LossesView.this
							.getTitleColumn()
							.getViewer()
							.editElement(
									LossesView.this.getTableViewer()
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
				UUID id = getCurrentSelection();
				LossesView.this.getDataInterface().setAccidentDescription(id,
							description);
				}
		});

		final AbstractEditingSupport titleEditingSupport = new AccEditingSupport(
				LossesView.this.getTableViewer());
		this.addTitleEditor(titleEditingSupport);

		TableViewerColumn linksColumn;
		linksColumn = new TableViewerColumn(
				LossesView.this.getTableViewer(), SWT.NONE);
		linksColumn.getColumn().setText(Messages.Links);
		linksColumn.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				String linkString = ""; //$NON-NLS-1$
				List<ITableModel> links = LossesView.this.getDataInterface()
						.getLinkedHazards(((Loss) element).getId());
				if (!(links == null)) {
					for (int i = 0; i < links.size(); i++) {
						linkString += ((Vulnerability)links.get(i)).getIdString();
						if (i < (links.size() - 1)) {
							linkString += ", "; //$NON-NLS-1$
						}
					}
				}
				return linkString;
			}
		});

		final int linksWeight = 10;
		final int linksMinWidth = 50;
		this.getTableColumnLayout().setColumnData(linksColumn.getColumn(),
				new ColumnWeightData(linksWeight, linksMinWidth, false));

		// KeyListener for deleting accidents by selecting them and pressing the
		// delete key
		LossesView.this.getTableViewer().getControl()
				.addKeyListener(new KeyAdapter() {

					@Override
					public void keyReleased(final KeyEvent e) {
						if ((e.keyCode == SWT.DEL)
								|| ((e.stateMask == SWT.COMMAND) && (e.keyCode == SWT.BS))) {
							IStructuredSelection selection = (IStructuredSelection) LossesView.this
									.getTableViewer().getSelection();
							if (selection.isEmpty()) {
								return;
							}
							LossesView.this.deleteItems();
						}
					}
				});

		// Adding a right click context menu and the option to delete an entry
		// this way
		MenuManager menuMgr = new MenuManager();
		Menu menu = menuMgr.createContextMenu(LossesView.this
				.getTableViewer().getControl());
		menuMgr.addMenuListener(new IMenuListener() {

			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if (LossesView.this.getTableViewer().getSelection()
						.isEmpty()) {
					return;
				}
				if (LossesView.this.getTableViewer().getSelection() instanceof IStructuredSelection) {
					Action deleteAccident = new Action(
							SecMessages.Delete_losses) {

						@Override
						public void run() {
							LossesView.this.deleteItems();
						}
					};
					manager.add(deleteAccident);
				}
			}
		});

		menuMgr.setRemoveAllWhenShown(true);
		LossesView.this.getTableViewer().getControl().setMenu(menu);

		this.updateTable();

	}


	@Override
	protected void deleteEntry(ATableModel model) {
	  resetCurrentSelection();
    this.getDataInterface().removeAccident(model.getId());
	}
	private class AccEditingSupport extends AbstractEditingSupport {

		/**
		 * EditingSupport for the title column
		 * 
		 * @author Jarkko Heidenwag
		 * 
		 * @param viewer
		 *            the ColumnViewer
		 */
		public AccEditingSupport(ColumnViewer viewer) {
			super(viewer);
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return new TextCellEditor(LossesView.this.getTableViewer()
					.getTable());
		}

		@Override
		protected Object getValue(Object element) {
			return getValue(((Loss) element).getTitle());
		}

		@Override
		protected void setValue(Object element, Object value) {
			LossesView.this.getDataInterface().setAccidentTitle(((Loss) element).getId(),String.valueOf(value));
		}
	}

	/**
	 * @author Jarkko Heidenwag
	 * 
	 */
	@Override
	public void updateTable() {
		LossesView.this.getTableViewer().setInput(
				this.getDataInterface().getAllAccidents());
	}

	@Override
	public String getId() {
		return LossesView.ID;
	}

	@Override
	public String getTitle() {
		return SecMessages.Losses;
	}

	@Override
	public void dispose() {
		this.getDataInterface().deleteObserver(this);
		super.dispose();
	}

	@Override
	protected void moveEntry(UUID id, boolean moveUp) {
		getDataInterface().moveEntry(false, moveUp, id, ObserverValue.ACCIDENT);
	}

}
