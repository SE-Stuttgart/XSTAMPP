package xstampp.ui.navigation;

import java.util.UUID;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.widgets.TreeItem;

/**
 * A subclass of {@link AbstractSelector} for a project selction
 * 
 * @author Lukas Balzer
 * @see AbstractSelector
 *
 */
public class ProjectSelector extends AbstractSelector {
  private IContributionItem openEntry;
  private IAction customAction;

  /**
   *
   * @author Lukas Balzer
   *
   * @param item
   *          {@link AbstractSelector#getItem()}
   * @param projectId
   *          {@link AbstractSelector#getProjectId()}
   */
  public ProjectSelector(TreeItem item, UUID projectId, IProjectSelection parent) {
    super(item, projectId, parent);

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
