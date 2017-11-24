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

import xstampp.ui.editors.StandartEditorPart;
import xstampp.util.IUndoCallback;

public class UndoRedoService extends AbstractSourceProvider implements IPartListener, IUndoRedoProvider {

  private Map<String, IUndoRedoProvider> providerToEditorIds;
  private IUndoRedoProvider currentProvider;

  public UndoRedoService() {
    this.currentProvider = new DummyUndoRedoProvider();
    this.providerToEditorIds = new HashMap<>();
  }

  @Override
  public void startRecord() {
    this.currentProvider.startRecord();
  }

  @Override
  public List<IUndoCallback> getRecord() {
    return this.currentProvider.getRecord();
  }

  @Override
  public boolean pushRecord() {
    return this.currentProvider.pushRecord();
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
    if (part instanceof StandartEditorPart) {
      String editorId = ((StandartEditorPart) part).getId();
      this.providerToEditorIds.putIfAbsent(editorId, new UndoRedoInstance(this));
      this.currentProvider = this.providerToEditorIds.get(editorId);
      this.currentProvider.activate();
    }
  }

  @Override
  public void partDeactivated(IWorkbenchPart part) {
    fireSourceChanged(ISources.WORKBENCH, CAN_REDO, false);
    fireSourceChanged(ISources.WORKBENCH, CAN_UNDO, false);
    this.currentProvider = new DummyUndoRedoProvider();
  }

  @Override
  public void partBroughtToTop(IWorkbenchPart part) {
  }

  @Override
  public void partClosed(IWorkbenchPart part) {
  }

  @Override
  public void partOpened(IWorkbenchPart part) {

  }

  @Override
  public void activate() {
  }
}
