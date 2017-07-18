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

package xstampp.stpasec.ui.sds;

import xstampp.astpa.ui.sds.SafetyConstraintView;
import xstampp.stpasec.messages.SecMessages;

/**
 * @author Jarkko Heidenwag
 * 
 */
public class SecurityConstraintView extends SafetyConstraintView {

	/**
	 * @author Jarkko Heidenwag
	 * 
	 */
	public static final String ID = "stpasec.steps.step1_5"; //$NON-NLS-1$

	public SecurityConstraintView() {
    super(SecMessages.SecurityConstraints);
  }
	@Override
	public String getId() {
		return SecurityConstraintView.ID;
	}

	@Override
	public String getTitle() {
		return SecMessages.SecurityConstraints;
	}

}
