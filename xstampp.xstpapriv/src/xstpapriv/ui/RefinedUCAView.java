/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstpapriv.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import org.eclipse.swt.widgets.Composite;

import messages.Messages;
import xstampp.astpa.model.controlaction.UCAHazLink;
import xstampp.astpa.model.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.interfaces.IExtendedDataModel;
import xstampp.astpa.ui.CommonGridView;
import xstampp.model.AbstractLTLProvider;
import xstampp.model.IValueCombie;
import xstampp.model.ObserverValue;
import xstampp.stpapriv.messages.PrivMessages;
import xstampp.stpapriv.ui.unsecurecontrolaction.UnsecureControlActionsView;
import xstampp.ui.common.grid.DeleteGridEntryAction;
import xstampp.ui.common.grid.GridCellBlank;
import xstampp.ui.common.grid.GridCellColored;
import xstampp.ui.common.grid.GridCellText;
import xstampp.ui.common.grid.GridRow;

public class RefinedUCAView extends CommonGridView<IExtendedDataModel> {
	

	private static final String CA="control action";
	private static final String UCA="refined privacy-compromising control actions";
  private static final String[] columns = new String[] {
      Messages.ControlAction, PrivMessages.NotGiven2,
      PrivMessages.GivenIncorrectly2, PrivMessages.WrongTiming2,
      Messages.StoppedTooSoon };
	private static final String ID = "xstpapriv.editors.refinedUCA";
	
	public RefinedUCAView() {
		setUseFilter(true);
	}
	
	@Override
	public void createPartControl(Composite parent) {
	  super.createPartControl(parent,columns);
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
		return "Refined Privacy-Compromising Control Actions";
	}
	@Override
	protected void fillTable() {
		ArrayList<AbstractLTLProvider> allNotProvidedRUCA = new ArrayList<>();
    ArrayList<AbstractLTLProvider> allProvidedRUCA = new ArrayList<>();
    ArrayList<AbstractLTLProvider> allWrongTimedRUCA = new ArrayList<>();
    List<AbstractLTLProvider> refinedEntrys = getDataModel().getExtendedDataController().getAllScenarios(true,false,false);
    
    for (IControlAction ca : getDataModel().getAllControlActionsU()) {
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
    	GridRow controlActionRow =new GridRow(columns.length);
    	controlActionRow.addCell(0,new GridCellText(ca.getTitle()));
  
  		controlActionRow.addCell(1,new GridCellColored(getGridWrapper(),
  				UnsecureControlActionsView.PARENT_BACKGROUND_COLOR));
  		controlActionRow.addCell(2,new GridCellColored(getGridWrapper(),
  				UnsecureControlActionsView.PARENT_BACKGROUND_COLOR));
  		controlActionRow.addCell(3,new GridCellColored(getGridWrapper(),
  				UnsecureControlActionsView.PARENT_BACKGROUND_COLOR));
  		controlActionRow.addCell(4,new GridCellColored(getGridWrapper(),
  				UnsecureControlActionsView.PARENT_BACKGROUND_COLOR));
  		
  		for (int i = 0; i < localRows; i++) {
  		  
  			GridRow idRow = new GridRow(columns.length,3);
  			GridRow ucaRow = new GridRow(columns.length,3);
  			GridRow linkRow = new GridRow(columns.length,3);
  
  			AbstractLTLProvider notGivenUca = null;
  			AbstractLTLProvider incorrectUca = null;
  			AbstractLTLProvider timingUca = null;
  			boolean addRow = false;
  			
  			if (allNotProvidedRUCA.size() > i
  					&& !isFiltered(allNotProvidedRUCA.get(i).getRefinedUCA(), UCA)) {
  				addRow=true;
  				notGivenUca = allNotProvidedRUCA.get(i);
  				addRUCA(1,idRow, ucaRow, linkRow,notGivenUca);
  			}else {
  				// add placeholders
  				idRow.addCell(1,new GridCellBlank(true));
  				ucaRow.addCell(1,new GridCellBlank(true));
  				linkRow.addCell(1,new GridCellBlank(true));
  			}
  
  			if (allProvidedRUCA.size() > i
  					&& !isFiltered(allProvidedRUCA.get(i).getRefinedUCA(), UCA)) {
  				addRow=true;
  				incorrectUca = allProvidedRUCA.get(i);
  				addRUCA(2,idRow, ucaRow, linkRow, incorrectUca);
  			}else {
  				// add placeholders
  				idRow.addCell(2,new GridCellBlank(true));
  				ucaRow.addCell(2,new GridCellBlank(true));
  				linkRow.addCell(2,new GridCellBlank(true));
  			}
  
  			if (allWrongTimedRUCA.size() > i
  					&& !isFiltered(allWrongTimedRUCA.get(i).getRefinedUCA(), UCA)) {
  				addRow=true;
  				timingUca = allWrongTimedRUCA.get(i);
  				addRUCA(3,idRow, ucaRow, linkRow, timingUca);
  			}else {
  				// add placeholders
  				idRow.addCell(3,new GridCellBlank(true));
  				ucaRow.addCell(3,new GridCellBlank(true));
  				linkRow.addCell(3,new GridCellBlank(true));
  			}
  			// add placeholders
  			idRow.addCell(4,new GridCellBlank(true));
  			ucaRow.addCell(4,new GridCellBlank(true));
  			linkRow.addCell(4,new GridCellBlank(true));
  			
  			if(addRow){
  				controlActionRow.addChildRow(idRow);
  				controlActionRow.addChildRow(ucaRow);
  				controlActionRow.addChildRow(linkRow);
  			}
  			
  		}
  		if(localRows > 0){
  			getGridWrapper().addRow(controlActionRow);
  		}
    }
    getGridWrapper().reloadTable();
    getGridWrapper().resizeRows();
	  	    
	}

	private void addRUCA(int index, GridRow idRow,GridRow ucaRow,GridRow linkRow,AbstractLTLProvider provider){
		String links = "";
		
		for (UCAHazLink ucaLink: getDataModel().getAllUCALinks()) {
			if(provider.getUCALinks() != null && provider.getUCALinks().contains(ucaLink.getUnsafeControlActionId())){
				links = links.concat("[V-"+getDataModel().getHazard(ucaLink.getHazardId()).getNumber()+"],");
			}
		}
		if(links.isEmpty()){
			links = "";
		}else{
			links = links.substring(0, links.length()-1);			
		}
		idRow.addCell(index,new GridCellText("RPCA1."+provider.getNumber()));
		
		GridCellText editor = new GridCellText(provider.getRefinedUCA());
		ucaRow.addCell(index,editor);
		linkRow.addCell(index,new GridCellText(links));
	}

  @Override
  protected void updateFilter() {
    reloadTable();
  }
  
	@Override
	public void update(Observable IExtendedDataModel, Object updatedValue) {
		ObserverValue type = (ObserverValue) updatedValue;
		switch (type) {
			case COMBINATION_STATES:
			case Extended_DATA:
				if (!getGridWrapper().getGrid().isDisposed() && IExtendedDataModel instanceof IExtendedDataModel) {
					this.fillTable();
				}
		default:
			break;
		}
	}
	
  @Override
  public DeleteGridEntryAction<IExtendedDataModel> getDeleteAction() {
    return null;
  }

}
