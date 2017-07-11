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
import xstampp.stpapriv.Activator;
import xstampp.stpapriv.wizards.AbstractExportWizard;
import xstampp.ui.wizards.TableExportPage;

public class XLTLIMGWizard extends AbstractExportWizard {

	public XLTLIMGWizard() {
		super("");
		String[] filters = new String[] {"*.png" ,"*.bmp"}; //$NON-NLS-1$ 
		this.setExportPage(new TableExportPage(filters,
				Messages.LTLFormulasTable + " " + Messages.AsImage, Activator.PLUGIN_ID)); //$NON-NLS-2$
		
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport("/fopLTLPropertys.xsl", false,//$NON-NLS-1$
						Messages.LTLFormulasTable, false); 
	}
}
