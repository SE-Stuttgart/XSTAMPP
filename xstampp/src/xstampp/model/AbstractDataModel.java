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
package xstampp.model;

import java.util.Observable;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;

import xstampp.ui.menu.file.commands.CommandState;

public abstract class AbstractDataModel extends Observable implements IDataModel {
  /**
   * Shows if there are unsaved changes or not
   */
  private boolean unsavedChanges;

  public AbstractDataModel() {
    
    // Enable the save entries in the menu
    ISourceProviderService sourceProviderService = (ISourceProviderService) PlatformUI.getWorkbench()
        .getService(ISourceProviderService.class);
    CommandState saveStateService = (CommandState) sourceProviderService.getSourceProvider(CommandState.SAVE_STATE);
    addObserver(saveStateService);
    this.unsavedChanges = false;
  }

  @Override
  public boolean hasUnsavedChanges() {
    return this.unsavedChanges;
  }

  /**
   * Sets that there are no unsaved changes
   * 
   * @author Fabian Toth
   */
  @Override
  public void setStored() {
    this.unsavedChanges = false;
    updateValue(ObserverValue.SAVE);
  }

  protected void setUnsavedAndChanged(ObserverValue value) {
    this.updateValue(value);
    setUnsavedAndChanged();

  }

  @Override
  public void setUnsavedAndChanged() {
    this.unsavedChanges = true;
    this.updateValue(ObserverValue.UNSAVED_CHANGES);
  }

  @Override
  public void updateValue(ObserverValue value) {
    this.setChanged();
    this.notifyObservers(value);
  }

}
