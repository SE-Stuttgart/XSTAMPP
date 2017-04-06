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

package xstampp.model;

import java.util.List;
import java.util.Map;

/**
 * an interface for migrating a data object containing System Properties in
 * linear temporal logic, a Map of the related values mapped to a variable and
 * the name of the system
 * 
 * @author Lukas Balzer
 * @since 2.0.2
 */
public interface ISafetyDataModel {

  /**
   * 
   * @return a List with all ILTLProvider objects currently stored in that
   *         dataModel
   * 
   * @see AbstractLTLProvider
   */
  List<AbstractLTLProvider> getLTLPropertys();

  /**
   * 
   * @return a map containing value lists mapped to their variables
   */
  Map<String, List<String>> getValuesTOVariables();

  /**
   *
   * @author Lukas Balzer
   *
   * @return returns the project Name <b>must not be null</b>
   */
  String getProjectName();
}
