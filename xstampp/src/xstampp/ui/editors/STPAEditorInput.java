package xstampp.ui.editors;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import messages.Messages;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import xstampp.Activator;
import xstampp.preferences.IPreferenceConstants;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.navigation.StepSelector;
import xstampp.util.STPAPluginUtils;

/**
 * The Standard Editor input for this Platform,
 * 
 * @author Lukas Balzer
 * @since version 2.0.0
 * @see IEditorInput
 */
public class STPAEditorInput implements IEditorInput {
	private final IPreferenceStore store = Activator.getDefault()
			.getPreferenceStore();
	private UUID projectId;
	private String stepEditorId;
	private UUID id;
	private String stepName;
	private String pathHistory;
	private TreeItem stepItem;
	/**
	 * The Default editorInput 
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param projectId
	 *            the id of the project which is related to this input
	 * @param editorId {@link StepSelector#getDefaultEditorId()}
	 * 			
	 * @param refItem {@link StepSelector}
	 */
	public STPAEditorInput(UUID projectId, String editorId, TreeItem refItem) {
		this.stepItem=refItem;
		this.projectId = projectId;
		this.stepEditorId = editorId;
		this.id= UUID.randomUUID();
		this.stepName="";

	}

	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public IPersistableElement getPersistable() {
		// no persistence supported at the moment
		return null;
	}

	@Override
	public String getToolTipText() {
		return this.stepName + " - " + ProjectManager.getContainerInstance().getTitle(this.projectId);
	}
	
	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return the id of the project which is related to this input
	 */
	public UUID getProjectID() {
		return this.projectId;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int hashCode() {
		return (int) this.id.getMostSignificantBits();
	}

	@Override
	public boolean equals(Object arg0) {
		boolean equality= false;
		
		if(arg0 instanceof STPAEditorInput){
			equality= this.projectId.equals(((STPAEditorInput) arg0).projectId);
			return equality && this.stepEditorId.equals(((STPAEditorInput) arg0).stepEditorId);
		}
		return super.equals(arg0);
	}

	/**
	 * @return the stepName
	 */
	public String getStepName() {
		return this.stepName;
	}

	/**
	 * @param stepName the stepName to set
	 */
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	
	/**
	 * @param pathHistory the pathHistory to set
	 */
	public void setPathHistory(String pathHistory) {
		this.pathHistory = pathHistory;
	}
	
	/**
	 * called when the editor handeled by this input is opened 
	 * updates the workbench and optionally highlightes the related step in the project Explorer
	 * @author Lukas Balzer
	 * @throws PartInitException 
	 *
	 */
	public void activate() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().
									setText(Messages.PlatformName + " -" +this.pathHistory);
		if(!this.store.getBoolean(IPreferenceConstants.USE_NAVIGATION_COLORS)){
			return;
		}
		Color navigationColor = new Color(Display.getCurrent(), PreferenceConverter
				.getColor(this.store, IPreferenceConstants.NAVIGATION_ITEM_SELECTED));
		this.stepItem.setBackground(navigationColor);
		if(!this.stepItem.getParentItem().getExpanded()){
			
			this.stepItem.getParentItem().setBackground(ColorConstants.lightGray);
		}
		if(!this.stepItem.getParentItem().getParentItem().getExpanded()){
			this.stepItem.getParentItem().getParentItem().setBackground(ColorConstants.lightGray);
		}
	}

	/**
	 *if the step is deactivated this method notifys the platform to reset the path highlighting 
	 * @author Lukas Balzer
	 *
	 */
	public void deactivate() {
		
		this.stepItem.setBackground(null);
		
		this.stepItem.getParentItem().setBackground(null);
		this.stepItem.getParentItem().getParentItem().setBackground(null);
	}

	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @param item the related step item
	 */
	public void setStepItem(TreeItem item) {
		this.stepItem=item;
		
	}
	
	public IAction getActivationAction() {
		return new ActivationAction();
	}
	
	private class ActivationAction extends Action{
		public ActivationAction() {
			setText(getStepName());
		}
		@Override
		public void run() {
				Map<String,String> values= new HashMap<>();
				values.put("xstampp.command.steps.open",STPAEditorInput.this.stepEditorId);
				activate();
				STPAPluginUtils.executeParaCommand("astpa.command.openStep", values);
		}
		
	}
}
