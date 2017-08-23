/*******************************************************************************
 * Copyright (C) 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lukas Balzer- initial API and implementation
 ******************************************************************************/
package xstampp.usermanagement.ui.contribution;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.jobs.Job;

import xstampp.model.AbstractDataModel;
import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.usermanagement.api.IUserProject;
import xstampp.usermanagement.api.IUserSystem;

import java.io.File;
import java.util.UUID;

public class EmptyProject extends AbstractDataModel implements IUserProject {

  @Override
  public IUserSystem getUserSystem() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IUserSystem createUserSystem() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IUserSystem loadUserSystem() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public UUID getExclusiveUserId() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setExclusiveUserId(UUID userId) {
    // TODO Auto-generated method stub

  }

  @Override
  public Job doSave(File file, Logger log, boolean isUIcall) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean prepareForExport() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void prepareForSave() {
    // TODO Auto-generated method stub

  }

  @Override
  public String getProjectName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public UUID getProjectId() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean setProjectName(String projectName) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void initializeProject() {
    // TODO Auto-generated method stub

  }

  @Override
  public void initializeProject(IDataModel original) {
    // TODO Auto-generated method stub

  }

  @Override
  public String getFileExtension() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getPluginID() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void lockUpdate() {
    // TODO Auto-generated method stub

  }

  @Override
  public void releaseLockAndUpdate(ObserverValue[] values) {
    // TODO Auto-generated method stub

  }

}
