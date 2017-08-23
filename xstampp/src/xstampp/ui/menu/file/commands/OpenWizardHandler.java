/*******************************************************************************
 * Copyright (c) 2013, 2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.ui.menu.file.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.ParameterValueConversionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.wizards.IWizardDescriptor;

/**
 * this hander provides a command that opens a wizard by its id given in the
 * plugin.xml and passed in the parameter
 * <code>xstampp.commandParameter.openwizard</code>
 * 
 * @author Lukas Balzer
 * @since 1.0
 */
public class OpenWizardHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    String wizardId = event.getParameter("xstampp.commandParameter.openwizard"); //$NON-NLS-1$

    if (wizardId == null) {
      return null;
    }
    IWorkbenchWizard wizard = null;
    try {
      wizard = (IWorkbenchWizard) convertToObject(wizardId);
    } catch (ParameterValueConversionException e) {
      e.printStackTrace();
      return null;
    }
    // call wizard
    WizardDialog dialog = new WizardDialog(PlatformUI.createDisplay().getActiveShell(), wizard);
    dialog.open();
    return null;
  }

  /**
   * expects a wizard id <br>
   * <i>note: the wizard mmust be registered in an installed plugin.xml</i>.
   * <p>
   * this method converts all import, export and newWizards
   * 
   * @param parameterValue
   *          must be a wizard id which is registered for a import, export or
   *          newWizard extension
   * @return a new instance of a IWorkbenchWizard, or null if no wizard for the
   *         given id could be found
   * @throws ParameterValueConversionException
   *           if an incorrect value has been given
   */
  public Object convertToObject(String parameterValue) throws ParameterValueConversionException {
    // the method searches for for export, import and news wizards
    IWorkbenchWizard wizard = null;
    IWizardDescriptor wizardDescriptor = PlatformUI.getWorkbench().getExportWizardRegistry()
        .findWizard(parameterValue);
    if (wizardDescriptor == null) {
      wizardDescriptor = PlatformUI.getWorkbench().getNewWizardRegistry()
          .findWizard(parameterValue);
    }
    if (wizardDescriptor == null) {
      wizardDescriptor = PlatformUI.getWorkbench().getImportWizardRegistry()
          .findWizard(parameterValue);
    }

    if (wizardDescriptor == null) {
      throw new ParameterValueConversionException(parameterValue + " is no valid wizard id!"); //$NON-NLS-1$
    }
    try {
      wizard = wizardDescriptor.createWizard();
    } catch (CoreException e) {
      throw new ParameterValueConversionException(e.getMessage());
    }

    return wizard;
  }
}
