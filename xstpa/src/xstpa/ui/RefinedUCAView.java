package xstpa.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import messages.Messages;
import xstampp.astpa.haz.controlaction.interfaces.IControlAction;
import xstampp.astpa.haz.controlaction.interfaces.IUCAHazLink;
import xstampp.astpa.model.controlaction.IValueCombie;
import xstampp.astpa.ui.common.grid.GridCellBlank;
import xstampp.astpa.ui.common.grid.GridCellColored;
import xstampp.astpa.ui.common.grid.GridCellText;
import xstampp.astpa.ui.common.grid.GridRow;
import xstampp.astpa.ui.unsafecontrolaction.UnsafeControlActionsView;
import xstampp.model.ILTLProvider;
import xstpa.model.ControlActionEntry;
import xstpa.model.XSTPADataController;

public class RefinedUCAView extends UnsafeControlActionsView {
	
	private static final String ID = "xstpa.editors.refinedUCA";
	private XSTPADataController dataController;
	
	public RefinedUCAView() {
		this.useFilter = false;
	}
	@Override
	public String getId() {
		return ID;
	}
	@Override
	public String getTitle() {
		return "Refined Unsafe Control Actions";
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
		
			ArrayList<ILTLProvider> allNotProvidedRUCA = new ArrayList<>();
	  	    ArrayList<ILTLProvider> allProvidedRUCA = new ArrayList<>();
	  	    ArrayList<ILTLProvider> allWrongTimedRUCA = new ArrayList<>();
	  	    List<ILTLProvider> refinedEntrys = dataController.getModel().getAllRefinedRules();
	  	    
	  	    ArrayList<ControlActionEntry> allCAEntrys = new ArrayList<>();
	  	    allCAEntrys.addAll(dataController.getDependenciesIFProvided());
	  	    allCAEntrys.addAll(dataController.getDependenciesNotProvided());
	  	    for (IControlAction ca : dataController.getModel().getAllControlActions()) {
	  	    	
	  	    	allNotProvidedRUCA.clear();
	  	    	allProvidedRUCA.clear();
	  	    	allWrongTimedRUCA.clear();
	  	    	
	  			for (ILTLProvider provider : refinedEntrys) {
	  				if(provider.getRelatedControlActionID().equals(ca.getId())){
	  					switch(provider.getType()){
	  					case IValueCombie.TYPE_NOT_PROVIDED :
	  						allNotProvidedRUCA.add(provider);
	  						break;
	  					case IValueCombie.TYPE_ANYTIME :
	  						allProvidedRUCA.add(provider);
							break;
	  					case IValueCombie.TYPE_TOO_EARLY :
	  					case IValueCombie.TYPE_TOO_LATE :
	  						allWrongTimedRUCA.add(provider);
							break;
	  					}
	  				}
				}
		  	    
		  	    int localRows=Math.max(allProvidedRUCA.size(), allWrongTimedRUCA.size());
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

					ILTLProvider notGivenUca = null;
					ILTLProvider incorrectUca = null;
					ILTLProvider timingUca = null;
					
					if (allNotProvidedRUCA.size() > i ) {
						notGivenUca = allNotProvidedRUCA.get(i);
						addRUCA(idRow, ucaRow, linkRow,notGivenUca);
					}else {
						// add placeholders
						idRow.addCell(new GridCellBlank());
						ucaRow.addCell(new GridCellBlank());
						linkRow.addCell(new GridCellBlank());
					}

					if (allProvidedRUCA.size() > i) {
						incorrectUca = allProvidedRUCA.get(i);
						addRUCA(idRow, ucaRow, linkRow, incorrectUca);
					}else {
						// add placeholders
						idRow.addCell(new GridCellBlank());
						ucaRow.addCell(new GridCellBlank());
						linkRow.addCell(new GridCellBlank());
					}

					if (allWrongTimedRUCA.size() > i) {
						timingUca = allWrongTimedRUCA.get(i);
						addRUCA(idRow, ucaRow, linkRow, timingUca);
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
	  	    this.grid.reloadTable();
	  	    this.grid.resizeRows();
	  	    
	}

	private void addRUCA(GridRow idRow,GridRow ucaRow,GridRow linkRow,ILTLProvider provider){
		String links = "";
		
		for (IUCAHazLink ucaLink: dataController.getModel().getAllUCALinks()) {
			if(provider.getUCALinks() != null && provider.getUCALinks().contains(ucaLink.getUnsafeControlActionId())){
				links = links.concat("[H-"+dataController.getModel().getHazard(ucaLink.getHazardId()).getNumber()+"],");
			}
		}
		if(links.isEmpty()){
			links = "not hazardous";
		}else{
			links = links.substring(0, links.length()-1);			
		}
		idRow.addCell(new GridCellText("RUCA1."+provider.getNumber()));
		
		GridCellText editor = new GridCellText(provider.getRefinedUCA());
		ucaRow.addCell(editor);
		linkRow.addCell(new GridCellText(links));
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
		if (!this.grid.getGrid().isDisposed() && dataModelController instanceof XSTPADataController) {
			this.grid.clearRows();
			this.fillTable(((XSTPADataController)dataModelController).getModel().getAllControlActions());
			this.grid.reloadTable();
		}
	}
	

}
