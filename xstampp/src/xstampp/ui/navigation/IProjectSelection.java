package xstampp.ui.navigation;

import java.util.UUID;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;

public interface IProjectSelection extends ISelection {
	/**
	 * @param id
	 *            the id of the Contribution Group the
	 *            IContributionItems-/IActions are appended to
	 * @param manager
	 *            the MenuManager of the contextMenu
	 */
	public void addOpenEntry(String id, IMenuManager manager);

	/**
	 * @return the projectId
	 */
	public UUID getProjectId();

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param expand
	 *            expand or not
	 */
	public void expandTree(boolean expand);

	public void changeItem(TreeItem item);
	
	public TreeItem getItem();
	

	/**
	 *	tells the selector that he is active so that he can update the header 
	 * @author Lukas Balzer
	 *
	 */
	public void activate();
	
	/**
	 *Setter for the path history which is shown in the shell title
	 * @author Lukas Balzer
	 * @param pathHistory 
	 * 			the relative path of the step which means: 
	 * 			<br/><code>project->category->step</code>
	 *
	 */
	void setPathHistory(String pathHistory);
	
	void cleanUp();
	
}
