/*******************************************************************************
 * Copyright (C) 2018 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology, Software
 * Engineering Group University of Stuttgart, Germany - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.model.linking;

import xstampp.astpa.model.hazacc.Hazard;

public enum LinkingType {

  /**
   * @deprecated This is a legacy entry UCA_HAZ_LINK
   */
  UNSAFE_CONTROL_ACTION(false),
  /**
   * Enum for changes in linking between hazards and accidents.
   * 
   * @author Fabian Toth
   */
  HAZ_ACC_LINK(false),
  /**
   * <ol>
   * <li>A should be the UUID of an Accident in STPA Step 1
   * <li>B should be the UUID of a Safety Constraint in STPA Step 1
   * </ol>
   */
  ACC_S0_LINK(false),
  /**
   * <ol>
   * <li>A should be the UUID of an Hazard in STPA Step 1
   * <li>B should be the UUID of a Safety Constraint in STPA Step 1
   * </ol>
   */
  HAZ_S0_LINK(false),

  /**
   * <ol>
   * <li>A should be the UUID of a Design Requirement in STPA
   * <li>B should be the UUID of a Safety Constraint in STPA
   * </ol>
   */
  DR_SC_LINK(false),

  /**
   * <ol>
   * <li>A should be the UUID of a Design Requirement in STPA Step 0
   * <li>B should be the UUID of a Safety Constraint in STPA Step 0
   * </ol>
   */
  DR0_SC_LINK(false),

  /**
   * <ol>
   * <li>A should be the UUID of a Design Requirement in STPA Step 1
   * <li>B should be the UUID of a Safety Constraint in STPA Step 1
   * </ol>
   */
  DR1_CSC_LINK(false),

  /**
   * <ol>
   * <li>A should be the UUID of a Design Requirement in STPA Step 2
   * <li>B should be the UUID of a Safety Constraint in STPA Step 2
   * </ol>
   */
  DR2_CausalSC_LINK(false),

  /**
   * <ol>
   * <li>A should be the UUID of a Design Requirement in STPA Step 2
   * <li>B should be the UUID of a Causal Scenario in STPA Step 2
   * </ol>
   */
  DR2_CausalScenarioSC_LINK(false),

  /**
   * <ol>
   * <li>A should be the UUID of a UCA
   * <li>B should be the UUID of a Hazard
   * </ol>
   */
  UCA_HAZ_LINK(false),

  /**
   * enum for a link<A,B> between a unsafe control action and a causal factor in a stpa analysis.
   * <ol>
   * <li>A should be the UUID of a UCA
   * <li>B should be the UUID of a CF
   * </ol>
   */
  UCA_CausalFactor_LINK(true),

  /**
   * A Link that is Labeled with this constant should describe an entry in the causal factors table.
   * <ol>
   * <li>A should be the UUID of a {@link LinkingType#UCA_CausalFactor_LINK}
   * <li>B should be the UUID of a Control Structure Component
   * </ol>
   */
  UcaCfLink_Component_LINK(true),

  /**
   * <ol>
   * <li>A should be the UUID of a {@link LinkingType#UcaCfLink_Component_LINK}
   * <li>B should be the UUID of a Hazard that is linked to the UCA
   * </ol>
   */
  CausalEntryLink_HAZ_LINK(true),

  /**
   * A link of this type is set instead of multiple {@link LinkingType#CausalEntryLink_HAZ_LINK}'s
   * for each {@link Hazard} linked to an UCA.
   * <ol>
   * <li>A should be the UUID of a {@link LinkingType#UcaCfLink_Component_LINK}
   * <li>B should be the UUID of a Safety Constraint in STPA Step 2
   * </ol>
   */
  CausalEntryLink_SC2_LINK(true),

  /**
   * A link of this type is set instead of multiple {@link LinkingType#CausalEntryLink_HAZ_LINK}'s
   * for each {@link Hazard} linked to an UCA.
   * <ol>
   * <li>A should be the UUID of a {@link LinkingType#UcaCfLink_Component_LINK}
   * <li>B is not used by this
   * </ol>
   */
  CausalEntryLink_ANCHOR(true),

  /**
   * <ol>
   * <li>A should be the UUID of a {@link LinkingType#CausalEntryLink_HAZ_LINK}
   * <li>B should be the UUID of a Safety Constraint in STPA Step 2
   * </ol>
   */
  CausalHazLink_SC2_LINK(true),

  /**
   * <ol>
   * <li>A should be the UUID of a {@link LinkingType#UcaCfLink_Component_LINK}
   * <li>B should be the UUID of a Hazardous Scenario that is linked to the UCA
   * </ol>
   */
  CausalEntryLink_Scenario_LINK(true),

  /**
   * <ol>
   * <li>B should be the UUID of a Safety Constraint in STPA Step 2
   * <li>B should be the UUID of a Safety Constraint in STPA Step 1
   * </ol>
   */
  SC2_SC1_LINK(false);
  
  private boolean acceptNullLinks;

  private LinkingType(boolean acceptNullLinks) {
    this.acceptNullLinks = acceptNullLinks;
  }
  
  public boolean isAcceptingNullLinks() {
    return acceptNullLinks;
  }
}
