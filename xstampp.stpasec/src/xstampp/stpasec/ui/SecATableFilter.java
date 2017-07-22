/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package xstampp.stpasec.ui;

import org.eclipse.jface.viewers.Viewer;

import messages.Messages;


import xstampp.stpasec.messages.SecMessages;
import xstampp.astpa.model.ATableModel;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.ui.ATableFilter;

/**
 * 
 * @author Jarkko Heidenwag
 * 
 */
public class SecATableFilter extends ATableFilter{
	public SecATableFilter() {
		this.getCategorys().clear();
		this.getCategorys().add(Messages.All);
		this.getCategorys().add(SecMessages.CorrespondingSecurityConstraints);
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if ((this.searchString == null) || (this.searchString.length() == 0)) {
			return true;
		}
		if (element instanceof ATableModel) {
			ATableModel p = (ATableModel) element;
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
	
		if ((this.getCSCFilterMode() == 1)) {
			if (cuca.getCorrespondingSafetyConstraint().getText().toLowerCase()
					.matches(this.searchString)) {
				return true;
			}
			return false;
		} else if ((this.getCSCFilterMode() == 2)) {
			if (cuca.getDescription().toLowerCase().matches(this.searchString)) {
				return true;
			}
			return false;
		} else if (this.getCSCFilterMode() == 0) {
			if ((cuca.getCorrespondingSafetyConstraint().getText()
					.toLowerCase().matches(this.searchString))
					|| (cuca.getDescription().toLowerCase()
							.matches(this.searchString))) {
				return true;
			}
			return false;
		} else {
			return false;
		}
	}

}