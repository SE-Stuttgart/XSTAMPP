package xstampp.ui.navigation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.CommandContributionItem;

import xstampp.ui.editors.STPAEditorInput;

/**
 * a Selection class which is used to carry information about the project step
 * 
 * @author Lukas Balzer
 * 
 */
public class StepSelector extends AbstractSelector implements IMenuListener{

	private String editorId;
	private Map<String,STPAEditorInput> inputs;
	private boolean showOpenWith;
	private String openWithPerspective;
	private ArrayList<String> additionalViews;

	/**
	 * constructs a step selector which manages the selection and interaction with a 
	 * step item in the project tree
	 * @author Lukas Balzer
	 *
	 * @param item {@link AbstractSelector#getItem()}
	 * @param projectId {@link AbstractSelector#getProjectId()}
	 * @param editorId the editor id as defined in the plugin.xml
	 * @param editorName the name of the editor which shall be dispayed in the workbench 
	 */
	public StepSelector(TreeItem item, UUID projectId, String editorId, String editorName) {
		super(item, projectId);
		this.editorId = editorId;
		this.inputs=new HashMap<>();
		STPAEditorInput input = new STPAEditorInput(projectId, editorId, item);
		input.setStepName(editorName);
		this.inputs.put(editorId,input);
		this.showOpenWith = false;
	}
	
	@Override
	public void changeItem(TreeItem item) {
		for(STPAEditorInput input : this.inputs.values()){
			input.setStepItem(item);
		}
		super.changeItem(item);
	}

	/**
	 * adds a editor which is registered for the step, the first editor which is
	 * added is the default
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param id
	 *            the id with which a EditorPart must be registered in the
	 *            <code>plugin.xml</code>
	 * @param editorName TODO
	 */
	public void addStepEditor(String id, String editorName) {
		STPAEditorInput input = new STPAEditorInput(getProjectId(), id,getItem());
		input.setStepName(editorName);
		this.inputs.put(id, input);
	}

	/**
	 * 
	 * @author Lukas Balzer
	 */
	public void openDefaultEditor() {
		openEditor(this.getDefaultEditorId());
	}

	public void openEditor(String id) {
		STPAEditorInput input = this.inputs.get(id);
		if (input != null) {
			input.addViews(this.additionalViews);
			input.setPerspective(openWithPerspective);
			try {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage()
				.openEditor(input, id);
				input.activate();
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	@Override
	public void setPathHistory(String pathHistory) {
		for(STPAEditorInput input : this.inputs.values()){
			input.setPathHistory(pathHistory);
		}
		super.setPathHistory(pathHistory);
	}
	
	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @return the input which is used by the selector 
	 */
	public IEditorInput getEditorInput() {
		return this.inputs.values().iterator().next();
	}
	
	
	/**
	 * 
	 * @author Lukas Balzer
	 * @return string
	 */
	public String getDefaultEditorName(){
		return this.inputs.values().iterator().next().getStepName();
	}
	
	
	@Override
	public void cleanUp(){
		for(STPAEditorInput input : this.inputs.values()){
			IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().findEditor(input);
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			.getActivePage().closeEditor(part, false);
		}
	}

	/**
	 * @return the editorId
	 */
	public String getDefaultEditorId() {
		return this.editorId;
	}

	/**
	 * @param editorId the availableEditor to set
	 */
	public void setDefaultEditorId(String editorId) {
		this.editorId = editorId;
	}

	@Override
	public void menuAboutToShow(IMenuManager manager) {
		
	}
	
	public Collection<STPAEditorInput> getInputs() {
		return this.inputs.values();
		
	}


	/**
	 * @return the openWithPerspective
	 */
	public String getOpenWithPerspective() {
		return this.openWithPerspective;
	}

	/**
	 * @param configurationElement the openWithPerspective to set
	 */
	public void setOpenWithPerspective(IConfigurationElement configurationElement) {
		
		this.openWithPerspective = configurationElement.getAttribute("targetId"); //$NON-NLS-1$
		this.additionalViews = new ArrayList<>();
		for(IConfigurationElement element : configurationElement.getChildren("view")){  //$NON-NLS-1$
			this.additionalViews.add(element.getAttribute("id")); //$NON-NLS-1$
		}
	}
	
	/**
	 * @param perspectiveId the openWithPerspective to set
	 */
	public void setOpenWithPerspective(String perspectiveId) {
		
		this.openWithPerspective = perspectiveId;
		this.additionalViews = new ArrayList<>();
	}
	
	public void addViews() throws PartInitException {
		for(int i=0;this.additionalViews != null && i< this.additionalViews.size();i++){
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(this.additionalViews.get(i));
		}
	}
}
