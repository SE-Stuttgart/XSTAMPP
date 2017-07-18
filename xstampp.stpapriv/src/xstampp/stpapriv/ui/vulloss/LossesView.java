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

package xstampp.stpapriv.ui.vulloss;

import xstampp.astpa.ui.acchaz.AccidentsView;
import xstampp.stpapriv.messages.SecMessages;

/**
 * @author Jarkko Heidenwag
 * 
 */
public class LossesView extends AccidentsView {

	/**
	 * @author Jarkko Heidenwag
	 * 
	 */
	public static final String ID = "stpapriv.steps.step1_2"; //$NON-NLS-1$



	/**
	 * @author Jarkko Heidenwag
	 * 
	 */
	public LossesView() {
		 super(SecMessages.Losses);

	}

	@Override
	public String getId() {
		return LossesView.ID;
	}

	@Override
	public String getTitle() {
		return SecMessages.Losses;
	}

}
