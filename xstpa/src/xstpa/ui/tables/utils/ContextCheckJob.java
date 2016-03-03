package xstpa.ui.tables.utils;

import java.util.List;
import java.util.Observable;
import java.util.UUID;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import xstampp.util.XstamppJob;
import xstpa.model.ContextTableCombination;
import xstpa.model.XSTPADataController;

public class ContextCheckJob extends XstamppJob {

	private int conflictCounter;
	private XSTPADataController dataController;

	public ContextCheckJob(String name,XSTPADataController dataController) {
		super(name);
		this.dataController = dataController;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		
		//verfies if there are no conflicts between CAProvided and not provided
		UUID caID= dataController.getLinkedCAE().getId();
		List<ContextTableCombination> notProvidedContext =dataController.getControlActionEntry(false, caID).getContextTableCombinations(false);
		List<ContextTableCombination> providedContext =dataController.getControlActionEntry(true, caID).getContextTableCombinations(false);
		conflictCounter = 0;
		boolean combiesFit;
		//initially all stored contextTableCombinations are set too non conflicting
		for (ContextTableCombination NPCombie : notProvidedContext) {
			NPCombie.setConflict(false);
		}
		for (ContextTableCombination pCombie : providedContext) {
			pCombie.setConflict(false);
		}
		
		for (ContextTableCombination NPCombie : notProvidedContext) {
			for (ContextTableCombination pCombie : providedContext) {
				if (monitor.isCanceled()) {
					return Status.CANCEL_STATUS;				
				}
				if(pCombie.getConflict() || NPCombie.getConflict()){
					continue;
				}
				//check if the combies are defining a combination of the same values
				combiesFit = NPCombie.getValueIDTOVariableIdMap().equals(pCombie.getValueIDTOVariableIdMap());
				
				//if the combies fit than it is checked if both cases are hazardous
				if(combiesFit){
					if(NPCombie.getHazardous() && pCombie.getHAnytime()){
						conflictCounter++;
						NPCombie.setConflict(true);
						pCombie.setConflict(true);
						break;
					}else{
						NPCombie.setConflict(false);
						pCombie.setConflict(false);
					}
				}else{
					NPCombie.setConflict(false);
					pCombie.setConflict(false);
				}
			}
		}
   	 
		
		return Status.OK_STATUS;
	}
	
	public int getConflictCounter() {
		return this.conflictCounter;
	}

	@Override
	protected Observable getModelObserver() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
