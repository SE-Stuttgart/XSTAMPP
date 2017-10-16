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
package xstampp.astpa.wizards.pdf;

import messages.Messages;
import xstampp.astpa.Activator;
import xstampp.astpa.ui.acchaz.HazardsView;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.ui.wizards.TableExportPage;

public class HazardsPDFWizard extends AbstractExportWizard {

  public HazardsPDFWizard() {
    super(HazardsView.ID);
    String[] filters = new String[] { "*.pdf" }; //$NON-NLS-1$
    this.setExportPage(new TableExportPage(filters,
        Messages.Hazards + Messages.AsPDF, Activator.PLUGIN_ID));
  }

  @Override
  public boolean performFinish() {
    return this.performXSLExport(
        "/fopHazards.xsl", false, Messages.CorrespondingSafetyConstraints, false); ////$NON-NLS-1$
  }
}
