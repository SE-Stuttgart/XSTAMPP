package xstampp.stpapriv.preferences;

/**
 * @author Lukas Balzer
 *
 */
public interface IASTPADefaults {

  /**
   * Whether or not it should be possible to define the severity of Hazards. <p>
   * <i><b>Note</b> This can be changed in the project settings menu of the program</i>
   */
  static final boolean USE_SEVERITY_ANALYSIS = false;

  /**
   * Whether or not it should be possible to define multiple control structure diagrams in an stpa
   * project for documentation reasons. Only the first control structure (Level 0) is used in the
   * causal analysis but all control structures can contribute control actions.
   * 
   * <p>
   * <i><b>NOTE:</b>this feature is still in <b>BETA</b> and can not be enabled/disabled in the project settings menu of the program<i>
   */
  static final boolean USE_MULTI_CONTROL_STRUCTURES = false;

  /**
   * Whether or not he causal analysis should include graphical support for defining scenarios <p>
   * <i><b>Note</b> This can be changed in the project settings menu of the program</i>
   */
  static final boolean USE_CAUSAL_SCENARIO_ANALYSIS = true;
}
