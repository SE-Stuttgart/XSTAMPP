package xstpa.ui.tables;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;

import xstampp.ui.menu.file.commands.CommandState;
import xstpa.model.XSTPADataController;

public abstract class AbstractTableComposite extends Composite implements Observer{

	protected XSTPADataController dataController;
	private boolean isActiv;
	

	AbstractTableComposite(Composite parent) {
		super(parent,SWT.BORDER);
	}
	
	public final void activateTable(){
		if(!isActiv){
			activate();
			this.isActiv = true;
		}
	}
	
	public final void deactivateTable() {
		this.isActiv = false;
	}
	protected abstract void activate();
	
	public abstract boolean refreshTable();

	protected void writeStatus(String status){
		if(status == null){
			// Enable the save entries in the menu
			ISourceProviderService sourceProviderService = (ISourceProviderService) PlatformUI
					.getWorkbench().getService(ISourceProviderService.class);
			((CommandState) sourceProviderService
					.getSourceProvider(CommandState.SAVE_STATE)).setStatusLine();
		}else{
			IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
			if(part instanceof IViewPart){
				((IViewPart) part).getViewSite().getActionBars().getStatusLineManager().setMessage(status);
			}
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		
		new Runnable() {
			
			@Override
			public void run() {
				if(isActiv && !refreshTable()){
					dataController.getModel().deleteObserver(AbstractTableComposite.this);
				}
			}
		}.run();
		
		
	}

	public void setController(XSTPADataController controller) {
		if(dataController != null){
			dataController.deleteObserver(this);
		}
		dataController = controller;
		dataController.addObserver(this);
		refreshTable();
	}
}
