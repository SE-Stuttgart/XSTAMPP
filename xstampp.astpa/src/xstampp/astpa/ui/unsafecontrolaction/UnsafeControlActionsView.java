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

package xstampp.astpa.ui.unsafecontrolaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;

import xstampp.astpa.haz.controlaction.UnsafeControlActionType;
import xstampp.astpa.haz.controlaction.interfaces.IControlAction;
import xstampp.astpa.haz.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.interfaces.IUnsafeControlActionDataModel;
import xstampp.astpa.ui.common.grid.GridCellBlank;
import xstampp.astpa.ui.common.grid.GridCellButton;
import xstampp.astpa.ui.common.grid.GridCellColored;
import xstampp.astpa.ui.common.grid.GridCellLinking;
import xstampp.astpa.ui.common.grid.GridCellText;
import xstampp.astpa.ui.common.grid.GridCellTextEditor;
import xstampp.astpa.ui.common.grid.GridRow;
import xstampp.astpa.ui.common.grid.GridWrapper;
import xstampp.astpa.ui.common.grid.IGridCell;
import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.StandartEditorPart;

/**
 * View used to handle the unsafe control actions.
 * 
 * @author Benedikt Markt, Patrick Wickenhaeuser
 */
public class UnsafeControlActionsView extends StandartEditorPart{

	/**
	 * ViewPart ID.
	 */
	public static final String ID = "astpa.steps.step2_2"; //$NON-NLS-1$

	/**
	 * The log4j logger.
	 */
	private static final Logger LOGGER = Logger.getRootLogger();
	private static final String HAZARD_ID_PREFIX = "H-"; //$NON-NLS-1$
	private static final String CONFIRMATION_TITLE = Messages.DeleteUnsafeControlAction;
	private static final String CONFIRMATION_DESCRIPTION = Messages.WantToDeleteTheUCA;

	private static final RGB PARENT_BACKGROUND_COLOR = new RGB(215, 240, 255);

	/**
	 * Interfaces to communicate with the data model.
	 */
	private UcaContentProvider ucaContentProvider = null;

	private IUnsafeControlActionDataModel ucaInterface;
	private Map<UUID,String> descriptionsToUUIDs = new HashMap<>();
	private GridWrapper grid;

	private Text filterText;

	private boolean lockreload;

	private class UnsafeControlActionCell extends GridCellTextEditor{

		private UUID unsafeControlAction;
		public UnsafeControlActionCell(GridWrapper grid,String initialText,UUID uca) {
			super(grid, initialText, true);
			this.unsafeControlAction = uca;
		}
		@Override
		public void delete() {
			UnsafeControlActionsView.LOGGER
					.debug("Delete unsafe control action"); //$NON-NLS-1$
			
			if (MessageDialog.openConfirm(
					UnsafeControlActionsView.this.grid.getGrid()
							.getShell(),
					UnsafeControlActionsView.CONFIRMATION_TITLE,
					UnsafeControlActionsView.CONFIRMATION_DESCRIPTION)) {
				UnsafeControlActionsView.this.ucaInterface
						.removeUnsafeControlAction(this.unsafeControlAction);
			}
		}
		
		@Override
		public UUID getUUID(){
			return this.unsafeControlAction;
		}
		@Override
		public void updateDataModel(String newValue) {
			UnsafeControlActionsView.this.descriptionsToUUIDs.put(this.unsafeControlAction,newValue);
		}
		
	}

	private class AddUcaButton extends GridCellButton {

		private IControlAction parentControlAction;
		private UnsafeControlActionType ucaType;

		public AddUcaButton(IControlAction parentItem, String text,
				UnsafeControlActionType type) {
			super(text);

			this.parentControlAction = parentItem;
			this.ucaType = type;
		}

		@Override
		public void onMouseDown(MouseEvent e,
				org.eclipse.swt.graphics.Point relativeMouse,
				Rectangle cellBounds) {
			UUID newUCA = UnsafeControlActionsView.this.ucaInterface
					.addUnsafeControlAction(this.parentControlAction.getId(),
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
		this.setDataModelInterface(ProjectManager.getContainerInstance()
				.getDataModel(this.getProjectID()));
		UnsafeControlActionsView.LOGGER.info("createPartControl()"); //$NON-NLS-1$
		parent.setLayout(new FormLayout());
		FormData data = new FormData();
		data.top = new FormAttachment(0);
		data.left= new FormAttachment(0);
		data.right= new FormAttachment(100);
		
		Composite filter= new Composite(parent, SWT.None);
		filter.setLayoutData(data);
		filter.setLayout(new GridLayout(2, false));
		Label label= new Label(filter,SWT.LEFT);
		label.setText("Filter");
		this.filterText= new Text(filter, SWT.LEFT |SWT.BORDER);
		this.filterText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		this.filterText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				reloadTable();
				
			}
		});
		this.filterText.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.character == 13){
					reloadTable();
				}
			}
		});
		
		data = new FormData();
		data.top = new FormAttachment(filter);
		data.left= new FormAttachment(0);
		data.right= new FormAttachment(100);
		data.bottom = new FormAttachment(100);
		this.grid = new GridWrapper(parent, new String[] {
				Messages.ControlAction, Messages.NotGiven,
				Messages.GivenIncorrectly, Messages.WrongTiming,
				Messages.StoppedTooSoon });
		this.grid.getGrid().setVisible(true);
		this.grid.getGrid().setLayoutData(data);
		this.reloadTable();

		MenuManager menuMgr = new MenuManager();
		Menu menu = menuMgr.createContextMenu(this.grid.getGrid());
		menuMgr.addMenuListener(new IMenuListener() {

			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if (UnsafeControlActionsView.this.grid.getGrid()
						.getCellSelectionCount() > 0) {

					Action deleteAccident = new Action(
							Messages.DeleteUnsafeControlActions) {

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
				if ((e.keyCode == SWT.DEL)
						|| ((e.stateMask == SWT.COMMAND) && (e.keyCode == SWT.BS))) {
					UnsafeControlActionsView.this.deleteSelectedUcas();
				}
			}
		});
	}

	private void deleteSelectedUcas() {
		if (MessageDialog.openConfirm(this.grid.getGrid().getShell(),
				UnsafeControlActionsView.CONFIRMATION_TITLE,
				UnsafeControlActionsView.CONFIRMATION_DESCRIPTION)) {
			List<IGridCell> selected = UnsafeControlActionsView.this.grid
					.getSelectedCellList();

			for (int i = 0; i < selected.size(); i++) {
				IGridCell cell = selected.get(i);

				if (cell instanceof UnsafeControlActionCell) {
					UnsafeControlActionCell editor= ((UnsafeControlActionCell) cell);
					UUID ucaID=editor.getUUID();
					UnsafeControlActionsView.this.ucaInterface
							.removeUnsafeControlAction(ucaID);
				}
			}
		}
	}

	private int getMaxHeight(IControlAction controlAction) {
		int maxHeight = controlAction.getUnsafeControlActions(
				UnsafeControlActionType.NOT_GIVEN).size();

		if (controlAction.getUnsafeControlActions(
				UnsafeControlActionType.GIVEN_INCORRECTLY).size() > maxHeight) {
			maxHeight = controlAction.getUnsafeControlActions(
					UnsafeControlActionType.GIVEN_INCORRECTLY).size();
		}
		if (controlAction.getUnsafeControlActions(
				UnsafeControlActionType.WRONG_TIMING).size() > maxHeight) {
			maxHeight = controlAction.getUnsafeControlActions(
					UnsafeControlActionType.WRONG_TIMING).size();
		}
		if (controlAction.getUnsafeControlActions(
				UnsafeControlActionType.STOPPED_TOO_SOON).size() > maxHeight) {
			maxHeight = controlAction.getUnsafeControlActions(
					UnsafeControlActionType.STOPPED_TOO_SOON).size();
		}

		return maxHeight;
	}

	protected void fillTable(List<IControlAction> controlActions) {
		
		if (controlActions.isEmpty()) {
			return;
		}
		for (IControlAction cAction : controlActions) {
			//fiter by the title of the control action
			boolean isDigit = false;
			int filterID = -1;
			for(Character c : this.filterText.getText().toCharArray()){
				isDigit = Character.isDigit(c);
				if(!isDigit){
					break;
				}
			}
			if(isDigit){
				filterID = Integer.parseInt(this.filterText.getText());
			}
			if(!cAction.getTitle().startsWith(this.filterText.getText()) || isDigit){
				continue;
			}
			GridRow controlActionRow = new GridRow(1);
			this.grid.addRow(controlActionRow);

			GridCellText descriptionItem = new GridCellText(cAction.getTitle());
//			descriptionItem.getTextEditor().setEditable(false);
			controlActionRow.addCell(descriptionItem);

			controlActionRow.addCell(new GridCellColored(this.grid,
					UnsafeControlActionsView.PARENT_BACKGROUND_COLOR));
			controlActionRow.addCell(new GridCellColored(this.grid,
					UnsafeControlActionsView.PARENT_BACKGROUND_COLOR));
			controlActionRow.addCell(new GridCellColored(this.grid,
					UnsafeControlActionsView.PARENT_BACKGROUND_COLOR));
			controlActionRow.addCell(new GridCellColored(this.grid,
					UnsafeControlActionsView.PARENT_BACKGROUND_COLOR));

			List<IUnsafeControlAction> allNotGiven = cAction
					.getUnsafeControlActions(UnsafeControlActionType.NOT_GIVEN);
			List<IUnsafeControlAction> allIncorrect = cAction
					.getUnsafeControlActions(UnsafeControlActionType.GIVEN_INCORRECTLY);
			List<IUnsafeControlAction> allWrongTiming = cAction
					.getUnsafeControlActions(UnsafeControlActionType.WRONG_TIMING);
			List<IUnsafeControlAction> allTooSoon = cAction
					.getUnsafeControlActions(UnsafeControlActionType.STOPPED_TOO_SOON);

			for (int i = 0; i <= this.getMaxHeight(cAction); i++) {
				
				GridRow idRow = new GridRow(3);
				GridRow ucaRow = new GridRow(3);
				GridRow linkRow = new GridRow(3);

				controlActionRow.addChildRow(idRow);
				controlActionRow.addChildRow(ucaRow);
				controlActionRow.addChildRow(linkRow);

				IUnsafeControlAction notGivenUca = null;
				IUnsafeControlAction incorrectUca = null;
				IUnsafeControlAction timingUca = null;
				IUnsafeControlAction tooSoonUca = null;

				// set descriptions
				if (allNotGiven.size() > i && (!isDigit || this.ucaInterface.getUCANumber(allNotGiven.get(i).getId()) == filterID)) {
					notGivenUca = allNotGiven.get(i);
					if(this.ucaContentProvider.getLinkedItems(notGivenUca.getId()).isEmpty()){
						idRow.addCell(new GridCellBlank());
					}else{
						idRow.addCell(new GridCellText("UCA1." + this.ucaInterface.getUCANumber(notGivenUca.getId())));
					}
					UnsafeControlActionCell editor = new UnsafeControlActionCell(this.grid,notGivenUca.getDescription(),
							notGivenUca.getId());
					ucaRow.addCell(editor);
					linkRow.addCell(new GridCellLinking<UcaContentProvider>(
							notGivenUca.getId(), this.ucaContentProvider,
							this.grid,
							UnsafeControlActionsView.HAZARD_ID_PREFIX));
				} else if (allNotGiven.size() == i) {
					// add placeholder
					idRow.addCell(new GridCellBlank());
					ucaRow.addCell(new GridCellBlank());
					linkRow.addCell(new AddUcaButton(cAction,
							Messages.AddNotGivenUCA,
							UnsafeControlActionType.NOT_GIVEN));
				} else {
					// add placeholders
					idRow.addCell(new GridCellBlank());
					ucaRow.addCell(new GridCellBlank());
					linkRow.addCell(new GridCellBlank());
				}

				if (allIncorrect.size() > i && (!isDigit || this.ucaInterface.getUCANumber(allIncorrect.get(i).getId()) == filterID)) {
					incorrectUca = allIncorrect.get(i);
					if(this.ucaContentProvider.getLinkedItems(incorrectUca.getId()).isEmpty()){
						idRow.addCell(new GridCellBlank());
					}else{
						idRow.addCell(new GridCellText("UCA1." + this.ucaInterface.getUCANumber(incorrectUca.getId())));
					}
					UnsafeControlActionCell editor = new UnsafeControlActionCell(this.grid,incorrectUca.getDescription(),
							incorrectUca.getId());
					ucaRow.addCell(editor);
					linkRow.addCell(new GridCellLinking<UcaContentProvider>(
							incorrectUca.getId(), this.ucaContentProvider,
							this.grid,
							UnsafeControlActionsView.HAZARD_ID_PREFIX));
				} else if (allIncorrect.size() == i) {
					// add placeholder
					idRow.addCell(new GridCellBlank());
					ucaRow.addCell(new GridCellBlank());
					linkRow.addCell(new AddUcaButton(cAction,
							Messages.AddGivenIncorrectlyUCA,
							UnsafeControlActionType.GIVEN_INCORRECTLY));
				} else {
					// add placeholders
					idRow.addCell(new GridCellBlank());
					ucaRow.addCell(new GridCellBlank());
					linkRow.addCell(new GridCellBlank());
				}

				if (allWrongTiming.size() > i && (!isDigit || this.ucaInterface.getUCANumber(allWrongTiming.get(i).getId()) == filterID)) {
					timingUca = allWrongTiming.get(i);
					if(this.ucaContentProvider.getLinkedItems(timingUca.getId()).isEmpty()){
						idRow.addCell(new GridCellBlank());
					}else{
						idRow.addCell(new GridCellText("UCA1." + this.ucaInterface.getUCANumber(timingUca.getId())));
					}
					UnsafeControlActionCell editor = new UnsafeControlActionCell(this.grid,timingUca.getDescription(),
							timingUca.getId());
					ucaRow.addCell(editor);
					linkRow.addCell(new GridCellLinking<UcaContentProvider>(
							timingUca.getId(), this.ucaContentProvider,
							this.grid,
							UnsafeControlActionsView.HAZARD_ID_PREFIX));
				} else if (allWrongTiming.size() == i) {
					// add placeholder
					idRow.addCell(new GridCellBlank());
					ucaRow.addCell(new GridCellBlank());
					linkRow.addCell(new AddUcaButton(cAction,
							Messages.AddWrongTimingUCA,
							UnsafeControlActionType.WRONG_TIMING));
				} else {
					// add placeholders
					idRow.addCell(new GridCellBlank());
					ucaRow.addCell(new GridCellBlank());
					linkRow.addCell(new GridCellBlank());
				}

				if (allTooSoon.size() > i && (!isDigit || this.ucaInterface.getUCANumber(allTooSoon.get(i).getId()) == filterID)) {
					tooSoonUca = allTooSoon.get(i);
					if(this.ucaContentProvider.getLinkedItems(tooSoonUca.getId()).isEmpty()){
						idRow.addCell(new GridCellBlank());
					}else{
						idRow.addCell(new GridCellText("UCA1." + this.ucaInterface.getUCANumber(tooSoonUca.getId())));
					}
					UnsafeControlActionCell editor = new UnsafeControlActionCell(this.grid,tooSoonUca.getDescription(),
							tooSoonUca.getId());
					ucaRow.addCell(editor);
					linkRow.addCell(new GridCellLinking<UcaContentProvider>(
							tooSoonUca.getId(), this.ucaContentProvider,
							this.grid,
							UnsafeControlActionsView.HAZARD_ID_PREFIX));
				} else if (allTooSoon.size() == i) {
					// add placeholder
					idRow.addCell(new GridCellBlank());
					ucaRow.addCell(new GridCellBlank());
					linkRow.addCell(new AddUcaButton(cAction,
							Messages.AddStoppedTooSoonUCA,
							UnsafeControlActionType.STOPPED_TOO_SOON));
				} else {
					// add placeholders
					idRow.addCell(new GridCellBlank());
					ucaRow.addCell(new GridCellBlank());
					linkRow.addCell(new GridCellBlank());
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
	public String getTitle() {
		return "Unsafe Control Actions Table"; //$NON-NLS-1$
	}
	
	/**
	 * sets the data model object for this editor
	 *
	 * @author Lukas
	 *
	 * @param controlActionsInterface the data model object
	 */
	public void setDataModelInterface(IDataModel controlActionsInterface) {
		this.ucaInterface = (IUnsafeControlActionDataModel) controlActionsInterface;
		this.ucaInterface.addObserver(this);
		this.ucaContentProvider = new UcaContentProvider(
				(IUnsafeControlActionDataModel) controlActionsInterface);
	}

	protected IDataModel getDataModel(){
		return this.ucaInterface;
	}
	private void reloadTable() {
		if(!this.lockreload){
			int tmp= this.grid.getGrid().getVerticalBar().getSelection();
			this.lockreload = true;
			if(this.descriptionsToUUIDs.size() > 0){
				for(UUID ucaID: this.descriptionsToUUIDs.keySet()){
					this.ucaInterface.setUcaDescription(ucaID, this.descriptionsToUUIDs.get(ucaID));
				}
				this.descriptionsToUUIDs= new HashMap<>();
			}
			this.grid.clearRows();
			this.fillTable(this.ucaInterface.getAllControlActionsU());
			this.grid.reloadTable();
			this.lockreload = false;
			this.grid.getGrid().setTopIndex(tmp);
		}
	}

	@Override
	public void update(Observable dataModelController, Object updatedValue) {

		super.update(dataModelController, updatedValue);
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
	public void partBroughtToTop(IWorkbenchPart arg0) {
		
		if(!this.lockreload && this.descriptionsToUUIDs.size() > 0){
			this.lockreload = true;
			for(UUID ucaID: this.descriptionsToUUIDs.keySet()){
				this.ucaInterface.setUcaDescription(ucaID, this.descriptionsToUUIDs.get(ucaID));
			}
			this.descriptionsToUUIDs= new HashMap<>();
			this.lockreload = false;
		}
		super.partBroughtToTop(arg0);
	}
	@Override
	public void dispose() {
		this.ucaInterface.deleteObserver(this);
		super.dispose();
	}
	
	
}
