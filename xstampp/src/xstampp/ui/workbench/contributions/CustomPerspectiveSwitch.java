package xstampp.ui.workbench.contributions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.bcel.generic.NEW;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchWindow;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

import xstampp.Activator;
import xstampp.util.STPAPluginUtils;

public class CustomPerspectiveSwitch extends WorkbenchWindowControlContribution implements IPerspectiveListener {

	private class PerspectiveShortcut extends ToolBarContributionItem{
		private Image icon,img;
		private String perspectiveId;
		private String perspectiveName;
		private Map<String,String> comValues;
		private Label shortcut;
		private boolean selected;
		private ToolItem item; 
		
		
		/**
		 * @param bar 
		 * @param visible TODO
		 */
		public void setItemVisible(ToolBar bar, boolean visible) {
			if(!visible){
				this.item.dispose();
			}else if(this.item.isDisposed()){
				fill(bar, 0);
			}
		}
		/**
		 * @param item the item to set
		 */
		public void setItem(ToolItem item) {
			this.item = item;
		}
		public PerspectiveShortcut(IPerspectiveDescriptor element) {
			
			this.comValues = new HashMap<>();
			this.perspectiveId = element.getId();
			this.comValues.put(IWorkbenchCommandConstants.PERSPECTIVES_SHOW_PERSPECTIVE_PARM_ID, this.perspectiveId);
			this.perspectiveName = element.getLabel();
			this.icon = element.getImageDescriptor().createImage(); 
			if(icon == null){
				this.icon = PlatformUI.getWorkbench().getSharedImages().
						getImage(ISharedImages.IMG_DEF_VIEW);
			}
			ImageData imData = new ImageData(50, 16, 24, new PaletteData(0xff0000,0x00ff00, 0x0000ff));
		    imData.setAlpha(0, 0, 0); // just to force alpha array allocation with the right size
		    Arrays.fill(imData.alphaData, (byte) 0); // set whole image as transparent
		    
		    
			  Image src = new Image(null, 50, 16);   
			  GC gc = new GC(src);
			  gc.drawString("test", 16, 0, true);
			  gc.dispose();
			    ImageData imageData = src.getImageData();
			    imageData.transparentPixel = imageData.getPixel(0, 0);
			    src.dispose();
			   this.img= new Image(null, imageData);
			   gc = new GC(this.icon);
			   gc.copyArea(this.img, 0, 0);
			    //draw on the icon with gc
		}
		@Override
		public void fill(ToolBar parent, int index) {
			this.item =new ToolItem(parent, SWT.None);
			item.setImage(this.img);
			
		}
		public void  addToParent(Composite parent) {
			Composite main= new Composite(parent, SWT.None);
			main.setLayout(new FormLayout());
			
			Label iconLabel = new Label(main, SWT.None);
			FormData data = new FormData(16, 16);
			iconLabel.setLayoutData(data);
			if(this.icon != null){
				iconLabel.setImage(this.icon);
			}else{
				iconLabel.setImage(PlatformUI.getWorkbench().getSharedImages().
						getImage(ISharedImages.IMG_DEF_VIEW));
			}
			this.shortcut = new Label(main,  SWT.SHADOW_IN);
			this.shortcut.setText(this.perspectiveName);
			this.shortcut.setSize(main.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			data =new FormData(this.shortcut.getSize().x, this.shortcut.getSize().y);
			data.left = new FormAttachment(iconLabel);
			this.shortcut.setLayoutData(data);
			
			main.setLayoutData(new GridData(16 + this.shortcut.getSize().x, this.shortcut.getSize().y));
			
			this.shortcut.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseUp(MouseEvent e) {
					STPAPluginUtils.executeParaCommand(IWorkbenchCommandConstants.PERSPECTIVES_SHOW_PERSPECTIVE,
														PerspectiveShortcut.this.comValues);
				}
			});
			
		}
		public void setSelectedIfEqual(String id) {
		}
		
		public String getPerspectiveId() {
			return this.perspectiveId;
		}


	}

	private Map<String,List<PerspectiveShortcut>> configsToPerspectiveIds; 
	private Map<String,PerspectiveShortcut> shortcutsToPerspectiveIds;
	private String activePerspectiveId;
	private Composite parentBar;
	private ToolBarManager manager;
	private ToolBar bar;
	@Override
	protected Control createControl(Composite parent) {
		GridLayout layout = new GridLayout(4, false);
		GridData data = new GridData();
		this.bar = new ToolBar(parent, SWT.None);
		this.manager = new ToolBarManager(this.bar);
//		this.parentBar = parent;
//		this.mainSwitch = new Composite(parent, SWT.None);
//		this.mainSwitch.setLayout(new GridLayout(10,false));
		this.configsToPerspectiveIds = new HashMap<>();
		this.shortcutsToPerspectiveIds = new HashMap<>();
		IPerspectiveDescriptor descriptor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getPerspective();
		for(IConfigurationElement element : Platform.getExtensionRegistry().
											getConfigurationElementsFor("org.eclipse.ui.perspectiveExtensions")){
			String targetId = element.getAttribute("targetID"); //$NON-NLS-1$
			List<PerspectiveShortcut> configs;
			if(this.configsToPerspectiveIds.containsKey(targetId)){
				configs = this.configsToPerspectiveIds.get(targetId);
			}else{
				configs = new ArrayList<PerspectiveShortcut>();
				this.configsToPerspectiveIds.put(targetId, configs);
			}
			for(IConfigurationElement child : element.getChildren("perspectiveShortcut")){ //$NON-NLS-1$
				if(addOrGetShortcut(child.getAttribute("id"), this.bar) != null){
					configs.add(addOrGetShortcut(child.getAttribute("id"), this.bar));
				}
			}
			
		};
		
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().addPerspectiveListener(this);
		return this.bar;
	}
	
	@Override
	public void fill(CoolBar parent, int index) {
		super.fill(parent, index);
	}
	private PerspectiveShortcut addOrGetShortcut(String targetId, ToolBar parent) {
		
		if(this.shortcutsToPerspectiveIds.containsKey(targetId)){
			return this.shortcutsToPerspectiveIds.get(targetId);
		}
		IPerspectiveDescriptor descriptor = PlatformUI.getWorkbench()
				.getPerspectiveRegistry()
				.findPerspectiveWithId(targetId);//$NON-NLS-1$
		if(descriptor != null){
			PerspectiveShortcut shortcut = new PerspectiveShortcut(descriptor);
			shortcut.fill(parent, 0);
			
			this.shortcutsToPerspectiveIds.put(targetId, shortcut);
			return shortcut;
		}
		return null;
	}

	@Override
	public void perspectiveActivated(IWorkbenchPage page,
			IPerspectiveDescriptor descriptor) {
		this.manager.removeAll();
		this.activePerspectiveId = descriptor.getId();
		List<PerspectiveShortcut> shortcuts= this.configsToPerspectiveIds.get(this.activePerspectiveId);
		
		for(PerspectiveShortcut shortcut: this.shortcutsToPerspectiveIds.values()){
			shortcut.setSelectedIfEqual(this.activePerspectiveId);
			if(!shortcuts.contains(shortcut)){
				shortcut.setItemVisible(this.bar,false);
				this.bar.redraw();
				this.bar.layout();
				this.manager.add(shortcut);
			}
		}
	    IWorkbenchWindow workbenchWindow =  PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	    ICoolBarManager manager  = ((WorkbenchWindow)workbenchWindow).getActionBars().getCoolBarManager();
		
	}

	@Override
	public void perspectiveChanged(IWorkbenchPage arg0,
			IPerspectiveDescriptor arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

}
