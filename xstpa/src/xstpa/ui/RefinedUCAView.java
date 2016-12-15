package xstpa.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import xstampp.astpa.haz.controlaction.interfaces.IControlAction;
import xstampp.astpa.haz.controlaction.interfaces.IUCAHazLink;
import xstampp.astpa.model.controlaction.interfaces.IHAZXControlAction;
import xstampp.astpa.model.interfaces.IExtendedDataModel;
import xstampp.astpa.ui.unsafecontrolaction.UnsafeControlActionsView;
import xstampp.model.AbstractLtlProvider;
import xstampp.model.IValueCombie;
import xstampp.model.ObserverValue;
import xstampp.ui.common.grid.GridCellBlank;
import xstampp.ui.common.grid.GridCellColored;
import xstampp.ui.common.grid.GridCellText;
import xstampp.ui.common.grid.GridRow;
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
	protected Map<String, Boolean> getCategories() {
		Map<String, Boolean> categories= new HashMap<>();
		categories.put(CA, false);
		categories.put(UCA, false);
		return categories;
	}
	@Override
	protected String[] getCategoryArray() {
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
			ArrayList<AbstractLtlProvider> allNotProvidedRUCA = new ArrayList<>();
	  	    ArrayList<AbstractLtlProvider> allProvidedRUCA = new ArrayList<>();
	  	    ArrayList<AbstractLtlProvider> allWrongTimedRUCA = new ArrayList<>();
	  	    List<AbstractLtlProvider> refinedEntrys = dataController.getModel().getAllRefinedRules(true);
	  	    ArrayList<ControlActionEntry> allCAEntrys = new ArrayList<>();
	  	    allCAEntrys.addAll(dataController.getDependenciesIFProvided());
	  	    allCAEntrys.addAll(dataController.getDependenciesNotProvided());
	  	    
	  	    for (IControlAction ca : dataController.getModel().getAllControlActionsU()) {
	  	    	if(isFiltered(ca.getTitle(), CA)){
	  	    		continue;
	  	    	}
	  	    	allNotProvidedRUCA.clear();
	  	    	allProvidedRUCA.clear();
	  	    	allWrongTimedRUCA.clear();
	  	    	
	  			for (AbstractLtlProvider provider : refinedEntrys) {
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

					AbstractLtlProvider notGivenUca = null;
					AbstractLtlProvider incorrectUca = null;
					AbstractLtlProvider timingUca = null;
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

	private void addRUCA(GridRow idRow,GridRow ucaRow,GridRow linkRow,AbstractLtlProvider provider){
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
	public void update(Observable IExtendedDataModel, Object updatedValue) {
		ObserverValue type = (ObserverValue) updatedValue;
		switch (type) {
			case COMBINATION_STATES:
			case Extended_DATA:
				if (!this.grid.getGrid().isDisposed() && IExtendedDataModel instanceof IExtendedDataModel) {
					this.grid.clearRows();
					this.fillTable(((IExtendedDataModel)IExtendedDataModel).getAllControlActionsU());
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
