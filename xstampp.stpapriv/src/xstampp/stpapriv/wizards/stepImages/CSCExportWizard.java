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
package xstampp.stpapriv.wizards.stepImages;

import messages.Messages;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.stpapriv.Activator;
import xstampp.stpapriv.messages.PrivMessages;
import xstampp.stpapriv.ui.sds.CSCView;
import xstampp.ui.wizards.TableExportPage;

public class CSCExportWizard extends AbstractExportWizard {

	public CSCExportWizard() {
		super(CSCView.ID);
		String[] filters = new String[] {"*.png" ,"*.bmp"}; //$NON-NLS-1$ //$NON-NLS-2$ 
		this.setExportPage(new TableExportPage(filters,
				PrivMessages.CorrespondingSecurityConstraints + Messages.AsImage, Activator.PLUGIN_ID));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopCorrespondingSafetyConstraints.xsl", false, "", false); ////$NON-NLS-1$
	}
}

