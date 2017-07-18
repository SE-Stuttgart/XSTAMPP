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

import messages.Messages;

/**
 * @author Jarkko Heidenwag
 * 
 */
public class ControlActionView extends xstampp.astpa.ui.sds.ControlActionView {

	/**
	 * @author Jarkko Heidenwag
	 * 
	 */
	public static final String ID = "stpasec.steps.step2_1"; //$NON-NLS-1$

	@Override
	public String getId() {
		return ControlActionView.ID;
	}

	@Override
	public String getTitle() {
		return Messages.ControlActions;
	}
}
