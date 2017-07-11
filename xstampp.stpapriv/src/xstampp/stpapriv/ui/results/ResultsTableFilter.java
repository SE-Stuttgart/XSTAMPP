package xstampp.stpapriv.ui.results;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import xstampp.stpapriv.model.results.ConstraintResult;

public class ResultsTableFilter extends ViewerFilter {

	public String filterType = "";

	public void setFilterType(String tp) {
		this.filterType = tp;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (filterType == null || filterType.length() == 0) {
			return true;
		}
		ConstraintResult p = (ConstraintResult) element;
		if (filterType.equals("Safety related")) {
			if (p.getIdCompare()[0] == 0) {
				return true;
			} else {
				if (p.isSafe()) {

					return true;
				}

			}
		} else if (filterType.equals("Security related")) {
			if (p.getIdCompare()[0] == 0) {
				return true;
			} else {
				if (p.isSecure()) {
					return true;
				}

			}
		} else if (filterType.equals("Privacy related")) {
			if (p.getIdCompare()[0] == 0) {
				return true;
			} else {
				if (p.isPrivate()) {
					return true;
				}

			}
		}else if (filterType.equals("All three")) {
			if (p.getIdCompare()[0] == 0) {
				return true;
			} else if (p.isSafe() && p.isSecure()&& p.isPrivate()) {
				return true;
			}

		} else if (filterType.equals("All")) {
			return true;
		}

		return false;
	}

}
