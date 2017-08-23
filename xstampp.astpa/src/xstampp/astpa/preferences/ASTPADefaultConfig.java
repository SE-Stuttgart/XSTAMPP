/*******************************************************************************
 * Copyright (C) 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lukas Balzer - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.preferences;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.core.runtime.Platform;

import xstampp.ui.common.ProjectManager;

/**
 * @author Lukas Balzer
 *
 */
public class ASTPADefaultConfig {

  private static ASTPADefaultConfig instance;

  /**
   * Whether or not it should be possible to define the severity of Hazards.
   * <p>
   * <i><b>Note</b> This can be changed in the project settings menu of the program</i>
   */
  public final boolean USE_SEVERITY_ANALYSIS;

  /**
   * Whether or not it should be possible to define multiple control structure diagrams in an stpa
   * project for documentation reasons. Only the first control structure (Level 0) is used in the
   * causal analysis but all control structures can contribute control actions.
   * 
   * <p>
   * <i><b>NOTE:</b>this feature is still in <b>BETA</b> and can not be enabled/disabled in the
   * project settings menu of the program<i>
   */
  public final boolean USE_MULTI_CONTROL_STRUCTURES;

  /**
   * Whether or not he causal analysis should include graphical support for defining scenarios
   * <p>
   * <i><b>Note</b> This can be changed in the project settings menu of the program</i>
   */
  public final boolean USE_CAUSAL_SCENARIO_ANALYSIS;

  private ASTPADefaultConfig() {
    USE_CAUSAL_SCENARIO_ANALYSIS = fetchSTPAConfig("stpa.defaults.useCausalScenarioAnalysis",
        false);
    USE_MULTI_CONTROL_STRUCTURES = fetchSTPAConfig("stpa.defaults.useMultiControlStructures",
        false);
    USE_SEVERITY_ANALYSIS = fetchSTPAConfig("stpa.defaults.useSeverityAnalysis", true);
  }

  public static ASTPADefaultConfig getInstance() {
    if (instance == null) {
      instance = new ASTPADefaultConfig();
    }
    return instance;
  }

  private boolean fetchSTPAConfig(String valueId, boolean defaultValue) {
    boolean returnValue = defaultValue;
    try (
        InputStream stream = Platform.getConfigurationLocation().getDataArea("/config.ini")
            .openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
      String line = reader.readLine();
      ProjectManager.getLOGGER().debug(
          "fetching default config from "
              + Platform.getConfigurationLocation().getDataArea("/config.ini").getPath());
      while (line != null) {
        if (line.startsWith(valueId)) {
          String[] split = line.split("=");
          returnValue = Boolean.parseBoolean(split[1]);
          break;
        }
        line = reader.readLine();
      }
    } catch (Exception ioExc) {
      ProjectManager.getLOGGER().warn("Couldn't fetch default stpa preference " + valueId, ioExc);
    }
    return returnValue;
  }
}
