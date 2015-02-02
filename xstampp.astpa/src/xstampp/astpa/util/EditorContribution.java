package xstampp.astpa.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import messages.Messages;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

import xstampp.astpa.Activator;
import xstampp.astpa.controlstructure.IControlStructureEditor;
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
	private Combo zoomLabel;
	private ZoomManager zoomManager;
	private Slider zoomSlider;
	private Label decoButton;
	private Button zoomInButton;
	private Button zoomOutButton;
	private IZoomContributor contributor;
	/**
	 * the value of isDecorated is used to store the content of the
	 * {@link IZoomContributor#IS_DECORATED} property
	 */
	private boolean isDecorated;
	
	@Override
	protected Control createControl(Composite parent) {
		this.isDecorated=false;
		this.contributor = new EmptyZoomContributor();
		
		// Create a composite to place the label in
		Composite comp = new Composite(parent, SWT.NONE);

		// Give some room around the control
		FormLayout layout = new FormLayout();
		layout.marginHeight = 2;
		layout.marginWidth = 2;
		comp.setLayout(layout);
		FormData data= new FormData(20,20);
		this.decoButton= new Label(comp,SWT.None);
		this.decoButton.setToolTipText(Messages.DecorationToolTip);
		this.decoButton.setImage(Activator
				.getImageDescriptor("/icons/buttons/controlstructure/process_32.png").createImage()); //$NON-NLS-1$
		this.decoButton.setLayoutData(data);
		this.decoButton.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {
				EditorContribution.this.contributor.fireToolPropertyChange(IZoomContributor.IS_DECORATED,
																	   !EditorContribution.this.isDecorated);
				
			}
		});
		//adding a zoomslider to the toolbar
		data= new FormData(SWT.DEFAULT, SWT.DEFAULT);
		data.left= new FormAttachment(this.decoButton,2);
		this.zoomSlider= new Slider(comp, SWT.HORIZONTAL);
		this.zoomSlider.setMinimum(ZOOM_SLIDER_RANGE.x);
		this.zoomSlider.setMaximum(ZOOM_SLIDER_RANGE.y);
		this.zoomSlider.setIncrement(10);
		this.zoomSlider.setLayoutData(data);
		this.zoomSlider.setDragDetect(true);
		this.zoomSlider.setSelection(STANDART_ZOOM);
		
		this.zoomSlider.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				int zoom=EditorContribution.this.zoomSlider.getSelection();
				if(Math.abs(zoom -STANDART_ZOOM) < 9){
					EditorContribution.this.zoomSlider.setSelection(STANDART_ZOOM);
					zoom = STANDART_ZOOM;
				}
				EditorContribution.this.updateLabel();
				if(EditorContribution.this.zoomManager != null){
					EditorContribution.this.zoomManager.setZoom(((double) zoom)/100);
				}
			}
		});
		this.zoomOutButton= new Button(comp, SWT.PUSH);
		this.zoomOutButton.setImage(Activator
				.getImageDescriptor("/icons/buttons/controlstructure/minus.png").createImage()); //$NON-NLS-1$
		data= new FormData(20, 18);
		data.left= new FormAttachment(this.zoomSlider,2);
		this.zoomOutButton.setLayoutData(data);
		this.zoomOutButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int zoom=EditorContribution.this.zoomSlider.getSelection() -EditorContribution.this.zoomSlider.getIncrement();
				EditorContribution.this.zoomSlider.setSelection(zoom);
				EditorContribution.this.updateLabel();
				if(EditorContribution.this.zoomManager != null){
					EditorContribution.this.zoomManager.setZoom(((double) zoom)/100);
				}
			}
		});
		
		
		
		this.zoomLabel= new Combo(comp, SWT.NONE);
		
		this.zoomLabel.setItems(ZOOM_LEVEL);
		this.updateLabel();
		data= new FormData(SWT.DEFAULT, SWT.DEFAULT);
		data.left= new FormAttachment(this.zoomOutButton,2);
		this.zoomLabel.setLayoutData(data);
		this.zoomLabel.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String selection=EditorContribution.this.zoomLabel.getText().replace('%', ' ');
				try{
					int zoom=Integer.parseInt(selection.trim());
					EditorContribution.this.zoomSlider.setSelection(zoom);
					EditorContribution.this.updateLabel();
					if(EditorContribution.this.zoomManager != null){
						EditorContribution.this.zoomManager.setZoom(((double) zoom)/100);
					}
				}catch(NumberFormatException e){
				}
				
			}
		});
		
		this.zoomInButton= new Button(comp, SWT.PUSH);
		this.zoomInButton.setImage(Activator
				.getImageDescriptor("/icons/buttons/controlstructure/plus.png").createImage()); //$NON-NLS-1$
		data= new FormData(20, 18);
		data.left= new FormAttachment(this.zoomLabel,2);
		this.zoomInButton.setLayoutData(data);
		this.zoomInButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int zoom=EditorContribution.this.zoomSlider.getSelection() +EditorContribution.this.zoomSlider.getIncrement();
				EditorContribution.this.zoomSlider.setSelection(zoom);
				EditorContribution.this.updateLabel();
				if(EditorContribution.this.zoomManager != null){
					EditorContribution.this.zoomManager.setZoom(((double) zoom)/100);
				}
			}
		});
		
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addPartListener(this);

		this.setEnabled(false);
		return comp;
	}



	@Override
	public void zoomChanged(double zoom) {
		this.zoomSlider.setSelection((int) (zoom *100));
		this.updateLabel();
	}

	private void updateLabel(){
		this.zoomLabel.setText(this.zoomSlider.getSelection() + "%"); //$NON-NLS-1$
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
			this.decoButton.setImage(Activator.getImageDescriptor("icons/buttons/controlstructure/DecoButton_Selected.png").createImage()); //$NON-NLS-1$
		}else{
			this.decoButton.setImage(Activator.getImageDescriptor("icons/buttons/controlstructure/DecoButton.png").createImage()); //$NON-NLS-1$
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
			this.setEnabled(true);
			this.zoomManager.addZoomListener(EditorContribution.this);
			this.zoomSlider.setSelection((int) (EditorContribution.this.zoomManager.getZoom() *100));
			this.zoomSlider.notifyListeners(SWT.Selection, null);
		}else{
			
			this.setEnabled(false);
			if(!(this.contributor instanceof EmptyZoomContributor)){
				this.zoomManager.removeZoomListener(EditorContribution.this);
				this.contributor = new EmptyZoomContributor();
			}
		}
	}
	
	@Override
	public void dispose() {
		
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().removePartListener(this);
		super.dispose();
	}
	
	@Override
	public void partOpened(IWorkbenchPart part) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void partDeactivated(IWorkbenchPart part) {
		
		
	}
	
	@Override
	public void partClosed(IWorkbenchPart part) {
		// nothing by default
		
	}
	
	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
		//do nothing
	}
	
}
