package xstampp.usermanagement.ui.contribution;

import java.io.File;
import java.util.Observer;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.jobs.Job;

import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.usermanagement.api.IUserProject;
import xstampp.usermanagement.api.IUserSystem;

public class EmptyProject implements IUserProject {

  @Override
  public void addObserver(Observer observer) {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteObserver(Observer observer) {
    // TODO Auto-generated method stub

  }

  @Override
  public Job doSave(File file, Logger log, boolean isUIcall) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int countObservers() {
    // TODO Auto-generated method stub
    return 0;
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
  public void updateValue(ObserverValue value) {
    // TODO Auto-generated method stub

  }

  @Override
  public String getProjectName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setStored() {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean hasUnsavedChanges() {
    // TODO Auto-generated method stub
    return false;
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

  @Override
  public void setUnsavedAndChanged() {
    // TODO Auto-generated method stub

  }

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
  public UUID getProjectId() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IUserSystem loadUserSystem() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <T> T getAdapter(Class<T> clazz) {
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

}
