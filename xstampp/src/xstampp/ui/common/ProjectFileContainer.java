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

package xstampp.ui.common;

import java.io.File;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.preference.IPreferenceStore;

import xstampp.Activator;
import xstampp.model.IDataModel;
import xstampp.preferences.IPreferenceConstants;

/**
 * This class provides fields and methods to store, rename, and delete project
 * data.
 * 
 * @author Lukas Balzer
 * @since 2.1.0
 */
public class ProjectFileContainer implements Comparable<ProjectFileContainer> {

  private IDataModel controller;
  private String extension;
  private String folderPath;
  private boolean lock;

  /**
   * constructs an instance of {@link ProjectFileContainer} which contains the
   * extension the projectName and the given controller as fields which can
   * later be easy changed..
   * 
   * @param controller
   *          the dataMdoel of the project containing all data which is than
   *          stored in the project file.
   * @param filePath
   *          the path which will be separated into an array where each element
   *          is Divided by one dot in the filePath, the extension and fileName
   *          will be defined by the last and and second last field in the
   *          array.
   */
  public ProjectFileContainer(IDataModel controller, String filePath) {
    this.controller = controller;
    File projectFile = new File(filePath);
    this.folderPath = projectFile.getParent();
    String[] dotSeperatedPath = projectFile.getName().split("\\.");
    Assert.isTrue(dotSeperatedPath.length == 2);
    this.extension = dotSeperatedPath[1];
    this.controller.setProjectName(dotSeperatedPath[0]);
    this.lock = false;
  }

  /**
   * @return the projectName
   */
  public String getProjectName() {
    return this.controller.getProjectName();
  }

  /**
   * @param projectName
   *          the projectName to set
   */
  public void setProjectName(String projectName) {
    this.controller.setProjectName(projectName);
  }

  /**
   * @return the extension
   */
  public String getExtension() {
    return extension;
  }

  /**
   * @param extension
   *          the extension to set
   */
  public void setExtension(String extension) {
    this.extension = extension;
  }

  /**
   * @return the path of the file on the file system
   */
  public String getPath() {
    return folderPath + File.separator + getProjectName() + "." + extension; //$NON-NLS-1$
  }

  public File getProjectFile() {
    return new File(getPath());
  }

  /**
   * @param path
   *          the path to set
   */
  public void setPath(String path) {
    this.folderPath = path;
  }

  /**
   * @return the controller
   */
  public IDataModel getController() {
    return controller;
  }

  @Override
  public int compareTo(ProjectFileContainer other) {
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    int extSortPref = store.getInt(IPreferenceConstants.NAVIGATION_EXTENSION_SORT);
    int nameSortPref = store.getInt(IPreferenceConstants.NAVIGATION_NAME_SORT);

    int extCompare = extSortPref * getExtension().compareTo(other.getExtension());
    if (extCompare == 0) {
      return nameSortPref * getProjectName().compareTo(other.getProjectName());
    }
    return extCompare;
  }

  /**
   * @return the lock
   */
  public boolean isLock() {
    return lock && !this.getController().hasUnsavedChanges();
  }

  /**
   * @param lock
   *          the lock to set
   */
  public void setLock(boolean lock) {
    this.lock = lock;
  }

}
