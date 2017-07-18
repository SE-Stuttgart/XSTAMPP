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
package xstampp.stpasec.wizards.stepImages;

import messages.Messages;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.stpasec.Activator;
import xstampp.stpasec.ui.unsecurecontrolaction.UnsecureControlActionsView;
import xstampp.ui.wizards.TableExportPage;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class UCATableExportWizard extends AbstractExportWizard {
	TableExportPage exportPage;

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public UCATableExportWizard() {
		super(UnsecureControlActionsView.ID);
		String[] filters = new String[] {"*.png" ,"*.bmp"}; //$NON-NLS-1$
		this.exportPage = new TableExportPage(filters,
				Messages.ExportPreferences + Messages.AsImage, Activator.PLUGIN_ID);
		this.setExportPage(this.exportPage);
	}

	@Override
	public void addPages() {
		this.addPage(this.exportPage);
	}

	@Override
	public boolean performFinish() {

		return this.performXSLExport(
				"/fopuca.xsl", false, "", false); //$NON-NLS-1$
	}
}
