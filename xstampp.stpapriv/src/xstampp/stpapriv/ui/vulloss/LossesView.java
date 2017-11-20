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

package xstampp.stpapriv.ui.vulloss;

import java.util.EnumSet;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.astpa.ui.acchaz.AccidentsView;
import xstampp.astpa.ui.linkingSupport.Step0ConstraintsLinkSupport;
import xstampp.stpapriv.messages.PrivMessages;
import xstampp.stpapriv.ui.linkSuppport.VulnerabilityLinkSupport;

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
    super(PrivMessages.Losses, EnumSet.of(TableStyle.RESTRICTED));

  }

  public LossesView(String title) {
    super(title, EnumSet.of(TableStyle.RESTRICTED));
  }

  @Override
  public String getId() {
    return LossesView.ID;
  }

  @Override
  public String getTitle() {
    return PrivMessages.Losses;
  }

  @Override
  protected void addLinkSupports() {
    addLinkSupport(new VulnerabilityLinkSupport((DataModelController) getDataInterface(),
        LinkingType.HAZ_ACC_LINK));
    addLinkSupport(new Step0ConstraintsLinkSupport((DataModelController) getDataInterface(),
        LinkingType.ACC_S0_LINK));
  }

}
