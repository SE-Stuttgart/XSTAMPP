/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.util.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IWorkbenchPart;

import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.IGraphicalEditor;
import xstampp.ui.editors.StandartEditorPart;
import xstampp.util.IUndoCallback;

public class UndoRedoService extends AbstractSourceProvider
    implements IPartListener, IUndoRedoProvider {

  private Map<String, IUndoRedoProvider> providerToEditorIds;
  private IUndoRedoProvider currentProvider;
  private String editorId;
  private boolean hasRecordDummy = false;

  public UndoRedoService() {
    this.currentProvider = new DummyUndoRedoProvider();
    this.providerToEditorIds = new HashMap<>();
  }

  public void startRecord(boolean useDummyProvider) {
    if (useDummyProvider) {
      this.currentProvider = new UndoRedoInstance(this);
      this.hasRecordDummy = true;
    }
    startRecord();
  }

  @Override
  public void startRecord() {
    this.currentProvider.startRecord();
  }

  @Override
  public List<IUndoCallback> getRecord() {
    List<IUndoCallback> record = this.currentProvider.getRecord();
    if (hasRecordDummy) {
      this.currentProvider = new DummyUndoRedoProvider();
      this.hasRecordDummy = false;
    }
    return record;
  }

  @Override
  public boolean pushRecord() {
    boolean pushRecord = this.currentProvider.pushRecord();
    if (hasRecordDummy) {
      this.currentProvider = new DummyUndoRedoProvider();
      this.hasRecordDummy = false;
    }
    return pushRecord;
  }

  @Override
  public IUndoCallback push(IUndoCallback callback) {
    return this.currentProvider.push(callback);
  }

  @Override
  public IUndoCallback undo() {
    return this.currentProvider.undo();
  }

  @Override
  public IUndoCallback redo() {
    return this.currentProvider.redo();
  }

  @Override
  public IUndoCallback[] getUndoStack() {
    return this.currentProvider.getUndoStack();
  }

  @Override
  public IUndoCallback[] getRedoStack() {
    return this.currentProvider.getRedoStack();
  }

  @Override
  public void dispose() {
    this.providerToEditorIds.values().forEach((provider) -> provider.dispose());
  }

  @Override
  public Map<String, Boolean> getCurrentState() {
    return this.currentProvider.getCurrentState();
  }

  @Override
  public String[] getProvidedSourceNames() {
    return new String[] { CAN_UNDO, CAN_REDO };
  }

  public void triggerSourceChanged(int sourcePriority, String sourceName, boolean sourceValue) {
    fireSourceChanged(sourcePriority, sourceName, sourceValue);
  }

  @Override
  public void partActivated(IWorkbenchPart part) {
  }

  @Override
  public void partDeactivated(IWorkbenchPart part) {
  }

  @Override
  public void partBroughtToTop(IWorkbenchPart part) {
    if (part instanceof IGraphicalEditor) {
      editorId = ((StandartEditorPart) part).getId();
      this.providerToEditorIds.putIfAbsent(editorId, new GraphicalEditorUndoProvider(this, (IGraphicalEditor) part));
      this.currentProvider = this.providerToEditorIds.get(editorId);
      ((GraphicalEditorUndoProvider) this.currentProvider)
          .setCommandStack(((IGraphicalEditor) part).getEditDomain().getCommandStack());
      ProjectManager.getLOGGER().debug("Undo Provider activated for " + editorId);
      this.currentProvider.activate();
    } else if (part instanceof StandartEditorPart) {
      editorId = ((StandartEditorPart) part).getId();
      this.providerToEditorIds.putIfAbsent(editorId, new UndoRedoInstance(this));
      this.currentProvider = this.providerToEditorIds.get(editorId);
      ProjectManager.getLOGGER().debug("Undo Provider activated for " + editorId);
      this.currentProvider.activate();
    }
  }

  @Override
  public void partClosed(IWorkbenchPart part) {
    try {
      if (part instanceof StandartEditorPart
          && ((StandartEditorPart) part).getId().equals(editorId)) {
        fireSourceChanged(ISources.WORKBENCH, CAN_REDO, false);
        fireSourceChanged(ISources.WORKBENCH, CAN_UNDO, false);
        ProjectManager.getLOGGER().debug("Undo provider disabled");
        this.currentProvider = new DummyUndoRedoProvider();
      }
    } catch (Exception exc) {
      exc.fillInStackTrace();
    }
  }

  @Override
  public void partOpened(IWorkbenchPart part) {

  }

  @Override
  public void activate() {
    this.currentProvider.activate();
  }
}
