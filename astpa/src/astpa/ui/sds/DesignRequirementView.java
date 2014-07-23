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
import astpa.export.BufferedCSVWriter;
import astpa.export.stepData.DesignRequirementsWizard;
import astpa.model.interfaces.IDataModel;
import astpa.model.interfaces.IDesignRequirementViewDataModel;
import astpa.model.sds.DesignRequirement;
import astpa.ui.acchaz.ATableFilter;
import astpa.ui.acchaz.CommonTableView;

/**
 * @author Jarkko Heidenwag
 * 
 */
public class DesignRequirementView extends CommonTableView {
	
	/**
	 * @author Jarkko Heidenwag
	 * 
	 */
	public static final String ID = "astpa.ui.acchaz.designrequirementviewpart"; //$NON-NLS-1$
	
	// the design requirement currently displayed in the text widget
	private DesignRequirement displayedDesignRequirement;
	
	private IDesignRequirementViewDataModel dataInterface;
	
	
	/**
	 * @author Jarkko Heidenwag
	 * 
	 */
	public DesignRequirementView() {
		
	}
	
	/**
	 * Create contents of the view part.
	 * 
	 * @author Jarkko Heidenwag
	 * @param parent The parent composite
	 */
	@Override
	public void createPartControl(Composite parent) {
		
		this.createCommonTableView(parent, Messages.DesignRequirements);
		
		this.getFilterTextField().addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent ke) {
				DesignRequirementView.this.getFilter().setSearchText(
					DesignRequirementView.this.getFilterTextField().getText());
				DesignRequirementView.this.refreshView();
			}
		});
		
		this.setFilter(new ATableFilter());
		this.getTableViewer().addFilter(this.getFilter());
		
		this.getAddNewItemButton().setImage(Activator.getImageDescriptor("/icons/buttons/commontables/add.png") //$NON-NLS-1$
			.createImage());
		this.getDeleteItemsButton().setImage(Activator.getImageDescriptor("/icons/buttons/commontables/remove.png") //$NON-NLS-1$
			.createImage());
		
		Listener addDesignRequirementListener = new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				if ((event.type == SWT.KeyDown) && (event.keyCode != 'n')) {
					return;
				}
				DesignRequirementView.this.getFilter().setSearchText(""); //$NON-NLS-1$
				DesignRequirementView.this.getFilterTextField().setText(""); //$NON-NLS-1$
				DesignRequirementView.this.refreshView();
				DesignRequirementView.this.dataInterface.addDesignRequirement("", Messages.DescriptionOfThisDesignReq); //$NON-NLS-1$
				int newID = DesignRequirementView.this.dataInterface.getAllDesignRequirements().size() - 1;
				DesignRequirementView.this.updateTable();
				DesignRequirementView.this.refreshView();
				DesignRequirementView.this.getTableViewer().setSelection(
					new StructuredSelection(DesignRequirementView.this.getTableViewer().getElementAt(newID)), true);
				DesignRequirementView.this.getTitleColumn().getViewer()
					.editElement(DesignRequirementView.this.getTableViewer().getElementAt(newID), 1);
			}
		};
		
		this.getAddNewItemButton().addListener(SWT.Selection, addDesignRequirementListener);
		
		this.getTableViewer().getTable().addListener(SWT.KeyDown, addDesignRequirementListener);
		
		// Listener for editing a title by pressing return
		Listener returnListener = new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				if ((event.type == SWT.KeyDown) && (event.keyCode == SWT.CR)
					&& (!DesignRequirementView.this.getTableViewer().getSelection().isEmpty())) {
					int indexFirstSelected =
						DesignRequirementView.this.getTableViewer().getTable().getSelectionIndices()[0];
					DesignRequirementView.this.getTitleColumn().getViewer()
						.editElement(DesignRequirementView.this.getTableViewer().getElementAt(indexFirstSelected), 1);
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
				if (description.compareTo(Messages.DescriptionOfThisDesignReq) == 0) {
					UUID id = DesignRequirementView.this.displayedDesignRequirement.getId();
					DesignRequirementView.this.dataInterface.setDesignRequirementDescription(id, ""); //$NON-NLS-1$
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
				if (DesignRequirementView.this.displayedDesignRequirement != null) {
					Text text = (Text) e.widget;
					String description = text.getText();
					UUID id = DesignRequirementView.this.displayedDesignRequirement.getId();
					DesignRequirementView.this.dataInterface.setDesignRequirementDescription(id, description);
				}
			}
		});
		
		// Listener for showing the description of the selected design
		// requirement
		DesignRequirementView.this.getTableViewer().addSelectionChangedListener(new DRSelectionChangedListener());
		
		this.getIdColumn().setLabelProvider(new ColumnLabelProvider() {
			
			@Override
			public String getText(Object element) {
				if (element instanceof DesignRequirement) {
					return Integer.toString(((DesignRequirement) element).getNumber());
				}
				return null;
			}
		});
		
		this.getTitleColumn().setLabelProvider(new ColumnLabelProvider() {
			
			@Override
			public String getText(Object element) {
				if (element instanceof DesignRequirement) {
					return ((DesignRequirement) element).getTitle();
				}
				return null;
			}
		});
		
		final EditingSupport titleEditingSupport = new DREditingSupport(DesignRequirementView.this.getTableViewer());
		this.getTitleColumn().setEditingSupport(titleEditingSupport);
		
		// KeyListener for deleting design requirements by selecting them and
		// pressing the delete key
		DesignRequirementView.this.getTableViewer().getControl().addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(final KeyEvent e) {
				if ((e.keyCode == SWT.DEL) || ((e.stateMask == SWT.COMMAND) && (e.keyCode == SWT.BS))) {
					IStructuredSelection selection =
						(IStructuredSelection) DesignRequirementView.this.getTableViewer().getSelection();
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
		Menu menu = menuMgr.createContextMenu(DesignRequirementView.this.getTableViewer().getControl());
		menuMgr.addMenuListener(new IMenuListener() {
			
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if (DesignRequirementView.this.getTableViewer().getSelection().isEmpty()) {
					return;
				}
				if (DesignRequirementView.this.getTableViewer().getSelection() instanceof IStructuredSelection) {
					Action deleteDesignRequirement = new Action(Messages.DeleteDesignRequirements) {
						
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
	
	/**
	 * deleting all selected design requirements
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 */
	@Override
	public void deleteItems() {
		final int maxNumOfDisplayedEntries = 10;
		IStructuredSelection selection =
			(IStructuredSelection) DesignRequirementView.this.getTableViewer().getSelection();
		if (selection.size() == 1) {
			// if only one design requirement is selected
			DesignRequirement deletedDesignRequirement = (DesignRequirement) selection.getFirstElement();
			String deletedDesignRequirementId = Integer.toString(deletedDesignRequirement.getNumber());
			String deletedDesignRequirementTitle = deletedDesignRequirement.getTitle();
			String confirmation = Messages.DoYouWishToDeleteTheDesignRequirement + deletedDesignRequirementId + ": " //$NON-NLS-1$
				+ deletedDesignRequirementTitle + "?"; //$NON-NLS-1$
			boolean b = MessageDialog.openQuestion(this.getTableContainer().getShell(), Messages.Confirm, confirmation);
			this.delOne(b, deletedDesignRequirement.getId());
		} else if ((selection.size() > 1) && (selection.size() <= maxNumOfDisplayedEntries)) {
			// if a few design requirements are selected
			String designRequirements = ""; //$NON-NLS-1$
			String newline = System.getProperty("line.separator"); //$NON-NLS-1$
			for (Iterator<DesignRequirement> i = selection.iterator(); i.hasNext();) {
				UUID id = i.next().getId();
				String title = this.dataInterface.getDesignRequirement(id).getTitle();
				int num = this.dataInterface.getDesignRequirement(id).getNumber();
				String designRequirement = newline + num + ": " + title; //$NON-NLS-1$
				designRequirements = designRequirements + designRequirement;
			}
			String confirmation =
				Messages.PleaseConfirmTheDeletionOfTheFollowingDesignRequirements + designRequirements;
			boolean b = MessageDialog.openQuestion(this.getTableContainer().getShell(), Messages.Confirm, confirmation);
			if (b) {
				this.getDescriptionWidget().setText(""); //$NON-NLS-1$
				DesignRequirementView.this.displayedDesignRequirement = null;
				for (Iterator<DesignRequirement> i = selection.iterator(); i.hasNext();) {
					this.dataInterface.removeDesignRequirement(i.next().getId());
				}
				DesignRequirementView.this.updateTable();
				this.refreshView();
			}
		} else if (selection.size() > maxNumOfDisplayedEntries) {
			// if many design requirements are selected
			boolean b =
				MessageDialog.openQuestion(this.getTableContainer().getShell(), Messages.Confirm,
					Messages.PleaseConfirmTheDeletionOfAllSelectedDesignRequirements);
			if (b) {
				this.getDescriptionWidget().setText(""); //$NON-NLS-1$
				DesignRequirementView.this.displayedDesignRequirement = null;
				for (Iterator<DesignRequirement> i = selection.iterator(); i.hasNext();) {
					this.dataInterface.removeDesignRequirement(i.next().getId());
				}
				DesignRequirementView.this.updateTable();
				this.refreshView();
			}
		} else {
			MessageDialog.openInformation(this.getTableContainer().getShell(), Messages.Information,
				Messages.NoDesignRequirementSelected);
		}
	}
	
	private void delOne(boolean b, UUID id) {
		if (b) {
			this.getDescriptionWidget().setText(""); //$NON-NLS-1$
			this.displayedDesignRequirement = null;
			this.dataInterface.removeDesignRequirement(id);
			DesignRequirementView.this.updateTable();
			this.refreshView();
		}
	}
	
	
	private class DREditingSupport extends EditingSupport {
		
		/**
		 * 
		 * @author Jarkko Heidenwag
		 * 
		 * @param viewer the ColumnViewer
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
			return new TextCellEditor(DesignRequirementView.this.getTableViewer().getTable());
		}
		
		@Override
		protected Object getValue(Object element) {
			if (element instanceof DesignRequirement) {
				// deleting the default title
				if ((((DesignRequirement) element).getTitle().compareTo(Messages.DoubleClickToEditTitle)) == 0) {
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
					((DesignRequirement) element).setTitle(Messages.DoubleClickToEditTitle);
				}
			}
			DesignRequirementView.this.refreshView();
		}
	}
	
	private class DRSelectionChangedListener implements ISelectionChangedListener {
		
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			// if the selection is empty clear the label
			if (event.getSelection().isEmpty()) {
				DesignRequirementView.this.displayedDesignRequirement = null;
				DesignRequirementView.this.getDescriptionWidget().setText(""); //$NON-NLS-1$
				DesignRequirementView.this.getDescriptionWidget().setEnabled(false);
				return;
			}
			if (event.getSelection() instanceof IStructuredSelection) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				if (selection.getFirstElement() instanceof DesignRequirement) {
					if (DesignRequirementView.this.displayedDesignRequirement == null) {
						DesignRequirementView.this.getDescriptionWidget().setEnabled(true);
					} else {
						DesignRequirementView.this.displayedDesignRequirement = null;
					}
					DesignRequirementView.this.getDescriptionWidget().setText(
						((DesignRequirement) selection.getFirstElement()).getDescription());
					DesignRequirementView.this.displayedDesignRequirement =
						(DesignRequirement) selection.getFirstElement();
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
		DesignRequirementView.this.getTableViewer().setInput(this.dataInterface.getAllDesignRequirements());
	}
	
	@Override
	public String getId() {
		return DesignRequirementView.ID;
	}
	
	@Override
	public String getTitle() {
		return Messages.DesignRequirements;
	}
	
	@Override
	public void setDataModelInterface(IDataModel dataInterface) {
		this.dataInterface = (IDesignRequirementViewDataModel) dataInterface;
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
		return commonTableType.DesignRequirementsView;
	}
	

	@Override
	public boolean writeCSVData(BufferedCSVWriter writer) throws IOException {
		this.exportAsCSV(this.dataInterface.getAllDesignRequirements(),writer);
		return true;
	}

	@Override
	public Class<?> getExportWizard() {
		return DesignRequirementsWizard.class;
	}
}
