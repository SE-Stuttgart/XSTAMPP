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
package xstampp.astpa.wizards.stepData;

import messages.Messages;
import xstampp.astpa.Activator;
import xstampp.astpa.ui.sds.ControlActionView;
import xstampp.astpa.util.jobs.ICSVExportConstants;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.ui.wizards.CSVExportPage;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class ControlActionsWizard extends AbstractExportWizard {

  /**
   * 
   * @author Lukas Balzer
   * 
   */
  public ControlActionsWizard() {
    super(ControlActionView.ID);
    String[] filters = new String[] { "*.csv" }; //$NON-NLS-1$
    this.setExportPage(new CSVExportPage(filters, Messages.ControlAction + Messages.AsDataSet,
        Activator.PLUGIN_ID));
  }

  @Override
  public boolean performFinish() {
    return this.performCSVExport(ICSVExportConstants.CONTROL_ACTION);
  }

}
