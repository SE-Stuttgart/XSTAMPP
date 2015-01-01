package xstampp.ui.navigation;

import java.util.UUID;

import messages.Messages;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

public class ProjectSelector extends AbstractSelector implements
		IProjectSelection {
	private IContributionItem openEntry;
	private IAction customAction;

	public ProjectSelector(TreeItem item, UUID projectId) {
		super(item, projectId);
		
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public void addOpenEntry(String id, IMenuManager manager) {
		if (this.openEntry != null) {
			manager.appendToGroup(id, this.openEntry);
		}
		if (this.customAction != null) {
			manager.appendToGroup(id, this.customAction);
		}
	}

}
