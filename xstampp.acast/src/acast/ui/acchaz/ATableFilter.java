/*******************************************************************************
 * Copyright (c) 2013 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
 * Grahovac, Jarkko Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian
 * Sieber, Fabian Toth, Patrick Wickenhäuser, Aliaksei Babkovich, Aleksander
 * Zotov).
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package acast.ui.acchaz;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import acast.model.ATableModelCAST;

/**
 *
 * @author Jarkko Heidenwag
 *
 */
public class ATableFilter extends ViewerFilter {

	private String searchString;
	// CSCView filter both (0) Unsafe Control Actions (1) or Resulting Safety
	// Constraints (2)
	private int cscFilterMode = 0;

	/**
	 *
	 * @author Jarkko Heidenwag
	 *
	 * @param s
	 *            the string for the filter
	 */
	public void setSearchText(String s) {
		// ensure that the value can be used for matching
		this.searchString = ".*" + s.toLowerCase() + ".*"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if ((this.searchString == null) || (this.searchString.length() == 0)) {
			return true;
		}
		if (element instanceof ATableModelCAST) {
			ATableModelCAST p = (ATableModelCAST) element;
			if (p.getTitle().toLowerCase().matches(this.searchString)) {
				return true;
			}
			if (p.getDescription().toLowerCase().matches(this.searchString)) {
				return true;
			}
		} else if (element instanceof ICorrespondingUnsafeControlAction) {
			ICorrespondingUnsafeControlAction cuca = (ICorrespondingUnsafeControlAction) element;
			return this.checkCSC(cuca);
		}
		return false;
	}

	private boolean checkCSC(ICorrespondingUnsafeControlAction cuca) {
		final int both = 0;
		final int uca = 1;
		final int rsc = 2;
		if ((this.getCSCFilterMode() == rsc)) {
			if (cuca.getCorrespondingSafetyConstraint().getText().toLowerCase().matches(this.searchString)) {
				return true;
			}
			return false;
		} else if ((this.getCSCFilterMode() == uca)) {
			if (cuca.getDescription().toLowerCase().matches(this.searchString)) {
				return true;
			}
			return false;
		} else if (this.getCSCFilterMode() == both) {
			if ((cuca.getCorrespondingSafetyConstraint().getText().toLowerCase().matches(this.searchString))
					|| (cuca.getDescription().toLowerCase().matches(this.searchString))) {
				return true;
			}
			return false;
		} else {
			return false;
		}
	}

	/**
	 * @return the rsc
	 */
	public int getCSCFilterMode() {
		return this.cscFilterMode;
	}

	/**
	 * @param cscFilterMode
	 *            the cscFilterMode to set
	 */
	public void setCSCFilterMode(int cscFilterMode) {
		this.cscFilterMode = cscFilterMode;
	}

}