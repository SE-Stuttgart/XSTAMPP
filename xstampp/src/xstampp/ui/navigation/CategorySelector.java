package xstampp.ui.navigation;

import java.util.UUID;

import org.eclipse.swt.widgets.TreeItem;

public class CategorySelector extends AbstractSelector implements
		IProjectSelection {

	public CategorySelector(TreeItem item, UUID projectId) {
		super(item, projectId);
	}

}
