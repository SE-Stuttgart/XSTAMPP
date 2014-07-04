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

import java.io.BufferedWriter;
import java.io.IOException;
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
import astpa.model.interfaces.IDataModel;
import astpa.model.interfaces.ISafetyConstraintViewDataModel;
import astpa.model.sds.SafetyConstraint;
import astpa.ui.acchaz.ATableFilter;
import astpa.ui.acchaz.CommonTableView;

/**
 * @author Jarkko Heidenwag
 * 
 */
public class SafetyConstraintView extends CommonTableView {
	
	/**
	 * @author Jarkko Heidenwag
	 * 
	 */
	public static final String ID = "astpa.ui.acchaz.safetyconstraintviewpart"; //$NON-NLS-1$
	
	// the safety constraint currently displayed in the text widget
	private SafetyConstraint displayedSafetyConstraint;
	
	private ISafetyConstraintViewDataModel dataInterface;
	
	
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
	 * @param parent The parent composite
	 */
	@Override
	public void createPartControl(Composite parent) {
		
		this.createCommonTableView(parent, Messages.SafetyConstraints);
		
		this.getFilterTextField().addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent ke) {
				SafetyConstraintView.this.getFilter().setSearchText(
					SafetyConstraintView.this.getFilterTextField().getText());
				SafetyConstraintView.this.refreshView();
			}
		});
		
		this.setFilter(new ATableFilter());
		this.getTableViewer().addFilter(this.getFilter());
		
		this.getAddNewItemButton().setImage(Activator.getImageDescriptor("/icons/buttons/commontables/add.png") //$NON-NLS-1$
			.createImage());
		this.getDeleteItemsButton().setImage(Activator.getImageDescriptor("/icons/buttons/commontables/remove.png") //$NON-NLS-1$
			.createImage());
		
		Listener addSafetyConstraintListener = new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				if ((event.type == SWT.KeyDown) && (event.keyCode != 'n')) {
					return;
				}
				SafetyConstraintView.this.getFilter().setSearchText(""); //$NON-NLS-1$
				SafetyConstraintView.this.getFilterTextField().setText(""); //$NON-NLS-1$
				SafetyConstraintView.this.refreshView();
				SafetyConstraintView.this.dataInterface.addSafetyConstraint("", Messages.DescriptionOfThisSafetyConstr); //$NON-NLS-1$
				int newID = SafetyConstraintView.this.dataInterface.getAllSafetyConstraints().size() - 1;
				SafetyConstraintView.this.updateTable();
				SafetyConstraintView.this.refreshView();
				SafetyConstraintView.this.getTableViewer().setSelection(
					new StructuredSelection(SafetyConstraintView.this.getTableViewer().getElementAt(newID)), true);
				SafetyConstraintView.this.getTitleColumn().getViewer()
					.editElement(SafetyConstraintView.this.getTableViewer().getElementAt(newID), 1);
			}
		};
		
		this.getAddNewItemButton().addListener(SWT.Selection, addSafetyConstraintListener);
		
		this.getTableViewer().getTable().addListener(SWT.KeyDown, addSafetyConstraintListener);
		
		// Listener for editing a title by pressing return
		Listener returnListener = new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				if ((event.type == SWT.KeyDown) && (event.keyCode == SWT.CR)
					&& (!SafetyConstraintView.this.getTableViewer().getSelection().isEmpty())) {
					int indexFirstSelected =
						SafetyConstraintView.this.getTableViewer().getTable().getSelectionIndices()[0];
					SafetyConstraintView.this.getTitleColumn().getViewer()
						.editElement(SafetyConstraintView.this.getTableViewer().getElementAt(indexFirstSelected), 1);
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
				if (description.compareTo(Messages.DescriptionOfThisSafetyConstr) == 0) {
					UUID id = SafetyConstraintView.this.displayedSafetyConstraint.getId();
					SafetyConstraintView.this.dataInterface.setSafetyConstraintDescription(id, ""); //$NON-NLS-1$
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
				if (SafetyConstraintView.this.displayedSafetyConstraint != null) {
					Text text = (Text) e.widget;
					String description = text.getText();
					UUID id = SafetyConstraintView.this.displayedSafetyConstraint.getId();
					SafetyConstraintView.this.dataInterface.setSafetyConstraintDescription(id, description);
				}
			}
		});
		
		// Listener for showing the description of the selected safety
		// constraint
		SafetyConstraintView.this.getTableViewer().addSelectionChangedListener(new SCSelectionChangedListener());
		
		this.getIdColumn().setLabelProvider(new ColumnLabelProvider() {
			
			@Override
			public String getText(Object element) {
				if (element instanceof SafetyConstraint) {
					return Integer.toString(((SafetyConstraint) element).getNumber());
				}
				return null;
			}
		});
		
		this.getTitleColumn().setLabelProvider(new ColumnLabelProvider() {
			
			@Override
			public String getText(Object element) {
				if (element instanceof SafetyConstraint) {
					return ((SafetyConstraint) element).getTitle();
				}
				return null;
			}
		});
		
		final EditingSupport titleEditingSupport = new SCEditingSupport(SafetyConstraintView.this.getTableViewer());
		this.getTitleColumn().setEditingSupport(titleEditingSupport);
		
		// KeyListener for deleting safety constraints by selecting them and
		// pressing the delete key
		SafetyConstraintView.this.getTableViewer().getControl().addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(final KeyEvent e) {
				if ((e.keyCode == SWT.DEL) || ((e.stateMask == SWT.COMMAND) && (e.keyCode == SWT.BS))) {
					IStructuredSelection selection =
						(IStructuredSelection) SafetyConstraintView.this.getTableViewer().getSelection();
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
		Menu menu = menuMgr.createContextMenu(SafetyConstraintView.this.getTableViewer().getControl());
		menuMgr.addMenuListener(new IMenuListener() {
			
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if (SafetyConstraintView.this.getTableViewer().getSelection().isEmpty()) {
					return;
				}
				if (SafetyConstraintView.this.getTableViewer().getSelection() instanceof IStructuredSelection) {
					Action deleteSafetyConstraint = new Action(Messages.DeleteSafetyConstraints) {
						
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
	
	/**
	 * deleting all selected safety constraints
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 */
	@Override
	public void deleteItems() {
		final int maxNumOfDisplayedEntries = 10;
		IStructuredSelection selection =
			(IStructuredSelection) SafetyConstraintView.this.getTableViewer().getSelection();
		if (selection.size() == 1) {
			// if only one safety constraint is selected
			SafetyConstraint deletedSafetyConstraint = (SafetyConstraint) selection.getFirstElement();
			String deletedSafetyConstraintId = Integer.toString(deletedSafetyConstraint.getNumber());
			String deletedSafetyConstraintTitle = deletedSafetyConstraint.getTitle();
			String confirmation = Messages.DoYouWishToDeleteTheSafetyConstraint + deletedSafetyConstraintId + ": " //$NON-NLS-1$
				+ deletedSafetyConstraintTitle + "?"; //$NON-NLS-1$
			boolean b = MessageDialog.openQuestion(this.getTableContainer().getShell(), Messages.Confirm, confirmation);
			this.delOne(b, deletedSafetyConstraint.getId());
		} else if ((selection.size() > 1) && (selection.size() <= maxNumOfDisplayedEntries)) {
			// if a few safety constraints are selected
			String safetyConstraints = ""; //$NON-NLS-1$
			String newline = System.getProperty("line.separator"); //$NON-NLS-1$
			for (Iterator<SafetyConstraint> i = selection.iterator(); i.hasNext();) {
				UUID id = i.next().getId();
				String title = this.dataInterface.getSafetyConstraint(id).getTitle();
				int num = this.dataInterface.getSafetyConstraint(id).getNumber();
				String safetyConstraint = newline + num + ": " + title; //$NON-NLS-1$
				safetyConstraints = safetyConstraints + safetyConstraint;
			}
			String confirmation = Messages.PleaseConfirmTheDeletionOfTheFollowingSafetyConstraints + safetyConstraints;
			boolean b = MessageDialog.openQuestion(this.getTableContainer().getShell(), Messages.Confirm, confirmation);
			if (b) {
				this.getDescriptionWidget().setText(""); //$NON-NLS-1$
				SafetyConstraintView.this.displayedSafetyConstraint = null;
				for (Iterator<SafetyConstraint> i = selection.iterator(); i.hasNext();) {
					this.dataInterface.removeSafetyConstraint(i.next().getId());
				}
				SafetyConstraintView.this.updateTable();
				this.refreshView();
			}
		} else if (selection.size() > maxNumOfDisplayedEntries) {
			// if many safety constraints are selected
			boolean b =
				MessageDialog.openQuestion(this.getTableContainer().getShell(), Messages.Confirm,
					Messages.PleaseConfirmTheDeletionOfAllSelectedSafetyConstraints);
			if (b) {
				this.getDescriptionWidget().setText(""); //$NON-NLS-1$
				SafetyConstraintView.this.displayedSafetyConstraint = null;
				for (Iterator<SafetyConstraint> i = selection.iterator(); i.hasNext();) {
					this.dataInterface.removeSafetyConstraint(i.next().getId());
				}
				SafetyConstraintView.this.updateTable();
				this.refreshView();
			}
		} else {
			MessageDialog.openInformation(this.getTableContainer().getShell(), Messages.Information,
				Messages.NoSafetyConstraintSelected);
		}
	}
	
	private void delOne(boolean b, UUID id) {
		if (b) {
			this.getDescriptionWidget().setText(""); //$NON-NLS-1$
			SafetyConstraintView.this.displayedSafetyConstraint = null;
			this.dataInterface.removeSafetyConstraint(id);
			SafetyConstraintView.this.updateTable();
			this.refreshView();
		}
	}
	
	
	private class SCEditingSupport extends EditingSupport {
		
		/**
		 * 
		 * @author Jarkko Heidenwag
		 * 
		 * @param viewer the ColumnViewer
		 */
		public SCEditingSupport(ColumnViewer viewer) {
			super(viewer);
		}
		
		@Override
		protected boolean canEdit(Object element) {
			return true;
		}
		
		@Override
		protected CellEditor getCellEditor(Object element) {
			return new TextCellEditor(SafetyConstraintView.this.getTableViewer().getTable());
		}
		
		@Override
		protected Object getValue(Object element) {
			if (element instanceof SafetyConstraint) {
				// deleting the default title
				if ((((SafetyConstraint) element).getTitle().compareTo(Messages.DoubleClickToEditTitle)) == 0) {
					((SafetyConstraint) element).setTitle(""); //$NON-NLS-1$
				}
				return ((SafetyConstraint) element).getTitle();
			}
			return null;
		}
		
		@Override
		protected void setValue(Object element, Object value) {
			if (element instanceof SafetyConstraint) {
				((SafetyConstraint) element).setTitle(String.valueOf(value));
				// Fill in the default title if the user left it blank
				if (((SafetyConstraint) element).getTitle().length() == 0) {
					((SafetyConstraint) element).setTitle(Messages.DoubleClickToEditTitle);
				}
			}
			SafetyConstraintView.this.refreshView();
		}
	}
	
	private class SCSelectionChangedListener implements ISelectionChangedListener {
		
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			// if the selection is empty clear the label
			if (event.getSelection().isEmpty()) {
				SafetyConstraintView.this.displayedSafetyConstraint = null;
				SafetyConstraintView.this.getDescriptionWidget().setText(""); //$NON-NLS-1$
				SafetyConstraintView.this.getDescriptionWidget().setEnabled(false);
				return;
			}
			if (event.getSelection() instanceof IStructuredSelection) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				if (selection.getFirstElement() instanceof SafetyConstraint) {
					if (SafetyConstraintView.this.displayedSafetyConstraint == null) {
						SafetyConstraintView.this.getDescriptionWidget().setEnabled(true);
					} else {
						SafetyConstraintView.this.displayedSafetyConstraint = null;
					}
					SafetyConstraintView.this.getDescriptionWidget().setText(
						((SafetyConstraint) selection.getFirstElement()).getDescription());
					SafetyConstraintView.this.displayedSafetyConstraint =
						(SafetyConstraint) selection.getFirstElement();
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
		SafetyConstraintView.this.getTableViewer().setInput(this.dataInterface.getAllSafetyConstraints());
	}
	
	@Override
	public String getId() {
		return SafetyConstraintView.ID;
	}
	
	@Override
	public String getTitle() {
		return Messages.SafetyConstraints;
	}
	
	@Override
	public void setDataModelInterface(IDataModel dataInterface) {
		this.dataInterface = (ISafetyConstraintViewDataModel) dataInterface;
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
		return commonTableType.SafetyConstraintsView;
	}
	
	@Override
	public boolean writeCSVData(BufferedWriter writer, char seperator) throws IOException {
		this.exportAsCSV(this.dataInterface.getAllSafetyConstraints(),writer, seperator);
		return true;
	}
}
