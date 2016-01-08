package xstpa;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import xstampp.astpa.model.DataModelController;
import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.STPAEditorInput;
import xstampp.ui.editors.StandartEditorPart;
import xstpa.model.XSTPADataController;
import xstpa.ui.View;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "xstpa"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private IPartListener editorListener;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addPartListener(editorListener = new IPartListener() {
		
		    private XSTPADataController defaultController = new XSTPADataController(null);
		    private Map<IDataModel,XSTPADataController> xstpaDataToIDataModel = new HashMap<>();
		    
			
			private void refreshView() {
				//if the view is active, get the projectId from the ControlStructure Editor
			     
		    	 IEditorPart editorPart =PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		    	 if (editorPart == null) {
		    	 }
		    	 else if(editorPart instanceof StandartEditorPart){
		    		 
			    	 IEditorInput input = editorPart.getEditorInput();
			    	 UUID projectId = ((STPAEditorInput) input).getProjectID();
			      
			    	 XSTPADataController dataController = getDataFor(ProjectManager.getContainerInstance().getDataModel(projectId));
			    	 // observer gets added, so whenever a value changes, the view gets updated;
			    	 IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("xstpa.view");
			    	 if(view != null){
			    		 ((View) view).setController(dataController);
			    	 }
		    	 }
			}
			private XSTPADataController getDataFor(IDataModel model){
				XSTPADataController data = this.xstpaDataToIDataModel.get(model);
				if(data == null && model instanceof DataModelController){
					data = new XSTPADataController((DataModelController) model);
					this.xstpaDataToIDataModel.put(model, data);
				}else if(data == null){
					data = defaultController;
				}
				return data;
			}
			
			@Override
		    public void partOpened(IWorkbenchPart part) {
			      refreshView();
		    }
			
			@Override
			public void partActivated(IWorkbenchPart part) {
			      refreshView();
			}

			@Override
			public void partBroughtToTop(IWorkbenchPart part) {
			      refreshView();
			}

			@Override
			public void partClosed(IWorkbenchPart part) {
			      refreshView();
			}

			@Override
			public void partDeactivated(IWorkbenchPart part) {
			      refreshView();
			}    
		   
			
		 });
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().removePartListener(editorListener);
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
