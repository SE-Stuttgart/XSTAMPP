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

import java.util.UUID;

import org.eclipse.jface.wizard.IWizardPage;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public interface IExportPage extends IWizardPage {

  /**
   * @return The path as uri
   */
  String getExportPath();

  /**
   * 
   * @author Lukas Balzer
   * 
   * @return whether the table shall be exported as a single image or in
   *         multiple images
   */
  public boolean asOne();

  /**
   * @return the id for the currently selected project or null if the chooser
   *         has not be instantiated
   */
  public UUID getProjectID();

  /**
   * @return the pageFormat
   */
  public String getPageFormat();

  /**
   * @return the contentSize
   */
  public int getContentSize();

  /**
   * @return the headSize
   */
  public int getHeadSize();

  /**
   * @return the titleSize
   */
  public int getTitleSize();
}
