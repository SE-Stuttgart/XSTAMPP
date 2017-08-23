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
package xstampp.astpa.wizards.stepImages;

import messages.Messages;
import xstampp.astpa.ui.systemdescription.SystemDescriptionView;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.SystemDescriptionExportPage;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class SystemDecriptionWizard extends AbstractExportWizard {

  /**
   * 
   * @author Lukas Balzer
   * 
   */
  public SystemDecriptionWizard() {
    super(SystemDescriptionView.ID);
    String[] filters = new String[] { "*.png", "*.bmp" }; //$NON-NLS-1$ //$NON-NLS-2$
    this.setExportPage(new SystemDescriptionExportPage(filters,
        Messages.SystemDescription + Messages.AsImage));
  }

  @Override
  public boolean performFinish() {
    return this.performXSLExport(
        "/fopSystemDescription.xsl", false, "", false); //$NON-NLS-1$
  }
}
