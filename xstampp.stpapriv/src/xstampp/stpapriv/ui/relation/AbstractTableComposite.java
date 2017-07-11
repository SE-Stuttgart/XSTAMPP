package xstampp.stpapriv.ui.relation;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;

import xstampp.stpapriv.model.relation.UnsafeUnsecureController;
import xstampp.ui.menu.file.commands.CommandState;

public abstract class AbstractTableComposite extends Composite implements Observer{

	protected UnsafeUnsecureController dataController;
	private boolean isActiv;
	

	protected AbstractTableComposite(Composite parent) {
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
					dataController.deleteObserver(AbstractTableComposite.this);
				}
			}
		}.run();
		
		
	}

	@Override
	public void dispose() {
	  if(dataController != null){
	    dataController.deleteObserver(this);
	  }
	  super.dispose();
	}
	public void setController(UnsafeUnsecureController controller) {
		if(dataController != null){
			dataController.deleteObserver(this);
		}
		dataController = controller;
		dataController.addObserver(this);
		refreshTable();
	}
}
