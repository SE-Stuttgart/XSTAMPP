package xstampp.stpasec.ui.results;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;

import xstampp.stpapriv.model.PrivacyController;
import xstampp.stpapriv.model.results.ConstraintResult;

public class EntryCellModifier implements ICellModifier {
  private Viewer viewer;
  private Label label;
  private PrivacyController model;

  public EntryCellModifier(Viewer viewer, PrivacyController controller, Label label) {
    this.viewer = viewer;
    this.model = controller;
    this.label = label;
  }

  /**
   * Returns whether the property can be modified
   * 
   * @param element
   *          the element
   * @param property
   *          the property
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
   *          the element
   * @param property
   *          the property
   * @return Object
   */
  public Object getValue(Object element, String property) {

    ConstraintResult entry = (ConstraintResult) element;
    if (property.equals("Safety related")) {
      return entry.isSafe();
    } else if (property.equals("Security related")) {
      return entry.isSecure();
    }

    else {
      return null;
    }
  }

  /**
   * Modifies the element
   * 
   * @param element
   *          the element
   * @param property
   *          the property
   * @param value
   *          the value
   */
  public void modify(Object element, String property, Object value) {
    if (element instanceof Item) {
      element = ((Item) element).getData();
    }

    ConstraintResult entry = (ConstraintResult) element;
    if (entry.getIdCompare()[0] != 0) {
      if (property.equals("Safety related")) {
        entry.setSafe(!entry.isSafe());
        model.setUCASafetyCritical(entry.getId(), entry.isSafe());
      } else if (property.equals("Security related")) {
        entry.setSecure(!entry.isSecure());
        model.setUCASecurityCritical(entry.getId(), entry.isSecure());
      }

      viewer.setInput(model.getConstraintController().getValuesList());
      label.setText("Security related: " + model.getConstraintController().getSecureValueCount()
          + "  Safety related: " + model.getConstraintController().getSafeValueCount() + "  Both: "
          + model.getConstraintController().getBothValueCount());
      // Force the viewer to refresh
      viewer.refresh();
    }

  }

}
