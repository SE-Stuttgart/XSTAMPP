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
import xstampp.stpasec.ui.causalfactors.SecCausalFactorsView;
import xstampp.stpasec.wizards.AbstractPrivacyExportWizard;
import xstampp.ui.wizards.TableExportPage;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class CausalFactorsTableWizard extends AbstractPrivacyExportWizard {

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public CausalFactorsTableWizard() {
		super(SecCausalFactorsView.ID);
		String[] filters = new String[] {"*.png" ,"*.bmp"}; //$NON-NLS-1$
		this.setExportPage(new TableExportPage(filters,
				Messages.CausalFactorsTable + Messages.AsImage, Activator.PLUGIN_ID));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(
				"/fopcausal.xsl", false, "", false); ////$NON-NLS-1$
	}
}
