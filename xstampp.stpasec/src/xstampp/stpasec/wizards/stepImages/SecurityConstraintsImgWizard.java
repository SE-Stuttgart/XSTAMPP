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
import xstampp.stpasec.Activator;
import xstampp.stpasec.messages.SecMessages;
import xstampp.stpasec.ui.sds.SecurityConstraintView;
import xstampp.stpasec.wizards.AbstractPrivacyExportWizard;
import xstampp.ui.wizards.TableExportPage;

public class SecurityConstraintsImgWizard extends AbstractPrivacyExportWizard {

	public SecurityConstraintsImgWizard() {
		super(SecurityConstraintView.ID);
		String[] filters = new String[] {"*.png" ,"*.bmp"}; //$NON-NLS-1$ //$NON-NLS-2$ 
		this.setExportPage(new TableExportPage(filters,
				SecMessages.SecurityConstraints + Messages.AsImage, Activator.PLUGIN_ID));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopSafetyConstraints.xsl", false, "", false); ////$NON-NLS-1$
	}
}
