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

package xstampp.stpapriv.ui.linking;

import xstampp.astpa.ui.linking.LinkingView;
import xstampp.stpapriv.messages.PrivMessages;

/**
 * Editor to create and delete links between hazards and accidents. There are
 * two table modes: accident mode and hazard mode, which identifies whether
 * accidents are linked to hazards or hazards to accidents respectively.
 * 
 * @author Patrick Wickenhaeuser
 * @author Jarkko Heidenwag
 */
public class PrivacyLinkingView extends LinkingView {

	/**
	 * Identifier of the view
	 */
	public static final String ID = "stpapriv.steps.step1_4";

	
	/**
	 * Updates the text of the mode button.
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	public void updateMode() {
		if (isInAtoB_Mode()) {
			this.setUpdateSelected(PrivMessages.Losses);
			this.setUpdateAvailableForLinking(PrivMessages.VulnerabilitiesAvailableForLinking);
			this.setUpdateCurrentlyLinked(PrivMessages.CurrentlyLinkedVulnerabilities);
			this.setUpdateModeButton(PrivMessages.SwitchToVulnerabilities);
		} else {
			this.setUpdateSelected(PrivMessages.Vulnerabilities);
			this.setUpdateAvailableForLinking(PrivMessages.LossesAvailableForLinking);
			this.setUpdateCurrentlyLinked(PrivMessages.CurrentlyLinkedLosses);
			this.setUpdateModeButton(PrivMessages.SwitchToLosses);
		}

		updateSelection();
	}

	
	public String getTitle() {
		return PrivMessages.LinkingOfLossesAndVulnerabilities;
	}

}
