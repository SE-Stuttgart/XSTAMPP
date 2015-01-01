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
public class StepSelector extends AbstractSelector implements IProjectSelection {

	private String availableEditor;
	private boolean isLocked;
	private STPAEditorInput input;

	public StepSelector(TreeItem item, UUID projectId, String editorId) {
		super(item, projectId);
		this.availableEditor = editorId;
		this.isLocked = false;
		this.input=null;
		this.setEditorInput(new STPAEditorInput(projectId, editorId));
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
		this.availableEditor = id;
	}

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @throws PartInitException
	 *             if the platform fails to open the Editor
	 */
	public void getDefaultEditor() throws PartInitException {

		if ((this.getEditorInput() != null) && (this.availableEditor != null)) {
			
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage()
					.openEditor(this.getEditorInput(), this.availableEditor);
		}
	}

	
	@Override
	public void setPathHistory(String pathHistory) {
		this.input.setPathHistory(pathHistory);
		super.setPathHistory(pathHistory);
	}
	public IEditorInput getEditorInput() {
		return this.input;
	}
	
	protected void setEditorInput(STPAEditorInput input){
		this.input=input;
	}
	
	public void setEditorName(String name){
		this.input.setStepName(name);
	}
	
	/**
	 * Setter for the editor name
	 * @author Lukas Balzer
	 *
	 * @param name the editor name
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
	
	
}
