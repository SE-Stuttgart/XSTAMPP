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
package xstampp.ui.wizards;

import messages.Messages;

import org.eclipse.swt.events.ModifyListener;

/**
 * Creates a Page which collects basic informations about formatting the table
 * Export
 * 
 * @author Lukas Balzer
 * 
 */
public class TableExportPage extends PdfExportPage implements ModifyListener {

  /**
   * 
   * @author Lukas Balzer
   * @param filters
   *          the file extensions, which shall be excepted by in the dialog
   * @param pageName
   *          the Name of this page, that is displayed in the header of the
   *          wizard
   * @param pluginID
   *          TODO
   */
  public TableExportPage(String[] filters, PDFExportConfiguration config, String pluginID) {
    super(config, pluginID);
    setFilterExtensions(filters, filters);
    setShowCompanyFields(false);
    setShowDecorateCSButton(false);
    this.setDescription(Messages.SetValuesForTheExportFile);
  }

  /**
   * 
   * @author Lukas Balzer
   * @param filters
   *          the file extensions, which shall be excepted by in the dialog
   * @param pageName
   *          the Name of this page, that is displayed in the header of the
   *          wizard
   * @param pluginID
   *          TODO
   */
  public TableExportPage(String[] filters, String pageName, String pluginID) {
    super(new PDFExportConfiguration(pageName, pageName), pluginID);
    setFilterExtensions(filters, filters);
    setShowCompanyFields(false);
    this.setDescription(Messages.SetValuesForTheExportFile);
  }

}
