/*******************************************************************************
 * 
 * Copyright (c) 2013-2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package xstampp.stpasec.ui.causalfactors;

import messages.Messages;
import xstampp.astpa.ui.causalfactors.CausalFactorsView;
import xstampp.stpasec.messages.SecMessages;

/**
 * The view to add causal factors to control structure components, edit them and
 * add links to the related hazards.
 * 
 * @author Benedikt Markt, Patrick Wickenhaeuser, Lukas Balzer
 */
public class SecCausalFactorsView extends CausalFactorsView{
	private static String[] _withScenarioColumns = new String[] { Messages.Component,
      Messages.CausalFactors,"Unsecure Control Action", SecMessages.VulnerabilityLinks,"Causal Scenarios",
      SecMessages.SecurityConstraint, Messages.NotesSlashRationale };
	private static String[] _withoutColumns = new String[] { Messages.Component,
		      Messages.CausalFactors, "Unsecure Control Action", SecMessages.VulnerabilityLinks,
		      SecMessages.SecurityConstraint, Messages.NotesSlashRationale };
  /**
   * ViewPart ID.
   */
  public static final String ID = "stpapriv.steps.step3_2";

  public String[] getScenarioColumns() {
    return _withScenarioColumns;
  }

  public String[] getColumns() {
    return _withoutColumns;
  }
}
