/*******************************************************************************
 * Copyright (c) 2013 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
 * Grahovac, Jarkko Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian
 * Sieber, Fabian Toth, Patrick Wickenhäuser, Aliaksei Babkovich, Aleksander
 * Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package astpa.ui.statusline;

import java.util.Observable;

import messages.Messages;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import astpa.Activator;
import astpa.model.ObserverValue;
import astpa.model.interfaces.IDataModel;
import astpa.model.interfaces.IStatusLineDataModel;
import astpa.ui.common.IViewBase;
import astpa.ui.common.ViewContainer;

/**
 * Class for simplifying the access to the status line
 * 
 * @author Benedikt Markt, Fabian Toth
 * 
 */
public final class StatusLineManager implements IViewBase {
	
	private static StatusLineManager instance;
	
	private IStatusLineManager statusLine;
	
	private IStatusLineDataModel dataInterface;
	
	
	private StatusLineManager() {
		
	}
	
	/**
	 * Returns the only instance of StatusLineManager
	 * 
	 * @author Benedikt Markt, Fabian Toth
	 * 
	 * @return the instance
	 */
	public static StatusLineManager getInstance() {
		if (StatusLineManager.instance == null) {
			StatusLineManager.instance = new StatusLineManager();
		}
		return StatusLineManager.instance;
	}
	
	/**
	 * Changes the text on the main field of the status bar.
	 * 
	 * Get the IActionBars with editor.getEditorSite().getActionBars() or
	 * view.getViewSite().getActionBars()
	 * 
	 * @author Benedikt Markt, Fabian Toth
	 * 
	 * @param message The message to write to the Status Line
	 */
	public void setMainMessage(String message) {
		ViewContainer viewContainer =
			(ViewContainer) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(ViewContainer.ID);
		viewContainer.updateStatus(message);
	}
	
	/**
	 * Changes the text on the main field of the status bar.
	 * 
	 * Get the IActionBars with editor.getEditorSite().getActionBars() or
	 * view.getViewSite().getActionBars()
	 * 
	 * @author Benedikt Markt, Fabian Toth
	 * 
	 * @param message The message to write to the Status Line
	 * @param image the image to print to the status line
	 */
	public void setMainMessageWithImage(String message, Image image) {
		ViewContainer viewContainer =
			(ViewContainer) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(ViewContainer.ID);
		viewContainer.updateStatusWithImage(message, image);
	}
	
	/**
	 * Initializes the status line manager
	 * 
	 * @author Benedikt Markt, Fabian Toth
	 * 
	 * @param statusLineManager The IStatusLineManager of the current View
	 */
	public void initialize(IStatusLineManager statusLineManager) {
		this.statusLine = statusLineManager;
	}
	
	/**
	 * Updates the Status Line
	 * 
	 * @author Benedikt Markt, Fabian Toth
	 */
	public void update() {
		this.statusLine.update(true);
		
	}
	
	@Override
	public void setDataModelInterface(IDataModel dataInterface) {
		this.dataInterface = (IStatusLineDataModel) dataInterface;
		this.dataInterface.addObserver(this);
	}
	
	@Override
	public void update(Observable dataModelController, Object updatedValue) {
		ObserverValue type = (ObserverValue) updatedValue;
		switch (type) {
		case UNSAVED_CHANGES:
			if (this.dataInterface.hasUnsavedChanges()) {
				Image image = Activator.getImageDescriptor("/icons/statusline/warning.png").createImage(); //$NON-NLS-1$
				this.setMainMessageWithImage(Messages.ThereAreUnsafedChanges, image);
			} else {
				this.setMainMessage(null);
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public String getId() {
		return null;
	}
	
	@Override
	public String getTitle() {
		return Messages.StatusLine;
	}
	
	@Override
	public void onActivateView() {
		// Nothing to do here.
	}
	
	@Override
	public void createPartControl(Composite parent) {
		// Nothing to do here.
	}

	@Override
	public boolean triggerExport() {
		// TODO Auto-generated method stub
		return false;
	}
}
