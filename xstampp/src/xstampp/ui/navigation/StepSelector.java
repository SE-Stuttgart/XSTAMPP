package xstampp.ui.navigation;

import java.util.UUID;

import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import xstampp.util.STPAEditorInput;

/**
 * a Selection class which is used to carry information about the project step
 * 
 * @author Lukas Balzer
 * 
 */
public class StepSelector extends AbstractSelector {

	private String editorId;
	private STPAEditorInput input;

	/**
	 * constructs a step selector which manages the selection and interaction with a 
	 * step item in the project tree
	 * @author Lukas Balzer
	 *
	 * @param item {@link AbstractSelector#getItem()}
	 * @param projectId {@link AbstractSelector#getProjectId()}
	 * @param editorId the editor id as defined in the plugin.xml
	 */
	public StepSelector(TreeItem item, UUID projectId, String editorId) {
		super(item, projectId);
		this.editorId = editorId;
		this.input=null;
		this.setEditorInput(new STPAEditorInput(projectId, editorId,item));
	}
	
	@Override
	public void changeItem(TreeItem item) {
		this.input.setStepItem(item);
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
	 */
	public void addStepEditor(String id) {
		this.editorId = id;
	}

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @throws PartInitException
	 *             if the platform fails to open the Editor
	 */
	public void getDefaultEditor() throws PartInitException {

		if ((this.getEditorInput() != null) && (this.editorId != null)) {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage()
					.openEditor(this.getEditorInput(), this.editorId);
			
		}
	}

	
	@Override
	public void setPathHistory(String pathHistory) {
		this.input.setPathHistory(pathHistory);
		super.setPathHistory(pathHistory);
	}
	
	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @return the input which is used by the selector 
	 */
	public IEditorInput getEditorInput() {
		return this.input;
	}
	
	protected void setEditorInput(STPAEditorInput input){
		this.input=input;
	}
	
	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @param name the name for the step editor 
	 */
	public void setEditorName(String name){
		this.input.setStepName(name);
	}
	
	/**
	 * 
	 * @author Lukas Balzer
	 * @return string
	 */
	public String getEditorName(){
		return this.input.getStepName();
	}
	
	
	@Override
	public void cleanUp(){
		IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
		.getActivePage().findEditor(this.input);
		PlatformUI.getWorkbench().getActiveWorkbenchWindow()
		.getActivePage().closeEditor(part, false);
	}

	/**
	 * @return the editorId
	 */
	public String getEditorId() {
		return this.editorId;
	}

	/**
	 * @param editorId the availableEditor to set
	 */
	public void setEditorId(String editorId) {
		this.editorId = editorId;
	}
	
	
}
