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

import java.io.File;

import org.apache.log4j.Logger;

import messages.Messages;
import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;

/**
 * An abstarct definition of a load job used by Xstampp to load all projects in
 * the workspace
 *
 * @author Lukas Balzer
 *
 */
public abstract class AbstractLoadJob extends XstamppJob {
  private File file;
  private final Logger log = ProjectManager.getLOGGER();
  private IDataModel controller;
  private File saveFile;

  /**
   *
   * @author Lukas Balzer
   *
   */
  public AbstractLoadJob() {
    super(Messages.loadHaz);
  }

  /**
   * @return the controller
   */
  public IDataModel getController() {
    return this.controller;
  }

  /**
   * @param controller
   *          the controller to set
   */
  public void setController(IDataModel controller) {
    this.controller = controller;
  }

  /**
   * @return the file
   */
  public File getFile() {
    return this.file;
  }

  /**
   * @return the file
   */
  public File getSaveFile() {
    return this.saveFile;
  }

  /**
   * @return the log
   */
  public Logger getLog() {
    return this.log;
  }

  /**
   *
   * @author Lukas Balzer
   *
   * @param saveFile
   *          the FIle in which the loaded file should than be saved
   */
  public void setSaveFile(String saveFile) {
    this.saveFile = new File(saveFile);
  }

  /**
   *
   * @author Lukas Balzer
   *
   * @param file
   *          the file which is to be loaded
   */
  public void setFile(String file) {
    this.file = new File(file);
  }

}