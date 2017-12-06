package xstampp.astpa.model.linking;

import xstampp.model.ObserverValue;

public enum LinkingType {

  /**
   * @deprecated This is a legacy entry UCA_HAZ_LINK
   */
  UNSAFE_CONTROL_ACTION,
  /**
   * Enum for changes in linking between hazards and accidents.
   * 
   * @author Fabian Toth
   */
  HAZ_ACC_LINK,
  /**
   * <ol>
   * <li>A should be the UUID of an Accident in STPA Step 1
   * <li>B should be the UUID of a Safety Constraint in STPA Step 1
   * </ol>
   */
  ACC_S0_LINK,

  /**
   * <ol>
   * <li>A should be the UUID of a Design Requirement in STPA Step 0
   * <li>B should be the UUID of a Safety Constraint in STPA Step 0
   * </ol>
   */
  DR0_SC_LINK,

  /**
   * <ol>
   * <li>A should be the UUID of a Design Requirement in STPA Step 1
   * <li>B should be the UUID of a Safety Constraint in STPA Step 1
   * </ol>
   */
  DR1_CSC_LINK,

  /**
   * <ol>
   * <li>A should be the UUID of a Design Requirement in STPA Step 2
   * <li>B should be the UUID of a Safety Constraint in STPA Step 2
   * </ol>
   */
  DR2_CausalSC_LINK,

  /**
   * <ol>
   * <li>A should be the UUID of a Design Requirement in STPA Step 2
   * <li>B should be the UUID of a Causal Scenario in STPA Step 2
   * </ol>
   */
  DR2_CausalScenarioSC_LINK,

  /**
   * <ol>
   * <li>A should be the UUID of a UCA
   * <li>B should be the UUID of a Hazard
   * </ol>
   */
  UCA_HAZ_LINK,

  /**
   * enum for a link<A,B> between a unsafe control action and a causal factor in a stpa analysis.
   * <ol>
   * <li>A should be the UUID of a UCA
   * <li>B should be the UUID of a CF
   * </ol>
   */
  UCA_CausalFactor_LINK,

  /**
   * A Link that is Labeled with this constant should describe an entry in the causal factors table.
   * <ol>
   * <li>A should be the UUID of a {@link ObserverValue#UCA_CausalFactor_LINK}
   * <li>B should be the UUID of a Control Structure Component
   * </ol>
   */
  UcaCfLink_Component_LINK,

  /**
   * <ol>
   * <li>A should be the UUID of a {@link ObserverValue#UcaCfLink_Component_LINK}
   * <li>B should be the UUID of a Hazard that is linked to the UCA
   * </ol>
   */
  CausalEntryLink_HAZ_LINK,

  /**
   * <ol>
   * <li>A should be the UUID of a {@link ObserverValue#UCAEntryLink_HAZ_LINK}
   * <li>B should be the UUID of a Safety Constraint in STPA Step 2
   * </ol>
   */
  CausalHazLink_SC2_LINK,

  /**
   * <ol>
   * <li>A should be the UUID of a {@link ObserverValue#UcaCfLink_Component_LINK}
   * <li>B should be the UUID of a Hazardous Scenario that is linked to the UCA
   * </ol>
   */
  CausalEntryLink_Scenario_LINK,

  /**
   * <ol>
   * <li>B should be the UUID of a Safety Constraint in STPA Step 2
   * <li>B should be the UUID of a Safety Constraint in STPA Step 1
   * </ol>
   */
  SC2_SC1_LINK;
}
