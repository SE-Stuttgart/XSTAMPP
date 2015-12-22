package xstpa.ui.tables.utils;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * The content provider class is responsible for providing objects to the
 * view. It can wrap existing objects in adapters or simply return objects
 * as-is. These objects may be sensitive to the current input of the view,
 * or ignore it and always show the same content (like Task List, for
 * example).
 */
public class MainViewContentProvider implements IStructuredContentProvider {
	
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	  /**
	   * Returns the objects for the tables
	   */
	  public Object[] getElements(Object inputElement) {
	    return ((List<?>) inputElement).toArray();
	  }
}
