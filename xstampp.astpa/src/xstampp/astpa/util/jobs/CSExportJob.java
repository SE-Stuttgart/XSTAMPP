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
package xstampp.astpa.util.jobs;

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.UUID;

import messages.Messages;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import xstampp.astpa.Activator;
import xstampp.astpa.controlstructure.CSEditor;
import xstampp.astpa.controlstructure.CSEditorWithPM;
import xstampp.astpa.controlstructure.IControlStructureEditor;
import xstampp.astpa.controlstructure.controller.editparts.RootEditPart;
import xstampp.astpa.controlstructure.controller.factorys.CSEditPartFactory;
import xstampp.astpa.controlstructure.figure.RootFigure;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;
import xstampp.ui.common.ProjectManager;
import xstampp.util.XstamppJob;

/**
 * 
 * @author Lukas Balzer
 * @since 2.0
 * 
 */
public class CSExportJob extends XstamppJob {

	private String path;
	private String editorId;
	private int imageType;
	private int imgOffset;
	private boolean showPreview;
	private boolean deco;
	private IFigure printableFigure;
	private Image srcImage;
	private IControlStructureEditorDataModel model;
	private final double factor = 5.0;
	private UUID projectID;
	private ByteArrayOutputStream outputStream;
	private float ratio;

	

	/**
	 * this constructor creates a new Job to print the current Control Structure
	 * 
	 * @author Lukas Balzer
	 * @param path
	 *            the path defined as a String
	 * @param imgOffset
	 *            the amount of pixels which are added as Border
	 * @param editorId
	 *            the editor for which the picture should be created
	 * @param projectId
	 *            the project for which the picture should be created
	 * @param showPreview
	 *            if the file should be opened after the export
	 * @param decorate
	 *            whether the structure should be printed with decoration
	 * @see GC
	 * @see ImageLoader
	 * @see SWT#IMAGE_PNG
	 * @see SWT#IMAGE_JPEG
	 */
	public CSExportJob(String path, int imgOffset, String editorId,
			UUID projectId, boolean showPreview, boolean decorate) {
		super(Messages.ExportCS);
		this.projectID=projectId;
		this.model = (IControlStructureEditorDataModel) ProjectManager
				.getContainerInstance().getDataModel(projectId);
		this.path = path;
		this.imgOffset = imgOffset;
		this.showPreview = showPreview;
		this.editorId = editorId;
		this.deco = decorate;
		if (path == null) {
			this.imageType = -1;
		} else if (path.endsWith("png")) { //$NON-NLS-1$
			this.imageType = SWT.IMAGE_PNG;
		} else if (path.endsWith("bmp")) { //$NON-NLS-1$
			this.imageType = SWT.IMAGE_BMP;
		} else if (path.endsWith("jpg")) { //$NON-NLS-1$
			this.imageType = SWT.IMAGE_JPEG;
		} else {
			this.imageType = -1;
		}
	}

	public CSExportJob(ByteArrayOutputStream stream, int imgOffset, String editorId,
			UUID projectId, boolean decorate){
		this((String)null, imgOffset, editorId, projectId, false, decorate);
		this.outputStream = stream;
		this.imageType = SWT.IMAGE_PNG;
	}
	/**
	 * this constructor creates a new Job to print the current Control Structure
	 * 
	 * @author Lukas Balzer
	 * @param path
	 *            the path defined as a String
	 * @param editorId
	 *            the editor for which the picture should be created
	 * @param projectId
	 *            the project for which the picture should be created
	 * @param imgOffset
	 *            the amount of pixels which are added as Border
	 * @param forcedDeco
	 *            this bool can be set if a decoration in the next Image is
	 *            wished
	 * @see GC
	 * @see ImageLoader
	 * @see SWT#IMAGE_PNG
	 * @see SWT#IMAGE_JPEG
	 */
	public CSExportJob(String path, String editorId, UUID projectId,
			int imgOffset, boolean forcedDeco) {
		this(path, imgOffset, editorId, projectId, false, forcedDeco);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {

		this.getPrintableRoot();

		return Status.OK_STATUS;

	}

	/**
	 * this method prints the control structure
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return
	 */
	public synchronized IStatus getPrintableRoot() {
		Runnable run = new CSImageCalculator();

		Display.getDefault().syncExec(run);
		while (this.printableFigure == null) {
			// wait 
		}

		if (this.imageType < 0) {
			return Status.CANCEL_STATUS;
		}
		Rectangle clipRectangle = getClippingRectangle();

		// this additional Image is created with the actual Bounds
		// and the first one is clipped inside the scaled image
		Image scaledImage = new Image(null, clipRectangle.width
				+ (2 * this.imgOffset), clipRectangle.height
				+ (2 * this.imgOffset));
		GC imageGC = new GC(scaledImage);
		Graphics graphics = new SWTGraphics(imageGC);
		
		if ((this.srcImage.getBounds().width >= 0)
				&& (scaledImage.getBounds().width >= 0)) {
			graphics.drawImage(this.srcImage, clipRectangle, new Rectangle(
					this.imgOffset, this.imgOffset, clipRectangle.width,
					clipRectangle.height));
		}
		ratio = (float) scaledImage.getBounds().width / scaledImage.getBounds().height;
		ImageLoader imgLoader = new ImageLoader();
		imgLoader.data = new ImageData[] { scaledImage.getImageData() };
		if(outputStream != null){
			imgLoader.save(outputStream, this.imageType);
		}else{
		
			imgLoader.save(this.path, this.imageType);
			File imageFile = new File(this.path);
			if (imageFile.exists() && this.showPreview) {
				if (Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().open(imageFile);
					} catch (IOException e) {
						return Status.CANCEL_STATUS;
					}
				}
			}
		}
		return Status.OK_STATUS;
	}

	
	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param preview
	 *            true if the file should be opened with after the export
	 */
	public void setPreview(boolean preview) {
		this.showPreview = preview;
	}

	/** 
	 * create a clip rectangle to cut the unnecessary whitespace
	 * 
	 * @author Lukas Balzer
	 *
	 * @return a rectangle which describes the bounds of the control structure
	 */
	private Rectangle getClippingRectangle() {
		boolean isFirst = true;

		Rectangle clipRectangle = new Rectangle(0,0,0,0);
		for (Object layers : this.printableFigure.getChildren()) {
			// Layer&ConnectionLayer
			for (Object part : ((IFigure) layers).getChildren()) {
				if (part instanceof RootFigure) {
					for (final Object child : ((IFigure) part).getChildren()) {
						if (child.getClass().equals(Figure.class)) {
							// skip children which are not instances of a CS
							// class
						} else if (isFirst) {
							// the first component which is found by the loop is
							// added
							// as starting Point for the rectangle
							isFirst = false;
							clipRectangle = new Rectangle(
									((IFigure) child).getBounds());
						} else {
							clipRectangle.union(((IFigure) child).getBounds());
						}
					}
				} else {
					clipRectangle.union(((IFigure) part).getBounds());
				}
			}

		}
		// cut off negative x-/y-parts
		clipRectangle.width = clipRectangle.width
				+ Math.min(0, clipRectangle.x);
		clipRectangle.height = clipRectangle.height
				+ Math.min(0, clipRectangle.y);
		// the clipRectangle is minimally located at (0,0)
		clipRectangle.x = Math.max(0, clipRectangle.x);
		clipRectangle.y = Math.max(0, clipRectangle.y);
		clipRectangle.scale(this.factor);
		if ((clipRectangle.height + clipRectangle.y) > this.srcImage
				.getBounds().height) {
			clipRectangle.height = this.srcImage.getBounds().height
					- clipRectangle.y;
		}
		if ((clipRectangle.width + clipRectangle.x) > this.srcImage.getBounds().width) {
			clipRectangle.width = this.srcImage.getBounds().width
					- clipRectangle.x;
		}
		return clipRectangle;
	}
	
	private class CSImageCalculator implements Runnable{

		@Override
		public void run() {
			Shell shell=new Shell();
			Composite canvas = new Composite(shell,33554432);
			ScrollingGraphicalViewer viewer = new ScrollingGraphicalViewer();
			if (!CSExportJob.this.editorId.equals(CSEditor.ID)
					&& !CSExportJob.this.editorId.equals(CSEditorWithPM.ID)) {
				// the editor id must be one of CSEditor or CSEditorWithPM
				return;
			}

			viewer.createControl(canvas);
			viewer.setEditPartFactory(new CSEditPartFactory(
					CSExportJob.this.model, CSExportJob.this.editorId, Activator.getDefault().getPreferenceStore()));
			viewer.setProperty(IControlStructureEditor.STEP_EDITOR,
					CSExportJob.this.editorId);

			ScalableRootEditPart rootEditPart = new ScalableRootEditPart();
			viewer.setRootEditPart(rootEditPart);
			IRectangleComponent root = CSExportJob.this.model.getRoot();

			if (root == null) {
				CSExportJob.this.model.setRoot(new Rectangle(),
						new String());
				root = CSExportJob.this.model.getRoot();
			}
			viewer.setContents(root);
			viewer.getContents().refresh();
			((RootEditPart) rootEditPart.getContents()).getFigure()
					.setDeco(CSExportJob.this.deco);

			viewer.getContents().refresh();
			
			IFigure tmpFigure = rootEditPart
					.getLayer(LayerConstants.PRINTABLE_LAYERS);
			// create a rectangle to guarantee that the src image 
			Rectangle srcRectangle = tmpFigure.getBounds();
			for (Object layers : tmpFigure.getChildren()) {
				// Layer&ConnectionLayer
				for (Object part : ((IFigure) layers).getChildren()) {
					srcRectangle.union(((IFigure) part).getBounds());
				}

			}
			
			// a plain Image is created on which we can draw any graphics
			CSExportJob.this.srcImage = new Image(null, (int) Math.max(
					CSExportJob.this.factor * tmpFigure.getBounds().width,
					1), (int) Math.max(
					CSExportJob.this.factor * tmpFigure.getBounds().height,
					1));
			GC imageGC = new GC(CSExportJob.this.srcImage);
			Graphics graphics = new SWTGraphics(imageGC);
			graphics.scale(CSExportJob.this.factor);
			tmpFigure.paint(graphics);
			CSExportJob.this.printableFigure = tmpFigure;
		}
		
	}

	@Override
	protected Observable getModelObserver() {
		return (DataModelController)ProjectManager.getContainerInstance().getDataModel(projectID);
	}
	
	@Override
	protected void canceling() {
		
		super.canceling();
	}

	/**
	 * @return the ratio
	 */
	public float getRatio() {
		return this.ratio;
	}

	/**
	 * @param ratio the ratio to set
	 */
	public void setRatio(float ratio) {
		this.ratio = ratio;
	}
}
