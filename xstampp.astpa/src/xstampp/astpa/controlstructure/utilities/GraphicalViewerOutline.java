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
package xstampp.astpa.controlstructure.utilities;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import xstampp.astpa.controlstructure.CSAbstractEditor;

public class GraphicalViewerOutline extends ViewPart implements IPartListener {

  private ScrollableThumbnail thumbnail;
  private LightweightSystem lws;
  private Canvas canvas;


  @Override
  public void setFocus() {
  }

  @Override
  public void createPartControl(Composite parent) {
    this.canvas = new Canvas(parent, SWT.BORDER);
    this.lws = new LightweightSystem(this.canvas);

    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addPartListener(this);
  }

  @Override
  public void dispose() {
    if (this.thumbnail != null) {
      this.thumbnail = null;
    }
    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().removePartListener(this);

  }

  @Override
  public void partActivated(IWorkbenchPart part) {

    if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .getActiveEditor() instanceof CSAbstractEditor) {
      CSAbstractEditor editor = (CSAbstractEditor) PlatformUI.getWorkbench()
          .getActiveWorkbenchWindow().getActivePage().getActiveEditor();
      this.thumbnail = new ScrollableThumbnail();
      this.thumbnail.setViewport((Viewport) ((ScalableRootEditPart) editor.getGraphicalViewer()
          .getRootEditPart()).getFigure());
      RectangleFigure f = new RectangleFigure();
      f.setBounds(((ScalableRootEditPart) editor.getGraphicalViewer().getRootEditPart())
          .getLayer(LayerConstants.PRINTABLE_LAYERS).getBounds());
      this.thumbnail.setSource(f);

      this.lws.setContents(this.thumbnail);
    } else {
      this.lws.setContents(new RectangleFigure());
      this.thumbnail = null;
    }
  }

  @Override
  public void partBroughtToTop(IWorkbenchPart part) {
  }

  @Override
  public void partClosed(IWorkbenchPart part) {
  }

  @Override
  public void partDeactivated(IWorkbenchPart part) {
  }

  @Override
  public void partOpened(IWorkbenchPart part) {
  }
}
