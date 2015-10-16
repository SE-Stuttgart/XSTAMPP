package acast.jobs;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
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

import messages.Messages;
import xstampp.astpa.controlstructure.CSAbstractEditor;
import xstampp.astpa.controlstructure.controller.editparts.RootEditPart;
import xstampp.astpa.controlstructure.controller.factorys.CSEditPartFactory;
import xstampp.astpa.controlstructure.figure.RootFigure;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;
import xstampp.ui.common.ProjectManager;

/**
 *
 * @author Lukas Balzer
 * @since 2.0
 *
 */
public class CSExportJob extends Job {

	private String path;
	private String process;
	private String editorId;
	private int imageType;
	private int imgOffset;
	private boolean showPreview;
	private boolean deco;
	private IFigure printableFigure;
	private Image srcImage;
	private final double factor = 4.0;
	private UUID projectID;
	private IControlStructureEditorDataModel model;

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
	public CSExportJob(String path, int imgOffset, String editorId, UUID projectId, boolean showPreview,
			boolean decorate) {
		super(Messages.ExportCS);
		this.projectID = projectId;
		this.model = (IControlStructureEditorDataModel) ProjectManager.getContainerInstance().getDataModel(projectId);
		this.path = path;
		this.process = Messages.ExportingCS;

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
	public CSExportJob(String path, String editorId, UUID projectId, int imgOffset, boolean forcedDeco) {
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
	public IStatus getPrintableRoot() {
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
		Image scaledImage = new Image(null, clipRectangle.width + (2 * this.imgOffset),
				clipRectangle.height + (2 * this.imgOffset));
		GC imageGC = new GC(scaledImage);
		Graphics graphics = new SWTGraphics(imageGC);

		if ((this.srcImage.getBounds().width >= 0) && (scaledImage.getBounds().width >= 0)) {
			graphics.drawImage(this.srcImage, clipRectangle,
					new Rectangle(this.imgOffset, this.imgOffset, clipRectangle.width, clipRectangle.height));
		}
		ImageLoader imgLoader = new ImageLoader();
		imgLoader.data = new ImageData[] { this.srcImage.getImageData() };

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

		Rectangle clipRectangle = new Rectangle();
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
							clipRectangle = new Rectangle(((IFigure) child).getBounds());
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
		clipRectangle.width = clipRectangle.width + Math.min(0, clipRectangle.x);
		clipRectangle.height = clipRectangle.height + Math.min(0, clipRectangle.y);
		// the clipRectangle is minimally located at (0,0)
		clipRectangle.x = Math.max(0, clipRectangle.x);
		clipRectangle.y = Math.max(0, clipRectangle.y);
		clipRectangle.scale(this.factor);
		if ((clipRectangle.height + clipRectangle.y) > this.srcImage.getBounds().height) {
			clipRectangle.height = this.srcImage.getBounds().height - clipRectangle.y;
		}
		if ((clipRectangle.width + clipRectangle.x) > this.srcImage.getBounds().width) {
			clipRectangle.width = this.srcImage.getBounds().width - clipRectangle.x;
		}
		return clipRectangle;
	}

	private class CSImageCalculator implements Runnable {

		@Override
		public void run() {
			Shell shell = new Shell();
			Composite canvas = new Composite(shell, 33554432);
			ScrollingGraphicalViewer viewer = new ScrollingGraphicalViewer();

			viewer.createControl(canvas);
			viewer.setEditPartFactory(new CSEditPartFactory(CSExportJob.this.model, CSExportJob.this.editorId));
			viewer.setProperty(CSAbstractEditor.STEP_EDITOR, CSExportJob.this.editorId);

			ScalableRootEditPart rootEditPart = new ScalableRootEditPart();
			viewer.setRootEditPart(rootEditPart);
			IRectangleComponent root = CSExportJob.this.model.getRoot();

			if (root == null) {
				CSExportJob.this.model.setRoot(new Rectangle(), new String());
				root = CSExportJob.this.model.getRoot();
			}
			viewer.setContents(root);
			viewer.getContents().refresh();
			((RootEditPart) rootEditPart.getContents()).getFigure().setDeco(CSExportJob.this.deco);

			viewer.getContents().refresh();

			IFigure tmpFigure = rootEditPart.getLayer(LayerConstants.PRINTABLE_LAYERS);
			// create a rectangle to guarantee that the src image
			Rectangle srcRectangle = tmpFigure.getBounds();
			for (Object layers : tmpFigure.getChildren()) {
				// Layer&ConnectionLayer
				for (Object part : ((IFigure) layers).getChildren()) {
					srcRectangle.union(((IFigure) part).getBounds());
				}

			}

			// a plain Image is created on which we can draw any graphics
			CSExportJob.this.srcImage = new Image(null,
					(int) Math.max(CSExportJob.this.factor * tmpFigure.getBounds().width, 1),
					(int) Math.max(CSExportJob.this.factor * tmpFigure.getBounds().height, 1));
			GC imageGC = new GC(CSExportJob.this.srcImage);
			Graphics graphics = new SWTGraphics(imageGC);
			graphics.scale(CSExportJob.this.factor);
			tmpFigure.paint(graphics);
			CSExportJob.this.printableFigure = tmpFigure;
		}

	}
}
