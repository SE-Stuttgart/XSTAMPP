package xstampp.ui.workbench.contributions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import messages.Messages;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

import xstampp.Activator;
/**
* This toolbar Contribution provides tools for a graphical editor
* Editors can interact with this contribution by implementing IZoomContributior<p/>
* The provided decoration switch can be used by using  {@link IZoomContributor#IS_DECORATED}
* 
* @author Lukas Balzer
* @since 2.0.0
*/
public class EditorContribution extends WorkbenchWindowControlContribution implements ZoomListener,
																	PropertyChangeListener,IPartListener{

	private static final int STANDART_ZOOM = 100;
	private final static String[] ZOOM_LEVEL= new String[]{"25","50","75","100","150","200","250"};
	private final static Point ZOOM_SLIDER_RANGE=new Point(10, 300);
	private ComboContribiution zoomLabel;
	private ZoomManager zoomManager;
	private SliderContribution zoomSlider;
	private ButtonContribution decoButton;
	private ButtonContribution zoomInButton;
	private ButtonContribution zoomOutButton;
	private IZoomContributor contributor;
	/**
	 * the value of isDecorated is used to store the content of the
	 * {@link IZoomContributor#IS_DECORATED} property
	 */
	private boolean isDecorated;
	
	@Override
	protected Control createControl(Composite parent) {
		this.isDecorated=false;
		if(System.getProperty("os.name").toLowerCase().contains("linux")){
	    	return new ToolBar(parent, SWT.NONE);
	    }
		ToolBarManager manager = new ToolBarManager(SWT.HORIZONTAL | SWT.FLAT);
		this.contributor = new EmptyZoomContributor();
		
		this.decoButton= new ButtonContribution("decoButton",SWT.TOGGLE){
			@Override
			protected Control createControl(Composite parent) {
				Control control = super.createControl(parent);
				getButtonControl().setToolTipText(Messages.DecorationToolTip);
				getButtonControl().setImage(Activator
						.getImageDescriptor("icons/buttons/DecoButton.png").createImage()); //$NON-NLS-1$
				getButtonControl().addMouseListener(new MouseAdapter() {
					
					@Override
					public void mouseUp(MouseEvent arg0) {
						EditorContribution.this.contributor.fireToolPropertyChange(IZoomContributor.IS_DECORATED,
																			   !EditorContribution.this.isDecorated);
						setDecoSelection((boolean) contributor.getProperty(IZoomContributor.IS_DECORATED));
					}
				});
				return control;
			}
		};
		manager.add(decoButton);
		//adding a zoomslider to the toolbar
		
		final SelectionAdapter adapter = new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(e.getSource() instanceof Slider){
					int zoom=((Slider)e.getSource()).getSelection();
					if(Math.abs(zoom -STANDART_ZOOM) < 9){
						EditorContribution.this.zoomSlider.getSliderControl().setSelection(STANDART_ZOOM);
						zoom = STANDART_ZOOM;
					}
					EditorContribution.this.updateLabel();
					if(EditorContribution.this.zoomManager != null){
						EditorContribution.this.zoomManager.setZoom(((double) zoom)/100);
					}
				}
			}
		};
		
		this.zoomSlider= new SliderContribution("zoomSlider", SWT.HORIZONTAL,200){
			@Override
			protected Control createControl(Composite parent) {
				Control control =super.createControl(parent); 
				getSliderControl().setMinimum(ZOOM_SLIDER_RANGE.x);
				getSliderControl().setMaximum(ZOOM_SLIDER_RANGE.y);
				getSliderControl().setIncrement(10);
				getSliderControl().setDragDetect(true);
				getSliderControl().setSelection(STANDART_ZOOM);
				getSliderControl().addSelectionListener(adapter);
				return control;
			}
		};
		manager.add(zoomSlider);
		
		
		this.zoomOutButton= new ButtonContribution("zoomOutButton", SWT.PUSH){
			@Override
			protected Control createControl(Composite parent) {
				Control control =super.createControl(parent); 
				getButtonControl().setImage(Activator
						.getImageDescriptor("/icons/buttons/minus.png").createImage()); //$NON-NLS-1$
				getButtonControl().addSelectionListener(new SelectionAdapter() {
					
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						int zoom=EditorContribution.this.zoomSlider.getSliderControl().getSelection() 
								-EditorContribution.this.zoomSlider.getSliderControl().getIncrement();
						EditorContribution.this.zoomSlider.getSliderControl().setSelection(zoom);
						EditorContribution.this.updateLabel();
						if(EditorContribution.this.zoomManager != null){
							EditorContribution.this.zoomManager.setZoom(((double) zoom)/100);
						}
					}
				});
				return control;
			}
		};
		manager.add(zoomOutButton);
		
		
		
		this.zoomLabel= new ComboContribiution("zoomCombo", SWT.DROP_DOWN | SWT.NONE,100){
			@Override
			protected Control createControl(Composite parent) {
				Control control =super.createControl(parent);
				
				getComboControl().setItems(ZOOM_LEVEL);
				EditorContribution.this.updateLabel();
				getComboControl().addSelectionListener(new SelectionAdapter() {
					
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						String selection=EditorContribution.this.zoomLabel.getComboControl().getText().replace('%', ' ');
						try{
							int zoom=Integer.parseInt(selection.trim());
							EditorContribution.this.zoomSlider.getSliderControl().setSelection(zoom);
							EditorContribution.this.updateLabel();
							if(EditorContribution.this.zoomManager != null){
								EditorContribution.this.zoomManager.setZoom(((double) zoom)/100);
							}
						}catch(NumberFormatException e){
						}
						
					}
				});
				return control;
			}
		};
		manager.add(zoomLabel);
		
		
		this.zoomInButton= new ButtonContribution("zoomInButton", SWT.PUSH){
			@Override
			protected Control createControl(Composite parent) {
				Control control =super.createControl(parent); 
				getButtonControl().setImage(Activator
						.getImageDescriptor("/icons/buttons/plus.png").createImage()); //$NON-NLS-1$
				getButtonControl().addSelectionListener(new SelectionAdapter() {
					
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						int zoom=EditorContribution.this.zoomSlider.getSliderControl().getSelection()
								+EditorContribution.this.zoomSlider.getSliderControl().getIncrement();
						EditorContribution.this.zoomSlider.getSliderControl().setSelection(zoom);
						EditorContribution.this.updateLabel();
						if(EditorContribution.this.zoomManager != null){
							EditorContribution.this.zoomManager.setZoom(((double) zoom)/100);
						}
					}
				});
				return control;
			}
		};
		manager.add(zoomInButton);
		
		try{
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addPartListener(this);
		}catch(NullPointerException e){
			// if there is a nullpointer one one cant do anything
		}
		return manager.createControl(parent);
	}



	@Override
	public void zoomChanged(double zoom) {
		this.zoomSlider.getSliderControl().setSelection((int) (zoom *100));
		this.updateLabel();
	}

	private void updateLabel(){
		this.zoomLabel.getComboControl().setText(this.zoomSlider.getSliderControl().getSelection() + "%"); //$NON-NLS-1$
	}
	
	/**
	 * enables all widgets contained in the main composite of this class
	 *
	 * @author Lukas Balzer
	 *
	 * @param enabled if components should be enabled
	 */
	private void setEnabled(boolean enabled){
		try{
		this.zoomLabel.setEnabled(enabled);
		this.zoomSlider.setEnabled(enabled);
		this.setDecoSelection((boolean) this.contributor.getProperty(IZoomContributor.IS_DECORATED));
		this.decoButton.setEnabled(enabled);
		this.zoomInButton.setEnabled(enabled);
		this.zoomOutButton.setEnabled(enabled);
		}catch(Exception e){
			return;
		}
	}

	private void setDecoSelection(boolean enabled){
		this.isDecorated=enabled;
		if(enabled){
			this.decoButton.getButtonControl().setImage(Activator.getImageDescriptor("icons/buttons/DecoButton_Selected.png").createImage()); //$NON-NLS-1$
		}else{
			this.decoButton.getButtonControl().setImage(Activator.getImageDescriptor("icons/buttons/DecoButton.png").createImage()); //$NON-NLS-1$
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if(arg0.getPropertyName().equals(IZoomContributor.IS_DECORATED)){
			this.setDecoSelection((boolean) arg0.getNewValue());
		}
	}
	
	@Override
	public void partActivated(IWorkbenchPart part) {
		if(part instanceof IZoomContributor){
			this.contributor= ((IZoomContributor) part);
			this.contributor.addPropertyListener(this);
			this.zoomManager= this.contributor.getZoomManager();
			
			this.zoomManager.addZoomListener(EditorContribution.this);
			this.zoomSlider.getSliderControl().setSelection((int) (EditorContribution.this.zoomManager.getZoom() *100));
			this.zoomSlider.getSliderControl().notifyListeners(SWT.Selection, null);
		}else{
			
			if(!(this.contributor instanceof EmptyZoomContributor)){
				this.zoomManager.removeZoomListener(EditorContribution.this);
				this.contributor.removePropertyListener(this);
				this.contributor = new EmptyZoomContributor();
			}
		}
	}
	
	@Override
	public void dispose() {
		try{
			if(this.contributor != null){
				this.contributor.removePropertyListener(this);
			}
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().removePartListener(this);
		}catch(NullPointerException e){
			// if there is a nullpointer one can't delete anything
		}
		super.dispose();
	}
	
	@Override
	public void partOpened(IWorkbenchPart part) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void partDeactivated(IWorkbenchPart part) {
		if(this.contributor != null){
			this.contributor.removePropertyListener(this);
		}
	}
	
	@Override
	public void partClosed(IWorkbenchPart part) {
		if(this.contributor != null){
			this.contributor.removePropertyListener(this);
		}
	}
	
	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
		//do nothing
	}
	
}
