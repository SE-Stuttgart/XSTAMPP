package xstpa;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.UUID;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import xstampp.DefaultPerspective;
import xstampp.astpa.controlstructure.CSEditorWithPM;
import xstampp.astpa.model.interfaces.IExtendedDataModel;
import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.STPAEditorInput;
import xstampp.util.STPAPluginUtils;
import xstpa.model.XSTPADataController;
import xstpa.ui.CSContextEditor;
import xstpa.ui.RefinedSafetyConstraintsView;
import xstpa.ui.RefinedUCAView;
import xstpa.ui.View;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "xstpa"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

    private static Map<IDataModel,XSTPADataController> xstpaDataToIDataModel = new HashMap<>();
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
		PlatformUI.getWorkbench().addWorkbenchListener(new IWorkbenchListener() {
			
			@Override
			public boolean preShutdown(IWorkbench workbench, boolean forced) {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().removePartListener(editorListener);
				PlatformUI.getWorkbench().removeWorkbenchListener(this);
				return true;
			}
			
			@Override
			public void postShutdown(IWorkbench workbench) {
				// this listener kills itself in preShudown
				
			}
		});
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addPartListener(editorListener = new IPartListener() {
		
			private IWorkbenchPart activePart;
			private String oldPerspective = DefaultPerspective.ID;;

			@Override
		    public void partOpened(IWorkbenchPart part) {
				if(part instanceof RefinedUCAView){
					RefinedUCAView rucaView =((RefinedUCAView) part);
					rucaView.setDataController(getDataFor(rucaView.getProjectID()));
				}else if(part instanceof RefinedSafetyConstraintsView){
					RefinedSafetyConstraintsView rucaView =((RefinedSafetyConstraintsView) part);
					rucaView.setDataController(getDataFor(rucaView.getProjectID()));
				}
		    }
			
			@Override
			public void partActivated(IWorkbenchPart part) {
				
			}

			@Override
			public void partBroughtToTop(IWorkbenchPart part) {
//				if(part instanceof CSContextEditor && !part.equals(activePart)){
//					STPAEditorInput input = ((STPAEditorInput)((CSEditorWithPM) part).getEditorInput());
//					UUID projectId = ((STPAEditorInput) input).getProjectID();
//			      
//					XSTPADataController dataController = getDataFor(projectId);
//					// observer gets added, so whenever a value changes, the view gets updated;
//					IPerspectiveDescriptor currentPerspective = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getPerspective();
//					if(currentPerspective != null && !currentPerspective.getId().equals(xstpaPerspective.ID)){
//						oldPerspective = currentPerspective.getId();
//					}else{
//						oldPerspective = DefaultPerspective.ID;
//					}
//					
//					Map<String,String> values = new HashMap<>();
//					values.put("org.eclipse.ui.perspectives.showPerspective.perspectiveId", xstpaPerspective.ID);
//					STPAPluginUtils.executeParaCommand("org.eclipse.ui.perspectives.showPerspective", values);
//					
//					
//					IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(View.ID);
//					if(view == null){
//						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().resetPerspective();
//						view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(View.ID);
//					}
//					
//					if(view != null && view instanceof View){
//						((View) view).setController(dataController);
//					}
//
//					activePart = part;
//				}//if an CSContextEditor is open than the perspective that was last shown is shown
//				else if(activePart == null ||  !PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().isPartVisible(activePart)){
//					activePart = null;
//					IPerspectiveDescriptor currentPerspective = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getPerspective();
//					if(currentPerspective != null && currentPerspective.getId().equals(xstpaPerspective.ID)){
//						Map<String,String> values = new HashMap<>();
//						values.put("org.eclipse.ui.perspectives.showPerspective.perspectiveId", oldPerspective);
//						STPAPluginUtils.executeParaCommand("org.eclipse.ui.perspectives.showPerspective", values);
//					}
//				}
			}
			@Override
			public void partClosed(IWorkbenchPart part) {
//
//				IWorkbenchPart part2 = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
//				if(part.equals(activePart) && part instanceof CSContextEditor && !(part2 instanceof CSContextEditor)){
//					IPerspectiveDescriptor currentPerspective = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getPerspective();
//					if(currentPerspective != null && currentPerspective.getId().equals(xstpaPerspective.ID)){
//						Map<String,String> values = new HashMap<>();
//						values.put("org.eclipse.ui.perspectives.showPerspective.perspectiveId", oldPerspective);
//						STPAPluginUtils.executeParaCommand("org.eclipse.ui.perspectives.showPerspective", values);
//					}
//				}
			}

			@Override
			public void partDeactivated(IWorkbenchPart part) {
			}    
		   
			
		 });
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
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

	public static XSTPADataController getDataFor(UUID modelID){
		IDataModel model = ProjectManager.getContainerInstance()
				.getDataModel(modelID);
			if(model != null){
			XSTPADataController data = xstpaDataToIDataModel.get(model);
			if(data == null && model instanceof IExtendedDataModel){
				data = new XSTPADataController((IExtendedDataModel) model);
				ProjectManager.getContainerInstance().addProjectAdditionForUUID(ProjectManager.getContainerInstance().
						getProjectID((Observable) model), data);
				model.addObserver(data);
				xstpaDataToIDataModel.put(model, data);
			}else if(data == null){
				data = null;
			}
			return data;
		}
		return null;
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
