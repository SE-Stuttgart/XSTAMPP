/*******************************************************************************
 * Copyright (c) 2013-2016 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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
import java.util.Map.Entry;
import java.util.Observable;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchPart;

import messages.Messages;
import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.haz.controlaction.UnsafeControlActionType;
import xstampp.astpa.haz.controlaction.interfaces.IControlAction;
import xstampp.astpa.haz.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.controlaction.interfaces.IHAZXControlAction;
import xstampp.astpa.model.interfaces.IUnsafeControlActionDataModel;
import xstampp.astpa.ui.causalScenarios.ActionMenuListener;
import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.common.grid.GridCellBlank;
import xstampp.ui.common.grid.GridCellButton;
import xstampp.ui.common.grid.GridCellColored;
import xstampp.ui.common.grid.GridCellLinking;
import xstampp.ui.common.grid.GridCellText;
import xstampp.ui.common.grid.GridCellTextEditor;
import xstampp.ui.common.grid.GridRow;
import xstampp.ui.common.grid.GridWrapper;
import xstampp.ui.editors.AbstractFilteredEditor;

/**
 * View used to handle the unsafe control actions.
 * 
 * @author Benedikt Markt, Patrick Wickenhaeuser, Lukas Balzer
 */
public class UnsafeControlActionsView extends AbstractFilteredEditor{

  public static final String UCA1 = "UCA1.";
  /**
	 * ViewPart ID.
	 */
	public static final String ID = "astpa.steps.step2_2"; //$NON-NLS-1$

	private static final String CA_FILTER="Control Action"; //$NON-NLS-1$
	private static final String UCA_FILTER="unsafe Control Actions"; //$NON-NLS-1$
	private static final String UCAID_FILTER="UCA ID"; //$NON-NLS-1$
	private static final String HAZ_FILTER="Hazards"; //$NON-NLS-1$
	private static final String NOHAZ_FILTER="not hazardous"; //$NON-NLS-1$
	private static final String HAZID_FILTER="Hazard ID"; //$NON-NLS-1$
	/**
	 * The log4j logger.
	 */
	private static final Logger LOGGER = Logger.getRootLogger();
	private static final String HAZARD_ID_PREFIX = "H-"; //$NON-NLS-1$

	private int internalUpdate;

	/**
	 * Interfaces to communicate with the data model.
	 */
	private UcaContentProvider ucaContentProvider = null;

	private IUnsafeControlActionDataModel ucaInterface;
	private Map<UUID,String> descriptionsToUUIDs = new HashMap<>();
	protected GridWrapper grid;
	private boolean lockreload;
	private DeleteUcaAction deleteAction;
	
	public UnsafeControlActionsView() {
		setUseFilter(true);
	}
	
	@Override
	protected Map<String, Boolean> getCategories() {
		Map<String, Boolean> categories= new HashMap<>();
		categories.put(CA_FILTER, false);
		categories.put(UCA_FILTER, false);
		categories.put(UCAID_FILTER, false);
		categories.put(HAZ_FILTER, false);
		categories.put(NOHAZ_FILTER, false);
		categories.put(HAZID_FILTER, false);
		return categories;

	}
	

	@Override
	protected String[] getCategoryArray() {
		return new String[]{CA_FILTER,UCA_FILTER,UCAID_FILTER,HAZ_FILTER,HAZID_FILTER,NOHAZ_FILTER};
	}
	
	@Override
	protected void updateFilter() {
		reloadTable();
	}
	
	private class UnsafeControlActionCell extends GridCellTextEditor {

		public UnsafeControlActionCell(GridWrapper grid,String initialText,UUID uca,boolean showDelete, boolean isReadOnly) {
			super(grid, initialText, showDelete, isReadOnly,uca);
		}
		@Override
		public void delete() {
			deleteAction.run();
		}
		@Override
		public void updateDataModel(String newValue) {
			ucaInterface.setUcaDescription(getUUID(), newValue);
			descriptionsToUUIDs.remove(getUUID());
		}
		
		@Override
		public void onTextChange(String newValue) {
			UnsafeControlActionsView.this.descriptionsToUUIDs.put(getUUID(),newValue);
			final String value=newValue;
			Display.getDefault().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					internalUpdate++;
					updateDataModel(value);
				}
			});
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
		  if(e.button == 1){
  			UUID newUCA = UnsafeControlActionsView.this.ucaInterface
  					.addUnsafeControlAction(this.parentControlAction.getId(),
  							"", this.ucaType); //$NON-NLS-1$
  			UnsafeControlActionsView.this.grid.activateCell(newUCA);
  			UnsafeControlActionsView.LOGGER.debug(Messages.AddingNewUCA);
		  }
			
		}
	}

	/**
	 * User interface components.
	 * 
	 * @author Benedikt Markt, Patrick Wickenhaeuser
	 */
	@Override
	public void createPartControl(Composite parent) {
		internalUpdate = 0;
		this.setDataModelInterface(ProjectManager.getContainerInstance()
				.getDataModel(this.getProjectID()));

		updateHazards();
		UnsafeControlActionsView.LOGGER.info("createPartControl()"); //$NON-NLS-1$
		parent.setLayout(new GridLayout(1, false));
		
		super.createPartControl(parent);
		this.grid = new GridWrapper(parent, new String[] {
				Messages.ControlAction, Messages.NotGiven,
				Messages.GivenIncorrectly, Messages.WrongTiming,
				Messages.StoppedTooSoon });
		this.grid.setSelectRow(false);
		this.grid.getGrid().setVisible(true);

    this.grid.getGrid().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		this.reloadTable();
    deleteAction = new DeleteUcaAction(grid, ucaInterface,Messages.UnsafeControlActions,ucaContentProvider.getPrefix());
		MenuManager menuMgr = new MenuManager();
		Menu menu = menuMgr.createContextMenu(this.grid.getGrid());
		menuMgr.addMenuListener(new ActionMenuListener(deleteAction));
		menuMgr.setRemoveAllWhenShown(true);
		this.grid.getGrid().setMenu(menu);

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

	protected void fillTable(List<IHAZXControlAction> list) throws SWTException{
		
		if (list.isEmpty()) {
			return;
		}
		boolean addControlAction;
		for (IControlAction cAction : list) {
			//fiter by the title of the control action
			
			if(isFiltered(cAction.getTitle(), CA_FILTER)){
				continue;
			}
			GridRow controlActionRow = new GridRow(3);
			addControlAction = false;
			

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

				addControlAction |= addUCAEntry(allNotGiven,
											    i, 
											    Messages.AddNotGivenUCA,
											    UnsafeControlActionType.NOT_GIVEN,
											    idRow,
											    ucaRow,
											    linkRow,
											    cAction);
				addControlAction |= addUCAEntry(allIncorrect,
												i, 
											    Messages.AddGivenIncorrectlyUCA,
											    UnsafeControlActionType.GIVEN_INCORRECTLY,
											    idRow,
											    ucaRow,
											    linkRow,
											    cAction);
				
				addControlAction |= addUCAEntry(allWrongTiming,
											    i, 
											    Messages.AddWrongTimingUCA,
											    UnsafeControlActionType.WRONG_TIMING,
											    idRow,
											    ucaRow,
											    linkRow,
											    cAction);
				addControlAction |= addUCAEntry(allTooSoon,
											    i, 
											    Messages.AddStoppedTooSoonUCA,
											    UnsafeControlActionType.STOPPED_TOO_SOON,
											    idRow,
											    ucaRow,
											    linkRow,
											    cAction);
			}
			if(addControlAction){
				this.grid.addRow(controlActionRow);			
			}
		}
	}

	private boolean addUCAEntry(List<IUnsafeControlAction> ucaList,
							 int i,
							 String message,
							 UnsafeControlActionType type,
							 GridRow idRow,
							 GridRow ucaRow,
							 GridRow linkRow,
							 IControlAction cAction){
		if (ucaList.size() > i && !isUCAFiltered(ucaList.get(i))) {
			IUnsafeControlAction tooSoonUca = ucaList.get(i);
			if(this.ucaContentProvider.getLinkedItems(tooSoonUca.getId()).isEmpty()){
				idRow.addCell(new GridCellBlank());
			}else{
				idRow.addCell(new GridCellText(UCA1 + this.ucaInterface.getUCANumber(tooSoonUca.getId()))); //$NON-NLS-1$
			}
			UnsafeControlActionCell editor = new UnsafeControlActionCell(this.grid,tooSoonUca.getDescription(),
					tooSoonUca.getId(),true,false);
			ucaRow.addCell(editor);
			linkRow.addCell(new GridCellLinking<UcaContentProvider>(
					tooSoonUca.getId(), this.ucaContentProvider,
					this.grid,
					UnsafeControlActionsView.HAZARD_ID_PREFIX));
			return true;
		}

		if (ucaList.size() == i) {
			// add placeholder
			idRow.addCell(new GridCellBlank());
			ucaRow.addCell(new AddUcaButton(cAction,
          message,
          type));
			linkRow.addCell(new GridCellBlank());
		} else {
			// add placeholders
			idRow.addCell(new GridCellBlank());
			ucaRow.addCell(new GridCellBlank());
			linkRow.addCell(new GridCellBlank());
		}
		return false;
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
	 * !isFiltered(this.ucaInterface.getUCANumber(allWrongTiming.get(i).getId()),UCAID)
						&& !isFiltered(allWrongTiming.get(i).getDescription(),UCA)
	 * @param uca
	 * @return true if the uca is filtered out and should not be used
	 */
	private boolean isUCAFiltered(IUnsafeControlAction uca){
		switch (getActiveCategory()) {
		case UCAID_FILTER:
			return isFiltered(this.ucaInterface.getUCANumber(uca.getId()),UCAID_FILTER);
		case UCA_FILTER:
			return isFiltered(uca.getDescription(),UCA_FILTER);
		case HAZ_FILTER:
			if(this.ucaInterface.getLinkedHazardsOfUCA(uca.getId()).size() == 0){
				return true;
			}
			for(ITableModel model : this.ucaInterface.getLinkedHazardsOfUCA(uca.getId())){
				if(!isFiltered(model.getTitle(), HAZ_FILTER) ||  !isFiltered(model.getDescription(), HAZ_FILTER)){
					return false;
				}
				return true;
			}
		case NOHAZ_FILTER:
			if(this.ucaInterface.getLinkedHazardsOfUCA(uca.getId()).size() != 0){
				return true;
			}
		case HAZID_FILTER:
			if(this.ucaInterface.getLinkedHazardsOfUCA(uca.getId()).size() == 0){
				return true;
			}
			for(ITableModel model : this.ucaInterface.getLinkedHazardsOfUCA(uca.getId())){
				if(!isFiltered(model.getNumber(),HAZID_FILTER)){
					return false;
				}
			}
      return true;
		default:
			return isFiltered(uca.getDescription(),UCA_FILTER);
		}
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
	
	private void reloadTable() throws SWTException {
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

	private void updateHazards(){
		String[] choices= new String[ucaInterface.getAllHazards().size()];
		String[] choiceIDs= new String[ucaInterface.getAllHazards().size()];
		String[] choiceValues= new String[ucaInterface.getAllHazards().size()];
		int index = 0;
	
		for (ITableModel model : ucaInterface.getAllHazards()) {
			choices[index] = "H-" + model.getNumber() + ": "+ model.getTitle(); //$NON-NLS-1$ //$NON-NLS-2$
			choiceIDs[index] = "" + model.getNumber(); //$NON-NLS-1$
			choiceValues[index++] = model.getTitle();
		}
		this.addChoices(HAZID_FILTER, choices);
		this.addChoiceValues(HAZID_FILTER,choiceIDs);
		this.addChoices(HAZ_FILTER, choices);
		this.addChoiceValues(HAZ_FILTER,choiceValues);
	}
	@Override
	public void update(Observable dataModelController, Object updatedValue) {

		super.update(dataModelController, updatedValue);
		ObserverValue type = (ObserverValue) updatedValue;
		switch (type) {
		case UNSAFE_CONTROL_ACTION:
		case HAZARD:
			updateHazards();
		case CONTROL_ACTION:
			if(internalUpdate!=0){
				internalUpdate--;
			}else{
				try{
					this.reloadTable();
				}catch(SWTException e){
					dataModelController.deleteObserver(this);
				}
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
	public void partDeactivated(IWorkbenchPart arg0) {
		if(arg0 == this){
			for(Entry<UUID,String> ucaID: this.descriptionsToUUIDs.entrySet()){
				this.ucaInterface.setUcaDescription(ucaID.getKey(), ucaID.getValue());
			}
			this.descriptionsToUUIDs.clear();
		}
		super.partDeactivated(arg0);
	}
	@Override
	public void dispose() {
		this.ucaInterface.deleteObserver(this);
		super.dispose();
	}

	
	
}
