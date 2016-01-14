package xstpa.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import xstampp.astpa.haz.controlaction.interfaces.IControlAction;
import xstampp.astpa.ui.common.grid.GridCellBlank;
import xstampp.astpa.ui.common.grid.GridCellColored;
import xstampp.astpa.ui.common.grid.GridCellText;
import xstampp.astpa.ui.common.grid.GridRow;
import xstampp.astpa.ui.unsafecontrolaction.UnsafeControlActionsView;
import xstpa.model.ControlActionEntry;
import xstpa.model.ProcessModelVariables;
import xstpa.model.RefinedSafetyEntry;
import xstpa.model.XSTPADataController;

public class RefinedUCAView extends UnsafeControlActionsView {
	
	private static final String ID = "xstpa.editors.refinedUCA";
	private XSTPADataController dataController;
	
	@Override
	public String getId() {
		return ID;
	}
	
	@Override
	protected void fillTable(List<IControlAction> controlActions) {
		if(dataController == null){
			return;
		}
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
		
			ArrayList<RefinedSafetyEntry> allNotProvidedRUCA = new ArrayList<>();
	  	    ArrayList<RefinedSafetyEntry> allProvidedRUCA = new ArrayList<>();
	  	    ArrayList<RefinedSafetyEntry> allWrongTimedRUCA = new ArrayList<>();
	  	    
	  	    ArrayList<ControlActionEntry> allCAEntrys = new ArrayList<>();
	  	    allCAEntrys.addAll(dataController.getDependenciesIFProvided());
	  	    allCAEntrys.addAll(dataController.getDependenciesNotProvided());
	  	    int count = 0; 
	  	    for (IControlAction ca : controlActions) {
	  	    	allNotProvidedRUCA = new ArrayList<>();
		  	    allProvidedRUCA = new ArrayList<>();
		  	    allWrongTimedRUCA = new ArrayList<>();
				for (ProcessModelVariables variable : dataController.getControlActionEntry(true, ca.getId()).
																									getContextTableCombinations()) {
					
					RefinedSafetyEntry rsEntry = null;
					if(variable.getHAnytime()){
						rsEntry = RefinedSafetyEntry.getAnytimeEntry(count++,variable,dataController.getModel());
						allProvidedRUCA.add(rsEntry);
					}
					if(variable.getHEarly()){
						rsEntry = RefinedSafetyEntry.getTooEarlyEntry(count++,variable,dataController.getModel());
						allWrongTimedRUCA.add(rsEntry);
					}
					if(variable.getHLate()){
						rsEntry = RefinedSafetyEntry.getTooLateEntry(count++,variable,dataController.getModel());
						allWrongTimedRUCA.add(rsEntry);
					}
				}
		  	    int localRows=Math.max(allProvidedRUCA.size(), allWrongTimedRUCA.size());
		  	    
				for (ProcessModelVariables variable : dataController.getControlActionEntry(true, ca.getId()).
						getContextTableCombinations()) {

					RefinedSafetyEntry rsEntry = null;
					if(variable.getHazardous()){
						rsEntry = RefinedSafetyEntry.getNotProvidedEntry(count++,variable,dataController.getModel());
						allNotProvidedRUCA.add(rsEntry);
					}
				}
				localRows=Math.max(localRows, allNotProvidedRUCA.size());
		  	    GridRow controlActionRow =new GridRow(1);
		  	    

				GridCellText descriptionItem = new GridCellText(ca.getTitle());
		  	    controlActionRow.addCell(descriptionItem);

				controlActionRow.addCell(new GridCellColored(this.grid,
						UnsafeControlActionsView.PARENT_BACKGROUND_COLOR));
				controlActionRow.addCell(new GridCellColored(this.grid,
						UnsafeControlActionsView.PARENT_BACKGROUND_COLOR));
				controlActionRow.addCell(new GridCellColored(this.grid,
						UnsafeControlActionsView.PARENT_BACKGROUND_COLOR));
				controlActionRow.addCell(new GridCellColored(this.grid,
						UnsafeControlActionsView.PARENT_BACKGROUND_COLOR));
				
				for (int i = 0; i < localRows; i++) {
					GridRow idRow = new GridRow(3);
					GridRow ucaRow = new GridRow(3);
					GridRow linkRow = new GridRow(3);

					controlActionRow.addChildRow(idRow);
					controlActionRow.addChildRow(ucaRow);
					controlActionRow.addChildRow(linkRow);

					RefinedSafetyEntry notGivenUca = null;
					RefinedSafetyEntry incorrectUca = null;
					RefinedSafetyEntry timingUca = null;
					
					if (allNotProvidedRUCA.size() > i ) {
						notGivenUca = allNotProvidedRUCA.get(i);
						if(notGivenUca.getRelatedHazards().isEmpty()){
							idRow.addCell(new GridCellBlank());
						}else{
							idRow.addCell(new GridCellText("RUCA1." + notGivenUca.getNumber()));
						}
						GridCellText editor = new GridCellText(notGivenUca.getRUCA());
						ucaRow.addCell(editor);
						linkRow.addCell(new GridCellText(notGivenUca.getRelatedHazards()));
					}else {
						// add placeholders
						idRow.addCell(new GridCellBlank());
						ucaRow.addCell(new GridCellBlank());
						linkRow.addCell(new GridCellBlank());
					}

					if (allProvidedRUCA.size() > i) {
						incorrectUca = allProvidedRUCA.get(i);
						if(incorrectUca.getRelatedHazards().isEmpty()){
							idRow.addCell(new GridCellBlank());
						}else{
							idRow.addCell(new GridCellText("RUCA1." + incorrectUca.getNumber()));
						}
						GridCellText editor = new GridCellText(incorrectUca.getRUCA());
						ucaRow.addCell(editor);
						linkRow.addCell(new GridCellText(incorrectUca.getRelatedHazards()));
					}else {
						// add placeholders
						idRow.addCell(new GridCellBlank());
						ucaRow.addCell(new GridCellBlank());
						linkRow.addCell(new GridCellBlank());
					}

					if (allWrongTimedRUCA.size() > i) {
						timingUca = allWrongTimedRUCA.get(i);
						if(timingUca.getRelatedHazards().isEmpty()){
							idRow.addCell(new GridCellBlank());
						}else{
							idRow.addCell(new GridCellText("RUCA1." + timingUca.getNumber()));
						}
						GridCellText editor = new GridCellText(timingUca.getRUCA());
						ucaRow.addCell(editor);
						linkRow.addCell(new GridCellText(timingUca.getRelatedHazards()));
					}else {
						// add placeholders
						idRow.addCell(new GridCellBlank());
						ucaRow.addCell(new GridCellBlank());
						linkRow.addCell(new GridCellBlank());
					}
					// add placeholders
					idRow.addCell(new GridCellBlank());
					ucaRow.addCell(new GridCellBlank());
					linkRow.addCell(new GridCellBlank());
					
				}
				if(localRows > 0){
					this.grid.addRow(controlActionRow);
				}
			}
	  	    
	}

	/**
	 * @return the dataController
	 */
	public XSTPADataController getDataController() {
		return this.dataController;
	}

	/**
	 * @param dataController the dataController to set
	 */
	public void setDataController(XSTPADataController dataController) {
		if(this.dataController != dataController){
			this.dataController = dataController;
			
			this.grid.clearRows();
			this.fillTable(null);
			this.grid.reloadTable();
			this.dataController.addObserver(this);
		}
	}
	@Override
	public void update(Observable dataModelController, Object updatedValue) {
		if (!this.grid.getGrid().isDisposed()) {
			this.grid.clearRows();
			this.fillTable(((XSTPADataController)dataModelController).getModel().getAllControlActions());
			this.grid.reloadTable();
		}
	}
	

}
