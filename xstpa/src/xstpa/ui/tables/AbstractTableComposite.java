package xstpa.ui.tables;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

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
		IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
		if(part instanceof IViewPart){
			((IViewPart) part).getViewSite().getActionBars().getStatusLineManager().
									setMessage(status);
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
