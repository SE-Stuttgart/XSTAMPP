package xstpa.ui;

import java.util.ArrayList;
import java.util.List;

import xstampp.astpa.haz.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.controlaction.IValueCombie;
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
		for (IControlAction action : controlActions) {
			for(IValueCombie combie : ((DataModelController) getDataModel()).getValuesWhenCANotProvided(action.getId())){
				
			}
		}
	}
	public void refreshTable() {
  	    ArrayList<ControlActionEntry> allCAEntrys = new ArrayList<>();
  	    allCAEntrys.addAll(dataController.getDependenciesIFProvided());
  	    allCAEntrys.addAll(dataController.getDependenciesNotProvided());
  	    
  	    for (ControlActionEntry caEntry : allCAEntrys) {
			for(ProcessModelVariables variable : caEntry.getContextTableCombinations()){
				RefinedSafetyEntry rsEntry = null;
				if(variable.getHAnytime()){
					rsEntry = RefinedSafetyEntry.getAnytimeEntry(variable,dataController.getModel());
				}
				if(variable.getHEarly()){
					rsEntry = RefinedSafetyEntry.getTooEarlyEntry(variable,dataController.getModel());
				}
				if(variable.getHLate()){
					rsEntry = RefinedSafetyEntry.getTooLateEntry(variable,dataController.getModel());
				}
				if(variable.getHazardous() && rsEntry == null){
					rsEntry = RefinedSafetyEntry.getNotProvidedEntry(variable,dataController.getModel());
				}
			}
		}
  	   
  	    
	}
}
