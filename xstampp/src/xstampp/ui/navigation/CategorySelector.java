package xstampp.ui.navigation;

import java.util.UUID;

import org.eclipse.swt.widgets.TreeItem;

/**
 * @see AbstractSelector
 * @author Lukas Balzer
 * @since 1.0
 */
public class CategorySelector extends AbstractSelector  {

	/**
	 * constructs a selector for a category item in the project tree
	 * @author Lukas Balzer
	 *
	 * @param item {@link AbstractSelector#getItem()}
	 * @param projectId {@link AbstractSelector#getProjectId()}
 	 */
	public CategorySelector(TreeItem item, UUID projectId, IProjectSelection parent) {
		super(item, projectId, parent);
	}

}
