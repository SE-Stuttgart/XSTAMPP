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
package xstampp.util;

import org.eclipse.core.commands.AbstractParameterValueConverter;
import org.eclipse.core.commands.ParameterValueConversionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.wizards.IWizardDescriptor;

/**
 * this converter can convert a registered wizard id into an IWorkbenshWizard
 * 
 * @author Lukas Balzer
 * @since Version 2.0.0
 *
 */
public class WizardParameterConverter extends AbstractParameterValueConverter {

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
   */
  @Override
  public Object convertToObject(String parameterValue) throws ParameterValueConversionException {
    // the method searches for for export, import and news wizards
    IWorkbenchWizard wizard = null;

    IWizardDescriptor wizardDescriptor = PlatformUI.getWorkbench().getExportWizardRegistry().findWizard(parameterValue);
    if (wizardDescriptor == null) {
      wizardDescriptor = PlatformUI.getWorkbench().getNewWizardRegistry().findWizard(parameterValue);
    }
    if (wizardDescriptor == null) {
      wizardDescriptor = PlatformUI.getWorkbench().getImportWizardRegistry().findWizard(parameterValue);
    }

    if (wizardDescriptor == null) {
      throw new ParameterValueConversionException(parameterValue + " is no valid wizard id!");
    }
    try {
      wizard = wizardDescriptor.createWizard();
    } catch (CoreException e) {
      throw new ParameterValueConversionException(e.getMessage());
    }

    return wizard;
  }

  @Override
  public String convertToString(Object parameterValue) throws ParameterValueConversionException {
    // TODO Auto-generated method stub
    return null;
  }

}
