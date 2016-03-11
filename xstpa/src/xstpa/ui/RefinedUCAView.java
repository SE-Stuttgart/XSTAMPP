package xstpa.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import xstampp.astpa.haz.controlaction.interfaces.IControlAction;
import xstampp.astpa.haz.controlaction.interfaces.IUCAHazLink;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.controlaction.IValueCombie;
import xstampp.astpa.model.controlaction.interfaces.IHAZXControlAction;
import xstampp.astpa.ui.common.grid.GridCellBlank;
import xstampp.astpa.ui.common.grid.GridCellColored;
import xstampp.astpa.ui.common.grid.GridCellText;
import xstampp.astpa.ui.common.grid.GridRow;
import xstampp.astpa.ui.unsafecontrolaction.UnsafeControlActionsView;
import xstampp.model.AbstractLTLProvider;
import xstampp.model.ObserverValue;
import xstpa.model.ControlActionEntry;
import xstpa.model.XSTPADataController;

public class RefinedUCAView extends UnsafeControlActionsView {
	

	private static final String CA="control action";
	private static final String UCA="refined unsafe control actions";
	
	private static final String ID = "xstpa.editors.refinedUCA";
	private XSTPADataController dataController;
	
	public RefinedUCAView() {
		setUseFilter(true);
	}
	
	@Override
	protected String[] getCategories() {
		return new String[]{CA,UCA};
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
	protected void fillTable(List<IHAZXControlAction> controlActions) {
		if(dataController == null){
			return;
		}
			ArrayList<AbstractLTLProvider> allNotProvidedRUCA = new ArrayList<>();
	  	    ArrayList<AbstractLTLProvider> allProvidedRUCA = new ArrayList<>();
	  	    ArrayList<AbstractLTLProvider> allWrongTimedRUCA = new ArrayList<>();
	  	    List<AbstractLTLProvider> refinedEntrys = dataController.getModel().getAllRefinedRules();
	  	    ArrayList<ControlActionEntry> allCAEntrys = new ArrayList<>();
	  	    allCAEntrys.addAll(dataController.getDependenciesIFProvided());
	  	    allCAEntrys.addAll(dataController.getDependenciesNotProvided());
	  	    
	  	    for (IControlAction ca : dataController.getModel().getAllControlActions()) {
	  	    	if(isFiltered(ca.getTitle(), CA)){
	  	    		continue;
	  	    	}
	  	    	allNotProvidedRUCA.clear();
	  	    	allProvidedRUCA.clear();
	  	    	allWrongTimedRUCA.clear();
	  	    	
	  			for (AbstractLTLProvider provider : refinedEntrys) {
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

					AbstractLTLProvider notGivenUca = null;
					AbstractLTLProvider incorrectUca = null;
					AbstractLTLProvider timingUca = null;
					boolean addRow = false;
					
					if (allNotProvidedRUCA.size() > i
							&& !isFiltered(allNotProvidedRUCA.get(i).getRefinedUCA(), UCA)) {
						addRow=true;
						notGivenUca = allNotProvidedRUCA.get(i);
						addRUCA(idRow, ucaRow, linkRow,notGivenUca);
					}else {
						// add placeholders
						idRow.addCell(new GridCellBlank());
						ucaRow.addCell(new GridCellBlank());
						linkRow.addCell(new GridCellBlank());
					}

					if (allProvidedRUCA.size() > i
							&& !isFiltered(allProvidedRUCA.get(i).getRefinedUCA(), UCA)) {
						addRow=true;
						incorrectUca = allProvidedRUCA.get(i);
						addRUCA(idRow, ucaRow, linkRow, incorrectUca);
					}else {
						// add placeholders
						idRow.addCell(new GridCellBlank());
						ucaRow.addCell(new GridCellBlank());
						linkRow.addCell(new GridCellBlank());
					}

					if (allWrongTimedRUCA.size() > i
							&& !isFiltered(allWrongTimedRUCA.get(i).getRefinedUCA(), UCA)) {
						addRow=true;
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
					
					if(addRow){
						controlActionRow.addChildRow(idRow);
						controlActionRow.addChildRow(ucaRow);
						controlActionRow.addChildRow(linkRow);
					}
					
				}
				if(localRows > 0){
					this.grid.addRow(controlActionRow);
				}
			}
	  	    this.grid.reloadTable();
	  	    this.grid.resizeRows();
	  	    
	}

	private void addRUCA(GridRow idRow,GridRow ucaRow,GridRow linkRow,AbstractLTLProvider provider){
		String links = "";
		
		for (IUCAHazLink ucaLink: dataController.getModel().getAllUCALinks()) {
			if(provider.getUCALinks() != null && provider.getUCALinks().contains(ucaLink.getUnsafeControlActionId())){
				links = links.concat("[H-"+dataController.getModel().getHazard(ucaLink.getHazardId()).getNumber()+"],");
			}
		}
		if(links.isEmpty()){
			links = "";
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
		ObserverValue type = (ObserverValue) updatedValue;
		switch (type) {
			case COMBINATION_STATES:
			case Extended_DATA:
				if (!this.grid.getGrid().isDisposed() && dataModelController instanceof DataModelController) {
					this.grid.clearRows();
					this.fillTable(((DataModelController)dataModelController).getAllControlActionsU());
					this.grid.reloadTable();
				}
		default:
			break;
		}
	}
	
	@Override
	public void dispose() {
		this.dataController.deleteObserver(this);
	}

}
