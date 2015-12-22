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

	AbstractTableComposite(Composite parent,XSTPADataController controller) {
		super(parent,SWT.BORDER);
		this.dataController = controller;
	}
	
	public abstract void activate();
	
	public abstract void refreshTable();

	protected void writeStatus(String status){
		IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
		if(part instanceof IViewPart){
			((IViewPart) part).getViewSite().getActionBars().getStatusLineManager().
									setMessage(status);
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
}
