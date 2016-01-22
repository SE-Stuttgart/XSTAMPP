package xstampp.ui.navigation;

import java.io.File;
import java.util.UUID;

import messages.Messages;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;

/**
 * A Selector is connected to a treeItem and manages the interaction between the platform and the ProjectTree
 * @author Lukas Balzer
 *
 */
public abstract class AbstractSelector implements IProjectSelection {

	private TreeItem treeItem;
	private UUID projectId;
	private String pathHistory;

	/**
	 *constructs a new Selector for the given treeItem and project
	 * @author Lukas Balzer
	 *
	 * @param item {@link #getItem()}
	 * @param projectId {@link #getProjectId()}
	 */
	public AbstractSelector(TreeItem item, UUID projectId) {
		this.treeItem = item;
		this.projectId = projectId;
	}

	
	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addOpenEntry(String id, IMenuManager manager) {
		// TODO Auto-generated method stub

	}
	@Override
	public void cleanUp() {
		//by default the clean up is done by java
	}

	@Override
	public UUID getProjectId() {
		return this.projectId;
	}

	@Override
	public void expandTree(boolean expand) {
		this.treeItem.setExpanded(expand);
		for (TreeItem item : this.treeItem.getItems()) {
			item.setExpanded(expand);
		}
	}

	@Override
	public void changeItem(TreeItem item) {
		this.treeItem = item;
	}


	
	@Override
	public void activate() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().
									setText(Messages.PlatformName + " -" +this.pathHistory);
	}


	@Override
	public void setPathHistory(String pathHistory) {
		this.pathHistory = pathHistory;
	}

	@Override
	public TreeItem getItem(){
		return this.treeItem;
	}

	public String getProjectOutput(){
		return "Output"+ File.separator + ProjectManager.getContainerInstance().getTitle(getProjectId())+File.separator;
	}
	
	@Override
	public IDataModel getProjectData() {
		
		return ProjectManager.getContainerInstance().getDataModel(projectId);
	}
}
