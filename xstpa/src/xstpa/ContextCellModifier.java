package xstpa;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;

class ContextCellModifier implements ICellModifier {
	  private Viewer viewer;

	  public ContextCellModifier(Viewer viewer) {
	    this.viewer = viewer;
	  }

	  /**
	   * Returns whether the property can be modified
	   * 
	   * @param element
	   *            the element
	   * @param property
	   *            the property
	   * @return boolean
	   */
	  public boolean canModify(Object element, String property) {
	    // Allow editing of all values
	    return true;
	  }

	  /**
	   * Returns the value for the property
	   * 
	   * @param element
	   *            the element
	   * @param property
	   *            the property
	   * @return Object
	   */
	  public Object getValue(Object element, String property) {
			
			ProcessModelVariables entry = (ProcessModelVariables) element;
			for (int i = 0; i<View.contextProps.length;i++) {
				
					if (View.contextProps[i].equals(property)) {
						if (entry.getValues().isEmpty()) {
							return "";
						}
						return entry.getValues().get(i-1);
					}
				
			}	
				return null;
	  }

	  /**
	   * Modifies the element
	   * 
	   * @param element
	   *            the element
	   * @param property
	   *            the property
	   * @param value
	   *            the value
	   */
	  public void modify(Object element, String property, Object value) {
	    if (element instanceof Item) {
	      element = ((Item) element).getData();
	    }
	    
	    ProcessModelVariables entry = (ProcessModelVariables) element;
		for (int i = 0; i<View.contextProps.length;i++) {
			if (View.contextProps[i].equals(property)) {
				if (entry.getValues().isEmpty()) {
					for (int j = 0; j<View.contextProps.length-2;j++) {
						entry.getValues().add("");
					}
				}
				
				entry.getValues().set(i-1, (String) value);
				
			}
		}	

	    // Force the viewer to refresh
	    viewer.refresh();
	    
	  }
	}