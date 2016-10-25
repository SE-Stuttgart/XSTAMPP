/*******************************************************************************
 * Copyright (c) 2013 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
 * Grahovac, Jarkko Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian
 * Sieber, Fabian Toth, Patrick Wickenhäuser, Aliaksei Babkovich, Aleksander
 * Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.ui.menu.file.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import messages.Messages;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import xstampp.Activator;
import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.navigation.IProjectSelection;

/**
 * Class that stores whether the save menu should be enabled or not
 * 
 * @author Fabian Toth
 * 
 */
public class CommandState extends AbstractSourceProvider implements ISelectionChangedListener, Observer {

  /**
   * The id of the state
   * 
   * @author Fabian Toth
   */
  public static final String SAVE_STATE = "xstampp.ui.menu.file.commands.savestate"; //$NON-NLS-1$
  /**
   * The id of the state
   * 
   * @author Fabian Toth
   */
  public static final String SAVE_ALL_STATE = "xstampp.ui.menu.file.commands.saveAllState"; //$NON-NLS-1$
  /**
   * The string representation of the ENABLED value
   * 
   * @author Fabian Toth
   */
  public static final String S_ENABLED = "ENABLED"; //$NON-NLS-1$
  /**
   * The string representation of the DISABLED value
   * 
   * @author Fabian Toth
   */
  public static final String S_DISABLED = "DISABLED"; //$NON-NLS-1$

  private List<Object> saveList = new ArrayList<>();

  /**
   * The id of the state
   * 
   * @author Lukas Balzer
   * @since version 2.0.0
   */
  public static final String TEXT_STATE = "xstampp.ui.menu.file.commands.textstate"; //$NON-NLS-1$

  @Override
  public void dispose() {
    // Nothing to do here
  }

  @Override
  public String[] getProvidedSourceNames() {
    return new String[] { SAVE_ALL_STATE, SAVE_STATE, TEXT_STATE };
  }

  @Override
  public Map<String, String> getCurrentState() {
    Map<String, String> map = new HashMap<>();
    if ((PlatformUI.getWorkbench() == null) || (PlatformUI.getWorkbench().getActiveWorkbenchWindow() == null)
        || (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() == null)
        || (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() == null)) {
      map.put(SAVE_ALL_STATE, S_DISABLED);
      map.put(SAVE_STATE, S_DISABLED);
      map.put(TEXT_STATE, S_DISABLED);
      return map;
    }
    return map;
  }

  @Override
  public void selectionChanged(SelectionChangedEvent arg0) {
    if (arg0.getSelection() instanceof IProjectSelection) {
      UUID id = ((IProjectSelection) arg0.getSelection()).getProjectId();
      if (ProjectManager.getContainerInstance().getUnsavedChanges(id)) {
        changeState(SAVE_STATE, S_ENABLED, null);

      } else {
        changeState(SAVE_STATE, S_DISABLED, null);
      }
    }

  }

  public void isSaveable(boolean saveState, Object saveable) {
    if (saveState) {
      changeState(SAVE_STATE, S_ENABLED, saveable);
    } else {
      changeState(SAVE_STATE, S_DISABLED, saveable);
    }
  }

  @Override
  public void update(Observable arg0, Object updatedValue) {
    IDataModel dataModel = (IDataModel) arg0;
    ObserverValue type = (ObserverValue) updatedValue;
    switch (type) {
    case UNSAVED_CHANGES:
      setStatusLine();
      if (dataModel.hasUnsavedChanges()) {
        changeState(SAVE_STATE, S_ENABLED, dataModel);
      } else {
        changeState(SAVE_STATE, S_DISABLED, dataModel);
      }
    default:
      break;
    }
  }

  private void changeState(String key, String state, Object model) {
    this.fireSourceChanged(ISources.WORKBENCH, key, state);
    if (model != null && state.equals(S_ENABLED) && !this.saveList.contains(model)) {
      this.saveList.add(model);
    } else if (model != null && state.equals(S_DISABLED)) {
      this.saveList.remove(model);
    }
    if (this.saveList.isEmpty()) {
      this.fireSourceChanged(ISources.WORKBENCH, SAVE_ALL_STATE, S_DISABLED);
    } else {
      this.fireSourceChanged(ISources.WORKBENCH, SAVE_ALL_STATE, S_ENABLED);
    }

    setStatusLine();
  }

  /**
   * updates the status line
   *
   * @author Lukas Balzer
   *
   *
   */
  public void setStatusLine() {
    Display.getDefault().syncExec(new Runnable() {

      @Override
      public void run() {
        try {
          IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();

          IStatusLineManager manager = null;
          if (part instanceof IViewPart) {
            manager = ((IViewPart) part).getViewSite().getActionBars().getStatusLineManager();
          } else if (part instanceof IEditorPart) {
            manager = ((IEditorPart) part).getEditorSite().getActionBars().getStatusLineManager();
          }
          if (!saveList.isEmpty() && manager != null) {

            Image image = Activator.getImageDescriptor("/icons/statusline/warning.png").createImage(); //$NON-NLS-1$
            manager.setMessage(image, Messages.ThereAreUnsafedChanges);
          } else if (manager != null) {
            manager.setMessage(null);
          }
        } catch (SWTError e) {
          ProjectManager.getLOGGER().debug("Cannot write on the status line"); //$NON-NLS-1$
        } catch (NullPointerException e) {

        }
      }
    });

  }
}
