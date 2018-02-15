/*******************************************************************************
 * Copyright (C) 2018 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology, Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology, Software Engineering Group University of Stuttgart, Germany - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.usermanagement;

import java.util.UUID;

import org.eclipse.draw2d.geometry.Rectangle;
import org.junit.Before;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.TestObserver;
import xstampp.astpa.model.controlaction.interfaces.UnsafeControlActionType;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.model.ObserverValue;

public class AstpaCollaborationTest extends TestObserver {

  private DataModelController userData;
  private DataModelController controlData;

  @Before
  public void setUp() throws Exception {
    this.userData = new DataModelController(true);
    this.controlData = new DataModelController(true);

    UUID h_1 = this.userData.getHazAccController().addHazard("", "");
    UUID a_1 = this.userData.getHazAccController().addAccident("", "");
    UUID dr0_1 = this.userData.getSdsController().addDesignRequirement("", "", ObserverValue.DESIGN_REQUIREMENT);
    UUID sc0_1 = this.userData.getSdsController().addSafetyConstraint("", "", null);
    UUID g_1 = this.userData.getSdsController().addSystemGoal("", "");
    UUID root = this.userData.getControlStructureController().setRoot(new Rectangle(), "");
    UUID controller = this.userData.getControlStructureController().addComponent(root, new Rectangle(), "",
        ComponentType.CONTROLLER, 0);
    UUID haz1Acc1Link = this.userData.getLinkController().addLink(LinkingType.HAZ_ACC_LINK, h_1, a_1);
    UUID acc1SC0_1Link = this.userData.getLinkController().addLink(LinkingType.ACC_S0_LINK, a_1, sc0_1);
    UUID dr0_1SC0_1Link = this.userData.getLinkController().addLink(LinkingType.DR0_SC_LINK, dr0_1, sc0_1);

    UUID ca_1 = this.userData.getControlActionController().addControlAction("", "");
    UUID uca_1 = this.userData.addUnsafeControlAction(ca_1, "", UnsafeControlActionType.GIVEN_INCORRECTLY);
    UUID uca_1H_1Link = this.userData.getLinkController().addLink(LinkingType.UCA_HAZ_LINK, uca_1, h_1);
    UUID dr1_1 = this.userData.getSdsController().addDesignRequirement("", "", ObserverValue.DESIGN_REQUIREMENT_STEP1);
    this.userData.getControlActionController().setCorrespondingSafetyConstraint(uca_1, "constraint");
    this.userData.getControlActionController().getCorrespondingSafetyConstraint(uca_1);

    UUID cf_1 = this.userData.addCausalFactor();
    UUID ucaCfLink = this.userData.getLinkController().addLink(LinkingType.UCA_CausalFactor_LINK, uca_1, cf_1);
    UUID causalEntryLink = this.userData.getLinkController().addLink(LinkingType.UcaCfLink_Component_LINK, ucaCfLink,
        controller);
    UUID dr2_1 = this.userData.getSdsController().addDesignRequirement("", "", ObserverValue.DESIGN_REQUIREMENT_STEP2);

    this.userData.addCausalFactor();
  }

  public AstpaCollaborationTest() {
    // TODO Auto-generated constructor stub
  }

}
