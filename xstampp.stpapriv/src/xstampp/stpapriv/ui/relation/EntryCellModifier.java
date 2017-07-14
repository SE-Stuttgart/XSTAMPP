package xstampp.stpapriv.ui.relation;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;

import xstampp.stpapriv.model.PrivacyController;
import xstampp.stpapriv.model.relation.ControlEntry;

public class EntryCellModifier implements ICellModifier {
	private Viewer viewer;
	private PrivacyRelationsView view;
	private PrivacyController model;

	public EntryCellModifier(Viewer viewer, PrivacyController controller) {
		this.viewer = viewer;
		this.model = controller;
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

		ControlEntry entry = (ControlEntry) element;
		if (PrivacyRelationsView.SAFETY_CRITICAL.equals(property)) {
			return Boolean.valueOf(entry.getSafetyCritical());
		} else if (PrivacyRelationsView.COMMENTS.equals(property)) {
			return entry.getComments();
		} else if (PrivacyRelationsView.SECURITY_CRITICAL.equals(property)) {
			return Boolean.valueOf(entry.getSecurityCritical());
		} else if (PrivacyRelationsView.PRIVACY_CRITICAL.equals(property)) {
			return Boolean.valueOf(entry.getPrivacyCritical());
		}

		else {
			return null;
		}
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

    ControlEntry entry = (ControlEntry) element;
    if (PrivacyRelationsView.SAFETY_CRITICAL.equals(property)) {
      entry.setSafetyCritical(!(Boolean) entry.getSafetyCritical());
      model.setUCASafetyCritical(entry.getId(), entry.getSafetyCritical());
    } else if (PrivacyRelationsView.SECURITY_CRITICAL.equals(property)) {
      entry.setSecurityCritical(!(Boolean) entry.getSecurityCritical());
      model.setUCASecurityCritical(entry.getId(), entry.getSecurityCritical());
    }else if (PrivacyRelationsView.PRIVACY_CRITICAL.equals(property)) {
      entry.setPrivacyCritical(!(Boolean) entry.getPrivacyCritical());
      model.setUCAPrivacyCritical(entry.getId(), entry.getPrivacyCritical());
    } else if (PrivacyRelationsView.COMMENTS.equals(property)) {
      entry.setComments((String) value);
      model.setControlActionDescription(entry.getIdCA(), (String) value);
    }
		// Force the viewer to refresh
		viewer.refresh();

	}

	public PrivacyRelationsView getView() {
		return view;
	}

	public void setView(PrivacyRelationsView view) {
		this.view = view;
	}
}
