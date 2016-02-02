package xstampp.astpa.controlstructure.utilities;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import xstampp.astpa.controlstructure.CSAbstractEditor;

public class GraphicalViewerOutline extends ViewPart implements IPartListener {

	private ScrollableThumbnail thumbnail; 
    private DisposeListener disposeListener;
	private EditPartViewer viewer;
	private LightweightSystem lws;
	private Canvas canvas;

	private EditPartViewer getViewer() {
		return this.viewer;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createPartControl(Composite parent) {
		 this.canvas = new Canvas(parent, SWT.BORDER); 
		 this.lws = new LightweightSystem(this.canvas); 

        

         this.disposeListener = new DisposeListener() { 
                 @Override 
                 public void widgetDisposed(DisposeEvent e) { 
                         if (GraphicalViewerOutline.this.thumbnail != null) { 
                                 GraphicalViewerOutline.this.thumbnail.deactivate(); 
                                 GraphicalViewerOutline.this.thumbnail = null; 
                         } 
                 } 
         }; 

         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addPartListener(this);
	} 
	 
	@Override
	public void dispose() { 
		if(this.thumbnail != null){
			this.thumbnail= null;
		}
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().removePartListener(this);
		
	}

	@Override
	public void partActivated(IWorkbenchPart part) {
		
		if(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() instanceof CSAbstractEditor){
			CSAbstractEditor editor = (CSAbstractEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			this.thumbnail = new ScrollableThumbnail();
	        this.thumbnail.setViewport((Viewport) ((ScalableRootEditPart) editor.getGraphicalViewer() 
	                .getRootEditPart()).getFigure()); 
	        RectangleFigure f = new RectangleFigure();
	        f.setBounds(((ScalableRootEditPart) editor.getGraphicalViewer().getRootEditPart()) 
  					.getLayer(LayerConstants.PRINTABLE_LAYERS).getBounds());
			  this.thumbnail.setSource(f);
			  
	        this.lws.setContents(this.thumbnail); 
		}else{
			IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			this.lws.setContents(new RectangleFigure());
			this.thumbnail = null;
		}
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partClosed(IWorkbenchPart part) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partOpened(IWorkbenchPart part) {
		// TODO Auto-generated method stub
		
	}
}
