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
package xstpapriv.ui.tables;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;

import xstampp.ui.menu.file.commands.CommandState;
import xstpapriv.model.XSTPADataController;

public abstract class AbstractTableComposite extends Composite implements Observer{

	protected XSTPADataController dataController;
	private boolean isActiv;
	

	public AbstractTableComposite(Composite parent) {
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
	public void setController(XSTPADataController controller) {
		if(dataController != null){
			dataController.deleteObserver(this);
		}
		dataController = controller;
		dataController.addObserver(this);
		refreshTable();
	}
}
