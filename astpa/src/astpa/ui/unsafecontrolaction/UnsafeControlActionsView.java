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

package astpa.ui.unsafecontrolaction;

import java.util.List;
import java.util.Observable;
import java.util.UUID;

import messages.Messages;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;

import astpa.model.ObserverValue;
import astpa.model.controlaction.UnsafeControlActionType;
import astpa.model.controlaction.interfaces.IControlAction;
import astpa.model.controlaction.interfaces.IUnsafeControlAction;
import astpa.model.interfaces.IDataModel;
import astpa.model.interfaces.IUnsafeControlActionDataModel;
import astpa.ui.common.IViewBase;
import astpa.ui.common.grid.GridCellButton;
import astpa.ui.common.grid.GridCellColored;
import astpa.ui.common.grid.GridCellEditor;
import astpa.ui.common.grid.GridCellLinking;
import astpa.ui.common.grid.GridCellRenderer;
import astpa.ui.common.grid.GridCellText;
import astpa.ui.common.grid.GridRow;
import astpa.ui.common.grid.GridWrapper;
import astpa.ui.common.grid.GridWrapper.NebulaGridRowWrapper;
import astpa.ui.common.grid.IGridCell;

/**
 * View used to handle the unsafe control actions.
 * 
 * @author Benedikt Markt, Patrick Wickenhaeuser
 */
public class UnsafeControlActionsView implements IViewBase {
	
	/**
	 * ViewPart ID.
	 */
	public static final String ID = "UnsafeControlActionsView"; //$NON-NLS-1$
	
	/**
	 * The log4j logger.
	 */
	private static final Logger LOGGER = Logger.getRootLogger();
	
	private static final String HAZARD_ID_PREFIX = "H-"; //$NON-NLS-1$
	private static final String CONFIRMATION_TITLE = Messages.DeleteUnsafeControlAction;
	private static final String CONFIRMATION_DESCRIPTION = Messages.WantToDeleteTheUCA;
	
	private static final RGB PARENT_BACKGROUND_COLOR = new RGB(215, 240, 255);
	private static final Color TEXT_COLOR = new Color(Display.getCurrent(), 0, 0, 0);
	
	/**
	 * Interfaces to communicate with the data model.
	 */
	private UcaContentProvider ucaContentProvider = null;
	
	private IUnsafeControlActionDataModel ucaInterface;
	
	private GridWrapper grid;
	
	
	private class UnsafeControlActionCell extends GridCellEditor {
		
		private IUnsafeControlAction unsafeControlAction;
		private Label deleteButton = null;
		
		
		public UnsafeControlActionCell(GridWrapper gridWrapper, String initialText,
			IUnsafeControlAction unsafeControlAction) {
			super(gridWrapper, initialText);
			this.unsafeControlAction = unsafeControlAction;
			
			Composite area = this.getCompositeArea();
			area.setLayout(new GridLayout(2, false));
			
			this.deleteButton = new Label(area, SWT.PUSH);
			this.deleteButton.setImage(GridWrapper.getDeleteButton16());
			this.deleteButton.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseDoubleClick(MouseEvent e) {
					// intentionally empty
				}
				
				@Override
				public void mouseDown(MouseEvent e) {
					UnsafeControlActionsView.LOGGER.debug("Delete unsafe control action"); //$NON-NLS-1$
					
					if (MessageDialog.openConfirm(UnsafeControlActionsView.this.grid.getGrid().getShell(),
						UnsafeControlActionsView.CONFIRMATION_TITLE, UnsafeControlActionsView.CONFIRMATION_DESCRIPTION)) {
						UnsafeControlActionsView.this.ucaInterface
							.removeUnsafeControlAction(UnsafeControlActionCell.this.unsafeControlAction.getId());
					}
				}
				
				@Override
				public void mouseUp(MouseEvent e) {
					// intentionally empty
				}
			});
			
			GridData gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.verticalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			gridData.grabExcessVerticalSpace = true;
			gridData.horizontalSpan = 1;
			this.getTextEditor().setLayoutData(gridData);
			
		}
		
		@Override
		public void onTextChanged(String newText) {
			UnsafeControlActionsView.this.ucaInterface.setUcaDescription(this.unsafeControlAction.getId(), newText);
		}
		
		@Override
		public void onEditorFocus() {
			if (this.getTextEditor().getText().equals(GridCellEditor.EMPTY_CELL_TEXT)) {
				this.getTextEditor().setText(""); //$NON-NLS-1$
			}
		}
		
		@Override
		public void paint(GridCellRenderer renderer, GC gc, NebulaGridRowWrapper item) {
			gc.setForeground(TEXT_COLOR);
			this.getCompositeArea().setBackground(this.getBackgroundColor(renderer, gc));
			this.getTextEditor().setBackground(this.getBackgroundColor(renderer, gc));
			this.deleteButton.setBackground(this.getBackgroundColor(renderer, gc));
			
			super.paint(renderer, gc, item);
		}
		
		@Override
		public UUID getUUID() {
			return this.unsafeControlAction.getId();
		}
		
		public IUnsafeControlAction getUnsafeControlAction() {
			return this.unsafeControlAction;
		}
		
	}
	
	private class AddUcaButton extends GridCellButton {
		
		private IControlAction parentControlAction;
		private UnsafeControlActionType ucaType;
		
		
		public AddUcaButton(IControlAction parentItem, String text, UnsafeControlActionType type) {
			super(text);
			
			this.parentControlAction = parentItem;
			this.ucaType = type;
		}
		
		@Override
		public void onMouseDown(MouseEvent e, org.eclipse.swt.graphics.Point relativeMouse, Rectangle cellBounds) {
			UUID newUCA =
				UnsafeControlActionsView.this.ucaInterface.addUnsafeControlAction(this.parentControlAction.getId(),
					"", this.ucaType); //$NON-NLS-1$
			UnsafeControlActionsView.this.grid.activateCell(newUCA);
			UnsafeControlActionsView.LOGGER.debug(Messages.AddingNewUCA);
		}
	}
	
	
	/**
	 * User interface components.
	 * 
	 * @author Benedikt Markt, Patrick Wickenhaeuser
	 */
	@Override
	public void createPartControl(Composite parent) {
		UnsafeControlActionsView.LOGGER.info("createPartControl()"); //$NON-NLS-1$
		
		this.grid =
			new GridWrapper(parent, new String[] {Messages.ControlAction, Messages.NotGiven, Messages.GivenIncorrectly,
				Messages.WrongTiming, Messages.StoppedTooSoon});
		this.grid.getGrid().setVisible(true);
		this.reloadTable();
		
		MenuManager menuMgr = new MenuManager();
		Menu menu = menuMgr.createContextMenu(this.grid.getGrid());
		menuMgr.addMenuListener(new IMenuListener() {
			
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if (UnsafeControlActionsView.this.grid.getGrid().getCellSelectionCount() > 0) {
					
					Action deleteAccident = new Action(Messages.DeleteUnsafeControlActions) {
						
						@Override
						public void run() {
							UnsafeControlActionsView.this.deleteSelectedUcas();
						}
					};
					manager.add(deleteAccident);
				}
			}
		});
		menuMgr.setRemoveAllWhenShown(true);
		this.grid.getGrid().setMenu(menu);
		
		this.grid.getGrid().addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				// Intentionally empty
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if ((e.keyCode == SWT.DEL) || ((e.stateMask == SWT.COMMAND) && (e.keyCode == SWT.BS))) {
					UnsafeControlActionsView.this.deleteSelectedUcas();
				}
			}
		});
	}
	
	private void deleteSelectedUcas() {
		if (MessageDialog.openConfirm(this.grid.getGrid().getShell(), UnsafeControlActionsView.CONFIRMATION_TITLE,
			UnsafeControlActionsView.CONFIRMATION_DESCRIPTION)) {
			List<IGridCell> selected = UnsafeControlActionsView.this.grid.getSelectedCellList();
			
			for (int i = 0; i < selected.size(); i++) {
				IGridCell cell = selected.get(i);
				
				if (cell instanceof UnsafeControlActionCell) {
					UnsafeControlActionsView.this.ucaInterface
						.removeUnsafeControlAction(((UnsafeControlActionCell) cell).getUnsafeControlAction().getId());
				}
			}
		}
	}
	
	private int getMaxHeight(IControlAction controlAction) {
		int maxHeight = controlAction.getUnsafeControlActions(UnsafeControlActionType.NOT_GIVEN).size();
		
		if (controlAction.getUnsafeControlActions(UnsafeControlActionType.GIVEN_INCORRECTLY).size() > maxHeight) {
			maxHeight = controlAction.getUnsafeControlActions(UnsafeControlActionType.GIVEN_INCORRECTLY).size();
		}
		if (controlAction.getUnsafeControlActions(UnsafeControlActionType.WRONG_TIMING).size() > maxHeight) {
			maxHeight = controlAction.getUnsafeControlActions(UnsafeControlActionType.WRONG_TIMING).size();
		}
		if (controlAction.getUnsafeControlActions(UnsafeControlActionType.STOPPED_TOO_SOON).size() > maxHeight) {
			maxHeight = controlAction.getUnsafeControlActions(UnsafeControlActionType.STOPPED_TOO_SOON).size();
		}
		
		return maxHeight;
	}
	
	private void fillTable(List<IControlAction> controlActions) {
		
		if (controlActions.isEmpty()) {
			return;
		}
		
		for (IControlAction cAction : controlActions) {
			GridRow controlActionRow = new GridRow();
			this.grid.addRow(controlActionRow);
			
			GridCellText descriptionItem = new GridCellText(cAction.getTitle());
			controlActionRow.addCell(descriptionItem);
			
			controlActionRow.addCell(new GridCellColored(this.grid.getGrid().getDisplay(),
				UnsafeControlActionsView.PARENT_BACKGROUND_COLOR));
			controlActionRow.addCell(new GridCellColored(this.grid.getGrid().getDisplay(),
				UnsafeControlActionsView.PARENT_BACKGROUND_COLOR));
			controlActionRow.addCell(new GridCellColored(this.grid.getGrid().getDisplay(),
				UnsafeControlActionsView.PARENT_BACKGROUND_COLOR));
			controlActionRow.addCell(new GridCellColored(this.grid.getGrid().getDisplay(),
				UnsafeControlActionsView.PARENT_BACKGROUND_COLOR));
			
			List<IUnsafeControlAction> allNotGiven = cAction.getUnsafeControlActions(UnsafeControlActionType.NOT_GIVEN);
			List<IUnsafeControlAction> allIncorrect =
				cAction.getUnsafeControlActions(UnsafeControlActionType.GIVEN_INCORRECTLY);
			List<IUnsafeControlAction> allWrongTiming =
				cAction.getUnsafeControlActions(UnsafeControlActionType.WRONG_TIMING);
			List<IUnsafeControlAction> allTooSoon =
				cAction.getUnsafeControlActions(UnsafeControlActionType.STOPPED_TOO_SOON);
			
			for (int i = 0; i <= this.getMaxHeight(cAction); i++) {
				GridRow ucaRow = new GridRow();
				GridRow linkRow = new GridRow();
				controlActionRow.addChildRow(ucaRow);
				controlActionRow.addChildRow(linkRow);
				
				IUnsafeControlAction notGivenUca = null;
				IUnsafeControlAction incorrectUca = null;
				IUnsafeControlAction timingUca = null;
				IUnsafeControlAction tooSoonUca = null;
				
				// set descriptions
				if (allNotGiven.size() > i) {
					notGivenUca = allNotGiven.get(i);
					
					ucaRow.addCell(new UnsafeControlActionCell(this.grid, notGivenUca.getDescription(), notGivenUca));
					linkRow.addCell(new GridCellLinking<UcaContentProvider>(notGivenUca.getId(),
						this.ucaContentProvider, this.grid, UnsafeControlActionsView.HAZARD_ID_PREFIX));
				} else if (allNotGiven.size() == i) {
					// add placeholder
					ucaRow.addCell(new GridCellText("")); //$NON-NLS-1$
					linkRow.addCell(new AddUcaButton(cAction, Messages.AddNotGivenUCA,
						UnsafeControlActionType.NOT_GIVEN));
				} else {
					// add placeholders
					ucaRow.addCell(new GridCellText("")); //$NON-NLS-1$
					linkRow.addCell(new GridCellText("")); //$NON-NLS-1$
				}
				
				if (allIncorrect.size() > i) {
					incorrectUca = allIncorrect.get(i);
					
					ucaRow.addCell(new UnsafeControlActionCell(this.grid, incorrectUca.getDescription(), incorrectUca));
					linkRow.addCell(new GridCellLinking<UcaContentProvider>(incorrectUca.getId(),
						this.ucaContentProvider, this.grid, UnsafeControlActionsView.HAZARD_ID_PREFIX));
				} else if (allIncorrect.size() == i) {
					// add placeholder
					ucaRow.addCell(new GridCellText("")); //$NON-NLS-1$
					linkRow.addCell(new AddUcaButton(cAction, Messages.AddGivenIncorrectlyUCA,
						UnsafeControlActionType.GIVEN_INCORRECTLY));
				} else {
					// add placeholders
					ucaRow.addCell(new GridCellText("")); //$NON-NLS-1$
					linkRow.addCell(new GridCellText("")); //$NON-NLS-1$
				}
				
				if (allWrongTiming.size() > i) {
					timingUca = allWrongTiming.get(i);
					
					ucaRow.addCell(new UnsafeControlActionCell(this.grid, timingUca.getDescription(), timingUca));
					linkRow.addCell(new GridCellLinking<UcaContentProvider>(timingUca.getId(), this.ucaContentProvider,
						this.grid, UnsafeControlActionsView.HAZARD_ID_PREFIX));
				} else if (allWrongTiming.size() == i) {
					// add placeholder
					ucaRow.addCell(new GridCellText("")); //$NON-NLS-1$
					linkRow.addCell(new AddUcaButton(cAction, Messages.AddWrongTimingUCA,
						UnsafeControlActionType.WRONG_TIMING));
				} else {
					// add placeholders
					ucaRow.addCell(new GridCellText("")); //$NON-NLS-1$
					linkRow.addCell(new GridCellText("")); //$NON-NLS-1$
				}
				
				if (allTooSoon.size() > i) {
					tooSoonUca = allTooSoon.get(i);
					
					ucaRow.addCell(new UnsafeControlActionCell(this.grid, tooSoonUca.getDescription(), tooSoonUca));
					linkRow.addCell(new GridCellLinking<UcaContentProvider>(tooSoonUca.getId(),
						this.ucaContentProvider, this.grid, UnsafeControlActionsView.HAZARD_ID_PREFIX));
				} else if (allTooSoon.size() == i) {
					// add placeholder
					ucaRow.addCell(new GridCellText("")); //$NON-NLS-1$
					linkRow.addCell(new AddUcaButton(cAction, Messages.AddStoppedTooSoonUCA,
						UnsafeControlActionType.STOPPED_TOO_SOON));
				} else {
					// add placeholders
					ucaRow.addCell(new GridCellText("")); //$NON-NLS-1$
					linkRow.addCell(new GridCellText("")); //$NON-NLS-1$
				}
			}
		}
	}
	
	@Override
	public String getId() {
		UnsafeControlActionsView.LOGGER.info("getID()"); //$NON-NLS-1$
		return UnsafeControlActionsView.ID;
	}
	
	@Override
	public void onActivateView() {
		UnsafeControlActionsView.LOGGER.info("refresh"); //$NON-NLS-1$
		
		this.reloadTable();
	}
	
	@Override
	public String getTitle() {
		return "Unsafe Control Actions Table"; //$NON-NLS-1$
	}
	
	@Override
	public void setDataModelInterface(IDataModel controlActionsInterface) {
		this.ucaInterface = (IUnsafeControlActionDataModel) controlActionsInterface;
		this.ucaInterface.addObserver(this);
		this.ucaContentProvider = new UcaContentProvider((IUnsafeControlActionDataModel) controlActionsInterface);
	}
	
	private void reloadTable() {
		this.grid.clearRows();
		this.fillTable(this.ucaInterface.getAllControlActionsU());
		this.grid.reloadTable();
	}
	
	@Override
	public void update(Observable dataModelController, Object updatedValue) {
		ObserverValue type = (ObserverValue) updatedValue;
		
		switch (type) {
		case UNSAFE_CONTROL_ACTION:
		case CONTROL_ACTION:
			if (!this.grid.getGrid().isDisposed()) {
				this.reloadTable();
			}
			break;
		
		default:
			break;
		}
	}

	@Override
	public boolean triggerExport() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
