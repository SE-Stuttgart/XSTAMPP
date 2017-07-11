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
package xstampp.stpapriv.wizards.pdf;

import messages.Messages;
import xstampp.stpapriv.Activator;
import xstampp.stpapriv.messages.SecMessages;
import xstampp.stpapriv.ui.sds.CSCView;
import xstampp.stpapriv.wizards.AbstractExportWizard;
import xstampp.ui.wizards.TableExportPage;

public class CSCPDFWizard extends AbstractExportWizard {

	public CSCPDFWizard() {
		super(CSCView.ID);
		String[] filters = new String[] { "*.pdf" }; //$NON-NLS-1$ 
		this.setExportPage(new TableExportPage(filters,
				SecMessages.CorrespondingSecurityConstraints + Messages.AsPDF, Activator.PLUGIN_ID));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopCorrespondingSafetyConstraints.xsl", false, SecMessages.CorrespondingSecurityConstraints, false); ////$NON-NLS-1$
	}
}
