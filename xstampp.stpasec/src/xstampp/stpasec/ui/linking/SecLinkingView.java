/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
 * Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian Sieber, Fabian Toth, Patrick Wickenhäuser,
 * Aliaksei Babkovich, Aleksander Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.stpasec.ui.linking;

import xstampp.astpa.ui.linking.LinkingView;
import xstampp.stpasec.messages.SecMessages;

/**
 * Editor to create and delete links between hazards and accidents. There are two table modes:
 * accident mode and hazard mode, which identifies whether accidents are linked to hazards or
 * hazards to accidents respectively.
 * 
 * @author Patrick Wickenhaeuser
 * @author Jarkko Heidenwag
 */
public class SecLinkingView extends LinkingView {

  /**
   * Identifier of the view
   */
  public static final String ID = "stpasec.steps.step1_4";

  /**
   * Updates the text of the mode button.
   * 
   * @author Patrick Wickenhaeuser
   */
  public void updateMode() {
    if (isInAtoB_Mode()) {
      this.setUpdateSelected(SecMessages.Losses);
      this.setUpdateAvailableForLinking(SecMessages.VulnerabilitiesAvailableForLinking);
      this.setUpdateCurrentlyLinked(SecMessages.CurrentlyLinkedVulnerabilities);
      this.setUpdateModeButton(SecMessages.SwitchToVulnerabilities);
    } else {
      this.setUpdateSelected(SecMessages.Vulnerabilities);
      this.setUpdateAvailableForLinking(SecMessages.LossesAvailableForLinking);
      this.setUpdateCurrentlyLinked(SecMessages.CurrentlyLinkedLosses);
      this.setUpdateModeButton(SecMessages.SwitchToLosses);
    }

    updateSelection();
  }

  public String getTitle() {
    return SecMessages.LinkingOfLossesAndVulnerabilities;
  }

}
