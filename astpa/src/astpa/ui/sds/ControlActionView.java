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

import java.util.Iterator;
import java.util.UUID;

import messages.Messages;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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

import astpa.Activator;
import astpa.model.controlaction.ControlAction;
import astpa.model.interfaces.IControlActionViewDataModel;
import astpa.model.interfaces.IDataModel;
import astpa.ui.acchaz.ATableFilter;
import astpa.ui.acchaz.CommonTableView;

/**
 * @author Jarkko Heidenwag
 * 
 */
public class ControlActionView extends CommonTableView {
	
	/**
	 * @author Jarkko Heidenwag
	 * 
	 */
	public static final String ID = "astpa.ui.acchaz.controlactionviewpart"; //$NON-NLS-1$
	
	// the control action currently displayed in the text widget
	private ControlAction displayedControlAction;
	
	private IControlActionViewDataModel dataInterface;
	
	
	/**
	 * @author Jarkko Heidenwag
	 * 
	 */
	public ControlActionView() {
		
	}
	
	/**
	 * Create contents of the view part.
	 * 
	 * @author Jarkko Heidenwag
	 * @param parent The parent composite
	 */
	@Override
	public void createPartControl(Composite parent) {
		
		this.createCommonTableView(parent, Messages.ControlActions);
		
		this.getFilterTextField().addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent ke) {
				ControlActionView.this.getFilter().setSearchText(ControlActionView.this.getFilterTextField().getText());
				ControlActionView.this.refreshView();
			}
		});
		
		this.setFilter(new ATableFilter());
		this.getTableViewer().addFilter(this.getFilter());
		
		this.getAddNewItemButton().setImage(Activator.getImageDescriptor("/icons/buttons/commontables/add.png") //$NON-NLS-1$
			.createImage());
		this.getDeleteItemsButton().setImage(Activator.getImageDescriptor("/icons/buttons/commontables/remove.png") //$NON-NLS-1$
			.createImage());
		
		Listener addControlActionListener = new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				if ((event.type == SWT.KeyDown) && (event.keyCode != 'n')) {
					return;
				}
				ControlActionView.this.getFilter().setSearchText(""); //$NON-NLS-1$
				ControlActionView.this.getFilterTextField().setText(""); //$NON-NLS-1$
				ControlActionView.this.refreshView();
				ControlActionView.this.dataInterface.addControlAction("", Messages.DescriptionOfThisControlAction); //$NON-NLS-1$
				int newID = ControlActionView.this.dataInterface.getAllControlActions().size() - 1;
				ControlActionView.this.updateTable();
				ControlActionView.this.refreshView();
				ControlActionView.this.getTableViewer().setSelection(
					new StructuredSelection(ControlActionView.this.getTableViewer().getElementAt(newID)), true);
				ControlActionView.this.getTitleColumn().getViewer()
					.editElement(ControlActionView.this.getTableViewer().getElementAt(newID), 1);
			}
		};
		
		this.getAddNewItemButton().addListener(SWT.Selection, addControlActionListener);
		
		this.getTableViewer().getTable().addListener(SWT.KeyDown, addControlActionListener);
		
		// Listener for editing a title by pressing return
		Listener returnListener = new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				if ((event.type == SWT.KeyDown) && (event.keyCode == SWT.CR)
					&& (!ControlActionView.this.getTableViewer().getSelection().isEmpty())) {
					int indexFirstSelected =
						ControlActionView.this.getTableViewer().getTable().getSelectionIndices()[0];
					ControlActionView.this.getTitleColumn().getViewer()
						.editElement(ControlActionView.this.getTableViewer().getElementAt(indexFirstSelected), 1);
				}
			}
		};
		
		this.getTableViewer().getTable().addListener(SWT.KeyDown, returnListener);
		
		// check if the description is default and delete it in that case
		this.getDescriptionWidget().addFocusListener(new FocusListener() {
			
			@Override
			public void focusGained(FocusEvent e) {
				Text text = (Text) e.widget;
				String description = text.getText();
				if (description.compareTo(Messages.DescriptionOfThisControlAction) == 0) {
					UUID id = ControlActionView.this.displayedControlAction.getId();
					ControlActionView.this.dataInterface.setControlActionDescription(id, ""); //$NON-NLS-1$
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
				if (ControlActionView.this.displayedControlAction != null) {
					Text text = (Text) e.widget;
					String description = text.getText();
					UUID id = ControlActionView.this.displayedControlAction.getId();
					ControlActionView.this.dataInterface.setControlActionDescription(id, description);
				}
			}
		});
		
		// Listener for showing the description of the selected control action
		ControlActionView.this.getTableViewer().addSelectionChangedListener(new CASelectionChangedListener());
		
		this.getIdColumn().setLabelProvider(new ColumnLabelProvider() {
			
			@Override
			public String getText(Object element) {
				if (element instanceof ControlAction) {
					return Integer.toString(((ControlAction) element).getNumber());
				}
				return null;
			}
		});
		
		this.getTitleColumn().setLabelProvider(new ColumnLabelProvider() {
			
			@Override
			public String getText(Object element) {
				if (element instanceof ControlAction) {
					return ((ControlAction) element).getTitle();
				}
				return null;
			}
		});
		
		final EditingSupport titleEditingSupport = new CAEditingSupport(ControlActionView.this.getTableViewer());
		this.getTitleColumn().setEditingSupport(titleEditingSupport);
		
		// KeyListener for deleting control actions by selecting them and
		// pressing the delete key
		ControlActionView.this.getTableViewer().getControl().addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(final KeyEvent e) {
				if ((e.keyCode == SWT.DEL) || ((e.stateMask == SWT.COMMAND) && (e.keyCode == SWT.BS))) {
					IStructuredSelection selection =
						(IStructuredSelection) ControlActionView.this.getTableViewer().getSelection();
					if (selection.isEmpty()) {
						return;
					}
					ControlActionView.this.deleteItems();
				}
			}
		});
		
		// Adding a right click context menu and the option to delete an entry
		// this way
		MenuManager menuMgr = new MenuManager();
		Menu menu = menuMgr.createContextMenu(ControlActionView.this.getTableViewer().getControl());
		menuMgr.addMenuListener(new IMenuListener() {
			
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if (ControlActionView.this.getTableViewer().getSelection().isEmpty()) {
					return;
				}
				if (ControlActionView.this.getTableViewer().getSelection() instanceof IStructuredSelection) {
					Action deleteControlAction = new Action(Messages.DeleteControlActions) {
						
						@Override
						public void run() {
							ControlActionView.this.deleteItems();
						}
					};
					manager.add(deleteControlAction);
				}
			}
		});
		menuMgr.setRemoveAllWhenShown(true);
		ControlActionView.this.getTableViewer().getControl().setMenu(menu);
		this.updateTable();
	}
	
	/**
	 * deleting all selected control actions
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 */
	@Override
	public void deleteItems() {
		final int maxNumOfDisplayedEntries = 10;
		IStructuredSelection selection = (IStructuredSelection) ControlActionView.this.getTableViewer().getSelection();
		if (selection.size() == 1) {
			// if only one control action is selected
			ControlAction deletedControlAction = (ControlAction) selection.getFirstElement();
			String deletedControlActionId = Integer.toString(deletedControlAction.getNumber());
			String deletedControlActionTitle = deletedControlAction.getTitle();
			String confirmation = Messages.DoYouWishToDeleteTheControlAction + deletedControlActionId + ": " //$NON-NLS-1$
				+ deletedControlActionTitle + "?"; //$NON-NLS-1$
			boolean b = MessageDialog.openQuestion(this.getTableContainer().getShell(), Messages.Confirm, confirmation);
			this.delOne(b, deletedControlAction.getId());
			deletedControlAction = null;
		} else if ((selection.size() > 1) && (selection.size() <= maxNumOfDisplayedEntries)) {
			// if a few control actions are selected
			String controlActions = ""; //$NON-NLS-1$
			String newline = System.getProperty("line.separator"); //$NON-NLS-1$
			for (Iterator<ControlAction> i = selection.iterator(); i.hasNext();) {
				UUID id = i.next().getId();
				String title = this.dataInterface.getControlAction(id).getTitle();
				int num = this.dataInterface.getControlAction(id).getNumber();
				String controlAction = newline + num + ": " + title; //$NON-NLS-1$
				controlActions = controlActions + controlAction;
			}
			String confirmation = Messages.PleaseConfirmTheDeletionOfTheFollowingControlActions + controlActions;
			boolean b = MessageDialog.openQuestion(this.getTableContainer().getShell(), Messages.Confirm, confirmation);
			if (b) {
				this.getDescriptionWidget().setText(""); //$NON-NLS-1$
				ControlActionView.this.displayedControlAction = null;
				for (Iterator<ControlAction> i = selection.iterator(); i.hasNext();) {
					this.dataInterface.removeControlAction(i.next().getId());
				}
				ControlActionView.this.updateTable();
				this.refreshView();
			}
		} else if (selection.size() > maxNumOfDisplayedEntries) {
			// if many control actions are selected
			boolean b =
				MessageDialog.openQuestion(this.getTableContainer().getShell(), Messages.Confirm,
					Messages.PleaseConfirmTheDeletionOfAllSelectedControlActions);
			if (b) {
				this.getDescriptionWidget().setText(""); //$NON-NLS-1$
				ControlActionView.this.displayedControlAction = null;
				for (Iterator<ControlAction> i = selection.iterator(); i.hasNext();) {
					this.dataInterface.removeControlAction(i.next().getId());
				}
				ControlActionView.this.updateTable();
				this.refreshView();
			}
		} else {
			MessageDialog.openInformation(this.getTableContainer().getShell(), Messages.Information,
				Messages.NoControlActionSelected);
		}
	}
	
	private void delOne(boolean b, UUID id) {
		if (b) {
			this.getDescriptionWidget().setText(""); //$NON-NLS-1$
			ControlActionView.this.displayedControlAction = null;
			this.dataInterface.removeControlAction(id);
			ControlActionView.this.updateTable();
			this.refreshView();
		}
	}
	
	
	private class CAEditingSupport extends EditingSupport {
		
		/**
		 * 
		 * @author Jarkko Heidenwag
		 * 
		 * @param viewer the ColumnViewer
		 */
		public CAEditingSupport(ColumnViewer viewer) {
			super(viewer);
		}
		
		@Override
		protected boolean canEdit(Object element) {
			return true;
		}
		
		@Override
		protected CellEditor getCellEditor(Object element) {
			return new TextCellEditor(ControlActionView.this.getTableViewer().getTable());
		}
		
		@Override
		protected Object getValue(Object element) {
			if (element instanceof ControlAction) {
				// deleting the default title
				if ((((ControlAction) element).getTitle().compareTo(Messages.DoubleClickToEditTitle)) == 0) {
					((ControlAction) element).setTitle(""); //$NON-NLS-1$
				}
				return ((ControlAction) element).getTitle();
			}
			return null;
		}
		
		@Override
		protected void setValue(Object element, Object value) {
			if (element instanceof ControlAction) {
				((ControlAction) element).setTitle(String.valueOf(value));
				// Fill in the default title if the user left it blank
				if (((ControlAction) element).getTitle().length() == 0) {
					((ControlAction) element).setTitle(Messages.DoubleClickToEditTitle);
				}
			}
			ControlActionView.this.refreshView();
		}
	}
	
	private class CASelectionChangedListener implements ISelectionChangedListener {
		
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			// if the selection is empty clear the label
			if (event.getSelection().isEmpty()) {
				ControlActionView.this.displayedControlAction = null;
				ControlActionView.this.getDescriptionWidget().setText(""); //$NON-NLS-1$
				ControlActionView.this.getDescriptionWidget().setEnabled(false);
				return;
			}
			if (event.getSelection() instanceof IStructuredSelection) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				if (selection.getFirstElement() instanceof ControlAction) {
					if (ControlActionView.this.displayedControlAction == null) {
						ControlActionView.this.getDescriptionWidget().setEnabled(true);
					} else {
						ControlActionView.this.displayedControlAction = null;
					}
					ControlActionView.this.getDescriptionWidget().setText(
						((ControlAction) selection.getFirstElement()).getDescription());
					ControlActionView.this.displayedControlAction = (ControlAction) selection.getFirstElement();
				}
			}
		}
	}
	
	
	/**
	 * @author Jarkko Heidenwag
	 * 
	 */
	@Override
	public void updateTable() {
		ControlActionView.this.getTableViewer().setInput(this.dataInterface.getAllControlActions());
	}
	
	@Override
	public String getId() {
		return ControlActionView.ID;
	}
	
	@Override
	public String getTitle() {
		return Messages.ControlActions;
	}
	
	@Override
	public void setDataModelInterface(IDataModel dataInterface) {
		this.dataInterface = (IControlActionViewDataModel) dataInterface;
		this.dataInterface.addObserver(this);
	}
	
	/**
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 * @return the type of this view
	 */
	@Override
	public commonTableType getCommonTableType() {
		return commonTableType.ControlActionsView;
	}
	
	@Override
	public boolean triggerExport(String path) {
		this.exportAsCSV(this.dataInterface.getAllControlActions());
		return true;
	}
}
